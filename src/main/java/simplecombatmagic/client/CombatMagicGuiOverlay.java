package simplecombatmagic.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import simplecombatmagic.SimpleCombatMagic;
import simplecombatmagic.capabilities.CombatMagicInstance;

public class CombatMagicGuiOverlay extends AbstractGui {
	
	private final ResourceLocation background = new ResourceLocation(SimpleCombatMagic.MOD_ID, "textures/gui/overlay.png");
	private final ResourceLocation missing = new ResourceLocation(SimpleCombatMagic.MOD_ID, "textures/gui/missing.png");
	private final int iconSize = 22, texCDBarHeight = 3;
	
	@SubscribeEvent
	public void renderScreen(RenderGameOverlayEvent event) {
		if(event.getType() == ElementType.AIR) {

			Minecraft mc = Minecraft.getInstance();
			final int screenHeight = mc.getMainWindow().getScaledHeight();
//			final int screenWidth = mc.currentScreen.width;
			ClientPlayerEntity player = mc.player;
			player.getCapability(CombatMagicInstance.COMBAT_MAGIC).ifPresent(instance -> {
				if(instance.getMagicSpec() != null) {
					mc.getTextureManager().bindTexture(background);
					this.blit(0, screenHeight - iconSize, 0, 0, iconSize, iconSize); //render background
					int texYOffset = (instance.getMagicSpec().ordinal() + 1) * iconSize;
					this.blit(0, screenHeight - iconSize, 0, texYOffset, iconSize, iconSize); //render magic specialization icon
					
					//draw background, spec, and cooldown bars
					for(int i = 0; i < instance.getSpells().length; i++) {
						if(i == instance.getSelectedSpellIndex())
							this.blit(((iconSize) * (i + 1)), screenHeight - iconSize, 22, 22, iconSize, iconSize);
						
						int cdHeight = screenHeight - iconSize - texCDBarHeight;
						int cd = instance.getCooldowns()[i];
						if(instance.getSpells()[i] != null) {
							if(cd > 0) {
								float cdPercent = (float) (1.0 - ((float)cd / instance.getSpells()[i].getCooldown()));
								int cdWidth = (int) (cdPercent * iconSize);
								this.blit(((iconSize) * (i + 1)), cdHeight, 44, 0, cdWidth, texCDBarHeight);
							} else {
								this.blit(((iconSize) * (i + 1)), cdHeight, 44, texCDBarHeight, iconSize, texCDBarHeight);
							}
						}
					}

					//draw spell icons
					for(int i = 0; i < instance.getSpells().length; i++) {
						if(instance.getSpells()[i] != null) {
							if(mc.getResourceManager().hasResource(instance.getSpells()[i].getIcon())) {
								mc.getTextureManager().bindTexture(instance.getSpells()[i].getIcon());
							} else {
								mc.getTextureManager().bindTexture(missing);
							}
							AbstractGui.blit(((iconSize) * (i + 1)), screenHeight - iconSize, 5, 0, 0, 22, 22, 22, 22);
						}
					}
				}
			});
			mc.getTextureManager().bindTexture(this.GUI_ICONS_LOCATION);
		}
	}
}
