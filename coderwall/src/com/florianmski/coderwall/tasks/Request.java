package com.florianmski.coderwall.tasks;

import com.florianmski.coderwall.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Request<T>
{	
	protected String url;
	
	public Request(String url) 
	{		
		this.url = url;
	}

	public T get() throws Exception
	{
		HttpRequest request = buildHttpRequest();
		String json = request.body();
		
		if(request.code() != 200)
			throw new Exception(getError(request.code()));
		
		T data = new Gson().fromJson(json, new TypeToken<T>(){}.getType());

		return data;
	}
	
	protected HttpRequest buildHttpRequest()
	{
		return HttpRequest
				.get(url)
				.acceptGzipEncoding()
				.uncompress(true);
	}
	
	protected String getError(int requestCode)
	{
		return "Something went wrong :/";
	}
    
}