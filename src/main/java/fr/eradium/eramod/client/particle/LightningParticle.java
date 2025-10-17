package fr.eradium.eramod.client.particle;

import fr.eradium.eramod.EramodMod;
import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LightningParticle extends TextureSheetParticle {

	public static final ParticleEmitterInfo LIGHTNING = new ParticleEmitterInfo(ResourceLocation.fromNamespaceAndPath("eramod", "lightning"));

	public static LightningParticleProvider provider(SpriteSet spriteSet) {
		return new LightningParticleProvider(spriteSet);
	}

	public static class LightningParticleProvider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteSet;

		public LightningParticleProvider(SpriteSet spriteSet) {
			this.spriteSet = spriteSet;
		}

		@SuppressWarnings("null")
		public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			LightningParticle particle = new LightningParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
			return particle;
		}
	}
	
	@SuppressWarnings("unused")
	private final SpriteSet spriteSet;

	protected LightningParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet) {
		super(world, x, y, z);
		this.spriteSet = spriteSet;
		this.setSize(0.2f, 0.2f);
		this.lifetime = 1;
		this.gravity = 0f;
		this.hasPhysics = true;
		this.xd = vx * 1;
		this.yd = vy * 1;
		this.zd = vz * 1;
		this.pickSprite(spriteSet);
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	public void setRotation(float x, float y, float z) {
		LIGHTNING.rotation(x, y, z);
	}

	@Override
	public void tick() {
		super.tick();
		LIGHTNING.scale(0.5f);
		AAALevel.addParticle(this.level, LIGHTNING.position(this.x, this.y, this.z));
		EramodMod.LOGGER.debug("Lightning particle tick at position: " + this.x + ", " + this.y + ", " + this.z);
	}
}