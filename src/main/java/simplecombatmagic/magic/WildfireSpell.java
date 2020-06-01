package simplecombatmagic.magic;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import simplecombatmagic.effect.MagicEffects;

public class WildfireSpell extends MagicSpell {

	public WildfireSpell(int id, String name, String resource_name, MagicSpecializationEnum spec, int cooldown, boolean requiresTarget) {
		super(id, name, resource_name, spec, cooldown, requiresTarget);
	}

	@Override
	public void cast(PlayerEntity player) {
		World world = player.getEntityWorld();
		Random rand = world.getRandom();
		world.playSound(player, player.getPosition(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS,
				4.0F, (1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.2F) * 0.7F);
//		for(int i = 0; i < 500; i++) {
//			
//			double xSpeed = world.getRandom().nextFloat() ;
//			double ySpeed = Math.sin(i) * 0.6;
//			double zSpeed = Math.cos(i) * 0.6;
//			world.addParticle(ParticleTypes.FLAME, player.getPosX(), player.getPosYEye(), player.getPosZ(), xSpeed, ySpeed, zSpeed);
//		}
		
		//took this from fireworks rocket particle to make a ball of fire
		int size = 3;
        for(int i = -size; i <= size; ++i) {
            for(int j = -size; j <= size; ++j) {
               for(int k = -size; k <= size; ++k) {
                  double d3 = (double)j + (rand.nextDouble() - rand.nextDouble()) * 0.5D;
                  double d4 = (double)i + (rand.nextDouble() - rand.nextDouble()) * 0.5D;
                  double d5 = (double)k + (rand.nextDouble() - rand.nextDouble()) * 0.5D;
                  double d6 = (double)MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5) / 0.3 + rand.nextGaussian() * 0.05D;
                  world.addParticle(ParticleTypes.FLAME, player.getPosX(), player.getPosYEye(), player.getPosZ(), d3 / d6, d4 / d6, d5 / d6);
                  if (i != -size && i != size && j != -size && j != size) {
                     k += size * 2 - 1;
                  }
               }
            }
         }
		
		BlockPos posStart = player.getPosition().add(-(size + 2), -(size + 2), -(size + 2));
		BlockPos posEnd = player.getPosition().add(size + 2, size + 2, size + 2);
		List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(player, new AxisAlignedBB(posStart, posEnd));
		for(Entity entity : entities) {
			if(entity instanceof LivingEntity) {
				entity.attackEntityFrom(DamageSource.causeExplosionDamage(player), 2.0F);
				entity.setFire(10);
			}
		}
		
		player.addPotionEffect(new EffectInstance(MagicEffects.WILDFIRE, 15 * 20, 0));
	}

	@Override
	public String setDescription() {
		return "Instantly burst into flames, damaging and setting nearby mobs on fire. Grants a burst of speed and fire resistance for 15 seconds. "
				+ "During this duration all your attacks set the target on fire. Attacking targets already on fire increases the duration of this effect "
				+ "by 0.5 seconds.";
	}
}
