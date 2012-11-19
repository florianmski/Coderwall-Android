package com.florianmski.coderwall;

import java.util.List;

import android.app.Application;

import com.octo.android.robospice.persistence.file.InFileObjectPersister;
import com.octo.android.robospice.persistence.file.InFileObjectPersisterFactory;

public class MyJacksonObjectPersisterFactory extends InFileObjectPersisterFactory {

    public MyJacksonObjectPersisterFactory( Application application ) {
        super( application );
    }

    public MyJacksonObjectPersisterFactory( Application application, List< Class< ? >> listHandledClasses ) {
        super( application, listHandledClasses );
    }

    @Override
    public < DATA > InFileObjectPersister< DATA > createObjectPersister( Class< DATA > clazz ) {
        InFileObjectPersister< DATA > inFileObjectPersister = new MyJacksonObjectPersister< DATA >( getApplication(), clazz, getCachePrefix() );
        inFileObjectPersister.setAsyncSaveEnabled( isAsyncSaveEnabled() );
        return inFileObjectPersister;
    }

}
