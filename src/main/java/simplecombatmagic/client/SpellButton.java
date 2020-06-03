package simplecombatmagic.client;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import simplecombatmagic.SimpleCombatMagic;
import simplecombatmagic.magic.MagicSpell;

public class SpellButton extends Button {

	private final ResourceLocation background = new ResourceLocation(SimpleCombatMagic.MOD_ID, "textures/gui/overlay.png");
	private final ResourceLocation sprite;
	private final int iconSize = 32;
	private MagicSpell spell;
	private Screen parent;
	
	public SpellButton(int widthIn, int heightIn, int width, int height, IPressable onPress, ResourceLocation sprite, MagicSpell spell, Screen parent) {
		super(widthIn, heightIn, width, height, "", onPress);
		this.sprite = sprite;
		this.spell = spell;
		this.parent = parent;
	}

	@Override
	public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
		Minecraft minecraft = Minecraft.getInstance();

		GL11.glPushMatrix();
		GL11.glTranslated(this.x * 0.3, this.y * 0.3, 0);
		GL11.glScaled(0.7, 0.7, 0.7);
		RenderSystem.disableDepthTest();
		minecraft.getTextureManager().bindTexture(sprite);
		AbstractGui.blit(this.x, this.y, 4, 0, 0, iconSize, iconSize, iconSize, iconSize);

		minecraft.getTextureManager().bindTexture(background);
		if (this.isHovered()) {
			AbstractGui.blit(this.x, this.y, 50, iconSize, 0, iconSize, iconSize, 256, 256);
		} else {
			AbstractGui.blit(this.x, this.y, 50, 0, 0, iconSize, iconSize, 256, 256);
		}
		
		RenderSystem.enableDepthTest();
		GL11.glPopMatrix();
	}
	
	@Override
    public void renderToolTip(int p_renderToolTip_1_, int p_renderToolTip_2_) {
		List<String> string = new ArrayList<String>();
		string.add("§b" + spell.getName() + "§r" + " (" + spell.getSpec().getName() + ")");
		string.add(spell.getCooldown() / 20 + "s");
		string.add(spell.setDescription());
        parent.renderTooltip(string, p_renderToolTip_1_, p_renderToolTip_2_);
     }
	
	public MagicSpell getSpell() {
		return this.spell;
	}
}
