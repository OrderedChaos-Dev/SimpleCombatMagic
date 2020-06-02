package simplecombatmagic.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FrostParticle extends SpriteTexturedParticle {
   private FrostParticle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
      super(worldIn, xCoordIn, yCoordIn, zCoordIn);
      this.motionX = this.motionX * (double)0.01F + xSpeedIn;
      this.motionY = this.motionY * (double)0.01F + ySpeedIn;
      this.motionZ = this.motionZ * (double)0.01F + zSpeedIn;
      this.posX += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
      this.posY += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
      this.posZ += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
      this.maxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
   }

   public void tick() {
	      this.prevPosX = this.posX;
	      this.prevPosY = this.posY;
	      this.prevPosZ = this.posZ;
	      if (this.age++ >= this.maxAge) {
	         this.setExpired();
	      } else {
	         this.move(this.motionX, this.motionY, this.motionZ);
	         this.motionX *= (double)0.96F;
	         this.motionY *= (double)0.96F;
	         this.motionZ *= (double)0.96F;
	         if (this.onGround) {
	            this.motionX *= (double)0.7F;
	            this.motionZ *= (double)0.7F;
	         }

	      }
	   }

   public IParticleRenderType getRenderType() {
      return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
   }

   @OnlyIn(Dist.CLIENT)
   public static class Factory implements IParticleFactory<BasicParticleType> {
      private final IAnimatedSprite spriteSet;

      public Factory(IAnimatedSprite p_i50227_1_) {
         this.spriteSet = p_i50227_1_;
      }

      public Particle makeParticle(BasicParticleType typeIn, World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
         FrostParticle particle = new FrostParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
         particle.selectSpriteRandomly(this.spriteSet);
         return particle;
      }
   }
}