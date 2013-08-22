package com.gu.fronts.endtoend.engine;

public enum ActionsType {
	API("api"),
	UI("ui");
	private final String type;


	ActionsType(String type) {
		this.type = type;
	}

	public static ActionsType fromString(String type) {
		type = type.toLowerCase();

		for (ActionsType t : ActionsType.values()) {
			if (t.actionType().equals(type)) {
				return t;
			}
		}

		throw new IllegalArgumentException(String.format("No such action type: %s", type));

	}

	public String actionType() {
		return type;
	}
}
