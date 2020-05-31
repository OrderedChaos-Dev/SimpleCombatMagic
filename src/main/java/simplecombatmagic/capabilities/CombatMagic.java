package simplecombatmagic.capabilities;

import net.minecraft.util.ResourceLocation;
import simplecombatmagic.SimpleCombatMagic;

public class CombatMagic implements ICombatMagic {

	/** Basic spell cooldown time in ticks. Default: 1800 (1.5 minutes) */
	private final int BASIC_SPELL_COOLDOWN_TIME = 1800;
	
	/** Ultimate spell cooldown time in ticks. Default: 3600 (3 minutes) */
	private final int ULTIMATE_SPELL_COOLDOWN_TIME = 3600;
	
	private int basicCooldown = 0;
	private int ultimateCooldown = 0;
	private MagicSpecializationEnum spec = null;
	
	public static final ResourceLocation COMBAT_MAGIC_RESOURCE = new ResourceLocation(SimpleCombatMagic.MOD_ID, "magic_spec");
 
	@Override
	public void resetBasicCooldown() {
		this.basicCooldown = 0;
	}

	@Override
	public void setBasicCooldown(int value) {
		this.basicCooldown = value;
	}

	@Override
	public int getBasicCooldown() {
		return this.basicCooldown;
	}

	@Override
	public void resetUltimateCooldown() {
		this.ultimateCooldown = 0;
	}

	@Override
	public void setUltimateCooldown(int value) {
		this.ultimateCooldown = value;
	}

	@Override
	public int getUltimateCooldown() {
		return this.ultimateCooldown;
	}
	
	@Override
	public void tickCooldowns() {
		if(this.basicCooldown > 0)
			this.basicCooldown--;
		if(this.ultimateCooldown > 0)
			this.ultimateCooldown--;
	}

	@Override
	public MagicSpecializationEnum getMagicSpec() {
		return this.spec;
	}

	@Override
	public void setMagicSpec(MagicSpecializationEnum spec) {
		this.spec = spec;
	}

	@Override
	public MagicSpecializationEnum getSpecFromName(String name) {
		return MagicSpecializationEnum.fromString(name);
	}
}
