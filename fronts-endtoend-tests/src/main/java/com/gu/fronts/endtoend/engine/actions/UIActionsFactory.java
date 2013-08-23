package com.gu.fronts.endtoend.engine.actions;

import com.gu.fronts.endtoend.engine.Story;
import com.gu.fronts.endtoend.engine.TrailBlock;
import com.gu.fronts.endtoend.engine.TrailBlockMode;
import com.gu.fronts.endtoend.engine.actions.ui.AddStoryToTrailBlockUIAction;
import com.gu.fronts.endtoend.engine.actions.ui.DiscardDraftUIAction;
import com.gu.fronts.endtoend.engine.actions.ui.PublishDraftUIAction;
import com.gu.fronts.endtoend.engine.actions.ui.RemoveStoryFromTrailBlockUIAction;
import com.gu.fronts.endtoend.engine.actions.ui.TrailBlockCreateUIAction;
import com.gu.fronts.endtoend.engine.actions.ui.ViewTrailBlockUIAction;

public class UIActionsFactory implements TrailBlockActionFactory {
	@Override
	public ViewTrailBlockAction view(TrailBlock trailBlock) {
		return new ViewTrailBlockUIAction(trailBlock);
	}

	@Override
	public RemoveStoryFromTrailBlockAction removeStory(
		Story story, TrailBlock trailBlock
	) {
		return new RemoveStoryFromTrailBlockUIAction(story, trailBlock);
	}

	@Override
	public RemoveStoryFromTrailBlockAction removeStory(
		Story story, TrailBlock trailBlock, TrailBlockMode mode
	) {
		return new RemoveStoryFromTrailBlockUIAction(story, trailBlock, mode);
	}

	@Override
	public TrailBlockCreateAction createTrailBlock(TrailBlock trailBlock) {
		return new TrailBlockCreateUIAction(trailBlock);
	}

	@Override
	public AddStoryToTrailBlockAction addStoryToTrailblock(
		Story story, TrailBlock trailBlock, TrailBlockMode mode
	) {
		return new AddStoryToTrailBlockUIAction(story, trailBlock, mode);
	}

	@Override
	public AddStoryToTrailBlockAction addStoryToTrailblock(
		Story storyA, TrailBlock trailBlock, Story storyB
	) {
		return new AddStoryToTrailBlockUIAction(storyA, trailBlock, storyB);
	}

	@Override
	public PublishDraftAction publish(TrailBlock trailBlock) {
		return new PublishDraftUIAction(trailBlock);
	}

	@Override
	public DiscardDraftAction discard(TrailBlock trailBlock) {
		return new DiscardDraftUIAction(trailBlock);
	}
}
