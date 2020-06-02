package simplecombatmagic.magic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import simplecombatmagic.particle.MagicParticles;
import simplecombatmagic.util.RayTraceUtils;

public class TransfusionSpell extends MagicSpell {

	public TransfusionSpell(int id, String name, String resource_name, MagicSpecializationEnum spec, int cooldown, boolean requiresTarget) {
		super(id, name, resource_name, spec, cooldown, requiresTarget);
	}

	@Override
	public boolean cast(PlayerEntity player) {
		EntityRayTraceResult result = RayTraceUtils.getMouseOverEntityInRange(player, 15.0);
		if(result != null) {
			Entity entity = result.getEntity();
			if(entity instanceof LivingEntity) {
				player.swingArm(Hand.MAIN_HAND);
				entity.attackEntityFrom(DamageSource.causePlayerDamage(player), 2.0F);
				player.heal(2.0F);

				
				float particleIncr = 40;
				BlockPos pos = player.getPosition();
				BlockPos pos2 = entity.getPosition();
				double xDiff = (pos.getX() - pos2.getX()) / particleIncr;
				double yDiff = (pos.getY() - pos2.getY()) / particleIncr;
				double zDiff = (pos.getZ() - pos2.getZ()) / particleIncr;
				World world = player.getEntityWorld();

				for(int i = 0; i < particleIncr; i++) {
					double xCoord = pos2.getX() + (xDiff * i) + 0.5;
					double yCoord = pos2.getY() + (yDiff * i) + 0.5;
					double zCoord = pos2.getZ() + (zDiff * i) + 0.5;
					world.addParticle(MagicParticles.DRIPPING_BLOOD, xCoord, yCoord, zCoord, 0, 0, 0);
				}
				return true;
			}
		} else {
			return false;
		}
		return false;
	}

	@Override
	public String setDescription() {
		return "Steals 1 heart of health from the target.";
	}
	
	@Override
	public IParticleData getTargetParticle() {
		return MagicParticles.DRIPPING_BLOOD;
	}
}
