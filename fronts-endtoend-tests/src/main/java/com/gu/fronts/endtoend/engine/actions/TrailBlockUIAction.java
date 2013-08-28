package com.gu.fronts.endtoend.engine.actions;

import com.gu.fronts.endtoend.engine.TrailBlockAction;
import org.openqa.selenium.WebDriver;

public interface TrailBlockUIAction extends TrailBlockAction {

	void useDriver(WebDriver driver, String baseUrl);
}
