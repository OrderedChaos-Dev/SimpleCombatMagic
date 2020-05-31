package simplecombatmagic.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class CombatMagicInstance implements ICapabilitySerializable<CompoundNBT>{
	@CapabilityInject(ICombatMagic.class)
	public static final Capability<ICombatMagic> MAGIC_SPEC = null;
	
	private LazyOptional<ICombatMagic> instance = LazyOptional.of(MAGIC_SPEC::getDefaultInstance);

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return MAGIC_SPEC.orEmpty(cap, instance);
	}

	@Override
	public CompoundNBT serializeNBT() {
		return (CompoundNBT) MAGIC_SPEC.getStorage().writeNBT(MAGIC_SPEC, this.instance.orElseThrow(() ->
					new IllegalArgumentException("This shouldn't happen")), null);
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		MAGIC_SPEC.getStorage().readNBT(MAGIC_SPEC, this.instance.orElseThrow(() ->
					new IllegalArgumentException("This shouldn't happen")), null, nbt);
	}
}
