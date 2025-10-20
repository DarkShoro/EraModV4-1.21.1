package fr.eradium.eramod.block;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class GeanaCobblestoneSlabBlock extends SlabBlock {
	public GeanaCobblestoneSlabBlock() {
		super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLUE).strength(2f, 6f).requiresCorrectToolForDrops().dynamicShape());
	}
}