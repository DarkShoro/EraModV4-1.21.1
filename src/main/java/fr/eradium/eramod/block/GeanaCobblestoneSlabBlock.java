package fr.eradium.eramod.block;

import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SlabBlock;

public class GeanaCobblestoneSlabBlock extends SlabBlock {
	public GeanaCobblestoneSlabBlock() {
		super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLUE).strength(2f, 6f).requiresCorrectToolForDrops().dynamicShape());
	}
}