package com.florianmski.coderwall.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;

public abstract class BaseFragment extends SherlockFragment
{	
//	public SpiceManager getSpiceManager()
//	{
//		if(getActivity() instanceof BaseActivity)
//			return ((BaseActivity)getActivity()).spiceManager;
//		else
//			throw new UnsupportedOperationException("To get a SpiceManager, your activity should extend BaseActivity.");
//	}
	
	public void launchActivity(Class<?> activityToLaunch, Bundle args)
	{
		Intent i = new Intent(getActivity(), activityToLaunch);
		if(args != null)
			i.putExtras(args);
		startActivity(i);
	}

	public void launchActivity(Class<?> activityToLaunch)
	{
		launchActivity(activityToLaunch, null);
	}

	public ActionBar getActionBar()
	{
		return getSherlockActivity().getSupportActionBar();
	}

	protected void setTitle(String title)
	{
		getActionBar().setTitle(title);
	}

	protected void setSubtitle(String subtitle)
	{
		getActionBar().setSubtitle(subtitle);
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
	public void onDetach()
	{
		super.onDetach();
	}
}
