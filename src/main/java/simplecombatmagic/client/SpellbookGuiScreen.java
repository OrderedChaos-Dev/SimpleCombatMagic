package simplecombatmagic.client;

import java.util.ArrayList;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import simplecombatmagic.capabilities.CombatMagicInstance;
import simplecombatmagic.magic.MagicSpell;
import simplecombatmagic.network.MagicCapabilityNetwork;
import simplecombatmagic.network.MagicCapabilityPacket;

public class SpellbookGuiScreen extends Screen {

	private PlayerEntity player;
	
	public SpellbookGuiScreen(ITextComponent title, PlayerEntity player) {
		super(title);
		this.player = player;
	}
		
	@Override
	protected void init() {
		player.getCapability(CombatMagicInstance.COMBAT_MAGIC).ifPresent(instance -> {
			ArrayList<MagicSpell> spellbook = instance.getSpellbook();
			int index = 0;
			for(MagicSpell spell : spellbook) {
				this.addButton(new SpellButton((this.width / 2 - 100) + (26 * index), this.height / 5, 22, 22, (button) -> {
					System.out.println(spell.getName());
				}, spell.getIcon(), spell, this));
				index++;
			}
		});
	}
	
	@Override
	public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
		this.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);
		super.render(p_render_1_, p_render_2_, p_render_3_);
		for(Widget widget : this.buttons) {
			if(widget.isHovered())
				widget.renderToolTip(widget.x + 25, widget.y + 5);
		}
	}
	
	@Override
	public boolean keyPressed(int key, int p_keyPressed_2_, int p_keyPressed_3_) {
		player.getCapability(CombatMagicInstance.COMBAT_MAGIC).ifPresent(instance -> {
			for(Widget widget : this.buttons) {
				if(widget.isHovered() && widget instanceof SpellButton) {
					MagicSpell spell = ((SpellButton) widget).getSpell();
					if(key == 49) { //1
						System.out.println("yes");
						instance.setSpellAtIndex(0, spell);
					} else if(key == 50) { //2
						instance.setSpellAtIndex(1, spell);
					} else if(key == 51) { //3
						instance.setSpellAtIndex(2, spell);
					} else if(key == 52) { //4
						instance.setSpellAtIndex(3, spell);
					}
					MagicCapabilityPacket packet = MagicCapabilityNetwork.createPacket(instance);
					MagicCapabilityNetwork.NETWORK.sendToServer(packet);
					break;
				}
			}
		});
		return super.keyPressed(key, p_keyPressed_2_, p_keyPressed_3_);
	}
	
	@Override
	public boolean shouldCloseOnEsc() {
		return true;
	}
	
	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
