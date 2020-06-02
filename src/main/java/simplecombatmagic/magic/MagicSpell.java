package simplecombatmagic.magic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.ResourceLocation;
import simplecombatmagic.SimpleCombatMagic;

public abstract class MagicSpell {
	private int id;
	private String name;
	private MagicSpecializationEnum spec;
	private int cooldown;
	private final ResourceLocation icon;
	private boolean requiresTarget;
	
	public MagicSpell(int id, String name, String resource_name, MagicSpecializationEnum spec, int cooldown, boolean requiresTarget) {
		this.id = id;
		this.name = name;
		this.spec = spec;
		this.cooldown = cooldown;
		this.requiresTarget = requiresTarget;
		this.icon = new ResourceLocation(SimpleCombatMagic.MOD_ID, "textures/spells/" + resource_name + ".png");
	}
	
	public abstract boolean cast(PlayerEntity player);
	public abstract String setDescription();
	
	public int getCooldown() {
		return this.cooldown;
		
	}
	public int getID() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean requiresTarget() {
		return this.requiresTarget;
	}
	
	/**
	 * used to set client particle used if requiresTarget == true
	 */
	public IParticleData getTargetParticle() {
		return null;
	}
	
	public ResourceLocation getIcon() {
		return this.icon;
	}
	
	public MagicSpecializationEnum getSpec() {
		return this.spec;
	}
}
