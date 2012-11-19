package com.florianmski.coderwall.ui.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.florianmski.coderwall.Constants;
import com.florianmski.coderwall.R;
import com.florianmski.coderwall.adapters.ListBadgesAdapter;
import com.florianmski.coderwall.models.Badge;
import com.florianmski.coderwall.models.User;

public class BadgesFragment extends BaseFragment
{
	private ListView lvBadges;

	public static BadgesFragment newInstance(Bundle args)
	{
		BadgesFragment f = new BadgesFragment();
		f.setArguments(args);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);

		User u = (User) getArguments().get(Constants.BUNDLE_USER);

		if(u.getBadges() != null)
			lvBadges.setAdapter(new ListBadgesAdapter(getSherlockActivity(), new ArrayList<Badge>(u.getBadges())));		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View v = inflater.inflate(R.layout.fragment_badges, null);

		lvBadges = (ListView)v.findViewById(R.id.listViewBadges);

		return v;
	}
}
