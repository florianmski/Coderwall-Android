package com.florianmski.coderwall;

import com.squareup.otto.Bus;

public class CWBus 
{
	private static final Bus BUS = new Bus();

	public static Bus getInstance() 
	{
		return BUS;
	}

	private CWBus() {}
}
