package fr.eradium.eramod;

import fr.eradium.eramod.util.SkinCacheManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME)
public class ResetCache {

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void onPlayerLoggedOut(ClientPlayerNetworkEvent.LoggingOut event) {
		// Clear all skin and cape caches when player disconnects
		SkinCacheManager.clearAllCache();
		// System.out.println("Cleared skin and cape caches on disconnect");
		EramodMod.LOGGER.info("Cleared skin and cape caches on disconnect");
	}
}