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
		event.player.getCapability(CombatMagicInstance.MAGIC_SPEC).ifPresent(spec -> {
			if(event.phase == Phase.END && event.side == LogicalSide.SERVER) {
				ServerPlayerEntity player = (ServerPlayerEntity) event.player;
				
				/** THIS PART HANDLES SPELL CASTING */
				//basically, if the current cooldown == the spells cooldown, cast the spell
				int[] cooldowns = spec.getCooldowns();
				MagicSpell[] spells = spec.getSpells();
				for(int i = 0; i < cooldowns.length; i++) {
					if(spells[i] != null) {
						if(cooldowns[i] == spells[i].getCooldown()) {
							spells[i].cast(player);
						}
					}
				}
				/**								   	*/
				
				spec.tickCooldowns(); //decrement cooldown timers
				MagicCapabilityPacket packet = MagicCapabilityNetwork.createPacket(spec);
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
			player.getCapability(CombatMagicInstance.MAGIC_SPEC).ifPresent(spec -> {
				MagicSpecializationEnum m = MagicSpecializationEnum.cycle(spec.getMagicSpec());
				spec.setMagicSpec(m);
				spec.setSpellAtIndex(0, MagicSpells.FIREBALL);
				spec.setSpellAtIndex(1, MagicSpells.FIREBALL);
				spec.setSpellAtIndex(2, MagicSpells.FIREBALL);
				spec.setSpellAtIndex(3, MagicSpells.FIREBALL);
				MagicCapabilityPacket packet = MagicCapabilityNetwork.createPacket(spec);
				MagicCapabilityNetwork.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)player), packet);
			});
		}
	}
	
	//capability data are not saved on death, so need to copy over
	@SubscribeEvent
	public void clone(PlayerEvent.Clone event) {
		if(event.isWasDeath()) {
			PlayerEntity player = event.getPlayer();
			event.getOriginal().getCapability(CombatMagicInstance.MAGIC_SPEC).ifPresent(oldSpec -> {
				player.getCapability(CombatMagicInstance.MAGIC_SPEC).ifPresent(spec -> {
					
					//COOLDOWNS
					for(int i = 0; i < oldSpec.getCooldowns().length; i++) {
						spec.setCooldown(i, oldSpec.getCurrentCooldownTimer(i));
					}
					
					//SPEC
					spec.setMagicSpec(oldSpec.getMagicSpec());
					
					//SELECTED SPELL
					spec.resetSpellIndex();
					
					//SPELLBOOK
					ArrayList<MagicSpell> spellbook = oldSpec.getSpellbook();
					for(MagicSpell spell : spellbook) {
						spec.addSpell(spell);
					}
					
					//SELECTED SPELLS
					MagicSpell[] spells = oldSpec.getSpells();
					for(int i = 0; i < spells.length; i++) {
						spec.setSpellAtIndex(i, spells[i]);
					}
					
					MagicCapabilityPacket packet = MagicCapabilityNetwork.createPacket(oldSpec);
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
			player.getCapability(CombatMagicInstance.MAGIC_SPEC).ifPresent(spec -> {
				MagicCapabilityPacket packet = MagicCapabilityNetwork.createPacket(spec);
				MagicCapabilityNetwork.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)player), packet);
			});
		}
	}
}
