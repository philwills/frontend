package com.gu.fronts.endtoend.engine.actions.api;

import com.gu.fronts.endtoend.engine.TrailBlock;
import com.gu.fronts.endtoend.engine.TrailBlockApiAction;
import com.gu.fronts.endtoend.engine.actions.DiscardDraftAction;
import hu.meza.tools.HttpCall;
import hu.meza.tools.HttpClientWrapper;
import org.apache.http.HttpStatus;
import org.apache.http.cookie.Cookie;

public class DiscardDraftApiAction implements DiscardDraftAction, TrailBlockApiAction {
	private final TrailBlock trailBlock;
	private HttpClientWrapper client;
	private HttpCall httpCall;

	public DiscardDraftApiAction(TrailBlock trailBlock) {
		this.trailBlock = trailBlock;
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
		String data = "{\"discard\":true}";

		final String requestUrl = String.format("/fronts/api/%s", trailBlock.uri());
		httpCall = client.postJsonTo(requestUrl, data);
	}

	@Override
	public DiscardDraftApiAction copyOf() {
		return new DiscardDraftApiAction(trailBlock);
	}

}
