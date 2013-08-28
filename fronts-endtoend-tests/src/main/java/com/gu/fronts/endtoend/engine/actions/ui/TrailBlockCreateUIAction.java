package com.gu.fronts.endtoend.engine.actions.ui;

import com.gu.fronts.endtoend.engine.TrailBlock;
import com.gu.fronts.endtoend.engine.actions.TrailBlockCreateAction;
import com.gu.fronts.endtoend.engine.actions.TrailBlockUIAction;
import org.apache.http.cookie.Cookie;
import org.openqa.selenium.WebDriver;

public class TrailBlockCreateUIAction implements TrailBlockCreateAction, TrailBlockUIAction {
	private final TrailBlock trailBlock;
	private WebDriver driver;
	private String baseUrl;

	public TrailBlockCreateUIAction(TrailBlock trailBlock) {
		this.trailBlock = trailBlock;
	}

	@Override
	public void setAuthenticationData(Cookie cookie) {
		driver.manage().addCookie((org.openqa.selenium.Cookie) cookie);
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

	@Override
	public void useDriver(WebDriver driver, String baseUrl) {
		this.driver = driver;
		this.baseUrl = baseUrl;
	}

}
