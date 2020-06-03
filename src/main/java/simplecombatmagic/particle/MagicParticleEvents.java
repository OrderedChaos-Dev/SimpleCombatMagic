package simplecombatmagic.particle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import simplecombatmagic.capabilities.CombatMagicInstance;
import simplecombatmagic.effect.MagicEffects;
import simplecombatmagic.magic.MagicSpell;
import simplecombatmagic.util.RayTraceUtils;

/**
 * This is to handle client-side particles
 *
 */
public class MagicParticleEvents {

	private Random rand = new Random();
	
	//handle spawning particles around targeted mob for spell targeting
	@SubscribeEvent
	public void clientTick(ClientTickEvent event) {
		if(event.phase == Phase.END) {
			ClientPlayerEntity player = Minecraft.getInstance().player;
			if(player != null) {
				player.getCapability(CombatMagicInstance.COMBAT_MAGIC).ifPresent(instance -> {
					MagicSpell spell = instance.getSpells()[instance.getSelectedSpellIndex()];
					if(spell != null) {
						if(spell.requiresTarget()) {
							EntityRayTraceResult result = RayTraceUtils.getMouseOverEntityInRange(player, 15.0);
							if(result != null) {
								Entity entity = result.getEntity();
								if(entity instanceof LivingEntity) {
									if(entity.getEntityWorld().getGameTime() % 5 == 0) {
										double x = entity.getPosX() + 0.7 * (rand.nextFloat() - rand.nextFloat());
										double y = entity.getPosY() + entity.getHeight() + 0.5;
										double z = entity.getPosZ() + 0.7 * (rand.nextFloat() - rand.nextFloat());
										addParticle(instance.getSelectedSpell().getTargetParticle(), x, y, z, 0, 0, 0);
									}
								}
							}
						}
					}
				});
			}
		}
	}
	
	@SubscribeEvent
	public void update(LivingUpdateEvent event) {
		LivingEntity entity = event.getEntityLiving();
		List<EffectInstance> effects = new ArrayList<EffectInstance>(entity.getActivePotionEffects());
		for(EffectInstance effectinstance : effects) {
			Effect effect = effectinstance.getPotion();
			if(effect == MagicEffects.FROZEN) {
				for(int i = 0; i < 5; i++) {
					double x = entity.getPosXRandom(1.0);
					double y = entity.getPosYRandom();
					double z = entity.getPosZRandom(1.0);
					addParticle(MagicParticles.FROST, x, y, z, 0, 0, 0);
				}
			} else if(effect == MagicEffects.BLEEDING) {
				for(int i = 0; i < effectinstance.getAmplifier() + 1; i++) {
					double x = entity.getPosXRandom(1.0);
					double y = entity.getPosYRandom();
					double z = entity.getPosZRandom(1.0);
					addParticle(MagicParticles.BLOOD_SPLASH, x, y, z, 0, 0, 0);
				}
			}
		}
	}
	
	/**
	 * grabs instance of client world to spawn particle
	 */
	private static void addParticle(IParticleData particle, double x, double y, double z, double accX, double accY, double accZ) {
		Minecraft.getInstance().world.addParticle(particle, x, y, z, accX, accY, accZ);
	}
}
