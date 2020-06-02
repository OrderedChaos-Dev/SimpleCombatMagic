package simplecombatmagic.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DrippingBloodParticle extends SpriteTexturedParticle {
   private final Fluid fluid;

   private DrippingBloodParticle(World p_i49197_1_, double p_i49197_2_, double p_i49197_4_, double p_i49197_6_, Fluid p_i49197_8_) {
      super(p_i49197_1_, p_i49197_2_, p_i49197_4_, p_i49197_6_);
      this.setSize(0.01F, 0.01F);
      this.particleGravity = 0.06F;
      this.fluid = p_i49197_8_;
   }

   @Override
   public IParticleRenderType getRenderType() {
      return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
   }

   @Override
   public void tick() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.func_217576_g();
      if (!this.isExpired) {
         this.motionY -= (double)this.particleGravity;
         this.move(this.motionX, this.motionY, this.motionZ);
         this.func_217577_h();
         if (!this.isExpired) {
            this.motionX *= (double)0.98F;
            this.motionY *= (double)0.98F;
            this.motionZ *= (double)0.98F;
            BlockPos blockpos = new BlockPos(this.posX, this.posY, this.posZ);
            IFluidState ifluidstate = this.world.getFluidState(blockpos);
            if (ifluidstate.getFluid() == this.fluid && this.posY < (double)((float)blockpos.getY() + ifluidstate.getActualHeight(this.world, blockpos))) {
               this.setExpired();
            }

         }
      }
   }

   protected void func_217576_g() {
      if (this.maxAge-- <= 0) {
         this.setExpired();
      }

   }

   protected void func_217577_h() {
   }

   @OnlyIn(Dist.CLIENT)
   static class Dripping extends DrippingBloodParticle {
      private final IParticleData field_217579_C;

      private Dripping(World p_i50509_1_, double p_i50509_2_, double p_i50509_4_, double p_i50509_6_, Fluid p_i50509_8_, IParticleData p_i50509_9_) {
         super(p_i50509_1_, p_i50509_2_, p_i50509_4_, p_i50509_6_, p_i50509_8_);
         this.field_217579_C = p_i50509_9_;
         this.particleGravity *= 0.02F;
         this.maxAge = 40;
      }

      protected void func_217576_g() {
         if (this.maxAge-- <= 0) {
            this.setExpired();
            this.world.addParticle(this.field_217579_C, this.posX, this.posY, this.posZ, this.motionX, this.motionY, this.motionZ);
         }

      }

      protected void func_217577_h() {
         this.motionX *= 0.02D;
         this.motionY *= 0.02D;
         this.motionZ *= 0.02D;
      }
   }

   @OnlyIn(Dist.CLIENT)
   static class FallingLiquidParticle extends DrippingBloodParticle.FallingNectarParticle {
      protected final IParticleData field_228335_a_;

      private FallingLiquidParticle(World p_i225953_1_, double p_i225953_2_, double p_i225953_4_, double p_i225953_6_, Fluid p_i225953_8_, IParticleData p_i225953_9_) {
         super(p_i225953_1_, p_i225953_2_, p_i225953_4_, p_i225953_6_, p_i225953_8_);
         this.field_228335_a_ = p_i225953_9_;
      }

      protected void func_217577_h() {
         if (this.onGround) {
            this.setExpired();
            this.world.addParticle(this.field_228335_a_, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
         }

      }
   }

   @OnlyIn(Dist.CLIENT)
   static class FallingNectarParticle extends DrippingBloodParticle {
      private FallingNectarParticle(World p_i225955_1_, double p_i225955_2_, double p_i225955_4_, double p_i225955_6_, Fluid p_i225955_8_) {
         super(p_i225955_1_, p_i225955_2_, p_i225955_4_, p_i225955_6_, p_i225955_8_);
         this.maxAge = (int)(64.0D / (Math.random() * 0.8D + 0.2D));
      }

      protected void func_217577_h() {
         if (this.onGround) {
            this.setExpired();
         }

      }
   }

   @OnlyIn(Dist.CLIENT)
   public static class DrippingBloodFactory implements IParticleFactory<BasicParticleType> {
      protected final IAnimatedSprite spriteSet;

      public DrippingBloodFactory(IAnimatedSprite p_i50503_1_) {
         this.spriteSet = p_i50503_1_;
      }

      public Particle makeParticle(BasicParticleType typeIn, World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
         DrippingBloodParticle dripparticle = new DrippingBloodParticle.FallingLiquidParticle(worldIn, x, y, z, Fluids.WATER, MagicParticles.BLOOD_SPLASH);
         dripparticle.setColor(0.9F, 0.1F, 0.1F);
         dripparticle.selectSpriteRandomly(this.spriteSet);
         return dripparticle;
      }
   }

   @OnlyIn(Dist.CLIENT)
   static class Landing extends DrippingBloodParticle {
      private Landing(World p_i50507_1_, double p_i50507_2_, double p_i50507_4_, double p_i50507_6_, Fluid p_i50507_8_) {
         super(p_i50507_1_, p_i50507_2_, p_i50507_4_, p_i50507_6_, p_i50507_8_);
         this.maxAge = (int)(16.0D / (Math.random() * 0.8D + 0.2D));
      }
   }
}