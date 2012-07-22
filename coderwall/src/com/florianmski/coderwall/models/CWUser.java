package com.florianmski.coderwall.models;

import java.io.Serializable;

import net.caseydunham.coderwall.data.User;

public class CWUser extends User implements Serializable
{
	private static final long serialVersionUID = -1083786004953761147L;

	private String specialImage;
	private String about;
	
	public CWUser(User u)	
	{
		this.setAccounts(u.getAccounts());
		this.setBadges(u.getBadges());
		this.setEndorsements(u.getEndorsements());
		this.setLocation(u.getLocation());
		this.setName(u.getName());
		this.setTeam(u.getTeam());
		this.setUsername(u.getUsername());
		this.setAccomplishments(u.getAccomplishments());
		this.setCompany(u.getCompany());
		this.setThumbnail(u.getThumbnail());
		this.setSpecialities(u.getSpecialities());
		this.setTitle(u.getTitle());
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
