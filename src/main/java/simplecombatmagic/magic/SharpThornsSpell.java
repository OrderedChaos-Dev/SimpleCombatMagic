package simplecombatmagic.magic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import simplecombatmagic.effect.MagicEffects;

public class SharpThornsSpell extends MagicSpell {

	public SharpThornsSpell(int id, String name, String resource_name, MagicSpecializationEnum spec, int cooldown, boolean requiresTarget) {
		super(id, name, resource_name, spec, cooldown, requiresTarget);
	}

	@Override
	public boolean cast(PlayerEntity player) {
		EffectInstance effect = new EffectInstance(MagicEffects.SHARP_THORNS, 600, 2);
		player.addPotionEffect(effect);
		return true;
	}

	@Override
	public String setDescription() {
		return "Empowers your next 3 attacks to cause the target to bleed over time. This effect stacks.";
	}
}
