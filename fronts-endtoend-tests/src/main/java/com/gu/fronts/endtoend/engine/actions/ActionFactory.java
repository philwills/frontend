package com.gu.fronts.endtoend.engine.actions;


import com.gu.fronts.endtoend.engine.ActionsType;
import com.gu.fronts.endtoend.engine.Configuration;

public class ActionFactory {

	private final Configuration config;

	public ActionFactory(Configuration configuration) {
		this.config = configuration;
	}

	public TrailBlockActionFactory actionFactory() {
		final ActionsType actionsType = config.actionsType();
		switch (actionsType) {
			case API:
				return new ApiActionsFactory();
			case UI:
				return new UIActionsFactory();
			default:
				throw new RuntimeException(
					String.format("Can't identify actions type factory for type: %s", actionsType));
		}
	}

}
