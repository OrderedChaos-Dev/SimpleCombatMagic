package simplecombatmagic;

import net.minecraft.potion.Effect;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import simplecombatmagic.effect.MagicEffects;

@EventBusSubscriber(modid = SimpleCombatMagic.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class SimpleCombatMagicRegistry {
	
	@SubscribeEvent
	public static void registerEffects(RegistryEvent.Register<Effect> event) {
		MagicEffects.MAGIC_EFFECTS.forEach(effect -> event.getRegistry().register(effect));
	}
}
