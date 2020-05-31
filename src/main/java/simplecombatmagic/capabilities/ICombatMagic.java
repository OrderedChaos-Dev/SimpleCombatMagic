package simplecombatmagic.capabilities;

import simplecombatmagic.magic.MagicSpecializationEnum;

public interface ICombatMagic {
	void resetBasicCooldown();
	void setBasicCooldown(int value);
	int getBasicCooldown();

	void resetUltimateCooldown();
	void setUltimateCooldown(int value);
	int getUltimateCooldown();
	
	void tickCooldowns();
	
	MagicSpecializationEnum getMagicSpec();
	void setMagicSpec(MagicSpecializationEnum spec);
	MagicSpecializationEnum getSpecFromName(String name);
}
