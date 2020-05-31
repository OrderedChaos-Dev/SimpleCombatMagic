package simplecombatmagic.network;

import static simplecombatmagic.SimpleCombatMagic.MOD_ID;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import simplecombatmagic.capabilities.ICombatMagic;

public class MagicCapabilityNetwork {
	
	private static int id = 1;
	private static final String VERSION = "1";
	
	public static final SimpleChannel NETWORK = NetworkRegistry
			.newSimpleChannel(new ResourceLocation(MOD_ID, "network"),
								() -> VERSION,
								VERSION::equals,
								VERSION::equals);
	
	public static void registerPackets() {
		NETWORK.messageBuilder(MagicCapabilityPacket.class, id++)
				.encoder(MagicCapabilityPacket::encode)
				.decoder(MagicCapabilityPacket::new)
				.consumer(MagicCapabilityPacket::handle)
				.add();
	}
	
	/**
	 * Helper method to reduce clutter
	 */
	public static MagicCapabilityPacket createPacket(ICombatMagic spec) {
		CompoundNBT nbt = new CompoundNBT();
		if(spec.getMagicSpec() != null)
			nbt.putString("magicSpec", spec.getMagicSpec().getName());
		nbt.putInt("basicCooldown", spec.getBasicCooldown());
		nbt.putInt("ultimateCooldown", spec.getUltimateCooldown());
		MagicCapabilityPacket packet = new MagicCapabilityPacket(nbt);
		return packet;
	}
}