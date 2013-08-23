package com.gu.fronts.endtoend.engine.actions.api;

import com.gu.fronts.endtoend.engine.TrailBlock;
import com.gu.fronts.endtoend.engine.TrailBlockApiAction;
import com.gu.fronts.endtoend.engine.actions.TrailBlockCreateAction;
import hu.meza.tools.HttpCall;
import hu.meza.tools.HttpClientWrapper;
import org.apache.http.HttpStatus;
import org.apache.http.cookie.Cookie;

import java.util.UUID;

public class TrailBlockCreateApiAction implements TrailBlockCreateAction, TrailBlockApiAction {
	private final TrailBlock trailBlock;
	private HttpClientWrapper client;
	private HttpCall httpCall;

	public TrailBlockCreateApiAction(TrailBlock trailBlock) {
		this.trailBlock = trailBlock;
	}

	@Override
	public void setAuthenticationData(Cookie cookie) {
		client.addCookie(cookie);
	}

	@Override
	public void execute() {
		String data = "{" +
					  "\"item\":\"" + anything() + "\"" +
					  ",\"draft\":true" +
					  ",\"live\":true" +
					  "}";

		final String requestUrl = String.format("/fronts/api/%s", trailBlock.uri());
		httpCall = client.postJsonTo(requestUrl, data);
	}

	@Override
	public TrailBlockCreateApiAction copyOf() {
		return new TrailBlockCreateApiAction(trailBlock);
	}

	@Override
	public void useClient(HttpClientWrapper client) {
		this.client = client;
	}

	@Override
	public boolean success() {
		int statusCode = httpCall.response().getStatusLine().getStatusCode();

		if (statusCode != HttpStatus.SC_CREATED && statusCode != HttpStatus.SC_OK) {
			return false;
		}

		return true;
	}

	private String anything() {
		return UUID.randomUUID().toString();
	}
}
