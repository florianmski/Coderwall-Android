package com.florianmski.coderwall.api.service;

import java.util.List;

import com.florianmski.coderwall.models.Team;
import com.florianmski.coderwall.models.User;


public interface CoderwallApiClient 
{
	User user(String username) throws Exception;
	List<Team> leaderboard() throws Exception;
}
