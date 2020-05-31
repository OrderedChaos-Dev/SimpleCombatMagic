package simplecombatmagic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import simplecombatmagic.capabilities.CombatMagic;
import simplecombatmagic.capabilities.CombatMagicInstance;
import simplecombatmagic.capabilities.MagicSpecializationEnum;

public class SimpleCombatMagicEventBusHandler {
	
	@SubscribeEvent
	public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
		if(event.getObject() instanceof PlayerEntity) {
			event.addCapability(CombatMagic.COMBAT_MAGIC_RESOURCE, new CombatMagicInstance());
		}
	}
	
	@SubscribeEvent
	public void playerTick(TickEvent.PlayerTickEvent event) {
		event.player.getCapability(CombatMagicInstance.MAGIC_SPEC).ifPresent(spec -> {
			spec.tickCooldowns();
		});
	}
	
//	 @SubscribeEvent
//	 public void onPlayerSleep(PlayerSleepInBedEvent event) {
//		event.getPlayer().getCapability(CombatMagicInstance.MAGIC_SPEC).ifPresent(spec -> {
//			spec.setMagicSpec(MagicSpecializationEnum.FIRE);
//			event.getPlayer().sendMessage(new StringTextComponent("Magic Spec: " + spec.getMagicSpec().getName()));
//		});
//	 }
}
