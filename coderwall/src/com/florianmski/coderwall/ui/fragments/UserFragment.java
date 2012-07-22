package com.florianmski.coderwall.ui.fragments;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import net.caseydunham.coderwall.data.User;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.florianmski.coderwall.Constants;
import com.florianmski.coderwall.R;
import com.florianmski.coderwall.Utils;
import com.florianmski.coderwall.adapters.ListUserAdapter;
import com.florianmski.coderwall.adapters.PagerUserAdapter;
import com.florianmski.coderwall.api.ApiCache;
import com.florianmski.coderwall.api.CWManager;
import com.florianmski.coderwall.events.UserRetrievedEvent;
import com.florianmski.coderwall.models.CWUser;
import com.florianmski.coderwall.tasks.GetUserProfileTask;
import com.squareup.otto.Subscribe;
import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitlePageIndicator.IndicatorStyle;

public class UserFragment extends BaseFragment implements OnPageChangeListener
{
	private TitlePageIndicator pageIndicator;
	private ViewPager viewPager;

	private PagerUserAdapter pagerAdapter;
	private ListUserAdapter listAdapter;

	private FrameLayout flProgress;

	private CWUser u;
	private String username;
	private boolean refresh = false;

	private GetUserProfileTask task;

	public static UserFragment newInstance(Bundle args)
	{
		UserFragment f = new UserFragment();
		f.setArguments(args);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayShowHomeEnabled(false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);

		if(savedInstanceState != null)
			u = (CWUser) savedInstanceState.get(Constants.BUNDLE_USER);

		createProgressItem();

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getSherlockActivity());
		username = prefs.getString(Constants.PREF_USERNAME, null);

		if(username == null)
			createAlertDialog();

		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		getActionBar().setListNavigationCallbacks(listAdapter = new ListUserAdapter(getSherlockActivity(), new ArrayList<User>()), new OnNavigationListener() 
		{
			@Override
			public boolean onNavigationItemSelected(int itemPosition, long itemId)
			{
				//					if(username != null && (u == null || getUserIndex(u.getUsername()) != itemPosition))
				if(username != null)
					createTask(listAdapter.getItem(itemPosition).getUsername(), false).execute();
				return true;
			}
		});

		refreshNavigationListAdapter();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View v = inflater.inflate(R.layout.fragment_user, null);

		viewPager = (ViewPager) v.findViewById(R.id.paged_view);
		pageIndicator = (TitlePageIndicator) v.findViewById(R.id.page_indicator_title);

		return v;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		super.onCreateOptionsMenu(menu, inflater);

		menu.add(Menu.NONE, R.id.action_bar_invite, 0, "Search")
		.setIcon(R.drawable.ic_menu_invite)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		MenuItem refreshItem = menu.add(Menu.NONE, R.id.action_bar_refresh, 1, "Refresh");
		refreshItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		if(refresh)
			refreshItem.setActionView(flProgress);
		else
			refreshItem.setIcon(R.drawable.ic_menu_refresh);

		if(username != null && u!= null && !username.equals(u.getUsername()))
		{
			menu.add(Menu.NONE, R.id.action_bar_delete, Menu.NONE, "Delete")
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		}

