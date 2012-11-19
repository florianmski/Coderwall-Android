package com.florianmski.coderwall.ui.activities;

import android.os.Bundle;

import com.florianmski.coderwall.ui.fragments.TeamFragment;

public class TeamActivity extends BaseActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		if(savedInstanceState == null)
			setPrincipalFragment(TeamFragment.newInstance(getIntent().getExtras()));
	}
}