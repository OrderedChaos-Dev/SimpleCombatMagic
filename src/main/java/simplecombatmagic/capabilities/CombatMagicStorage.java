package simplecombatmagic.capabilities;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import simplecombatmagic.magic.MagicSpecializationEnum;
import simplecombatmagic.magic.MagicSpell;
import simplecombatmagic.magic.MagicSpells;

public class CombatMagicStorage implements IStorage<ICombatMagic> {

	@Override
	public INBT writeNBT(Capability<ICombatMagic> capability, ICombatMagic instance, Direction side) {
		CompoundNBT tag = new CompoundNBT();
		tag.putIntArray("cooldowns", instance.getCooldowns());
		tag.putInt("selectedIndex", instance.getSelectedSpellIndex());
		
		ArrayList<MagicSpell> spellbook = instance.getSpellbook();
		int[] spellbookIDs = spellbook.stream().mapToInt(spell -> spell.getID()).toArray();
		tag.putIntArray("spellbook", spellbookIDs);

		int[] spellsIDs = Arrays.stream(instance.getSpells()).mapToInt(spell -> {
			if(spell != null)
				return spell.getID();
			return 0;
		}).toArray();
		tag.putIntArray("spells", spellsIDs);
		
		if(instance.getMagicSpec() != null)
			tag.putString("magicSpec", instance.getMagicSpec().getName());
		
		return tag;
	}

	@Override
	public void readNBT(Capability<ICombatMagic> capability, ICombatMagic instance, Direction side, INBT nbt) {
		CompoundNBT tag = (CompoundNBT)nbt;
		//LOAD SPELLBOOK
		MagicSpell[] spellbook = MagicSpells.IDtoSpell(tag.getIntArray("spellbook"));
		for(MagicSpell spell : spellbook) {
			instance.addSpell(spell);
		}
		//LOAD SPELL LIST
		MagicSpell[] spells = MagicSpells.IDtoSpell(tag.getIntArray("spells"));
		for(int i = 0; i < spells.length; i++) {
			instance.setSpellAtIndex(i, spells[i]);
		}
		
		//SET SELECTED SPELL
		instance.setSelectedSpellIndex(tag.getInt("selectedIndex"));
		
		//SET COOLDOWNS
		int[] cooldowns = tag.getIntArray("cooldowns");
		for(int i = 0; i < cooldowns.length; i++) {
			instance.setCooldown(i, cooldowns[i]);
		}
		
		//SET SPEC
		instance.setMagicSpec(MagicSpecializationEnum.fromString(tag.getString("magicSpec")));
	}
	
}
