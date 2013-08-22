package com.gu.fronts.endtoend;

import com.gu.fronts.endtoend.engine.Story;
import com.gu.fronts.endtoend.engine.TrailBlock;
import com.gu.fronts.endtoend.engine.TrailBlockEditors;
import com.gu.fronts.endtoend.engine.TrailBlocks;
import com.gu.fronts.endtoend.engine.actions.ActionFactory;
import com.gu.fronts.endtoend.engine.actions.TrailBlockActionFactory;
import com.gu.fronts.endtoend.engine.actions.api.RemoveStoryFromTrailBlockAction;
import com.gu.fronts.endtoend.engine.actions.api.TrailBlockCreateAction;
import com.gu.fronts.endtoend.engine.actions.api.ViewTrailBlockActionApi;
import cucumber.api.java.en.Given;
import hu.meza.aao.DefaultScenarioContext;
import org.junit.Assert;

public class TrailBlockSetupSteps {

	private final TrailBlocks trailBlocks;
	private final TrailBlockEditors editors;
	private final DefaultScenarioContext context;
	private final TrailBlockActionFactory actionFactory;

	public TrailBlockSetupSteps(
		TrailBlocks trailBlocks, TrailBlockEditors editors, DefaultScenarioContext context,
		ActionFactory actionFactory
	) {
		this.trailBlocks = trailBlocks;
		this.editors = editors;
		this.context = context;
		this.actionFactory = actionFactory.actionFactory();
	}

	@Given("^(.*) is an existing trailblock$")
	public void isAnExistingTrailBlock(String trailBlockName) {

		TrailBlock trailBlock = new TrailBlock(trailBlockName);
		context.setSubject(trailBlock);


		ViewTrailBlockActionApi view = new ViewTrailBlockActionApi(trailBlock);

		editors.anyone().execute(view);

		for (String storyTitle : view.liveStories()) {
			RemoveStoryFromTrailBlockAction remove =
				new RemoveStoryFromTrailBlockAction(new Story(storyTitle), trailBlock);
			editors.anyone().execute(remove);
		}

		for (String storyTitle : view.draftStories()) {
			RemoveStoryFromTrailBlockAction remove =
				new RemoveStoryFromTrailBlockAction(new Story(storyTitle), trailBlock);
			editors.anyone().execute(remove);
		}


		TrailBlockCreateAction trailBlockCreateAction = new TrailBlockCreateAction(trailBlock);

		editors.anyone().execute(trailBlockCreateAction);

		if (!trailBlockCreateAction.success()) {
			Assert.fail("Failed to create trail block");
		}

		trailBlocks.add(trailBlock);

	}

}
