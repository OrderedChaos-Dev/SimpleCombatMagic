package simplecombatmagic.magic;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import simplecombatmagic.effect.MagicEffects;
import simplecombatmagic.particle.MagicParticles;

public class GlacialBurstSpell extends MagicSpell {

	public GlacialBurstSpell(int id, String name, String resource_name, MagicSpecializationEnum spec, int cooldown, boolean requiresTarget) {
		super(id, name, resource_name, spec, cooldown, requiresTarget);
	}

	@Override
	public boolean cast(PlayerEntity player) {
		World world = player.getEntityWorld();
		Random rand = world.getRandom();
		int size = 3;
        for(int i = -size; i <= size; ++i) {
            for(int j = -size; j <= size; ++j) {
               for(int k = -size; k <= size; ++k) {
                  double d3 = (double)j + (rand.nextDouble() - rand.nextDouble()) * 0.5D;
                  double d4 = (double)i + (rand.nextDouble() - rand.nextDouble()) * 0.5D;
                  double d5 = (double)k + (rand.nextDouble() - rand.nextDouble()) * 0.5D;
                  double d6 = (double)MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5) / 0.3 + rand.nextGaussian() * 0.05D;
                  world.addParticle(MagicParticles.FROST, player.getPosX(), player.getPosYEye(), player.getPosZ(), d3 / d6, d4 / d6, d5 / d6);
                  if (i != -size && i != size && j != -size && j != size) {
                     k += size * 2 - 1;
                  }
               }
            }
         }
		
		BlockPos posStart = player.getPosition().add(-(size + 2), -(size + 2), -(size + 2));
		BlockPos posEnd = player.getPosition().add(size + 2, size + 2, size + 2);
		
		if(!player.getEntityWorld().isRemote) {
			List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(player, new AxisAlignedBB(posStart, posEnd));
			for(Entity entity : entities) {
				if(entity instanceof LivingEntity) {
					entity.attackEntityFrom(DamageSource.causeExplosionDamage(player), 2.0F);
					((LivingEntity) entity).addPotionEffect(new EffectInstance(MagicEffects.FROZEN, 200, 0));
				}
			}
		}
        
		return true;
	}

	@Override
	public String setDescription() {
		return "Immediately freezes all nearby targets. Attacking frozen mobs shatters them for 2.5 hearts.";
	}
}
