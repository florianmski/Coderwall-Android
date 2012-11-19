package com.florianmski.coderwall.ui.activities;

import android.os.Bundle;

import com.florianmski.coderwall.ui.fragments.LeaderboardFragment;

public class LeaderboardActivity extends BaseActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		if(savedInstanceState == null)
			setPrincipalFragment(LeaderboardFragment.newInstance(getIntent().getExtras()));
	}
}
