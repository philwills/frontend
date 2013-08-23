package com.gu.fronts.endtoend;

import com.gu.fronts.endtoend.engine.TrailBlock;
import com.gu.fronts.endtoend.engine.TrailBlockEditor;
import com.gu.fronts.endtoend.engine.TrailBlockEditors;
import com.gu.fronts.endtoend.engine.TrailBlocks;
import com.gu.fronts.endtoend.engine.actions.ActionFactory;
import com.gu.fronts.endtoend.engine.actions.PublishDraftAction;
import com.gu.fronts.endtoend.engine.actions.TrailBlockActionFactory;
import cucumber.api.java.en.When;
import hu.meza.aao.DefaultScenarioContext;

public class PublishSteps {

	private final TrailBlocks trailBlocks;
	private final TrailBlockEditors editors;
	private final DefaultScenarioContext context;
	private final TrailBlockActionFactory actionFactory;

	public PublishSteps(
		TrailBlocks trailBlocks, TrailBlockEditors editors, DefaultScenarioContext context,
		ActionFactory actionFactory
	) {
		this.trailBlocks = trailBlocks;
		this.editors = editors;
		this.context = context;
		this.actionFactory = actionFactory.actionFactory();
	}


	@When("^([\\w]*) publishes the draft of ([\\w]*)$")
	public void publishesTheDraftOfTrailBlock(String actorLabel, String trailBlockLabel) {
		TrailBlock trailBlock = trailBlocks.get(trailBlockLabel);
		context.setSubject(trailBlock);

		TrailBlockEditor editor = editors.getActor(actorLabel);
		PublishDraftAction action = actionFactory.publish(trailBlock);

		editor.execute(action);
	}

	@When("^([\\w]*) publishes the draft$")
	public void publishesTheDraftOfTrailBlock(String actorLabel) {
		final TrailBlock subject = context.getSubject();
		publishesTheDraftOfTrailBlock(actorLabel, subject.getName());

	}
}
