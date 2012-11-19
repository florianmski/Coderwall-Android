package com.florianmski.coderwall.ui.activities;

import android.os.Bundle;

import com.florianmski.coderwall.ui.fragments.HomeFragment;

public class HomeActivity extends BaseActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		if(savedInstanceState == null)
			setPrincipalFragment(HomeFragment.newInstance(getIntent().getExtras()));
	}
}