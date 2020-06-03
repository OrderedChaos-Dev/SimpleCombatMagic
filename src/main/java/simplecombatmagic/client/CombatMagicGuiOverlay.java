package simplecombatmagic.client;

import org.lwjgl.opengl.GL11;

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
	private final int iconSize = 32, texCDBarHeight = 4;
	
	@SubscribeEvent
	public void renderScreen(RenderGameOverlayEvent event) {
		if(event.getType() == ElementType.AIR) {
			GL11.glPushMatrix();
			Minecraft mc = Minecraft.getInstance();
			final int screenHeight = mc.getMainWindow().getScaledHeight();
			GL11.glTranslated(0, screenHeight * 0.3, 0);
			GL11.glScaled(0.7, 0.7, 1);
//			final int screenWidth = mc.currentScreen.width;
			ClientPlayerEntity player = mc.player;
			player.getCapability(CombatMagicInstance.COMBAT_MAGIC).ifPresent(instance -> {
				if(instance.getMagicSpec() != null) {
					mc.getTextureManager().bindTexture(background);
					int texYOffset = (instance.getMagicSpec().ordinal() + 1) * iconSize;
					this.blit(0, screenHeight - iconSize, 0, texYOffset, iconSize, iconSize); //render magic specialization icon
					
					for(int i = 0; i < instance.getSpells().length; i++) {
						if(i == instance.getSelectedSpellIndex())
							AbstractGui.blit(((iconSize) * (i + 1)), screenHeight - iconSize, 3, iconSize, 0, iconSize, iconSize, 256, 256);
						else
							AbstractGui.blit(((iconSize) * (i + 1)), screenHeight - iconSize, 3, 0, 0, iconSize, iconSize, 256, 256);
						
					}
					
//					draw spell icons
					for(int i = 0; i < instance.getSpells().length; i++) {
						if(instance.getSpells()[i] != null) {
							if(mc.getResourceManager().hasResource(instance.getSpells()[i].getIcon())) {
								mc.getTextureManager().bindTexture(instance.getSpells()[i].getIcon());
							} else {
								mc.getTextureManager().bindTexture(missing);
							}
							AbstractGui.blit(((iconSize) * (i + 1)), screenHeight - iconSize, 2, 0, 0, iconSize, iconSize, iconSize, iconSize);
						}
					}
					
					mc.getTextureManager().bindTexture(background);
					//draw background, spec, and cooldown bars
					for(int i = 0; i < instance.getSpells().length; i++) {
						int cdHeight = screenHeight - iconSize - texCDBarHeight;
						int cd = instance.getCooldowns()[i];
						if(instance.getSpells()[i] != null) {
							if(cd > 0) {
								float cdPercent = (float) (1.0 - ((float)cd / instance.getSpells()[i].getCooldown()));
								int cdWidth = (int) (cdPercent * iconSize);
								this.blit(((iconSize) * (i + 1)), cdHeight, iconSize * 2, texCDBarHeight, cdWidth, texCDBarHeight);
							} else {
								this.blit(((iconSize) * (i + 1)), cdHeight, iconSize * 2, 0, iconSize, texCDBarHeight);
							}
						}
					}
				}
			});
			GL11.glPopMatrix();
			mc.getTextureManager().bindTexture(this.GUI_ICONS_LOCATION);
		}

	}
}
