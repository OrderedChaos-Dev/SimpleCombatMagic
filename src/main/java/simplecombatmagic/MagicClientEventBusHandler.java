package simplecombatmagic;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_GRAVE_ACCENT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;
import static simplecombatmagic.SimpleCombatMagic.MOD_ID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import simplecombatmagic.capabilities.CombatMagicInstance;
import simplecombatmagic.network.MagicCapabilityNetwork;
import simplecombatmagic.network.MagicCapabilityPacket;

@Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
public class MagicClientEventBusHandler {
	
	public static final KeyBinding CYCLE_SPELL_KEY = new KeyBinding(MOD_ID + ".key.cycleSpellKey", GLFW_KEY_GRAVE_ACCENT, "key.categories." + MOD_ID);
	public static final KeyBinding CAST_SPELL_KEY = new KeyBinding(MOD_ID + ".key.castSpellKey", GLFW_KEY_R, "key.categories." + MOD_ID);
	
	@SubscribeEvent
	public static void clientTick(ClientTickEvent event) {
		if(event.phase == Phase.END) {
			ClientPlayerEntity player = Minecraft.getInstance().player;
			if(CYCLE_SPELL_KEY.isPressed()) { //cycle through spells
				player.getCapability(CombatMagicInstance.MAGIC_SPEC).ifPresent(spec -> {
					spec.cycleSpellIndex();
					MagicCapabilityPacket packet = MagicCapabilityNetwork.createPacket(spec);
					MagicCapabilityNetwork.NETWORK.sendToServer(packet);
				});
			} else if(CAST_SPELL_KEY.isPressed()) { //casts currently selected spell
				player.getCapability(CombatMagicInstance.MAGIC_SPEC).ifPresent(spec -> {
					int index = spec.getSelectedSpellIndex();
					if(spec.getCurrentCooldownTimer(index) <= 0) {
						spec.putOnCooldown(index);
						MagicCapabilityPacket packet = MagicCapabilityNetwork.createPacket(spec);
						MagicCapabilityNetwork.NETWORK.sendToServer(packet);
					} else {
						//TODO: play failure to cast spell soundfx
					}
				});
			}
		}
	}
}
