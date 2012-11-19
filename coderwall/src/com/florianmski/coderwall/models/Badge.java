package com.florianmski.coderwall.models;

import java.io.Serializable;

public class Badge implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String name;

	private String badge;
	private String description;
	private String created;

	public Badge() {}

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public String getBadge() 
	{
		return badge;
	}

	public void setBadge(String badge) 
	{
		this.badge = badge;
	}

	public String getDescription() 
	{
		return description;
	}

	public void setDescription(String description) 
	{
		this.description = description;
	}

	public String getCreated() 
	{
		return created;
	}

	public void setCreated(String created) 
	{
		this.created = created;
	}

}
