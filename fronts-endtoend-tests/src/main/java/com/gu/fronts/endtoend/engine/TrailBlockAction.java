package com.gu.fronts.endtoend.engine;

import hu.meza.aao.Action;
import org.apache.http.cookie.Cookie;

public interface TrailBlockAction extends Action {

	void setAuthenticationData(Cookie cookie);

	boolean success();

}
