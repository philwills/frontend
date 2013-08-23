package com.gu.fronts.endtoend;

import com.gu.fronts.endtoend.engine.Story;
import com.gu.fronts.endtoend.engine.TrailBlock;
import com.gu.fronts.endtoend.engine.TrailBlockEditor;
import com.gu.fronts.endtoend.engine.TrailBlockEditors;
import com.gu.fronts.endtoend.engine.TrailBlockMode;
import com.gu.fronts.endtoend.engine.TrailBlocks;
import com.gu.fronts.endtoend.engine.actions.ActionFactory;
import com.gu.fronts.endtoend.engine.actions.AddStoryToTrailBlockAction;
import com.gu.fronts.endtoend.engine.actions.TrailBlockActionFactory;
import cucumber.api.java.en.When;
import hu.meza.aao.DefaultScenarioContext;

import java.util.UUID;

public class EditingSteps {

	private final TrailBlocks trailBlocks;
	private final TrailBlockEditors editors;
	private final DefaultScenarioContext context;
	private final TrailBlockActionFactory actionFactory;

	public EditingSteps(
		TrailBlocks trailBlocks, TrailBlockEditors editors, DefaultScenarioContext context, ActionFactory actionFactory
	) {
		this.trailBlocks = trailBlocks;
		this.editors = editors;
		this.context = context;
		this.actionFactory = actionFactory.actionFactory();
	}

	@When("^(.*) edits the draft of ([\\w]*)$")
	public void editsTheDraftOfTrailBlock(String actorLabel, String trailBlockLabel) {
		editTrailBlock(actorLabel, trailBlockLabel, TrailBlockMode.DRAFT);

	}

	@When("^([\\w]*) edits the live ([\\w]*)$")
	public void editsTheLiveTrailBlock(String actorLabel, String trailBlockLabel) {
		editTrailBlock(actorLabel, trailBlockLabel, TrailBlockMode.LIVE);
	}

	private void editTrailBlock(String actorLabel, String trailBlockLabel, TrailBlockMode mode) {
		TrailBlock trailBlock = trailBlocks.get(trailBlockLabel, context);
		TrailBlockEditor editor = editors.getActor(actorLabel);

		Story story = new Story(UUID.randomUUID().toString());

		AddStoryToTrailBlockAction action = actionFactory.addStoryToTrailblock(story, trailBlock, mode);
		editor.execute(action);
	}
}
