package simplecombatmagic.network;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.network.NetworkEvent;
import simplecombatmagic.capabilities.CombatMagicInstance;
import simplecombatmagic.capabilities.ICombatMagic;

public class MagicCapabilityPacket {
	
	private CompoundNBT nbt;
	
	public MagicCapabilityPacket(PacketBuffer buffer) {
		this.nbt = buffer.readCompoundTag();
	}
	
	public MagicCapabilityPacket(CompoundNBT nbt) {
		this.nbt = nbt;
	}
	
	public void encode(PacketBuffer buf) {
		buf.writeCompoundTag(nbt);
	}
	
	public void handle(Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		Capability.IStorage<ICombatMagic> storage = CombatMagicInstance.MAGIC_SPEC.getStorage();
		context.enqueueWork(() -> {
			if(context.getDirection().getOriginationSide().isServer()) { //SERVER TO CLIENT
				ClientPlayerEntity player = Minecraft.getInstance().player;
				player.getCapability(CombatMagicInstance.MAGIC_SPEC).ifPresent(spec -> {
					storage.readNBT(CombatMagicInstance.MAGIC_SPEC, spec, null, nbt);
				});
			} else if(context.getDirection().getOriginationSide().isClient()) { //CLIENT TO SERVER
				ServerPlayerEntity player = context.getSender();
				player.getCapability(CombatMagicInstance.MAGIC_SPEC).ifPresent(spec -> {
					storage.readNBT(CombatMagicInstance.MAGIC_SPEC, spec, null, nbt);
				});
			}
		});
		context.setPacketHandled(true);
	}
}
