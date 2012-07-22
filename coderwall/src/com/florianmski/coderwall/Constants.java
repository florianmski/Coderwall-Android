package com.florianmski.coderwall;

public class Constants 
{
	private final static String PACKAGE_NAME = "com.florianmski.coderwall";
	private final static String SEPARATOR = ".";
		
	public final static String BUNDLE_USER = get("User");
	
	public final static String PREF_USERNAME = get("Username");
	
	private final static String get(String text)
	{
		return PACKAGE_NAME + SEPARATOR + text;
	}
}
