package simplecombatmagic;

import java.util.ArrayList;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.PacketDistributor;
import simplecombatmagic.capabilities.CombatMagic;
import simplecombatmagic.capabilities.CombatMagicInstance;
import simplecombatmagic.magic.MagicSpecializationEnum;
import simplecombatmagic.magic.MagicSpell;
import simplecombatmagic.magic.MagicSpells;
import simplecombatmagic.network.MagicCapabilityNetwork;
import simplecombatmagic.network.MagicCapabilityPacket;

public class MagicEventBusHandler {
	
	@SubscribeEvent
	public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
		if(event.getObject() instanceof PlayerEntity) {
			event.addCapability(CombatMagic.COMBAT_MAGIC_RESOURCE, new CombatMagicInstance());
		}
	}
	
	@SubscribeEvent
	public void playerTick(TickEvent.PlayerTickEvent event) {
		event.player.getCapability(CombatMagicInstance.COMBAT_MAGIC).ifPresent(instance -> {
			if(event.phase == Phase.END && event.side == LogicalSide.SERVER) {
				ServerPlayerEntity player = (ServerPlayerEntity) event.player;
				
				/** THIS PART HANDLES SPELL CASTING */
				//basically, if the current cooldown == the spells cooldown, cast the spell
				int[] cooldowns = instance.getCooldowns();
				MagicSpell[] spells = instance.getSpells();
				for(int i = 0; i < cooldowns.length; i++) {
					if(spells[i] != null) {
						if(cooldowns[i] == spells[i].getCooldown()) {
							spells[i].cast(player);
						}
					}
				}
				/**								   	*/
				
				instance.tickCooldowns(); //decrement cooldown timers
				MagicCapabilityPacket packet = MagicCapabilityNetwork.createPacket(instance);
				MagicCapabilityNetwork.NETWORK.send(PacketDistributor.PLAYER.with(() -> player), packet);
			}
		});
	}
	
	@SubscribeEvent
	public void interact(PlayerInteractEvent.RightClickBlock event) {
		PlayerEntity player = event.getPlayer();
		//for some reason using player.isServerWorld() still passes on client
		//placeholder for now
		if(event.getSide() == LogicalSide.SERVER && event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.OAK_PLANKS) {
			player.getCapability(CombatMagicInstance.COMBAT_MAGIC).ifPresent(instance -> {
				MagicSpecializationEnum m = MagicSpecializationEnum.cycle(instance.getMagicSpec());
				instance.setMagicSpec(m);
				instance.setSpellAtIndex(0, MagicSpells.FIREBALL);
				instance.setSpellAtIndex(1, MagicSpells.FIREBALL);
				instance.setSpellAtIndex(2, MagicSpells.FIREBALL);
				instance.setSpellAtIndex(3, MagicSpells.FIREBALL);
				MagicCapabilityPacket packet = MagicCapabilityNetwork.createPacket(instance);
				MagicCapabilityNetwork.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)player), packet);
			});
		}
	}
	
	//capability data are not saved on death, so need to copy over
	@SubscribeEvent
	public void clone(PlayerEvent.Clone event) {
		if(event.isWasDeath()) {
			PlayerEntity player = event.getPlayer();
			event.getOriginal().getCapability(CombatMagicInstance.COMBAT_MAGIC).ifPresent(oldInstance -> {
				player.getCapability(CombatMagicInstance.COMBAT_MAGIC).ifPresent(newInstance -> {
					
					//COOLDOWNS
					for(int i = 0; i < oldInstance.getCooldowns().length; i++) {
						newInstance.setCooldown(i, oldInstance.getCurrentCooldownTimer(i));
					}
					
					//SPEC
					newInstance.setMagicSpec(oldInstance.getMagicSpec());
					
					//SELECTED SPELL
					newInstance.resetSpellIndex();
					
					//SPELLBOOK
					ArrayList<MagicSpell> spellbook = oldInstance.getSpellbook();
					for(MagicSpell spell : spellbook) {
						newInstance.addSpell(spell);
					}
					
					//SELECTED SPELLS
					MagicSpell[] spells = oldInstance.getSpells();
					for(int i = 0; i < spells.length; i++) {
						newInstance.setSpellAtIndex(i, spells[i]);
					}
					
					MagicCapabilityPacket packet = MagicCapabilityNetwork.createPacket(oldInstance);
					if(player.isServerWorld()) {
						MagicCapabilityNetwork.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)player), packet);
					}
				});
			});
		}
	}
	
	//sync to client on log in
	@SubscribeEvent
	public void login(PlayerEvent.PlayerLoggedInEvent event) {
		PlayerEntity player = event.getPlayer();
		if(player.isServerWorld()) {
			player.getCapability(CombatMagicInstance.COMBAT_MAGIC).ifPresent(instance -> {
				MagicCapabilityPacket packet = MagicCapabilityNetwork.createPacket(instance);
				MagicCapabilityNetwork.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)player), packet);
			});
		}
	}
	
	//sleeping resets cooldowns
	@SubscribeEvent
	public void wakeup(PlayerWakeUpEvent event) {
		PlayerEntity player = event.getPlayer();
		if(player.isServerWorld()) {
			player.getCapability(CombatMagicInstance.COMBAT_MAGIC).ifPresent(instance -> {
				instance.resetCooldowns();
				MagicCapabilityPacket packet = MagicCapabilityNetwork.createPacket(instance);
				MagicCapabilityNetwork.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)player), packet);
			});
		}
	}
}
