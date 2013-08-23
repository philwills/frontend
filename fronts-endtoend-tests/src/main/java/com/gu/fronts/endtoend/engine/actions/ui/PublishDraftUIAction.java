package com.gu.fronts.endtoend.engine.actions.ui;

import com.gu.fronts.endtoend.engine.TrailBlock;
import com.gu.fronts.endtoend.engine.actions.PublishDraftAction;
import com.gu.fronts.endtoend.engine.actions.TrailBlockUIAction;
import org.apache.http.cookie.Cookie;

public class PublishDraftUIAction implements PublishDraftAction, TrailBlockUIAction {
	private final TrailBlock trailBlock;

	public PublishDraftUIAction(TrailBlock trailBlock) {
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
	public PublishDraftUIAction copyOf() {
		return new PublishDraftUIAction(trailBlock);
	}

}
