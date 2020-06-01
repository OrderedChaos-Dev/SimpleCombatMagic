package simplecombatmagic.magic;

import net.minecraft.entity.player.PlayerEntity;

public class TransfusionSpell extends MagicSpell {

	public TransfusionSpell(int id, String name, String resource_name, MagicSpecializationEnum spec, int cooldown) {
		super(id, name, resource_name, spec, cooldown);
	}

	@Override
	public void cast(PlayerEntity player) {
		
	}

	@Override
	public String setDescription() {
		return "Empowers your next 3 attacks to cause the target to bleed over time. This effect stacks.";
	}
}
