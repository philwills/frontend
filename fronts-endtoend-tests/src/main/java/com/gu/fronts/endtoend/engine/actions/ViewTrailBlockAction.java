package com.gu.fronts.endtoend.engine.actions;

import com.gu.fronts.endtoend.engine.TrailBlock;
import com.gu.fronts.endtoend.engine.TrailBlockAction;
import com.gu.fronts.endtoend.engine.actions.api.ViewTrailBlockActionApi;

import java.util.List;

public interface ViewTrailBlockAction extends TrailBlockAction {
	ViewTrailBlockActionApi create(TrailBlock trailBlock);

	List<String> liveStories();

	List<String> draftStories();
}
