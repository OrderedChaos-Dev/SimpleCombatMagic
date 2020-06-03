package simplecombatmagic.effect;

import java.util.ArrayList;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import simplecombatmagic.SimpleCombatMagic;

public class MagicEffects {
	public static final String WILDFIRE_UUID = "56783c9f-fd75-4825-8fd6-f5bdec037b6b";
	public static final String WINDWALKER_UUID = "fca93b31-05b5-4667-8754-b2bb0280b0bd";
	
	public static final ArrayList<Effect> MAGIC_EFFECTS = new ArrayList<Effect>();
	
	public static final Effect BURNING_SOUL = registerEffect(new MagicEffect(EffectType.BENEFICIAL), "burning_soul");
	public static final Effect MASTER_OF_THE_SEAS = registerEffect(new MagicEffect(EffectType.BENEFICIAL), "master_of_the_seas");
	public static final Effect ONE_WITH_THE_EARTH = registerEffect(new MagicEffect(EffectType.BENEFICIAL), "one_with_the_earth");
	public static final Effect WINDWALKER = registerEffect(new MagicEffect(EffectType.BENEFICIAL)
						.addAttributesModifier(SharedMonsterAttributes.MOVEMENT_SPEED, WINDWALKER_UUID, 0.1, Operation.MULTIPLY_BASE), "windwalker");
	public static final Effect COLD_BLOODED = registerEffect(new MagicEffect(EffectType.BENEFICIAL), "cold_blooded");
	public static final Effect SOUL_KEEPER = registerEffect(new MagicEffect(EffectType.BENEFICIAL), "soul_keeper");
	public static final Effect BLOOD_PACT = registerEffect(new MagicEffect(EffectType.BENEFICIAL), "blood_pact");
	public static final Effect LIFEBOUND = registerEffect(new MagicEffect(EffectType.BENEFICIAL), "lifebound");
	
	public static final Effect SHARP_THORNS = registerEffect(new MagicEffect(EffectType.BENEFICIAL), "sharp_thorns");
	public static final Effect BLEEDING = registerEffect(new BleedingEffect(EffectType.HARMFUL), "bleeding");
	public static final Effect WILDFIRE = registerEffect(new MagicEffect(EffectType.BENEFICIAL)
						.addAttributesModifier(SharedMonsterAttributes.MOVEMENT_SPEED, WILDFIRE_UUID, 0.2, Operation.MULTIPLY_TOTAL), "wildfire");
	public static final Effect FROZEN = registerEffect(new MagicEffect(EffectType.HARMFUL), "frozen");
	
	public static Effect registerEffect(Effect effect, String name) {
		effect.setRegistryName(new ResourceLocation(SimpleCombatMagic.MOD_ID, name));
		MAGIC_EFFECTS.add(effect);
		return effect;
	}
	
	public static class MagicEffect extends Effect {
		public MagicEffect(EffectType typeIn) {
			super(typeIn, 0);
		}
	}
}
