package com.florianmski.coderwall.models;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String username;
	private String name;
	private String team;
	private String location;
	private String company;
	private String title;
	private String thumbnail;
	private Integer endorsements;
	private List<Badge> badges;
	private List<String> specialities;
	private List<String> accomplishments;
	private Account accounts;
	private String specialImage;
	private String about;

	public User() {}

	public String getUsername() 
	{
		return username;
	}

	public void setUsername(String username) 
	{
		this.username = username;
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public String getTeam() 
	{
		return team;
	}

	public void setTeam(String team) 
	{
		this.team = team;
	}

	public String getLocation() 
	{
		return location;
	}

	public void setLocation(String location) 
	{
		this.location = location;
	}

	public String getCompany() 
	{
		return company;
	}

	public void setCompany(String company) 
	{
		this.company = company;
	}

	public String getTitle() 
	{
		return title;
	}

	public void setTitle(String title) 
	{
		this.title = title;
	}

	public String getThumbnail() 
	{
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) 
	{
		this.thumbnail = thumbnail;
	}

	public Integer getEndorsements() 
	{
		return endorsements;
	}

	public void setEndorsements(Integer endorsements)
	{
		this.endorsements = endorsements;
	}

	public List<Badge> getBadges() 
	{
		return badges;
	}

	public void setBadges(List<Badge> badges) 
	{
		this.badges = badges;
	}

	public List<String> getSpecialities() 
	{
		return specialities;
	}

	public void setSpecialities(List<String> specialities) 
	{
		this.specialities = specialities;
	}

	public List<String> getAccomplishments() 
	{
		return accomplishments;
	}

	public void setAccomplishments(List<String> accomplishments) 
	{
		this.accomplishments = accomplishments;
	}

	public Account getAccounts() 
	{
		return accounts;
	}

	public void setAccounts(Account accounts) 
	{
		this.accounts = accounts;
	}
	
	public String getSpecialImage() 
	{
		return specialImage;
	}

	public void setSpecialImage(String specialImage) 
	{
		this.specialImage = specialImage;
	}

	public String getAbout() 
	{
		return about;
	}

	public void setAbout(String about) 
	{
		this.about = about;
	}

}
