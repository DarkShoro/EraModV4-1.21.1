package fr.eradium.eramod.block;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class GeanaWoodFenceBlock extends FenceBlock {
	public GeanaWoodFenceBlock() {
		super(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BLUE).sound(SoundType.WOOD).strength(2f, 3f).dynamicShape().instrument(NoteBlockInstrument.BASS).forceSolidOn());
	}
}