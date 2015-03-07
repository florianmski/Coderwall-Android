package com.florianmski.coderwall.data;

import com.florianmski.coderwall.framework.utils.PrefUtils;

public class CoderwallPrefs extends PrefUtils
{
    private static CoderwallPrefs instance;

    private CoderwallPrefs() {}

    public static CoderwallPrefs getInstance()
    {
        if(instance == null)
            instance = new CoderwallPrefs();

        return instance;
    }

    private final String LAST_USERNAME_ENTERED = getKey("lastUsernameEntered");

    public String getLastUsernameEntered()
    {
        return getString(LAST_USERNAME_ENTERED, "florianmski");
    }

    public void putLastUsernameEntered(String username)
    {
        putString(LAST_USERNAME_ENTERED, username);
    }
}
