package fr.eradium.eramod.block;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class GeanaStoneStairsBlock extends StairBlock {
	public GeanaStoneStairsBlock() {
		super(Blocks.AIR.defaultBlockState(), BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLUE).strength(1.5f, 6f).requiresCorrectToolForDrops().dynamicShape());
	}

	@Override
	public float getExplosionResistance() {
		return 6f;
	}
}