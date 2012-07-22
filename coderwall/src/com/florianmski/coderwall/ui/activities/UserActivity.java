package com.florianmski.coderwall.ui.activities;

import android.os.Bundle;

import com.florianmski.coderwall.ui.fragments.UserFragment;

public class UserActivity extends BaseActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		if(savedInstanceState == null)
			setPrincipalFragment(UserFragment.newInstance(getIntent().getExtras()));
	}
}
