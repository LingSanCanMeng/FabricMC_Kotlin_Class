package io.github.lingsancanmeng.dataGen

import io.github.lingsancanmeng.Main
import io.github.lingsancanmeng.blocks.ModBlocksMain
import io.github.lingsancanmeng.blocks.advanced.EggplantCrop
import io.github.lingsancanmeng.item.ModItemsMain
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider
import net.minecraft.block.Block
import net.minecraft.data.server.BlockLootTableGenerator
import net.minecraft.enchantment.Enchantments
import net.minecraft.item.Item
import net.minecraft.loot.LootTable
import net.minecraft.loot.condition.BlockStatePropertyLootCondition
import net.minecraft.loot.context.LootContextTypes
import net.minecraft.loot.entry.ItemEntry
import net.minecraft.loot.entry.LeafEntry
import net.minecraft.loot.entry.LootPoolEntry
import net.minecraft.loot.function.ApplyBonusLootFunction
import net.minecraft.loot.function.SetCountLootFunction
import net.minecraft.loot.provider.number.UniformLootNumberProvider
import net.minecraft.predicate.StatePredicate
import net.minecraft.util.Identifier
import java.util.function.BiConsumer

class ModBlockLootTableDataGen(dataGenerator: FabricDataGenerator?) :
    SimpleFabricLootTableProvider(dataGenerator, LootContextTypes.BLOCK) {
    override fun accept(t: BiConsumer<Identifier, LootTable.Builder>) {
        t.accept(
            Identifier(Main.MOD_ID, "blocks/zinc_block"),
            BlockLootTableGenerator.drops(ModBlocksMain.ZINC_BLOCK)
        )
        t.accept(
            Identifier(Main.MOD_ID, "blocks/zinc_ore"),
            BlockLootTableGenerator.oreDrops(ModBlocksMain.ZINC_ORE, ModItemsMain.RAW_ZINC)
        )
        t.accept(
            Identifier(Main.MOD_ID, "blocks/deepslate_zinc_ore"),
            oreDrops(ModBlocksMain.DEEPSLATE_ZINC_ORE, ModItemsMain.RAW_ZINC, 1f, 2f)
        )
        val eggplantCropBuilder = BlockStatePropertyLootCondition.builder(ModBlocksMain.EGGPLANT_CROP)
            .properties(StatePredicate.Builder.create().exactMatch(EggplantCrop.AGE, 6))
        t.accept(
            Identifier(Main.MOD_ID, "blocks/eggplant_crop"),
            BlockLootTableGenerator.cropDrops(ModBlocksMain.EGGPLANT_CROP, ModItemsMain.EGGPLANT,
                ModItemsMain.EGGPLANT_SEED, eggplantCropBuilder)
        )
    }

    private fun oreDrops(dropWithSilkTouch: Block, drop: Item, min: Float, max: Float): LootTable.Builder {
        return BlockLootTableGenerator.dropsWithSilkTouch(
            dropWithSilkTouch,
            (BlockLootTableGenerator.applyExplosionDecay(
                dropWithSilkTouch,
                ((ItemEntry
                    .builder(drop)
                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(min, max)))
                        ) as LeafEntry.Builder<*>)
                    .apply(ApplyBonusLootFunction.oreDrops(Enchantments.FORTUNE))
            )) as LootPoolEntry.Builder<*>
        )
    }
}