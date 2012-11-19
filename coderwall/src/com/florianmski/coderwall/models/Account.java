package com.florianmski.coderwall.models;

import java.io.Serializable;

public class Account implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String github;
	private String twitter;

	public Account() {}

	public String getGithub() 
	{
		return github; 
	}
	
	public void setGithub(String github) 
	{ 
		this.github = github; 
	}

	public String getTwitter() 
	{
		return twitter; 
	}
	
	public void setTwitter(String twitter) 
	{ 
		this.twitter = twitter; 
	}
}
