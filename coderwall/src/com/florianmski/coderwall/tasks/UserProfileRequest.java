package com.florianmski.coderwall.tasks;

import java.net.HttpURLConnection;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.florianmski.coderwall.models.User;
import com.octo.android.robospice.persistence.exception.SpiceException;

public class UserProfileRequest extends BaseRequest<User> 
{	
	private static final String URL = "https://www.coderwall.com/%s.json?full=true";
	
	private String username;
	
	public UserProfileRequest(String username) 
	{		
		super(User.class, String.format(URL, username));
		
		this.username = username;
	}

	@Override
	public User loadDataFromNetwork() throws Exception
	{
		if(username == null || username.trim().equals(""))
			throw new SpiceException("Username can't be empty!");
		
		User user = super.loadDataFromNetwork();

//		CWUser u = new CWUser(user);
//		Document doc = Jsoup.parse(CWManager.getInstance().getUserHtml(username));

		return user;
	}
	
	@Override
	protected String getError(int requestCode)
	{
		if(requestCode == HttpURLConnection.HTTP_NOT_FOUND)
			return "User may not exist.";
		
		return super.getError(requestCode);
	}
	
	private User extractHtmlInfos(Document doc, User u)
	{
		if(doc != null)
		{
			Elements specialImageElement = doc.getElementsByClass("special-image");
			if(specialImageElement != null)
			{
				try
				{
					String specialImage = 
							specialImageElement
							.get(0)
							.getElementsByTag("img")
							.get(0)
							.attr("src");
					u.setSpecialImage(specialImage);
				}
				catch(Exception e) {e.printStackTrace();}
				try
				{
					String about = 
							doc.getElementsByClass("profile-details")
							.get(0)
							.getElementsByTag("li")
							.get(0)
							.getElementsByTag("p")
							.get(0)
							.text();
					u.setAbout(about);
				}
				catch(Exception e) {e.printStackTrace();}
			}
		}
		return u;
	}
    
}
