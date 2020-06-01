package simplecombatmagic.magic;

import java.util.ArrayList;

public class MagicSpells {
	
	/** 
	 * SPELL ID 
	 *  ALWAYS STARTS AT 1 - 0 IS RESERVED FOR EMPTY/NO SPELL
	 *
	 */
	public static int spell_id = 1;
	public static final ArrayList<MagicSpell> SPELLS = new ArrayList<MagicSpell>();
	public static final MagicSpell FIREBALL = registerSpell(new FireballSpell(spell_id++, "fireball", MagicSpecializationEnum.FIRE, 1200));
	
	
	/**
	 * takes array of spell ids, returns array of spells with those ids in the same order
	 */
	public static MagicSpell[] IDtoSpell(int[] ids) {
		MagicSpell[] spells = new MagicSpell[ids.length];
		for(int i = 0; i < spells.length; i++) {
			for(MagicSpell s : SPELLS) {
				if(s.getID() == ids[i]) {
					spells[i] = s;
					break;
				}
			}
		}
		return spells;
	}
	
	public static MagicSpell registerSpell(MagicSpell spell) {
		SPELLS.add(spell);
		return spell;
	}
}
