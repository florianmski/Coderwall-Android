package com.florianmski.coderwall.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class BaseActivity extends SherlockFragmentActivity
{
//	public SpiceManager spiceManager = new SpiceManager(CoderwallSpiceService.class);

	@Override
	public void onStart() 
	{
		super.onStart();
//		spiceManager.start(this);
	}

	@Override
	public void onStop() 
	{
//		spiceManager.shouldStop();
		super.onStop();
	}
	
	public void setPrincipalFragment(Fragment fragment)
	{
		getSupportFragmentManager().beginTransaction().replace(android.R.id.content, fragment, null).commit();
	}

	protected void setTitle(String title)
	{
		getSupportActionBar().setTitle(title);
	}

	protected void setSubtitle(String subtitle)
	{
		getSupportActionBar().setSubtitle(subtitle);
	}

	public void launchActivity(Class<?> activityToLaunch, Bundle args)
	{
		Intent i = new Intent(this, activityToLaunch);
		if(args != null)
			i.putExtras(args);
		startActivity(i);
	}

	public void launchActivityForResult(Class<?> activityToLaunch, int requestCode, Bundle args)
	{
		Intent i = new Intent(this, activityToLaunch);
		if(args != null)
			i.putExtras(args);
		startActivityForResult(i, requestCode);
	}

	public void launchActivity(Class<?> activityToLaunch)
	{
		launchActivity(activityToLaunch, null);
	}

	public void launchActivityForResult(Class<?> activityToLaunch, int requestCode)
	{
		launchActivityForResult(activityToLaunch, requestCode, null);
	}
}
