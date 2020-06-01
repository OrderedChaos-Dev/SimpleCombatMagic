package simplecombatmagic.magic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class FireballSpell extends MagicSpell {

	public FireballSpell(int id, String name, MagicSpecializationEnum spec, int cooldown) {
		super(id, name, spec, cooldown);
	}

	@Override
	public void cast(PlayerEntity player) {
		player.sendMessage(new StringTextComponent("Fireball"));
	}

	@Override
	public String setDescription() {
		return "Hurls a ball of fire at the target, damaging it and setting it ablaze.";
	}
}
