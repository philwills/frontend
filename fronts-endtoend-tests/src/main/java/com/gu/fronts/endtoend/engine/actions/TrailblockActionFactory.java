package com.gu.fronts.endtoend.engine.actions;

import com.gu.fronts.endtoend.engine.Story;
import com.gu.fronts.endtoend.engine.TrailBlock;
import com.gu.fronts.endtoend.engine.TrailBlockMode;

public interface TrailBlockActionFactory {

	ViewTrailBlockAction view(TrailBlock trailBlock);

	RemoveStoryFromTrailBlockAction removeStory(Story story, TrailBlock trailBlock);

	RemoveStoryFromTrailBlockAction removeStory(Story story, TrailBlock trailBlock, TrailBlockMode draft);

	TrailBlockCreateAction createTrailBlock(TrailBlock trailBlock);

	AddStoryToTrailBlockAction addStoryToTrailblock(Story story, TrailBlock trailBlock, TrailBlockMode mode);

	AddStoryToTrailBlockAction addStoryToTrailblock(Story storyA, TrailBlock trailBlock, Story storyB);

	PublishDraftAction publish(TrailBlock trailBlock);

	DiscardDraftAction discard(TrailBlock trailBlock);
}
