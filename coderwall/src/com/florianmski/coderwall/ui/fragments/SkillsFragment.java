package com.florianmski.coderwall.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.florianmski.coderwall.Constants;
import com.florianmski.coderwall.R;
import com.florianmski.coderwall.adapters.ListSkillsAdapter;
import com.florianmski.coderwall.models.User;

public class SkillsFragment extends BaseFragment
{
	private ListView lvSkills;

	public static SkillsFragment newInstance(Bundle args)
	{
		SkillsFragment f = new SkillsFragment();
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

		lvSkills.setAdapter(new ListSkillsAdapter(getSherlockActivity(), u.getSpecialities()));		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View v = inflater.inflate(R.layout.fragment_skills, null);

		lvSkills = (ListView)v.findViewById(R.id.listViewSkills);

		return v;
	}
}
