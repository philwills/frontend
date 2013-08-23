package com.gu.fronts.endtoend.engine.actions.ui;

import com.gu.fronts.endtoend.engine.TrailBlock;
import com.gu.fronts.endtoend.engine.actions.TrailBlockUIAction;
import com.gu.fronts.endtoend.engine.actions.ViewTrailBlockAction;
import org.apache.http.cookie.Cookie;

import java.util.ArrayList;
import java.util.List;

public class ViewTrailBlockUIAction implements ViewTrailBlockAction, TrailBlockUIAction {
	private final TrailBlock trailBlock;

	public ViewTrailBlockUIAction(TrailBlock trailBlock) {
		this.trailBlock = trailBlock;
	}

	@Override
	public boolean success() {
		return false;
	}

	@Override
	public void setAuthenticationData(Cookie cookie) {
	}

	@Override
	public void execute() {
	}

	@Override
	public ViewTrailBlockUIAction copyOf() {
		return new ViewTrailBlockUIAction(trailBlock);
	}

	public List<String> liveStories() {
		return new ArrayList<>();
	}

	public List<String> draftStories() {
		return new ArrayList<>();
	}
}
