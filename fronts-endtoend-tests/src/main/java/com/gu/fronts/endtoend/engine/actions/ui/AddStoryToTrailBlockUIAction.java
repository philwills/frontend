package com.gu.fronts.endtoend.engine.actions.ui;

import com.gu.fronts.endtoend.engine.Story;
import com.gu.fronts.endtoend.engine.TrailBlock;
import com.gu.fronts.endtoend.engine.TrailBlockMode;
import com.gu.fronts.endtoend.engine.actions.AddStoryToTrailBlockAction;
import com.gu.fronts.endtoend.engine.actions.TrailBlockUIAction;
import org.apache.http.cookie.Cookie;
import org.openqa.selenium.WebDriver;

public class AddStoryToTrailBlockUIAction implements AddStoryToTrailBlockAction, TrailBlockUIAction {
	private final Story story;
	private final TrailBlock trailblock;
	private final TrailBlockMode mode;
	private Story positionOf;
	private WebDriver driver;
	private String baseUrl;

	public AddStoryToTrailBlockUIAction(Story storyA, TrailBlock trailBlock, Story storyB) {
		this(storyA, trailBlock, storyB, TrailBlockMode.LIVE);
	}

	public AddStoryToTrailBlockUIAction(Story story, TrailBlock trailBlock, TrailBlockMode mode) {
		this.story = story;
		this.trailblock = trailBlock;
		this.mode = mode;
	}

	public AddStoryToTrailBlockUIAction(
		Story storyA, TrailBlock trailBlock, Story storyB, TrailBlockMode mode
	) {
		story = storyA;
		this.trailblock = trailBlock;
		positionOf = storyB;
		this.mode = mode;
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
	public AddStoryToTrailBlockUIAction copyOf() {
		return new AddStoryToTrailBlockUIAction(story, trailblock, positionOf, mode);
	}

	@Override
	public void useDriver(WebDriver driver, String baseUrl) {
		this.driver = driver;
		this.baseUrl = baseUrl;
	}

	private String isLive() {
		return mode == TrailBlockMode.LIVE ? "true" : "false";
	}
}
