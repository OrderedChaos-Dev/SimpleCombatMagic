package simplecombatmagic;

import net.minecraft.client.Minecraft;
import net.minecraft.particles.ParticleType;
import net.minecraft.potion.Effect;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import simplecombatmagic.effect.MagicEffects;
import simplecombatmagic.particle.BloodSplashParticle;
import simplecombatmagic.particle.DrippingBloodParticle;
import simplecombatmagic.particle.FrostParticle;
import simplecombatmagic.particle.MagicParticles;

@EventBusSubscriber(modid = SimpleCombatMagic.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class SimpleCombatMagicRegistry {
	
	@SubscribeEvent
	public static void registerEffects(RegistryEvent.Register<Effect> event) {
		MagicEffects.MAGIC_EFFECTS.forEach(effect -> event.getRegistry().register(effect));
	}
	
	@SubscribeEvent
	public static void registerParticles(RegistryEvent.Register<ParticleType<?>> event) {
		MagicParticles.PARTICLES.forEach(particle -> event.getRegistry().register(particle));
	}
	
	@SubscribeEvent
	public static void registerParticles(ParticleFactoryRegisterEvent event) {
		Minecraft.getInstance().particles.registerFactory(MagicParticles.DRIPPING_BLOOD, DrippingBloodParticle.DrippingBloodFactory::new);
		Minecraft.getInstance().particles.registerFactory(MagicParticles.BLOOD_SPLASH, BloodSplashParticle.Factory::new);
		Minecraft.getInstance().particles.registerFactory(MagicParticles.FROST, FrostParticle.Factory::new);
	}
}
