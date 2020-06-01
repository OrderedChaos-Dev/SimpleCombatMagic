package simplecombatmagic.effect;

import java.util.ArrayList;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import simplecombatmagic.SimpleCombatMagic;

public class MagicEffects {
	public static final String WILDFIRE_UUID = "56783c9f-fd75-4825-8fd6-f5bdec037b6b";
	
	public static final ArrayList<Effect> MAGIC_EFFECTS = new ArrayList<Effect>();
	
	public static final Effect SHARP_THORNS = registerEffect(new MagicEffect(EffectType.BENEFICIAL), "sharp_thorns");
	public static final Effect BLEEDING = registerEffect(new BleedingEffect(EffectType.HARMFUL), "bleeding");
	public static final Effect WILDFIRE = registerEffect(new MagicEffect(EffectType.BENEFICIAL)
											.addAttributesModifier(SharedMonsterAttributes.MOVEMENT_SPEED, WILDFIRE_UUID, (double)0.2F, AttributeModifier.Operation.MULTIPLY_TOTAL), "wildfire");
	
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
