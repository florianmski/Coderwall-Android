package com.florianmski.coderwall.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.caseydunham.coderwall.data.User;
import net.caseydunham.coderwall.exception.CoderWallException;
import net.caseydunham.coderwall.service.CoderWall;

import com.florianmski.coderwall.Utils;
import com.google.gson.Gson;

public class CWManager implements CoderWall
{
	private static final String API_URL = "http://www.coderwall.com/";
	public static final String FILE_FORMAT = ".json";

	private static CWManager cwm;

	private CWManager()
	{

	}

	public static CWManager getInstance()
	{
		if(cwm == null)
			cwm = new CWManager();

		return cwm;
	}

	private InputStream getInputStream(String url) throws CoderWallException
	{
		try 
		{
			final URL u = new URL(url);
			final HttpURLConnection conn = (HttpURLConnection)u.openConnection();
			conn.setConnectTimeout(10*1000);
			conn.setReadTimeout(10*1000);
			int respCode = conn.getResponseCode();

			if (respCode != HttpURLConnection.HTTP_OK)
				throw new CoderWallException("Error when trying to reach coderwall.com");

			return conn.getInputStream();
		} 
		catch (final MalformedURLException ex) 
		{
			throw new CoderWallException(ex);
		} 
		catch (final IOException ex) 
		{
			throw new CoderWallException(ex);
		}
	}

	@Override
	public User getUser(final String username, final boolean full) throws CoderWallException 
	{
		if (Utils.isStringEmpty(username))
			throw new CoderWallException("invalid username");
		
		InputStream is = getInputStream(API_URL + username + FILE_FORMAT + (full ? "?full=true" : ""));
		String json = Utils.convertStreamToString(is);
		ApiCache.saveToCache(json, username, FILE_FORMAT);
//		InputStreamReader isr = new InputStreamReader(is);

		return new Gson().fromJson(json, User.class);
	}
	
	public String getUserHtml(final String username) throws CoderWallException 
	{
		if (Utils.isStringEmpty(username))
			throw new CoderWallException("invalid username");
		
		InputStream is = getInputStream(API_URL + username);
		String html = Utils.convertStreamToString(is);
		ApiCache.saveToCache(html, username, ".html");
		
		return html;
	}
}
