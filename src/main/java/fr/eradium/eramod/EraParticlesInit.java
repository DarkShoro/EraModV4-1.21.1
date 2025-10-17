package fr.eradium.eramod;

import dev.architectury.registry.ReloadListenerRegistry;
import mod.chloeprime.aaaparticles.client.loader.EffekAssetLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class EraParticlesInit {
	public EraParticlesInit() {
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		new EraParticlesInit();
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void clientLoad(FMLClientSetupEvent event) {
		ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, new EffekAssetLoader(), ResourceLocation.fromNamespaceAndPath(EramodMod.MODID, "effeks"));
		EramodMod.LOGGER.info("Effek Asset Loader registered");
	}

	@EventBusSubscriber
	private static class EraParticlesInitForgeBusEvents {
		@SubscribeEvent
		public static void serverLoad(ServerStartingEvent event) {
		}
	}
}