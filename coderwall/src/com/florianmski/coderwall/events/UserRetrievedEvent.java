package com.florianmski.coderwall.events;

import com.florianmski.coderwall.models.CWUser;

public class UserRetrievedEvent 
{
	private CWUser u;
	private boolean fromCache;
	private boolean taskOver;
	private Exception e;
	
	public UserRetrievedEvent(CWUser u, boolean fromCache, boolean taskOver)
	{
		this.u = u;
		this.fromCache = fromCache;
		this.taskOver = taskOver;
	}
	
	public CWUser getUser()
	{
		return u;
	}
	
	public boolean isFromCache()
	{
		return fromCache;
	}
	
	public boolean isTaskOver()
	{
		return taskOver;
	}
	
	public void setError(Exception e)
	{
		this.e = e;
	}
	
	public Exception getError()
	{
		return e;
	}
}
