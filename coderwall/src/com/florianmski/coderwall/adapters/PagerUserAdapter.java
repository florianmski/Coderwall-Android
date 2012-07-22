package com.florianmski.coderwall.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.florianmski.coderwall.Constants;
import com.florianmski.coderwall.models.CWUser;
import com.florianmski.coderwall.ui.fragments.AccomplishmentsFragment;
import com.florianmski.coderwall.ui.fragments.BadgesFragment;
import com.florianmski.coderwall.ui.fragments.ProfileFragment;
import com.florianmski.coderwall.ui.fragments.SkillsFragment;

public class PagerUserAdapter extends FragmentStatePagerAdapter
{
	private final static String titles[] = {"Profile", "Badges", "Skills", "Accomplishments"};

	private Bundle args;

	public PagerUserAdapter(FragmentManager fm, CWUser u) 
	{
		super(fm);

		this.args = new Bundle();
		args.putSerializable(Constants.BUNDLE_USER, u);
	}

	public void refreshUser(CWUser u)
	{
		args.putSerializable(Constants.BUNDLE_USER, u);
		this.notifyDataSetChanged();
	}
	
	@Override
	public CharSequence getPageTitle(int position)
	{
		return titles[position];
	}

	@Override
	public Fragment getItem(int position) 
	{
		switch(position)
		{
		case 0:
			return ProfileFragment.newInstance(args);
		case 1:
			return BadgesFragment.newInstance(args);
		case 2:
			return SkillsFragment.newInstance(args);
		case 3:
			return AccomplishmentsFragment.newInstance(args);
		default:
			return ProfileFragment.newInstance(args);
		}
	}

	@Override
	public int getCount() 
	{
		return titles.length;
	}
	
	@Override
	public int getItemPosition(Object object) 
	{
	    return POSITION_NONE;
	}
}
