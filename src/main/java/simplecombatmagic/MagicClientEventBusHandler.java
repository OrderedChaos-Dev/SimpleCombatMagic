package simplecombatmagic;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_GRAVE_ACCENT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_P;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;
import static simplecombatmagic.SimpleCombatMagic.MOD_ID;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import simplecombatmagic.capabilities.CombatMagicInstance;
import simplecombatmagic.client.SpellbookGuiScreen;
import simplecombatmagic.magic.MagicSpell;
import simplecombatmagic.network.MagicCapabilityNetwork;
import simplecombatmagic.network.MagicCapabilityPacket;

@Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
public class MagicClientEventBusHandler {
	
	public static final KeyBinding CYCLE_SPELL_KEY = new KeyBinding(MOD_ID + ".key.cycleSpellKey", GLFW_KEY_GRAVE_ACCENT, "key.categories." + MOD_ID);
	public static final KeyBinding CAST_SPELL_KEY = new KeyBinding(MOD_ID + ".key.castSpellKey", GLFW_KEY_R, "key.categories." + MOD_ID);
	public static final KeyBinding SPELLBOOK_KEY = new KeyBinding(MOD_ID + ".key.spellbookKey", GLFW_KEY_P, "key.categories." + MOD_ID);
	
	private static final Random rand = new Random();
	
	@SubscribeEvent
	public static void clientTick(ClientTickEvent event) {
		if(event.phase == Phase.END) {
			ClientPlayerEntity player = Minecraft.getInstance().player;
			if(player != null) {
				if(CYCLE_SPELL_KEY.isPressed()) { //cycle through spells
					player.getCapability(CombatMagicInstance.COMBAT_MAGIC).ifPresent(instance -> {
						instance.cycleSpellIndex();
						MagicCapabilityPacket packet = MagicCapabilityNetwork.createPacket(instance);
						MagicCapabilityNetwork.NETWORK.sendToServer(packet);
					});
				} else if(CAST_SPELL_KEY.isPressed()) { //casts currently selected spell
					player.getCapability(CombatMagicInstance.COMBAT_MAGIC).ifPresent(instance -> {
						int index = instance.getSelectedSpellIndex();
						if(instance.getCurrentCooldownTimer(index) <= 0) {
							instance.putOnCooldown(index);
							int[] cooldowns = instance.getCooldowns();
							MagicSpell[] spells = instance.getSpells();
							for(int i = 0; i < cooldowns.length; i++) {
								if(spells[i] != null) {
									if(cooldowns[i] == spells[i].getCooldown()) {
										if(!spells[i].cast(player)) {
											instance.setCooldown(i, 0);
										}
									}
								}
							}
							MagicCapabilityPacket packet = MagicCapabilityNetwork.createPacket(instance);
							MagicCapabilityNetwork.NETWORK.sendToServer(packet);
						} else {
							//TODO: play failure to cast spell soundfx
						}
					});
				} else if(SPELLBOOK_KEY.isPressed()) {
					StringTextComponent title = new StringTextComponent("Spellbook");
					Minecraft.getInstance().displayGuiScreen(new SpellbookGuiScreen(title, player));
				}
			}
		}
	}
}
