package simplecombatmagic.magic;

import net.minecraft.entity.player.PlayerEntity;

public interface IMagicSpec {
	void getPassiveEffect(PlayerEntity player);
	void castBasicSpell(PlayerEntity player);
	void castUltimateSpell(PlayerEntity player);
}
