package simplecombatmagic.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import simplecombatmagic.SimpleCombatMagic;
import simplecombatmagic.capabilities.CombatMagic;
import simplecombatmagic.capabilities.CombatMagicInstance;

public class CombatMagicGuiOverlay extends AbstractGui {
	
	private final ResourceLocation background = new ResourceLocation(SimpleCombatMagic.MOD_ID, "textures/gui/overlay.png");
	private final int iconSize = 22, cdBarHeight = 3;
	
	@SubscribeEvent
	public void renderScreen(RenderGameOverlayEvent event) {
		if(event.getType() == ElementType.TEXT) {
			Minecraft mc = Minecraft.getInstance();
			ClientPlayerEntity player = mc.player;
			player.getCapability(CombatMagicInstance.MAGIC_SPEC).ifPresent(spec -> {
				if(spec.getMagicSpec() != null) {
					mc.getTextureManager().bindTexture(background);
					this.blit(0, 0, 0, 0, iconSize, iconSize); //render background
					int yOffset = (spec.getMagicSpec().ordinal() + 1) * iconSize;
					this.blit(0, 0, 0, yOffset, iconSize, iconSize); //render magic specialization icon
					
					//render cooldowns
					if(spec.getBasicCooldown() > 0) {
						float cdPercent = (float) (1.0 - ((float)spec.getBasicCooldown() / CombatMagic.BASIC_SPELL_COOLDOWN_TIME));
						int width = (int) (cdPercent * iconSize);
						this.blit(0, iconSize, iconSize, 0, width, cdBarHeight);
					} else {
						this.blit(0, iconSize, iconSize, cdBarHeight * 2, iconSize, cdBarHeight);
					}
					
					if(spec.getUltimateCooldown() > 0) {
						float cdPercent = (float) (1.0 - ((float)spec.getUltimateCooldown() / CombatMagic.ULTIMATE_SPELL_COOLDOWN_TIME));
						int width = (int) (cdPercent * iconSize);
						this.blit(0, iconSize + cdBarHeight, iconSize, cdBarHeight, width, cdBarHeight);
					} else {
						this.blit(0, iconSize + cdBarHeight, iconSize, cdBarHeight * 3, iconSize, cdBarHeight);
					}
				}
			});
		}
	}
}
