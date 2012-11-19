package com.florianmski.coderwall.ui.fragments;

import java.util.ArrayList;
import java.util.Arrays;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.florianmski.coderwall.Constants;
import com.florianmski.coderwall.R;
import com.florianmski.coderwall.adapters.ListAccomplishmentsAdapter;
import com.florianmski.coderwall.models.User;

public class AccomplishmentsFragment extends BaseFragment
{
	private ListView lvAccomplishments;

	public static AccomplishmentsFragment newInstance(Bundle args)
	{
		AccomplishmentsFragment f = new AccomplishmentsFragment();
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

		lvAccomplishments.setAdapter(new ListAccomplishmentsAdapter(getSherlockActivity(), u.getAccomplishments()));		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View v = inflater.inflate(R.layout.fragment_accomplishments, null);

		lvAccomplishments = (ListView)v.findViewById(R.id.listViewAccomplishments);

		return v;
	}
}