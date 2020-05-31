package simplecombatmagic.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import simplecombatmagic.magic.MagicSpecializationEnum;

public class CombatMagicStorage implements IStorage<ICombatMagic> {

	@Override
	public INBT writeNBT(Capability<ICombatMagic> capability, ICombatMagic instance, Direction side) {
		CompoundNBT tag = new CompoundNBT();
		tag.putInt("basicCooldown", instance.getBasicCooldown());
		tag.putInt("ultimateCooldown", instance.getUltimateCooldown());
		
		if(instance.getMagicSpec() != null)
			tag.putString("magicSpec", instance.getMagicSpec().getName());
		return tag;
	}

	@Override
	public void readNBT(Capability<ICombatMagic> capability, ICombatMagic instance, Direction side, INBT nbt) {
		instance.setBasicCooldown(((CompoundNBT)nbt).getInt("basicCooldown"));
		instance.setUltimateCooldown(((CompoundNBT)nbt).getInt("ultimateCooldown"));
		instance.setMagicSpec(MagicSpecializationEnum.fromString(((CompoundNBT)nbt).getString("magicSpec")));
	}
}
