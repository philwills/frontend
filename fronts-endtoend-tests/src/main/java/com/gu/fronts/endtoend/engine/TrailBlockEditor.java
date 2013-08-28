package com.gu.fronts.endtoend.engine;

import com.gu.fronts.endtoend.engine.actions.TrailBlockUIAction;
import hu.meza.aao.RestfulActor;
import hu.meza.tools.HttpClientWrapper;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie2;
import org.joda.time.DateTime;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class TrailBlockEditor extends RestfulActor {

	private final String baseUrl;
	private final String cookieValue;
	private final ActionsType actionsType;
	private HttpClientWrapper client;
	private WebDriver driver;

	public TrailBlockEditor(String baseUrl, String cookieValue, ActionsType actionsType) {
		this.baseUrl = baseUrl;
		this.cookieValue = cookieValue;
		this.actionsType = actionsType;

	}

	@Override
	public Cookie authenticationData() {

		String cookieName = "PLAY_SESSION";
		BasicClientCookie2 cookie = new BasicClientCookie2(cookieName, cookieValue);

		URL url;
		try {
			url = new URL(baseUrl);
		} catch (MalformedURLException e) {
			throw new RuntimeException(String.format("Could not decode url: %s", baseUrl));
		}

		cookie.setDomain(url.getHost());
		cookie.setPath("/");
		cookie.setSecure(false);
		DateTime expiry = new DateTime().plusYears(10);
		cookie.setExpiryDate(expiry.toDate());

		return cookie;
	}

	public void execute(TrailBlockAction action) {

		switch (actionsType) {
			case API:
				client = setUpHttpClient(baseUrl);
				((TrailBlockApiAction) action).useClient(client);
				break;
			case UI:
				driver = new FirefoxDriver();
				driver.get(String.format("%s/%s", baseUrl, "logout"));

				((TrailBlockUIAction) action).useDriver(driver, baseUrl);
				break;

		}
		action.setAuthenticationData(authenticationData());


		super.execute(action);
	}

	private HttpClientWrapper setUpHttpClient(String baseUrl) {
		if (client != null) {
			return client;
		}

		HttpClientWrapper client = new HttpClientWrapper();
		client.dontCareAboutSSL();
		client.followRedirects();
		client.setHost(baseUrl);
		return client;
	}

}
