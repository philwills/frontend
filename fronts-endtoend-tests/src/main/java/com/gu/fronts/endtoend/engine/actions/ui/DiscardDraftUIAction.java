package com.gu.fronts.endtoend.engine.actions.ui;

import com.gu.fronts.endtoend.engine.TrailBlock;
import com.gu.fronts.endtoend.engine.actions.DiscardDraftAction;
import com.gu.fronts.endtoend.engine.actions.TrailBlockUIAction;
import org.apache.http.cookie.Cookie;
import org.openqa.selenium.WebDriver;

public class DiscardDraftUIAction implements DiscardDraftAction, TrailBlockUIAction {
	private final TrailBlock trailBlock;
	private WebDriver driver;
	private String baseUrl;

	public DiscardDraftUIAction(TrailBlock trailBlock) {
		this.trailBlock = trailBlock;
	}

	@Override
	public boolean success() {
		return false;
	}

	@Override
	public void setAuthenticationData(Cookie cookie) {
		driver.manage().addCookie((org.openqa.selenium.Cookie) cookie);
	}

	@Override
	public void execute() {
	}

	@Override
	public DiscardDraftUIAction copyOf() {
		return new DiscardDraftUIAction(trailBlock);
	}

	@Override
	public void useDriver(WebDriver driver, String baseUrl) {
		this.driver = driver;
		this.baseUrl = baseUrl;
	}

}
