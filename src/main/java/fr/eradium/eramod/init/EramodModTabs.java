/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package fr.eradium.eramod.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

import fr.eradium.eramod.EramodMod;

public class EramodModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, EramodMod.MODID);
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> GEANA_TAB = REGISTRY.register("geana_tab",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.eramod.geana_tab")).icon(() -> new ItemStack(EramodModBlocks.GEANA_LOG.get())).displayItems((parameters, tabData) -> {
				tabData.accept(EramodModBlocks.GEANA_GRASS.get().asItem());
				tabData.accept(EramodModBlocks.GEANA_DIRT.get().asItem());
				tabData.accept(EramodModBlocks.GEANA_STONE.get().asItem());
				tabData.accept(EramodModBlocks.GEANA_STONE_STAIRS.get().asItem());
				tabData.accept(EramodModBlocks.GEANA_STONE_SLAB.get().asItem());
				tabData.accept(EramodModBlocks.GEANA_STONE_WALL.get().asItem());
				tabData.accept(EramodModBlocks.GEANA_LEAVES.get().asItem());
				tabData.accept(EramodModBlocks.GEANA_COBBLESTONE.get().asItem());
				tabData.accept(EramodModBlocks.GEANA_COBBLESTONE_STAIRS.get().asItem());
				tabData.accept(EramodModBlocks.GEANA_COBBLESTONE_SLAB.get().asItem());
				tabData.accept(EramodModBlocks.GEANA_COBBLESTONE_WALL.get().asItem());
				tabData.accept(EramodModBlocks.GEANA_LOG.get().asItem());
				tabData.accept(EramodModBlocks.GEANA_WOOD.get().asItem());
				tabData.accept(EramodModBlocks.GEANA_STRIPPED_LOG.get().asItem());
				tabData.accept(EramodModBlocks.GEANA_STRIPPED_WOOD.get().asItem());
				tabData.accept(EramodModBlocks.GEANA_WOOD_PLANKS.get().asItem());
				tabData.accept(EramodModBlocks.GEANA_WOOD_STAIRS.get().asItem());
				tabData.accept(EramodModBlocks.GEANA_WOOD_SLAB.get().asItem());
				tabData.accept(EramodModBlocks.GEANA_WOOD_FENCE.get().asItem());
				tabData.accept(EramodModBlocks.GEANA_GALAXITE.get().asItem());
				tabData.accept(EramodModBlocks.GALAXITE_BLOCK.get().asItem());
				tabData.accept(EramodModItems.GEANA_STICK.get());
				tabData.accept(EramodModItems.GALAXITE.get());
				tabData.accept(EramodModItems.GALAXITE_KEY.get());
				tabData.accept(EramodModItems.GALAXITE_SWORD.get());
				tabData.accept(EramodModItems.GALAXITE_PICKAXE.get());
				tabData.accept(EramodModItems.GALAXITE_AXE.get());
				tabData.accept(EramodModItems.GALAXITE_SHOVEL.get());
				tabData.accept(EramodModItems.GALAXITE_HOE.get());
				tabData.accept(EramodModItems.GALAXITE_HAMMER.get());
				tabData.accept(EramodModItems.GALAXITE_ARMOR_HELMET.get());
				tabData.accept(EramodModItems.GALAXITE_ARMOR_CHESTPLATE.get());
				tabData.accept(EramodModItems.GALAXITE_ARMOR_LEGGINGS.get());
				tabData.accept(EramodModItems.GALAXITE_ARMOR_BOOTS.get());
			}).build());
}