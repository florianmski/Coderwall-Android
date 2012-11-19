package com.florianmski.coderwall.tasks;

import com.florianmski.coderwall.models.Teams;

public class LeaderboardRequest extends BaseRequest<Teams> 
{	
	private static final String URL = "https://www.coderwall.com/leaderboard.json";
		
	public LeaderboardRequest()
	{		
		super(Teams.class, URL);
	}
}
