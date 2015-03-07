package com.florianmski.coderwall.ui.activities;

import android.os.Bundle;

import com.florianmski.coderwall.R;
import com.florianmski.coderwall.framework.ui.activities.TranslucentActivity;
import com.florianmski.coderwall.ui.fragments.UserFragment;

public class UserActivity extends TranslucentActivity
{
    @Override
    protected int getContentViewId()
    {
        return R.layout.activity_user;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        if(savedInstanceState == null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_user, UserFragment.newInstance())
                    .commit();
        }
    }
}
