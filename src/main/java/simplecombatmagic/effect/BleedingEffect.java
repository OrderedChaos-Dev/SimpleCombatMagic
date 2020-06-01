package simplecombatmagic.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;
import simplecombatmagic.effect.MagicEffects.MagicEffect;

public class BleedingEffect extends MagicEffect {

	public BleedingEffect(EffectType typeIn) {
		super(typeIn);
	}

	@Override
	public void performEffect(LivingEntity entity, int amplifier) {
		entity.attackEntityFrom(DamageSource.MAGIC, 1.0F);
	}
	
	@Override
	public boolean isReady(int duration, int amplifier) {
		int j = 40 >> amplifier;
		if (j > 0) {
			return duration % j == 0;
		} else {
			return true;
		}
	}
}
