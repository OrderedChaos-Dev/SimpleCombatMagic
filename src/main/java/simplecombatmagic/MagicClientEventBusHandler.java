package simplecombatmagic;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_GRAVE_ACCENT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;
import static simplecombatmagic.SimpleCombatMagic.MOD_ID;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import simplecombatmagic.capabilities.CombatMagicInstance;
import simplecombatmagic.magic.MagicSpell;
import simplecombatmagic.magic.MagicSpells;
import simplecombatmagic.network.MagicCapabilityNetwork;
import simplecombatmagic.network.MagicCapabilityPacket;
import simplecombatmagic.util.RayTraceUtils;

@Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
public class MagicClientEventBusHandler {
	
	public static final KeyBinding CYCLE_SPELL_KEY = new KeyBinding(MOD_ID + ".key.cycleSpellKey", GLFW_KEY_GRAVE_ACCENT, "key.categories." + MOD_ID);
	public static final KeyBinding CAST_SPELL_KEY = new KeyBinding(MOD_ID + ".key.castSpellKey", GLFW_KEY_R, "key.categories." + MOD_ID);
	
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
										spells[i].cast(player);
									}
								}
							}
							MagicCapabilityPacket packet = MagicCapabilityNetwork.createPacket(instance);
							MagicCapabilityNetwork.NETWORK.sendToServer(packet);
						} else {
							//TODO: play failure to cast spell soundfx
						}
					});
				}
				
				player.getCapability(CombatMagicInstance.COMBAT_MAGIC).ifPresent(instance -> {
					if(instance.getSpells()[instance.getSelectedSpellIndex()] == MagicSpells.TRANSFUSION) {
						EntityRayTraceResult result = RayTraceUtils.getMouseOverEntityInRange(player, 15.0);
						if(result != null) {
							Entity entity = result.getEntity();
							if(entity instanceof LivingEntity) {
								entity.getEntityWorld().addParticle(ParticleTypes.SMOKE,
										entity.getPosX(), entity.getPosY() + entity.getHeight() + 0.5, entity.getPosZ(), 0, 0, 0);
							}
						}
					}
				});
			}
		}
	}
}
