package com.florianmski.coderwall;

import android.app.Application;

import com.octo.android.robospice.SpiceService;
import com.octo.android.robospice.persistence.CacheManager;

public class CoderwallSpiceService extends SpiceService
{

	@Override
	public CacheManager createCacheManager(Application app) 
	{
		CacheManager cacheManager = new CacheManager();
		
		MyJacksonObjectPersisterFactory inJSonFileObjectPersisterFactory = new MyJacksonObjectPersisterFactory(app);
//        inJSonFileObjectPersisterFactory.setAsyncSaveEnabled(true);

        cacheManager.addPersister(inJSonFileObjectPersisterFactory);
		
		return cacheManager;
	}

}
