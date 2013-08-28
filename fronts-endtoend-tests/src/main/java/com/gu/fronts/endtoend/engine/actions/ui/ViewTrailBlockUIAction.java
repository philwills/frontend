package com.gu.fronts.endtoend.engine.actions.ui;

import com.gu.fronts.endtoend.engine.TrailBlock;
import com.gu.fronts.endtoend.engine.actions.TrailBlockUIAction;
import com.gu.fronts.endtoend.engine.actions.ViewTrailBlockAction;
import org.apache.http.cookie.Cookie;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class ViewTrailBlockUIAction implements ViewTrailBlockAction, TrailBlockUIAction {
	private final TrailBlock trailBlock;
	private WebDriver driver;
	private String baseUrl;

	public ViewTrailBlockUIAction(TrailBlock trailBlock) {
		this.trailBlock = trailBlock;
	}

	@Override
	public boolean success() {
		List<WebElement> titleParts = driver.findElements(By.className("list-header__title__part"));
		StringBuilder sb = new StringBuilder();
		for (WebElement titlePart : titleParts) {
			sb.append(titlePart.getText());
			sb.append("/");
		}

		sb.deleteCharAt(sb.lastIndexOf("/"));

		return sb.toString().equals(trailBlock.uri());
	}

	@Override
	public void setAuthenticationData(Cookie cookie) {
		driver.manage().addCookie((org.openqa.selenium.Cookie) cookie);
	}

	@Override
	public void execute() {
		String url = String.format("%s/collections?blocks=%s", baseUrl, trailBlock.uri());
		driver.get(url);
	}

	@Override
	public ViewTrailBlockUIAction copyOf() {
		return new ViewTrailBlockUIAction(trailBlock);
	}

	@Override
	public void useDriver(WebDriver driver, String baseUrl) {
		this.driver = driver;
		this.baseUrl = baseUrl;
	}

	public List<String> liveStories() {
		return new ArrayList<>();
	}

	public List<String> draftStories() {
		return new ArrayList<>();
	}
}
