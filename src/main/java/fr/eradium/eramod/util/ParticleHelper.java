package fr.eradium.eramod.util;

import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ParticleHelper {
	
	/**
	 * Spawns a particle effect from a player with configurable parameters
	 * @param world The world/level to spawn the particle in
	 * @param player The player to spawn the particle from
	 * @param particleInfo The particle emitter info
	 * @param yNudge The vertical nudge offset (downward from eye height)
	 * @param forwardNudge The forward nudge offset (scales with pitch)
	 * @param yawOffset Additional yaw rotation offset in degrees
	 * @param pitchOffset Additional pitch rotation offset in degrees
	 * @param rollOffset Additional roll rotation offset in degrees
	 * @param scale The scale of the particle effect
	 */
	public static void spawnPlayerParticle(Level world, Player player, ParticleEmitterInfo particleInfo,
			float yNudge, float forwardNudge, float yawOffset, float pitchOffset, float rollOffset, float scale) {
		
		double x = player.getX();
		double y = player.getY() + player.getEyeHeight();
		double z = player.getZ();

		// Get player pitch for calculations
		float playerPitch = player.getXRot(); // Positive = looking down, Negative = looking up
		
		// to accurately position the particle emitter, nudge it down a bit
		// y nudge is a base: looking down is *2, looking up is *-2, straight is just yNudge
		float pitchRadians = playerPitch * ((float)Math.PI / 180F);
		float yNudgeMultiplier = 1.0F + (playerPitch / 45.0F); // -90° (up) = -1, 0° (straight) = 1, 90° (down) = 3
		y -= yNudge * yNudgeMultiplier;
		
		// also move it forward a bit
		// The more the player looks up, the more "f" should decrease to 0 
		// (0 is straight up or straight down, forwardNudge is looking perfectly horizontally)
		float pitchFactor = (float)Math.cos(pitchRadians);
		float f = forwardNudge * Math.abs(pitchFactor);
		
		x += -Math.sin(player.getYRot() * ((float)Math.PI / 180F)) * f;
		z += Math.cos(player.getYRot() * ((float)Math.PI / 180F)) * f;
		
		// pitch is based on player CAMERA rotation around X axis
		// yaw is based on player yrotation around y axis

		float pitch = 0.0F;
		float yaw = -player.getYRot() * ((float)Math.PI / 180F);
		float roll = -player.getXRot() * ((float)Math.PI / 180F);

		// Apply additional rotation offsets
		yaw += yawOffset * ((float)Math.PI / 180F);
		pitch += pitchOffset * ((float)Math.PI / 180F);
		roll += rollOffset * ((float)Math.PI / 180F);

		particleInfo.rotation(pitch, yaw, roll);
		particleInfo.position(x, y, z);
		particleInfo.scale(scale);
		AAALevel.addParticle(world, particleInfo);
	}
}
