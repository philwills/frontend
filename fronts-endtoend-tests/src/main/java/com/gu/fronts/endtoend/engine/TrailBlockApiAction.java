package com.gu.fronts.endtoend.engine;

import hu.meza.tools.HttpClientWrapper;

public interface TrailBlockApiAction extends TrailBlockAction {

	void useClient(HttpClientWrapper client);

}
