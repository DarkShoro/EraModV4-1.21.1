package fr.eradium.eramod.client;

import fr.eradium.eramod.client.renderer.PlayerStatueItemRenderer;
import fr.eradium.eramod.client.renderer.PlayerStatueRenderer;
import fr.eradium.eramod.init.EramodModBlockEntities;
import fr.eradium.eramod.init.EramodModItems;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientInit {

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(EramodModBlockEntities.PLAYER_STATUE.get(), PlayerStatueRenderer::new);
	}
	
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
		event.registerItem(new IClientItemExtensions() {
			@Override
			public PlayerStatueItemRenderer getCustomRenderer() {
				Minecraft minecraft = Minecraft.getInstance();
				return new PlayerStatueItemRenderer(minecraft.getBlockEntityRenderDispatcher(), minecraft);
			}
		}, EramodModItems.PLAYER_STATUE.get());
	}
}
