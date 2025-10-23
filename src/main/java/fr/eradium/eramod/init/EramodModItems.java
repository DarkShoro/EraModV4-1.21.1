/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package fr.eradium.eramod.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;

import fr.eradium.eramod.item.TeleCasterItem;
import fr.eradium.eramod.item.GeanaStickItem;
import fr.eradium.eramod.item.GalaxiteSwordItem;
import fr.eradium.eramod.item.GalaxiteShovelItem;
import fr.eradium.eramod.item.GalaxitePickaxeItem;
import fr.eradium.eramod.item.GalaxiteKeyItem;
import fr.eradium.eramod.item.GalaxiteItem;
import fr.eradium.eramod.item.GalaxiteHoeItem;
import fr.eradium.eramod.item.GalaxiteHammerItem;
import fr.eradium.eramod.item.GalaxiteAxeItem;
import fr.eradium.eramod.item.GalaxiteArmorItem;
import fr.eradium.eramod.EramodMod;

public class EramodModItems {
	public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(EramodMod.MODID);
	public static final DeferredItem<Item> TELE_CASTER = REGISTRY.register("tele_caster", TeleCasterItem::new);
	public static final DeferredItem<Item> GEANA_GRASS = block(EramodModBlocks.GEANA_GRASS);
	public static final DeferredItem<Item> GEANA_STONE = block(EramodModBlocks.GEANA_STONE);
	public static final DeferredItem<Item> GEANA_STRIPPED_WOOD = block(EramodModBlocks.GEANA_STRIPPED_WOOD);
	public static final DeferredItem<Item> GEANA_DIRT = block(EramodModBlocks.GEANA_DIRT);
	public static final DeferredItem<Item> GEANA_WOOD_FENCE = block(EramodModBlocks.GEANA_WOOD_FENCE);
	public static final DeferredItem<Item> GEANA_LEAVES = block(EramodModBlocks.GEANA_LEAVES);
	public static final DeferredItem<Item> GALAXITE_KEY = REGISTRY.register("galaxite_key", GalaxiteKeyItem::new);
	public static final DeferredItem<Item> GEANA_STONE_STAIRS = block(EramodModBlocks.GEANA_STONE_STAIRS);
	public static final DeferredItem<Item> GALAXITE_PICKAXE = REGISTRY.register("galaxite_pickaxe", GalaxitePickaxeItem::new);
	public static final DeferredItem<Item> GALAXITE_BLOCK = block(EramodModBlocks.GALAXITE_BLOCK);
	public static final DeferredItem<Item> GALAXITE = REGISTRY.register("galaxite", GalaxiteItem::new);
	public static final DeferredItem<Item> GEANA_COBBLESTONE_SLAB = block(EramodModBlocks.GEANA_COBBLESTONE_SLAB);
	public static final DeferredItem<Item> GEANA_WOOD = block(EramodModBlocks.GEANA_WOOD);
	public static final DeferredItem<Item> GALAXITE_AXE = REGISTRY.register("galaxite_axe", GalaxiteAxeItem::new);
	public static final DeferredItem<Item> GEANA_COBBLESTONE = block(EramodModBlocks.GEANA_COBBLESTONE);
	public static final DeferredItem<Item> GALAXITE_HAMMER = REGISTRY.register("galaxite_hammer", GalaxiteHammerItem::new);
	public static final DeferredItem<Item> GEANA_COBBLESTONE_WALL = block(EramodModBlocks.GEANA_COBBLESTONE_WALL);
	public static final DeferredItem<Item> GALAXITE_SWORD = REGISTRY.register("galaxite_sword", GalaxiteSwordItem::new);
	public static final DeferredItem<Item> GALAXITE_SHOVEL = REGISTRY.register("galaxite_shovel", GalaxiteShovelItem::new);
	public static final DeferredItem<Item> GEANA_GALAXITE = block(EramodModBlocks.GEANA_GALAXITE);
	public static final DeferredItem<Item> GEANA_WOOD_SLAB = block(EramodModBlocks.GEANA_WOOD_SLAB);
	public static final DeferredItem<Item> GEANA_LOG = block(EramodModBlocks.GEANA_LOG);
	public static final DeferredItem<Item> GEANA_WOOD_PLANKS = block(EramodModBlocks.GEANA_WOOD_PLANKS);
	public static final DeferredItem<Item> GEANA_STONE_WALL = block(EramodModBlocks.GEANA_STONE_WALL);
	public static final DeferredItem<Item> GEANA_STRIPPED_LOG = block(EramodModBlocks.GEANA_STRIPPED_LOG);
	public static final DeferredItem<Item> GEANA_STONE_SLAB = block(EramodModBlocks.GEANA_STONE_SLAB);
	public static final DeferredItem<Item> GEANA_WOOD_STAIRS = block(EramodModBlocks.GEANA_WOOD_STAIRS);
	public static final DeferredItem<Item> GEANA_STICK = REGISTRY.register("geana_stick", GeanaStickItem::new);
	public static final DeferredItem<Item> GALAXITE_HOE = REGISTRY.register("galaxite_hoe", GalaxiteHoeItem::new);
	public static final DeferredItem<Item> GALAXITE_ARMOR_HELMET = REGISTRY.register("galaxite_armor_helmet", GalaxiteArmorItem.Helmet::new);
	public static final DeferredItem<Item> GALAXITE_ARMOR_CHESTPLATE = REGISTRY.register("galaxite_armor_chestplate", GalaxiteArmorItem.Chestplate::new);
	public static final DeferredItem<Item> GALAXITE_ARMOR_LEGGINGS = REGISTRY.register("galaxite_armor_leggings", GalaxiteArmorItem.Leggings::new);
	public static final DeferredItem<Item> GALAXITE_ARMOR_BOOTS = REGISTRY.register("galaxite_armor_boots", GalaxiteArmorItem.Boots::new);
	public static final DeferredItem<Item> GEANA_COBBLESTONE_STAIRS = block(EramodModBlocks.GEANA_COBBLESTONE_STAIRS);
	public static final DeferredItem<Item> PLAYER_STATUE = block(EramodModBlocks.PLAYER_STATUE);

	// Start of user code block custom items
	// End of user code block custom items
	private static DeferredItem<Item> block(DeferredHolder<Block, Block> block) {
		return block(block, new Item.Properties());
	}

	private static DeferredItem<Item> block(DeferredHolder<Block, Block> block, Item.Properties properties) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), properties));
	}
}