		menu.add(Menu.NONE, R.id.action_bar_about, Menu.NONE, "About")
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch(item.getItemId()) 
		{	
		case R.id.action_bar_refresh:
			if(u != null)
				createTask(u.getUsername(), true).execute();
			break;
		case R.id.action_bar_invite:
			createAlertDialog();
			break;
		case R.id.action_bar_about:
			new AlertDialog.Builder(getSherlockActivity())
			.setTitle("About")
			.setMessage("When life gives you lemons, don't make lemonade. " +
					"Make life take the lemons back! Get mad! I don't want your damn lemons! " +
					"What am I supposed to do with these?! Demand to see life's manager! " +
					"Make life rue the day it thought it could give Cave Johnson lemons! " +
					"Do you know who I am? I'm the man who's gonna burn your house down! " +
					"With the lemons! I'm gonna get my engineers to invent a combustible lemon that burns your house down! ")
					.setPositiveButton("Dev profile", new DialogInterface.OnClickListener() 
					{
						public void onClick(DialogInterface dialog, int whichButton) 
						{
							createTask("florianmski", true).execute();
						}
					})
					.setNeutralButton("Close", new DialogInterface.OnClickListener() 
					{
						public void onClick(DialogInterface dialog, int whichButton) {}
					}).show();
			break;
		case R.id.action_bar_delete:
			new File(Utils.getAPICacheFolderPath() + u.getUsername() + ".html").delete();
			new File(Utils.getAPICacheFolderPath() + u.getUsername() + CWManager.FILE_FORMAT).delete();
			createTask(username, false).execute();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public GetUserProfileTask createTask(String username, boolean refreshFromWebOnly)
	{
		UserFragment.this.setRefresh(true);

		if(task != null && task.isRunning())
			task.cancel();

		Log.e("username","username : "+username);

		return task = new GetUserProfileTask(getSherlockActivity(), username, refreshFromWebOnly);
	}

	public void refreshNavigationListAdapter()
	{
		List<User> users = new ArrayList<User>();
		File dir = new File(Utils.getAPICacheFolderPath());
		if(dir.exists())
		{
			for(String s : dir.list(new FilenameFilter() 
			{
				@Override
				public boolean accept(File dir, String filename) 
				{
					return filename.contains(CWManager.FILE_FORMAT);
				}
			}))
			{
				try 
				{
					users.add(ApiCache.readUserJsonFromCache(s.replace(CWManager.FILE_FORMAT, "")));
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		}

		if(listAdapter == null)
			listAdapter = new ListUserAdapter(getSherlockActivity(), users);
		else
			listAdapter.refreshItems(users);

		if(u != null)
			getActionBar().setSelectedNavigationItem(getUserIndex(u.getUsername()));
	}

	public void createProgressItem()
	{
		flProgress = new FrameLayout(getSherlockActivity());
		ProgressBar pb = new ProgressBar(getSherlockActivity());
		pb.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress));
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
		lp.setMargins(px/2, px, px/2, px);
		pb.setLayoutParams(lp);
		flProgress.addView(pb);
		flProgress.setClickable(true);
		flProgress.setBackgroundResource(R.drawable.abs__item_background_holo_light);
	}

	public void setRefresh(boolean on)
	{
		this.refresh = on;
		getSherlockActivity().invalidateOptionsMenu();
	}

	public void createAlertDialog()
	{
		final EditText edtUsername = new EditText(getSherlockActivity());
		edtUsername.setSingleLine();
		edtUsername.setImeOptions(EditorInfo.IME_ACTION_DONE);

		final AlertDialog alert = new AlertDialog.Builder(getSherlockActivity())
		.setTitle("Coderwall")
		.setMessage("Please enter " + (username == null ? "your" : "a") + " username")
		.setView(edtUsername)
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int whichButton) 
			{
				createTask(edtUsername.getText().toString().trim(), true).execute();
			}
		}).show();

		edtUsername.setOnEditorActionListener(new OnEditorActionListener() 
		{
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) 
			{
				alert.getButton(AlertDialog.BUTTON_POSITIVE).performClick();
				return false;
			}
		});
	}

	private int getUserIndex(String username)
	{
		if(username != null)
		{
			int index = 0;
			for(User user : listAdapter.getItems())
			{
				if(user.getUsername().toUpperCase().trim().equals(username.toUpperCase().trim()))
					break;
				index++;
			}
			return index;
		}

		return 0;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		
		if(task != null && task.isRunning())
			task.cancel();
	}

	@Subscribe
	public void onUserRetrieved(UserRetrievedEvent event)
	{
		CWUser u = event.getUser();

		if(event.isTaskOver())
			UserFragment.this.setRefresh(false);

		if(event.getError() != null)
		{
			Toast.makeText(getSherlockActivity(), event.getError().getMessage(), Toast.LENGTH_LONG).show();
			return;
		}
		else if(u == null)
			return;

		if(UserFragment.this.username == null)
		{
			UserFragment.this.username = u.getUsername();
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getSherlockActivity());
			prefs.edit().putString(Constants.PREF_USERNAME, u.getUsername()).commit();
		}

		UserFragment.this.u = u;
		refreshNavigationListAdapter();

		if(pagerAdapter == null || viewPager.getAdapter() == null)
		{
			viewPager.setAdapter(pagerAdapter = new PagerUserAdapter(getFragmentManager(), u));
			pageIndicator.setViewPager(viewPager);
			pageIndicator.setOnPageChangeListener(UserFragment.this);
			pageIndicator.setBackgroundColor(getResources().getColor(R.color.coderwall_blue));
			pageIndicator.setFooterColor(Color.WHITE);
			pageIndicator.setFooterIndicatorStyle(IndicatorStyle.Triangle);
			pageIndicator.setFooterLineHeight(0);
		}
		else
			pagerAdapter.refreshUser(u);
	}

	@Override
	public void onPageScrollStateChanged(int state) {}

	@Override
	public void onPageScrolled(int position, float positionOffset,	int positionOffsetPixels) {}

	@Override
	public void onPageSelected(int position) {}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);

		outState.putSerializable(Constants.BUNDLE_USER, u);
	}
}
