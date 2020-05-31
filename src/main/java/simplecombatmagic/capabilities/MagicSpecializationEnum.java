package simplecombatmagic.capabilities;

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
}
