package simplecombatmagic.particle;

import java.util.ArrayList;

import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import simplecombatmagic.SimpleCombatMagic;

public class MagicParticles {
	public static final ArrayList<ParticleType<?>> PARTICLES = new ArrayList<ParticleType<?>>();
	
	public static final BasicParticleType DRIPPING_BLOOD = register(new BasicParticleType(false), "dripping_blood");
	public static final BasicParticleType BLOOD_SPLASH = register(new BasicParticleType(false), "blood_splash");
	public static final BasicParticleType FROST = register(new BasicParticleType(false), "frost");
	
	public static BasicParticleType register(ParticleType<?> type, String name) {
		type.setRegistryName(new ResourceLocation(SimpleCombatMagic.MOD_ID, name));
		PARTICLES.add(type);
		return (BasicParticleType) type;
	}
}
