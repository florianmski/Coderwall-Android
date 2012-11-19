package com.florianmski.coderwall.api.service.impl;

import java.util.List;

import com.florianmski.coderwall.api.service.CoderwallApiClient;
import com.florianmski.coderwall.models.Team;
import com.florianmski.coderwall.models.User;
import com.florianmski.coderwall.tasks.Request;

public class CoderwallApiClientImpl implements CoderwallApiClient
{
	private String endpoint = "https://www.coderwall.com/";
	
	private String getEndpoint(String path) 
	{
		return String.format("%s%s", endpoint, path);
	}

	public void setEndpoint(String endpoint) 
	{
		this.endpoint = endpoint;
	}

	@Override
	public User user(String username) throws Exception
	{
		return new Request<User>(getEndpoint(String.format("%s.json?full=true", username))).get();
		//		client.getAsync(delegate, getEndpoint(String.format("%s.json?full=true", username)));
	}

	@Override
	public List<Team> leaderboard() throws Exception 
	{
		return new Request<List<Team>>(getEndpoint("leaderboard.json")).get();
		//		client.getAsync(delegate, getEndpoint("leaderboard.json"));
	}
}
