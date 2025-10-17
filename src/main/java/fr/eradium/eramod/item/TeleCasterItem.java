package fr.eradium.eramod.item;

import fr.eradium.eramod.EramodMod;
import fr.eradium.eramod.configuration.ParticlesInfosConfiguration;
import fr.eradium.eramod.util.ParticleHelper;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class TeleCasterItem extends Item {

	public static final ParticleEmitterInfo LIGHTATTAK = new ParticleEmitterInfo(
			ResourceLocation.fromNamespaceAndPath("eramod", "lightattak"));

	public TeleCasterItem() {
		super(new Item.Properties());
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack itemStack = player.getItemInHand(hand);

		// Spawn particle effect
		ParticleHelper.spawnPlayerParticle(
				world,
				player,
				LIGHTATTAK,
				ParticlesInfosConfiguration.LIGHTATTAKYNUDGE.get().floatValue(),
				ParticlesInfosConfiguration.LIGHTATTAKFWNUDGE.get().floatValue(),
				ParticlesInfosConfiguration.LIGHTATTAKYAW.get().floatValue(),
				ParticlesInfosConfiguration.LIGHTATTAKPITCH.get().floatValue(),
				ParticlesInfosConfiguration.LIGHTATTAKROLL.get().floatValue(),
				ParticlesInfosConfiguration.LIGHTATTAKSCALE.get().floatValue());

		// trace a 6.5 block line in the look direction and hurt any entity hit
		Vec3 lookDirection = player.getLookAngle();
		double reach = 6.5;
		Vec3 start = new Vec3(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());
		Vec3 end = start.add(lookDirection.scale(reach));

		// Create a bounding box that encompasses the entire ray path
		AABB searchBox = new AABB(
				Math.min(start.x, end.x) - 1.0,
				Math.min(start.y, end.y) - 1.0,
				Math.min(start.z, end.z) - 1.0,
				Math.max(start.x, end.x) + 1.0,
				Math.max(start.y, end.y) + 1.0,
				Math.max(start.z, end.z) + 1.0);

		// Find all entities in the search box and schedule damage after 1.2 seconds (24 ticks)
		if (!world.isClientSide) {
			var server = world.getServer();
			if (server != null) {
				final Vec3 finalStart = start;
				final Vec3 finalEnd = end;
				final AABB finalSearchBox = searchBox;
				
				EramodMod.queueServerWork(9, () -> {
					for (Entity entity : world.getEntities(player, finalSearchBox)) {
						if (entity instanceof LivingEntity && entity != player) {
							// Check if the entity's bounding box intersects with our ray
							AABB entityBox = entity.getBoundingBox().inflate(0.3);
							if (entityBox.clip(finalStart, finalEnd).isPresent()) {
								// Deal damage to the entity
								DamageSource damageSource = world.damageSources().playerAttack(player);
								entity.hurt(damageSource, 8.0F); // 4 hearts of damage
							}
						}
					}
				});
			}
		}

		return InteractionResultHolder.success(itemStack);
	}
}