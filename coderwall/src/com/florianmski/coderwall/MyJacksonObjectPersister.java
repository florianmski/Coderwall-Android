package com.florianmski.coderwall;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import roboguice.util.temp.Ln;
import android.app.Application;

import com.octo.android.robospice.persistence.exception.CacheLoadingException;
import com.octo.android.robospice.persistence.exception.CacheSavingException;
import com.octo.android.robospice.persistence.file.InFileObjectPersister;

public class MyJacksonObjectPersister<T> extends InFileObjectPersister<T>  {

    // ============================================================================================
    // ATTRIBUTES
    // ============================================================================================

    private final ObjectMapper mJsonMapper;

    private String mFactoryPrefix;

    // ============================================================================================
    // CONSTRUCTOR
    // ============================================================================================
    public MyJacksonObjectPersister( Application application, Class< T > clazz, String factoryPrefix ) {
        super( application, clazz );
        this.mJsonMapper = new ObjectMapper();
        this.mFactoryPrefix = factoryPrefix;
    }

    // ============================================================================================
    // METHODS
    // ============================================================================================

    @Override
    protected String getCachePrefix() {
        return mFactoryPrefix + super.getCachePrefix();
    }

    @Override
    public final T loadDataFromCache( Object cacheKey, long maxTimeInCacheBeforeExpiry ) throws CacheLoadingException {
        T result = null;
        String resultJson = null;

        File file = getCacheFile( cacheKey );
        if ( file.exists() ) {
            long timeInCache = System.currentTimeMillis() - file.lastModified();
            if ( maxTimeInCacheBeforeExpiry == 0 || timeInCache <= maxTimeInCacheBeforeExpiry ) {
                try {
                	FileInputStream fis = new FileInputStream(file);
                	BufferedReader r = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
                	StringBuilder total = new StringBuilder(fis.available());
                	String line;
                	while ((line = r.readLine()) != null) {
                	    total.append(line);
                	}
                	r.close();
                	resultJson = total.toString();

                    // finally transform json in object
                    if ( resultJson != null && !resultJson.equals("") ) {
                        result = mJsonMapper.readValue( resultJson, getHandledClass() );
                        return result;
                    }
                    throw new CacheLoadingException( "Unable to restore cache content : cache file is empty" );
                } catch ( FileNotFoundException e ) {
                    // Should not occur (we test before if file exists)
                    // Do not throw, file is not cached
                    Ln.w( "file " + file.getAbsolutePath() + " does not exists", e );
                    return null;
                } catch ( CacheLoadingException e ) {
                    throw e;
                } catch ( Exception e ) {
                    throw new CacheLoadingException( e );
                }
            }
            Ln.v( "Cache content is expired since " + ( maxTimeInCacheBeforeExpiry - timeInCache ) );
            return null;
        }
        Ln.v( "file " + file.getAbsolutePath() + " does not exists" );
        return null;
    }

    @Override
    public T saveDataToCacheAndReturnData( final T data, final Object cacheKey ) throws CacheSavingException {

        try {
            if ( isAsyncSaveEnabled ) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            saveData( data, cacheKey );
                        } catch ( IOException e ) {
                            Ln.e( e, "An error occured on saving request " + cacheKey + " data asynchronously" );
                        } catch ( CacheSavingException e ) {
                            Ln.e( e, "An error occured on saving request " + cacheKey + " data asynchronously" );
                        } finally {
                            // notify that saving is finished for test purpose
                            lock.lock();
                            condition.signal();
                            lock.unlock();
                        }
                    };
                }.start();
            } else {
                saveData( data, cacheKey );
            }
        } catch ( CacheSavingException e ) {
            throw e;
        } catch ( Exception e ) {
            throw new CacheSavingException( e );
        }
        return data;
    }

    private void saveData( T data, Object cacheKey ) throws IOException, JsonGenerationException, JsonMappingException, CacheSavingException {
        String resultJson;
        // transform the content in json to store it in the cache
        resultJson = mJsonMapper.writeValueAsString( data );

        // finally store the json in the cache
        if ( resultJson != null && !resultJson.equals("") ) {
        	BufferedWriter writer = new BufferedWriter(new FileWriter(getCacheFile(cacheKey)));
            writer.write(resultJson);
            writer.close();
        } else {
            throw new CacheSavingException( "Data was null and could not be serialized in json" );
        }
    }

    @Override
    public boolean canHandleClass( Class< ? > clazz ) {
        return true;
    }

    /** for testing purpose only. Overriding allows to regive package level visibility. */
    @Override
    protected void awaitForSaveAsyncTermination( long time, TimeUnit timeUnit ) throws InterruptedException {
        super.awaitForSaveAsyncTermination( time, timeUnit );
    }

    /** for testing purpose only. Overriding allows to regive package level visibility. */
    @Override
    protected File getCacheFile( Object cacheKey ) {
        return super.getCacheFile( cacheKey );
    }
    
    @Override
    public List< Object > getAllCacheKeys() {
        final String prefix = getCachePrefix();
        String[] cacheFileNameList = getApplication().getCacheDir().list( new FilenameFilter() {
            @Override
            public boolean accept( File dir, String filename ) {
                return filename.startsWith( prefix );
            }
        } );
        List< Object > result = new ArrayList< Object >();
        for ( String cacheFileName : cacheFileNameList ) {
            result.add( cacheFileName.substring( prefix.length() ) );
        }

        return result;

    }

}

