/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package fr.eradium.eramod.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredBlock;

import net.minecraft.world.level.block.Block;

import fr.eradium.eramod.block.PlayerStatueBlock;
import fr.eradium.eramod.block.GeanaWoodStairsBlock;
import fr.eradium.eramod.block.GeanaWoodSlabBlock;
import fr.eradium.eramod.block.GeanaWoodPlanksBlock;
import fr.eradium.eramod.block.GeanaWoodFenceBlock;
import fr.eradium.eramod.block.GeanaWoodBlock;
import fr.eradium.eramod.block.GeanaStrippedWoodBlock;
import fr.eradium.eramod.block.GeanaStrippedLogBlock;
import fr.eradium.eramod.block.GeanaStoneWallBlock;
import fr.eradium.eramod.block.GeanaStoneStairsBlock;
import fr.eradium.eramod.block.GeanaStoneSlabBlock;
import fr.eradium.eramod.block.GeanaStoneBlock;
import fr.eradium.eramod.block.GeanaLogBlock;
import fr.eradium.eramod.block.GeanaLeavesBlock;
import fr.eradium.eramod.block.GeanaGrassBlock;
import fr.eradium.eramod.block.GeanaGalaxiteBlock;
import fr.eradium.eramod.block.GeanaDirtBlock;
import fr.eradium.eramod.block.GeanaCobblestoneWallBlock;
import fr.eradium.eramod.block.GeanaCobblestoneStairsBlock;
import fr.eradium.eramod.block.GeanaCobblestoneSlabBlock;
import fr.eradium.eramod.block.GeanaCobblestoneBlock;
import fr.eradium.eramod.block.GalaxiteBlockBlock;
import fr.eradium.eramod.EramodMod;

public class EramodModBlocks {
	public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(EramodMod.MODID);
	public static final DeferredBlock<Block> GEANA_GRASS = REGISTRY.register("geana_grass", GeanaGrassBlock::new);
	public static final DeferredBlock<Block> GEANA_STONE = REGISTRY.register("geana_stone", GeanaStoneBlock::new);
	public static final DeferredBlock<Block> GEANA_STRIPPED_WOOD = REGISTRY.register("geana_stripped_wood", GeanaStrippedWoodBlock::new);
	public static final DeferredBlock<Block> GEANA_DIRT = REGISTRY.register("geana_dirt", GeanaDirtBlock::new);
	public static final DeferredBlock<Block> GEANA_WOOD_FENCE = REGISTRY.register("geana_wood_fence", GeanaWoodFenceBlock::new);
	public static final DeferredBlock<Block> GEANA_LEAVES = REGISTRY.register("geana_leaves", GeanaLeavesBlock::new);
	public static final DeferredBlock<Block> GEANA_STONE_STAIRS = REGISTRY.register("geana_stone_stairs", GeanaStoneStairsBlock::new);
	public static final DeferredBlock<Block> GALAXITE_BLOCK = REGISTRY.register("galaxite_block", GalaxiteBlockBlock::new);
	public static final DeferredBlock<Block> GEANA_COBBLESTONE_SLAB = REGISTRY.register("geana_cobblestone_slab", GeanaCobblestoneSlabBlock::new);
	public static final DeferredBlock<Block> GEANA_WOOD = REGISTRY.register("geana_wood", GeanaWoodBlock::new);
	public static final DeferredBlock<Block> GEANA_COBBLESTONE = REGISTRY.register("geana_cobblestone", GeanaCobblestoneBlock::new);
	public static final DeferredBlock<Block> GEANA_COBBLESTONE_WALL = REGISTRY.register("geana_cobblestone_wall", GeanaCobblestoneWallBlock::new);
	public static final DeferredBlock<Block> GEANA_GALAXITE = REGISTRY.register("geana_galaxite", GeanaGalaxiteBlock::new);
	public static final DeferredBlock<Block> GEANA_WOOD_SLAB = REGISTRY.register("geana_wood_slab", GeanaWoodSlabBlock::new);
	public static final DeferredBlock<Block> GEANA_LOG = REGISTRY.register("geana_log", GeanaLogBlock::new);
	public static final DeferredBlock<Block> GEANA_WOOD_PLANKS = REGISTRY.register("geana_wood_planks", GeanaWoodPlanksBlock::new);
	public static final DeferredBlock<Block> GEANA_STONE_WALL = REGISTRY.register("geana_stone_wall", GeanaStoneWallBlock::new);
	public static final DeferredBlock<Block> GEANA_STRIPPED_LOG = REGISTRY.register("geana_stripped_log", GeanaStrippedLogBlock::new);
	public static final DeferredBlock<Block> GEANA_STONE_SLAB = REGISTRY.register("geana_stone_slab", GeanaStoneSlabBlock::new);
	public static final DeferredBlock<Block> GEANA_WOOD_STAIRS = REGISTRY.register("geana_wood_stairs", GeanaWoodStairsBlock::new);
	public static final DeferredBlock<Block> GEANA_COBBLESTONE_STAIRS = REGISTRY.register("geana_cobblestone_stairs", GeanaCobblestoneStairsBlock::new);
	public static final DeferredBlock<Block> PLAYER_STATUE = REGISTRY.register("player_statue", PlayerStatueBlock::new);
	// Start of user code block custom blocks
	// End of user code block custom blocks
}