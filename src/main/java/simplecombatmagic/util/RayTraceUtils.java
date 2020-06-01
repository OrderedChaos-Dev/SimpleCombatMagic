package simplecombatmagic.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class RayTraceUtils {
	
	public static EntityRayTraceResult getMouseOverEntityInRange(PlayerEntity player, double range) {
		RayTraceResult result = player.pick(10, 1.0F, true);
		EntityRayTraceResult entityraytraceresult = null;
		if(result != null) {
			if(result instanceof BlockRayTraceResult) {
	            double d0 = range;
	            Vec3d vec3d = player.getEyePosition(1.0F);
	            double d1 = d0;
	            d1 = d1 * d1;
	            d1 = result.getHitVec().squareDistanceTo(vec3d);

	            Vec3d vec3d1 = player.getLook(1.0F);
	            Vec3d vec3d2 = vec3d.add(vec3d1.x * d0, vec3d1.y * d0, vec3d1.z * d0);
	            AxisAlignedBB axisalignedbb = player.getBoundingBox().expand(vec3d1.scale(d0)).grow(1.0D, 1.0D, 1.0D);
	            entityraytraceresult = ProjectileHelper.rayTraceEntities(player, vec3d, vec3d2, axisalignedbb, (e) -> {
	               return !e.isSpectator() && e.canBeCollidedWith();
	            }, d1);

	            if (entityraytraceresult != null) {
	               Entity entity1 = entityraytraceresult.getEntity();
	               Vec3d vec3d3 = entityraytraceresult.getHitVec();
	               double d2 = vec3d.squareDistanceTo(vec3d3);
	               if (d2 < d1 || result == null) {
	                  result = entityraytraceresult;
	               }
	            }
			}
		}
        return entityraytraceresult;
	}
}
