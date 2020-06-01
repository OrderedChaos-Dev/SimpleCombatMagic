package simplecombatmagic.magic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import simplecombatmagic.SimpleCombatMagic;

public abstract class MagicSpell {
	private int id;
	private String name;
	private MagicSpecializationEnum spec;
	private int cooldown;
	private ResourceLocation icon;
	
	public MagicSpell(int id, String name, String resource_name, MagicSpecializationEnum spec, int cooldown) {
		this.id = id;
		this.name = name;
		this.spec = spec;
		this.cooldown = cooldown;
		this.icon = new ResourceLocation(SimpleCombatMagic.MOD_ID, "textures/spells/" + resource_name + ".png");
	}
	
	public abstract void cast(PlayerEntity player);
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
	
	public ResourceLocation getIcon() {
		return this.icon;
	}
	
	public MagicSpecializationEnum getSpec() {
		return this.spec;
	}
}
