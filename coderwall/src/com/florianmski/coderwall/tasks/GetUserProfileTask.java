package com.florianmski.coderwall.tasks;

import net.caseydunham.coderwall.data.User;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.content.Context;

import com.florianmski.coderwall.CWBus;
import com.florianmski.coderwall.Utils;
import com.florianmski.coderwall.api.ApiCache;
import com.florianmski.coderwall.api.CWManager;
import com.florianmski.coderwall.events.UserRetrievedEvent;
import com.florianmski.coderwall.models.CWUser;

public class GetUserProfileTask extends BackgroundTask<CWUser>
{	
	private Context context;
	private String username;
	
	private boolean refreshFromWebOnly;

	public GetUserProfileTask(Context context, String username, boolean refreshFromWebOnly)
	{
		this.context = context.getApplicationContext();
		this.username = username;
		this.refreshFromWebOnly = refreshFromWebOnly;
		setId(this.getClass().getName());
	}

	@Override
	protected CWUser doWorkInBackground() throws Exception 
	{
		User user = ApiCache.readUserJsonFromCache(username);

		if(!refreshFromWebOnly && user != null)
		{
			CWUser u = new CWUser(user);
			Document doc = Jsoup.parse(ApiCache.readUserHtmlFromCache(username), "utf-8", "");
			publishProgress(0, extractHtmlInfos(doc, u), (String[])null);
		}

		if(Utils.isOnline(context))
		{
			user = CWManager.getInstance().getUser(username, true);

			CWUser u = new CWUser(user);
			Document doc = Jsoup.parse(CWManager.getInstance().getUserHtml(username));

			return extractHtmlInfos(doc, u);
		}
		else if(refreshFromWebOnly)
			throw new Exception("Internet connection required!");
		
		return null;
	}

	private CWUser extractHtmlInfos(Document doc, CWUser u)
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

	@Override
	protected void onCompleted(CWUser result) 
	{
		CWBus.getInstance().post(new UserRetrievedEvent(result, false, true));
	}

	@Override
	protected void onFailed(Exception e) 
	{
		UserRetrievedEvent event = new UserRetrievedEvent(null, true, true);
		event.setError(e);
		CWBus.getInstance().post(event);
	}

	@Override
	protected void onPreExecute() 
	{

	}

	@Override
	protected void onProgressPublished(int progress, CWUser tmpResult, String... values)
	{
		CWBus.getInstance().post(new UserRetrievedEvent(tmpResult, true, false));
	}
}
