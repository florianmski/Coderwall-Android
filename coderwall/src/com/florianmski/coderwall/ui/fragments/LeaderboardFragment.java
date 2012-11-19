package com.florianmski.coderwall.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.florianmski.coderwall.R;

public class LeaderboardFragment extends BaseFragment
{
	private ListView lvLeaderboard;
	
	public static LeaderboardFragment newInstance(Bundle args)
	{
		LeaderboardFragment f = new LeaderboardFragment();
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
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View v = inflater.inflate(R.layout.fragment_leaderboard, null);

		lvLeaderboard = (ListView)v.findViewById(R.id.listViewLeaderbord);
		
		return v;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		super.onCreateOptionsMenu(menu, inflater);

//		menu.add(Menu.NONE, R.id.action_bar_invite, 0, "Search")
//		.setIcon(R.drawable.ic_menu_invite)
//		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
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
