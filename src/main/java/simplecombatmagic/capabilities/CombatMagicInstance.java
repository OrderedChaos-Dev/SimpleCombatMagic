package simplecombatmagic.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class CombatMagicInstance implements ICapabilitySerializable<CompoundNBT>{
	@CapabilityInject(ICombatMagic.class)
	public static final Capability<ICombatMagic> COMBAT_MAGIC = null;
	
	private LazyOptional<ICombatMagic> instance = LazyOptional.of(COMBAT_MAGIC::getDefaultInstance);

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return COMBAT_MAGIC.orEmpty(cap, instance);
	}

	@Override
	public CompoundNBT serializeNBT() {
		return (CompoundNBT) COMBAT_MAGIC.getStorage().writeNBT(COMBAT_MAGIC, this.instance.orElseThrow(() ->
					new IllegalArgumentException("This shouldn't happen")), null);
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		COMBAT_MAGIC.getStorage().readNBT(COMBAT_MAGIC, this.instance.orElseThrow(() ->
					new IllegalArgumentException("This shouldn't happen")), null, nbt);
	}
}
