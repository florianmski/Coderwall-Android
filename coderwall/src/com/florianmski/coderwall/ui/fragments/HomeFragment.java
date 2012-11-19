package com.florianmski.coderwall.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.florianmski.coderwall.R;
import com.florianmski.coderwall.ui.activities.LeaderboardActivity;
import com.florianmski.coderwall.ui.activities.UserActivity;

public class HomeFragment extends BaseFragment
{
	private Button btnProfile, btnTeam, btnAbout;
	
	public static HomeFragment newInstance(Bundle args)
	{
		HomeFragment f = new HomeFragment();
		f.setArguments(args);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		btnProfile.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				launchActivity(UserActivity.class);
			}
		});
		
		btnTeam.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				launchActivity(LeaderboardActivity.class);
			}
		});
		
		btnAbout.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//TODO
//				launchActivity(UserActivity.class);
			}
		});
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View v = inflater.inflate(R.layout.fragment_home, null);

		btnProfile = (Button) v.findViewById(R.id.button_profile);
		btnTeam = (Button) v.findViewById(R.id.button_team);
		btnAbout = (Button) v.findViewById(R.id.button_about);
		
		return v;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		super.onCreateOptionsMenu(menu, inflater);

		menu.add(Menu.NONE, R.id.action_bar_invite, 0, "Search")
		.setIcon(R.drawable.ic_menu_invite)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch(item.getItemId()) 
		{	
			
		}
		return super.onOptionsItemSelected(item);
	}
}
