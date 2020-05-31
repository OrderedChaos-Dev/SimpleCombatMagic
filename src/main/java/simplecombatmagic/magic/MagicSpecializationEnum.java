package simplecombatmagic.magic;

public enum MagicSpecializationEnum {
	FIRE("fire"),
	WATER("water"),
	EARTH("earth"),
	AIR("air"),
	FROST("frost"),
	NATURE("nature"),
	BLOOD("blood"),
	DEATH("death"),
	ENDER("ender");
	
	private final String name;
	
	private MagicSpecializationEnum(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public static MagicSpecializationEnum fromString(String name) {
		for(MagicSpecializationEnum spec : MagicSpecializationEnum.values()) {
			if(spec.getName().equals(name)) {
				return spec;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
	
	/** For testing - returns next spec in order */
	public static MagicSpecializationEnum cycle(MagicSpecializationEnum current) {
		if(current == null)
			return FIRE;
		int index = current.ordinal() + 1;
		if(index == MagicSpecializationEnum.values().length)
			index = 0;
		return MagicSpecializationEnum.values()[index];
	}
}
