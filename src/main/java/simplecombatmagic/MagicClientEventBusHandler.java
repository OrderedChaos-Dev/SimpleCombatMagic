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
import simplecombatmagic.capabilities.CombatMagic;
import simplecombatmagic.capabilities.CombatMagicInstance;
import simplecombatmagic.network.MagicCapabilityNetwork;
import simplecombatmagic.network.MagicCapabilityPacket;

@Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
public class MagicClientEventBusHandler {
	
	public static final KeyBinding BASIC_SPELL_KEY = new KeyBinding(MOD_ID + ".key.basicSpellKey", GLFW_KEY_R, "key.categories." + MOD_ID);
	public static final KeyBinding ULT_SPELL_KEY = new KeyBinding(MOD_ID + ".key.ultSpellKey", GLFW_KEY_GRAVE_ACCENT, "key.categories." + MOD_ID);
		
	@SubscribeEvent
	public static void clientTick(ClientTickEvent event) {
		if(event.phase == Phase.END) {
			ClientPlayerEntity player = Minecraft.getInstance().player;
			if(BASIC_SPELL_KEY.isPressed()) { //on basic spell key press, invoke spell cast on server
				player.getCapability(CombatMagicInstance.MAGIC_SPEC).ifPresent(spec -> {
					if(spec.getBasicCooldown() <= 0) {
						spec.setBasicCooldown(CombatMagic.BASIC_SPELL_COOLDOWN_TIME);
						MagicCapabilityPacket packet = MagicCapabilityNetwork.createPacket(spec);
						MagicCapabilityNetwork.NETWORK.sendToServer(packet);
					} else { //TODO: play on cooldown soundfx if spell is on cd
						
					}
				});
			} else if(ULT_SPELL_KEY.isPressed()) { //on ultimate spell key press, invoke spell cast on server
				player.getCapability(CombatMagicInstance.MAGIC_SPEC).ifPresent(spec -> {
					if(spec.getUltimateCooldown() <= 0) {
						spec.setUltimateCooldown(CombatMagic.ULTIMATE_SPELL_COOLDOWN_TIME);
						MagicCapabilityPacket packet = MagicCapabilityNetwork.createPacket(spec);
						MagicCapabilityNetwork.NETWORK.sendToServer(packet);
					} else { //TODO: play on cooldown soundfx if spell is on cd
						
					}
				});
			}
		}
	}
}
