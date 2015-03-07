package com.florianmski.coderwall.data;

import com.florianmski.coderwall.BuildConfig;
import com.florianmski.coderwall.data.models.User;

import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public enum CoderwallClient
{
    INSTANCE;

    private static final String API_URL = "https://coderwall.com";
    private Coderwall coderwall;

    private CoderwallClient()
    {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .build();

        coderwall = restAdapter.create(Coderwall.class);
    }

    public Coderwall get()
    {
        return coderwall;
    }

    public interface Coderwall
    {
        @GET("/{user}.json?full=true")
        Observable<User> user(
                @Path("user") String user
        );
    }
}
