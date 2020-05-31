package simplecombatmagic;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
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
import simplecombatmagic.magic.MagicSpec;
import simplecombatmagic.magic.MagicSpecializationEnum;
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
				
				/** THIS PART HANDLES SPELL FUNCTIONS */
				if(spec.getMagicSpec() != null) {
					if(spec.getBasicCooldown() == CombatMagic.BASIC_SPELL_COOLDOWN_TIME) {
						MagicSpec.fromSpecEnum(spec.getMagicSpec()).castBasicSpell(player);
					} else if(spec.getUltimateCooldown() == CombatMagic.ULTIMATE_SPELL_COOLDOWN_TIME) {
						MagicSpec.fromSpecEnum(spec.getMagicSpec()).castUltimateSpell(player);
					}
				}
				/**								   	*/
				
				spec.tickCooldowns();
				MagicCapabilityPacket packet = MagicCapabilityNetwork.createPacket(spec);
				MagicCapabilityNetwork.NETWORK.send(PacketDistributor.PLAYER.with(() -> player), packet);
				if(event.player.getEntityWorld().getGameTime() % 20 == 0) {
					int basicCD = spec.getBasicCooldown() / 20;
					int ultCD = spec.getUltimateCooldown() / 20;
					event.player.sendMessage(new StringTextComponent("Cooldowns: " + basicCD + " | " + ultCD));
				}
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
				player.sendMessage(new StringTextComponent("Spec: " + spec.getMagicSpec().getName()));
				MagicCapabilityPacket packet = MagicCapabilityNetwork.createPacket(spec);
				MagicCapabilityNetwork.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)player), packet);
			});
		}
	}
	
	@SubscribeEvent
	public void clone(PlayerEvent.Clone event) {
		PlayerEntity player = event.getPlayer();
		event.getOriginal().getCapability(CombatMagicInstance.MAGIC_SPEC).ifPresent(oldSpec -> {
			player.getCapability(CombatMagicInstance.MAGIC_SPEC).ifPresent(spec -> {
				if(oldSpec.getMagicSpec() != null)
					spec.setMagicSpec(oldSpec.getMagicSpec());
				spec.setBasicCooldown(oldSpec.getBasicCooldown());
				spec.setUltimateCooldown(oldSpec.getUltimateCooldown());
				if(player.isServerWorld()) {
					MagicCapabilityPacket packet = MagicCapabilityNetwork.createPacket(spec);
					MagicCapabilityNetwork.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)player), packet);
				}
			});
		});
	}
	
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
