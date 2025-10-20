package fr.eradium.eramod.block;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class GeanaDirtBlock extends Block {
	public GeanaDirtBlock() {
		super(BlockBehaviour.Properties.of().mapColor(MapColor.CRIMSON_HYPHAE).sound(SoundType.GRAVEL).strength(0.5f));
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 15;
	}
}