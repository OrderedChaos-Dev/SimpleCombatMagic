package simplecombatmagic.effect;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import simplecombatmagic.particle.MagicParticles;


/**
 * This is where functions related to magic effects are handled, e.g. shattering frozen mobs or handling the bleed stacking
 *
 */
public class MagicEffectEvents {
	
	@SubscribeEvent
	public void attack(AttackEntityEvent event) {
		PlayerEntity player = event.getPlayer();
		Entity target = event.getTarget();
		if(!player.getEntityWorld().isRemote) {
			//SHARP THORNS
			EffectInstance effect = player.getActivePotionEffect(MagicEffects.SHARP_THORNS);
			if(effect != null) {
				int level = effect.getAmplifier();
				int duration = effect.getDuration();
				if(target instanceof LivingEntity) {
					//apply new or stack effect on target
					EffectInstance bleedEffect;
					EffectInstance currentBleedEffect = ((LivingEntity) target).getActivePotionEffect(MagicEffects.BLEEDING);
					if(currentBleedEffect != null) {
						int newLevel = currentBleedEffect.getAmplifier() + 1;
						bleedEffect = new EffectInstance(MagicEffects.BLEEDING, 10 * 20, newLevel);
					} else {
						bleedEffect = new EffectInstance(MagicEffects.BLEEDING, 10 * 20, 0);
					}
					((LivingEntity) target).addPotionEffect(bleedEffect);
					
					//decrease thorns level on successful attack
					player.removeActivePotionEffect(MagicEffects.SHARP_THORNS);	
					if(level - 1 >= 0) {
						EffectInstance thorns = new EffectInstance(MagicEffects.SHARP_THORNS, duration, level - 1);
						player.addPotionEffect(thorns);
					}
				}
			}
			//WILDFIRE
			EffectInstance wildfire = player.getActivePotionEffect(MagicEffects.WILDFIRE);
			if(wildfire != null) {
				if(target instanceof LivingEntity) {
					if(target.getFireTimer() > 0) {
						int duration = wildfire.getDuration() + 10;
						player.addPotionEffect(new EffectInstance(MagicEffects.WILDFIRE, duration, 0));
						target.setFire(5);
					}
					target.setFire(5);
				}
			}
		} else {
			player.removeActivePotionEffect(MagicEffects.SHARP_THORNS);
		}
	}
	
	@SubscribeEvent
	public void attack(LivingAttackEvent event) {
		LivingEntity entity = event.getEntityLiving();
		EffectInstance wildfire = entity.getActivePotionEffect(MagicEffects.WILDFIRE);
		if(wildfire != null) {
			if(event.getSource().isFireDamage()) {
				event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent
	public void hurt(LivingHurtEvent event) {
		LivingEntity entity = event.getEntityLiving();
		EffectInstance frozen = entity.getActivePotionEffect(MagicEffects.FROZEN);
		if(!entity.getEntityWorld().isRemote) {
			entity.removePotionEffect(MagicEffects.FROZEN);
			if(frozen != null) {
				entity.attackEntityFrom(DamageSource.MAGIC, 5.0F);
				entity.getEntityWorld().playSound(null, entity.getPosition(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS,
						4.0F, (1.0F + (entity.getRNG().nextFloat() - entity.getRNG().nextFloat()) * 0.2F) * 0.7F);
			}
		}
	}
	
	@SubscribeEvent
	public void update(LivingUpdateEvent event) {
		LivingEntity entity = event.getEntityLiving();
		
		EffectInstance frozen = entity.getActivePotionEffect(MagicEffects.FROZEN);

		if(entity instanceof MobEntity) {
			if(frozen != null) {
				((MobEntity) entity).setNoAI(true);
			} else {
				((MobEntity) entity).setNoAI(false);
			}
		} else {
			if(frozen != null) {
				entity.setVelocity(0, 0, 0);
			}
		}
	}
}
