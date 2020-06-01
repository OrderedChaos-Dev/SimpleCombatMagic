package simplecombatmagic.capabilities;

import java.util.ArrayList;

import simplecombatmagic.magic.MagicSpell;
import simplecombatmagic.magic.MagicSpecializationEnum;

public interface ICombatMagic {
	ArrayList<MagicSpell> getSpellbook();
	void addSpell(MagicSpell spell);
	void setSpellAtIndex(int index, MagicSpell spell);
	MagicSpell[] getSpells();
	int getSelectedSpellIndex();
	void setSelectedSpellIndex(int index);
	void cycleSpellIndex();
	void resetSpellIndex();

	int getCurrentCooldownTimer(int spellIndex);
	void putOnCooldown(int spellIndex);
	void setCooldown(int spellIndex, int time);
	void resetCooldowns();
	void tickCooldowns();
	int[] getCooldowns();
	
	MagicSpecializationEnum getMagicSpec();
	void setMagicSpec(MagicSpecializationEnum spec);
	MagicSpecializationEnum getSpecFromName(String name);
}
