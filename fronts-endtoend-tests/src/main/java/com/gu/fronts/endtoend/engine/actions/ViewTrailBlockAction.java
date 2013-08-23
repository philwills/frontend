package com.gu.fronts.endtoend.engine.actions;

import com.gu.fronts.endtoend.engine.TrailBlockApiAction;

import java.util.List;

public interface ViewTrailBlockAction extends TrailBlockApiAction {

	List<String> liveStories();

	List<String> draftStories();
}
