package simplecombatmagic.magic;

/**
 * probably should've combined this with the enum but whatever
 */
public abstract class MagicSpec implements IMagicSpec {
	
	public static final MagicSpec FIRE_SPEC = new FireMagicSpec(MagicSpecializationEnum.FIRE);
	
	private MagicSpecializationEnum spec;
	
	public MagicSpec(MagicSpecializationEnum spec) {
		this.spec = spec;
	}
	
	public MagicSpecializationEnum getSpec() {
		return this.spec;
	}
	
	public static MagicSpec fromSpecEnum(MagicSpecializationEnum spec) {
		switch(spec) {
		case FIRE:
			default:
				return FIRE_SPEC;
		}
	}
}
