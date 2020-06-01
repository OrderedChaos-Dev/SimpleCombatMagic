package simplecombatmagic.magic;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FireballSpell extends MagicSpell {

	public FireballSpell(int id, String name, String resource_name, MagicSpecializationEnum spec, int cooldown) {
		super(id, name, resource_name, spec, cooldown);
	}

	@Override
	public void cast(PlayerEntity player) {
		World world = player.getEntityWorld();
		Vec3d vec = player.getLook(1.0F);
		BlockPos pos = player.getPosition();
		double posX = pos.getX() + Math.abs(vec.x);
		double posZ = pos.getZ() + Math.abs(vec.z);
        SmallFireballEntity fireball = new SmallFireballEntity(world, posX, player.getPosYHeight(0.5D) + 0.5D, posZ, vec.x, vec.y, vec.z) {
			@Override
			protected void onImpact(RayTraceResult result) {
				super.onImpact(result);
				if (!this.world.isRemote) {
					if (result.getType() == RayTraceResult.Type.ENTITY) {
						Entity entity = ((EntityRayTraceResult) result).getEntity();
						if(entity == this.shootingEntity)
							return;
						if (!entity.isImmuneToFire()) {
							int i = entity.getFireTimer();
							entity.setFire(5);
							boolean flag = entity.attackEntityFrom(
									DamageSource.causeFireballDamage(this, this.shootingEntity), 5.0F);
							if (flag) {
								this.applyEnchantments(this.shootingEntity, entity);
							} else {
								entity.setFireTimer(i);
							}
						}
						this.remove();
					} else if (this.shootingEntity == null || !(this.shootingEntity instanceof MobEntity)
							|| net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world,
									this.shootingEntity)) {
						BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult) result;
						BlockPos blockpos = blockraytraceresult.getPos().offset(blockraytraceresult.getFace());
						if (this.world.isAirBlock(blockpos)) {
							this.world.setBlockState(blockpos, Blocks.FIRE.getDefaultState());
						}
						this.remove();
					}
				}
			}
        };
        fireball.shootingEntity = player;
        world.addEntity(fireball);
        world.playSound(player, player.getPosition(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.PLAYERS, 0.5F, world.rand.nextFloat() * 0.15F + 0.6F);
	}

	@Override
	public String setDescription() {
		return "Hurls a ball of fire at the target, damaging it and setting it ablaze.";
	}
}
