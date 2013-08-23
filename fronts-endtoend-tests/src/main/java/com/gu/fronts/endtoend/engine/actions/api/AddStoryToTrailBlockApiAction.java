package com.gu.fronts.endtoend.engine.actions.api;

import com.gu.fronts.endtoend.engine.Story;
import com.gu.fronts.endtoend.engine.TrailBlock;
import com.gu.fronts.endtoend.engine.TrailBlockMode;
import com.gu.fronts.endtoend.engine.actions.AddStoryToTrailBlockAction;
import hu.meza.tools.HttpCall;
import hu.meza.tools.HttpClientWrapper;
import org.apache.http.HttpStatus;
import org.apache.http.cookie.Cookie;

public class AddStoryToTrailBlockApiAction implements AddStoryToTrailBlockAction {
	private final Story story;
	private final TrailBlock trailblock;
	private final TrailBlockMode mode;
	private Story positionOf;
	private HttpClientWrapper client;
	private HttpCall httpCall;

	public AddStoryToTrailBlockApiAction(Story storyA, TrailBlock trailBlock, Story storyB) {
		this(storyA, trailBlock, storyB, TrailBlockMode.LIVE);
	}

	public AddStoryToTrailBlockApiAction(Story story, TrailBlock trailBlock, TrailBlockMode mode) {
		this.story = story;
		this.trailblock = trailBlock;
		this.mode = mode;
	}

	public AddStoryToTrailBlockApiAction(
		Story storyA, TrailBlock trailBlock, Story storyB, TrailBlockMode mode
	) {
		story = storyA;
		this.trailblock = trailBlock;
		positionOf = storyB;
		this.mode = mode;
	}

	@Override
	public void useClient(HttpClientWrapper client) {
		this.client = client;
	}

	@Override
	public boolean success() {
		return HttpStatus.SC_OK == httpCall.response().getStatusLine().getStatusCode();
	}

	@Override
	public void setAuthenticationData(Cookie cookie) {
		client.addCookie(cookie);
	}

	@Override
	public void execute() {
		final String requestBody = requestBody();
		final String requestUrl = String.format("/fronts/api/%s", trailblock.uri());
		httpCall = client.postJsonTo(requestUrl, requestBody);
	}

	@Override
	public AddStoryToTrailBlockApiAction copyOf() {
		return new AddStoryToTrailBlockApiAction(story, trailblock, positionOf, mode);
	}

	private String requestBody() {
		if (positionOf == null) {
			return noPositionRequestBody();
		}
		return positionRequestBody();

	}

	private String positionRequestBody() {
		String data = "{" +
					  "\"item\":\"%s\"" +
					  ",\"draft\":true" +
					  ",\"live\":" + isLive() +
					  ",\"position\":\"%s\"" +
					  "}";

		return String.format(data, story.getName(), positionOf.getName());
	}

	private String isLive() {
		return mode == TrailBlockMode.LIVE ? "true" : "false";
	}

	private String noPositionRequestBody() {
		String data = "{" +
					  "\"item\":\"%s\"" +
					  ",\"draft\":true" +
					  ",\"live\":" + isLive() +
					  "}";

		return String.format(data, story.getName());
	}
}
