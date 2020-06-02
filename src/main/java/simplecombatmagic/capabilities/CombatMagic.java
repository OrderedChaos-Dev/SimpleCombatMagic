package simplecombatmagic.capabilities;

import java.util.ArrayList;

import net.minecraft.util.ResourceLocation;
import simplecombatmagic.SimpleCombatMagic;
import simplecombatmagic.magic.MagicSpell;
import simplecombatmagic.magic.MagicSpecializationEnum;

public class CombatMagic implements ICombatMagic {
	
	private ArrayList<MagicSpell> spellbook = new ArrayList<MagicSpell>();
	private MagicSpell[] spells = new MagicSpell[4];
	private int[] cooldowns = new int[4];
	private MagicSpecializationEnum spec = null;
	private int selectedSpellIndex = 0;
	private int target = 0;
	
	public static final ResourceLocation COMBAT_MAGIC_RESOURCE = new ResourceLocation(SimpleCombatMagic.MOD_ID, "combat_magic");

	@Override
	public ArrayList<MagicSpell> getSpellbook() {
		return this.spellbook;
	}

	@Override
	public void addSpell(MagicSpell spell) {
		if(!this.spellbook.contains(spell))
			this.spellbook.add(spell);
	}
	
	@Override
	public void setSpellAtIndex(int index, MagicSpell spell) {
		spells[index] = spell;
	}

	@Override
	public MagicSpell[] getSpells() {
		return this.spells;
	}
	
	@Override
	public MagicSpell getSelectedSpell() {
		return this.spells[this.selectedSpellIndex];
	}

	@Override
	public int getSelectedSpellIndex() {
		return this.selectedSpellIndex;
	}
	
	@Override
	public void setSelectedSpellIndex(int index) {
		this.selectedSpellIndex = index;
	}

	@Override
	public void cycleSpellIndex() {	
		int spellLength = 0;
		for(MagicSpell spell : this.spells) {
			if(spell != null) {
				spellLength++;
			}
		}
		if(spellLength <= 1) return;
		
		if(this.selectedSpellIndex + 1 >= spellLength) {
			this.selectedSpellIndex = 0;
		} else {
			this.selectedSpellIndex++;
		}
	}
	
	@Override
	public int[] getCooldowns() {
		return this.cooldowns;
	}
	
	@Override
	public void setCooldown(int spellIndex, int time) {
		this.cooldowns[spellIndex] = time;
	}
	
	@Override
	public void resetSpellIndex() {
		this.selectedSpellIndex = 0;
	}

	@Override
	public int getCurrentCooldownTimer(int spellIndex) {
		return this.cooldowns[spellIndex];
	}

	@Override
	public void putOnCooldown(int spellIndex) {
		if(this.spells[spellIndex] != null)
			this.cooldowns[spellIndex] = this.spells[spellIndex].getCooldown();
	}

	@Override
	public void resetCooldowns() {
		for(int i = 0; i < this.cooldowns.length; i++) {
			this.cooldowns[i] = 0;
		}
	}
	
	@Override
	public void tickCooldowns() {
		for(int i = 0; i < this.cooldowns.length; i++) {
			this.cooldowns[i] = Math.max(0, this.cooldowns[i] - 1);
		}
	}

	@Override
	public MagicSpecializationEnum getMagicSpec() {
		return this.spec;
	}

	@Override
	public void setMagicSpec(MagicSpecializationEnum spec) {
		this.spec = spec;
	}

	@Override
	public MagicSpecializationEnum getSpecFromName(String name) {
		return MagicSpecializationEnum.fromString(name);
	}
}
