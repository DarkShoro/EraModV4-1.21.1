/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package fr.eradium.eramod.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleType;

import fr.eradium.eramod.EramodMod;

public class EramodModParticleTypes {
	public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(Registries.PARTICLE_TYPE, EramodMod.MODID);
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> LIGHTNING = REGISTRY.register("lightning", () -> new SimpleParticleType(false));
}