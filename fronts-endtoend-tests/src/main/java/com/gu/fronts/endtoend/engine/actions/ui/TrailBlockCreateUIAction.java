package com.gu.fronts.endtoend.engine.actions.ui;

import com.gu.fronts.endtoend.engine.TrailBlock;
import com.gu.fronts.endtoend.engine.actions.TrailBlockCreateAction;
import com.gu.fronts.endtoend.engine.actions.TrailBlockUIAction;
import org.apache.http.cookie.Cookie;

public class TrailBlockCreateUIAction implements TrailBlockCreateAction, TrailBlockUIAction {
	private final TrailBlock trailBlock;

	public TrailBlockCreateUIAction(TrailBlock trailBlock) {
		this.trailBlock = trailBlock;
	}

	@Override
	public void setAuthenticationData(Cookie cookie) {
	}

	@Override
	public void execute() {
	}

	@Override
	public TrailBlockCreateUIAction copyOf() {
		return new TrailBlockCreateUIAction(trailBlock);
	}

	@Override
	public boolean success() {
		return false;
	}

}
