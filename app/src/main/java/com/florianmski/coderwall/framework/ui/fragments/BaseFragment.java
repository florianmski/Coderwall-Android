package com.florianmski.coderwall.framework.ui.fragments;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import com.florianmski.coderwall.framework.ui.activities.BaseActivity;
import com.florianmski.coderwall.framework.ui.activities.TranslucentActivity;
import com.florianmski.coderwall.framework.ui.widgets.DrawInsetsFrameLayout;

public abstract class BaseFragment extends Fragment implements DrawInsetsFrameLayout.OnInsetsCallback
{
    private Rect insets;

    public BaseActivity getBaseActivity()
    {
        return (BaseActivity)getActivity();
    }

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && getActivity() instanceof TranslucentActivity)
            ((TranslucentActivity)getActivity()).addOnInsetsCallback(this);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && getActivity() instanceof TranslucentActivity)
            ((TranslucentActivity)getActivity()).removeOnInsetsCallback(this);
    }

    public ActionBar getActionBar()
    {
        return ((BaseActivity)getActivity()).getSupportActionBar();
    }

    public Toolbar getToolbar()
    {
        return ((BaseActivity)getActivity()).getToolbar();
    }

    public ViewGroup getToolbarGroup()
    {
        return ((BaseActivity)getActivity()).getToolbarGroup();
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
    public void onInsetsChanged(Rect insets)
    {
        this.insets = insets;
    }

    public Rect getInsets()
    {
        return insets;
    }
}
