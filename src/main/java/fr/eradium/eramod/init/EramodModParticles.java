/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package fr.eradium.eramod.init;

import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import fr.eradium.eramod.client.particle.LightningParticle;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EramodModParticles {
	@SubscribeEvent
	public static void registerParticles(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(EramodModParticleTypes.LIGHTNING.get(), LightningParticle::provider);
	}
}