package com.florianmski.coderwall.api.service;

import com.florianmski.coderwall.api.service.impl.CoderwallApiClientImpl;

public class CoderwallApiProvider 
{
	private static CoderwallApiClient instance = new CoderwallApiClientImpl();

    private CoderwallApiProvider() {}

    public static CoderwallApiClient getClient() 
    {
        if (instance == null)
            instance = new CoderwallApiClientImpl();
        
        return instance;
    }
}
