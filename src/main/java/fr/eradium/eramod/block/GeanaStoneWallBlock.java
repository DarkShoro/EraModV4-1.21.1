package fr.eradium.eramod.block;

import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.WallBlock;

public class GeanaStoneWallBlock extends WallBlock {
	public GeanaStoneWallBlock() {
		super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLUE).strength(1.5f, 6f).requiresCorrectToolForDrops().dynamicShape().forceSolidOn());
	}
}