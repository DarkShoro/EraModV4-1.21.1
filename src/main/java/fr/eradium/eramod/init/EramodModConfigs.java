package fr.eradium.eramod.init;

import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.ModList;
import net.neoforged.bus.api.SubscribeEvent;

import fr.eradium.eramod.configuration.ParticlesInfosConfiguration;
import fr.eradium.eramod.EramodMod;

@EventBusSubscriber(modid = EramodMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class EramodModConfigs {
	@SubscribeEvent
	public static void register(FMLConstructModEvent event) {
		event.enqueueWork(() -> {
			ModList.get().getModContainerById("eramod").get().registerConfig(ModConfig.Type.CLIENT, ParticlesInfosConfiguration.SPEC, "eramod/particles.toml");
		});
	}
}