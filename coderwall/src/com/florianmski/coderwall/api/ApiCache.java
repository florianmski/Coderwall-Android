package com.florianmski.coderwall.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import net.caseydunham.coderwall.data.User;

import com.florianmski.coderwall.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class ApiCache 
{
	public static void saveToCache(String content, String username, String fileFormat)
	{
		String path = Utils.getAPICacheFolderPath() + username + fileFormat;
		File f = new File(path);
		f.getParentFile().mkdirs();
		PrintWriter out;
		try 
		{
			out = new PrintWriter(new FileWriter(f));
			out.print(content);  
			out.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}  
	}

	public static User readUserJsonFromCache(String username) throws JsonSyntaxException, JsonIOException, FileNotFoundException
	{
		File f = new File(Utils.getAPICacheFolderPath() + username + CWManager.FILE_FORMAT);
		if(f.exists())
			return new Gson().fromJson(new BufferedReader(new FileReader(f)), User.class);

		return null;
	}

	public static File readUserHtmlFromCache(String username) throws JsonSyntaxException, JsonIOException, FileNotFoundException
	{
		File f = new File(Utils.getAPICacheFolderPath() + username + ".html");
		if(f.exists())
			return f;

		return null;
	}
}
