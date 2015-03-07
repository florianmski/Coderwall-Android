package com.florianmski.coderwall.framework.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.florianmski.coderwall.BuildConfig;

public class PrefUtils
{
    protected static SharedPreferences prefs;

    public static void create(Context context)
    {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    protected String getString(String key, String defValue)
    {
        return prefs.getString(key, defValue);
    }

    protected boolean getBoolean(String key, boolean defValue)
    {
        return prefs.getBoolean(key, defValue);
    }

    protected int getInt(String key, int defValue)
    {
        return prefs.getInt(key, defValue);
    }

    protected long getLong(String key, long defValue)
    {
        return prefs.getLong(key, defValue);
    }

    protected double getDouble(String key, double defValue)
    {
        return Double.longBitsToDouble(getLong(key, Double.doubleToLongBits(defValue)));
    }

    protected double getDouble(String key, long defValue)
    {
        return Double.longBitsToDouble(getLong(key, defValue));
    }

    protected void putString(String key, String value)
    {
        prefs.edit().putString(key, value).apply();
    }

    protected void putBoolean(String key, boolean value)
    {
        prefs.edit().putBoolean(key, value).apply();
    }

    protected void putInt(String key, int value)
    {
        prefs.edit().putInt(key, value).apply();
    }

    protected void putLong(String key, long value)
    {
        prefs.edit().putLong(key, value).apply();
    }

    protected void putDouble(String key, double value)
    {
        putLong(key, Double.doubleToLongBits(value));
    }

    protected void clear()
    {
        prefs.edit().clear().apply();
    }

    protected String getKey(String key)
    {
        return String.format("%s.%s", BuildConfig.APPLICATION_ID, key);
    }
}
