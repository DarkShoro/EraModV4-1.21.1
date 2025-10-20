package fr.eradium.eramod.block;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class GeanaCobblestoneBlock extends Block {
	public GeanaCobblestoneBlock() {
		super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLUE).strength(2f, 6f).requiresCorrectToolForDrops());
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 15;
	}
}