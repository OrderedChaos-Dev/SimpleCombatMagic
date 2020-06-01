package simplecombatmagic.magic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import simplecombatmagic.util.RayTraceUtils;

public class TransfusionSpell extends MagicSpell {

	public TransfusionSpell(int id, String name, String resource_name, MagicSpecializationEnum spec, int cooldown, boolean requiresTarget) {
		super(id, name, resource_name, spec, cooldown, requiresTarget);
	}

	@Override
	public void cast(PlayerEntity player) {
		EntityRayTraceResult result = RayTraceUtils.getMouseOverEntityInRange(player, 15.0);
		if(result != null) {
			Entity entity = result.getEntity();
			if(entity instanceof LivingEntity) {
				entity.attackEntityFrom(DamageSource.causePlayerDamage(player), 2.0F);
				player.heal(2.0F);
			}
		}
	}

	@Override
	public String setDescription() {
		return "Steals 1 heart of health from the target.";
	}
}
