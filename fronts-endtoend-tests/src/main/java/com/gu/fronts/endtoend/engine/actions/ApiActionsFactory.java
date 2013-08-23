package com.gu.fronts.endtoend.engine.actions;

import com.gu.fronts.endtoend.engine.Story;
import com.gu.fronts.endtoend.engine.TrailBlock;
import com.gu.fronts.endtoend.engine.TrailBlockMode;
import com.gu.fronts.endtoend.engine.actions.api.AddStoryToTrailBlockApiAction;
import com.gu.fronts.endtoend.engine.actions.api.DiscardDraftApiAction;
import com.gu.fronts.endtoend.engine.actions.api.PublishDraftApiAction;
import com.gu.fronts.endtoend.engine.actions.api.RemoveStoryFromTrailBlockApiAction;
import com.gu.fronts.endtoend.engine.actions.api.TrailBlockCreateApiAction;
import com.gu.fronts.endtoend.engine.actions.api.ViewTrailBlockApiAction;

public class ApiActionsFactory implements TrailBlockActionFactory {

	@Override
	public ViewTrailBlockAction view(TrailBlock trailBlock) {
		return new ViewTrailBlockApiAction(trailBlock);
	}

	@Override
	public RemoveStoryFromTrailBlockAction removeStory(
		Story story, TrailBlock trailBlock
	) {
		return new RemoveStoryFromTrailBlockApiAction(story, trailBlock);
	}

	@Override
	public RemoveStoryFromTrailBlockAction removeStory(
		Story story, TrailBlock trailBlock, TrailBlockMode draft
	) {
		return new RemoveStoryFromTrailBlockApiAction(story, trailBlock, draft);
	}

	@Override
	public TrailBlockCreateAction createTrailBlock(TrailBlock trailBlock) {
		return new TrailBlockCreateApiAction(trailBlock);
	}

	@Override
	public AddStoryToTrailBlockAction addStoryToTrailblock(
		Story story, TrailBlock trailBlock, TrailBlockMode mode
	) {
		return new AddStoryToTrailBlockApiAction(story, trailBlock, mode);
	}

	@Override
	public AddStoryToTrailBlockAction addStoryToTrailblock(
		Story storyA, TrailBlock trailBlock, Story storyB
	) {
		return new AddStoryToTrailBlockApiAction(storyA, trailBlock, storyB);
	}

	@Override
	public PublishDraftAction publish(TrailBlock trailBlock) {
		return new PublishDraftApiAction(trailBlock);
	}

	@Override
	public DiscardDraftAction discard(TrailBlock trailBlock) {
		return new DiscardDraftApiAction(trailBlock);
	}
}
