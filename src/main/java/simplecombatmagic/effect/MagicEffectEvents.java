package simplecombatmagic.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MagicEffectEvents {
	
	@SubscribeEvent
	public void attack(AttackEntityEvent event) {
		PlayerEntity player = event.getPlayer();
		Entity target = event.getTarget();
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
				int duration = wildfire.getDuration() + 10;
				player.addPotionEffect(new EffectInstance(MagicEffects.WILDFIRE, duration, 0));
				target.setFire(5);
			}
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
}
