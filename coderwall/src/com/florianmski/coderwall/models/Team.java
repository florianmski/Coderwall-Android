package com.florianmski.coderwall.models;

import java.io.Serializable;
import java.util.List;

public class Team implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String about;
	private String id;
	private int rank;
	private int size;
	private String slug;
	private String avatar;
	private List<Team> neighbors;
	private List<Member> members;
	
	public Team() {}
	
	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public String getAbout() 
	{
		return about;
	}

	public void setAbout(String about) 
	{
		this.about = about;
	}

	public String getId() 
	{
		return id;
	}

	public void setId(String id) 
	{
		this.id = id;
	}

	public int getRank() 
	{
		return rank;
	}

	public void setRank(int rank) 
	{
		this.rank = rank;
	}

	public int getSize() 
	{
		return size;
	}

	public void setSize(int size) 
	{
		this.size = size;
	}

	public String getSlug() 
	{
		return slug;
	}

	public void setSlug(String slug) 
	{
		this.slug = slug;
	}

	public String getAvatar() 
	{
		return avatar;
	}

	public void setAvatar(String avatar) 
	{
		this.avatar = avatar;
	}
	
	public List<Team> getNeighbors() 
	{
		return neighbors;
	}

	public void setNeighbors(List<Team> neighbors) 
	{
		this.neighbors = neighbors;
	}

	public List<Member> getMembers() 
	{
		return members;
	}

	public void setMembers(List<Member> members) 
	{
		this.members = members;
	}

	public class Member implements Serializable
	{
		private static final long serialVersionUID = 1L;
		
		private String name;
		private String username;
		private int badgesCount;
		private int endorsementsCount;
		
		public Member() {}

		public String getName() 
		{
			return name;
		}

		public void setName(String name) 
		{
			this.name = name;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) 
		{
			this.username = username;
		}

		public int getBadgesCount() 
		{
			return badgesCount;
		}

		public void setBadgesCount(int badgesCount) 
		{
			this.badgesCount = badgesCount;
		}

		public int getEndorsementsCount() 
		{
			return endorsementsCount;
		}

		public void setEndorsementsCount(int endorsementsCount) 
		{
			this.endorsementsCount = endorsementsCount;
		}
	}
}
