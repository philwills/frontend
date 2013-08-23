package com.gu.fronts.endtoend.engine.actions.ui;

import com.gu.fronts.endtoend.engine.Story;
import com.gu.fronts.endtoend.engine.TrailBlock;
import com.gu.fronts.endtoend.engine.TrailBlockMode;
import com.gu.fronts.endtoend.engine.actions.RemoveStoryFromTrailBlockAction;
import com.gu.fronts.endtoend.engine.actions.TrailBlockUIAction;
import hu.meza.tools.HttpCall;
import hu.meza.tools.HttpClientWrapper;
import org.apache.http.cookie.Cookie;

public class RemoveStoryFromTrailBlockUIAction
	implements RemoveStoryFromTrailBlockAction, TrailBlockUIAction {

	private final Story story;
	private final TrailBlockMode mode;
	private final TrailBlock trailblock;
	private HttpClientWrapper client;
	private HttpCall httpCall;

	public RemoveStoryFromTrailBlockUIAction(Story story, TrailBlock trailblock) {
		this(story, trailblock, TrailBlockMode.LIVE);
	}

	public RemoveStoryFromTrailBlockUIAction(Story story, TrailBlock trailBlock, TrailBlockMode mode) {
		this.story = story;
		this.mode = mode;
		this.trailblock = trailBlock;
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
	public RemoveStoryFromTrailBlockUIAction copyOf() {
		return new RemoveStoryFromTrailBlockUIAction(story, trailblock, mode);
	}

	private String isLive() {
		return mode == TrailBlockMode.LIVE ? "true" : "false";
	}
}
