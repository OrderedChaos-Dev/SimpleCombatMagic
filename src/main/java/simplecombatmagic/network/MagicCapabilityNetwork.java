package simplecombatmagic.network;

import static simplecombatmagic.SimpleCombatMagic.MOD_ID;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import simplecombatmagic.capabilities.ICombatMagic;
import simplecombatmagic.magic.MagicSpell;

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
	public static MagicCapabilityPacket createPacket(ICombatMagic instance) {
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

		MagicCapabilityPacket packet = new MagicCapabilityPacket(tag);
		return packet;
	}
}