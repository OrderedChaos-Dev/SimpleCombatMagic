package simplecombatmagic.magic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import simplecombatmagic.effect.MagicEffects;

public class SharpThornsSpell extends MagicSpell {

	public SharpThornsSpell(int id, String name, String resource_name, MagicSpecializationEnum spec, int cooldown) {
		super(id, name, resource_name, spec, cooldown);
	}

	@Override
	public void cast(PlayerEntity player) {
		EffectInstance effect = new EffectInstance(MagicEffects.SHARP_THORNS, 600, 2);
		player.addPotionEffect(effect);
	}

	@Override
	public String setDescription() {
		return "Steals 1 heart of health from the target.";
	}
}
