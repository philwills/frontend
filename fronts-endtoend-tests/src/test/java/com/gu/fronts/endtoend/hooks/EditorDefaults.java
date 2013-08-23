package com.gu.fronts.endtoend.hooks;

import com.gu.fronts.endtoend.engine.Configuration;
import com.gu.fronts.endtoend.engine.TrailBlockEditor;
import com.gu.fronts.endtoend.engine.TrailBlockEditors;
import cucumber.api.java.Before;

public class EditorDefaults {

	private final TrailBlockEditors editors;
	private final Configuration config;

	public EditorDefaults(TrailBlockEditors editors, Configuration config) {
		this.editors = editors;
		this.config = config;
	}

	@Before
	public void createAnEditor() {
		editors.addActor("an editor",
						 new TrailBlockEditor(config.baseUrl(), config.cookieString(), config.actionsType()));
		editors.addActor("the editor",
						 new TrailBlockEditor(config.baseUrl(), config.cookieString(), config.actionsType()));
	}
}
