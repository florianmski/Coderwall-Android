package com.florianmski.coderwall.framework.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.florianmski.coderwall.R;

public abstract class BaseActivity extends ActionBarActivity
{
    public final static String BUNDLE_TITLE = "Title";
    public final static String BUNDLE_SUBTITLE = "Subtitle";

    private Toolbar toolbar;
    protected boolean actionBarShown = true;

    protected abstract View getContentView();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());

        // setup toolbar after layout
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null)
            setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        String title = getIntent().getStringExtra(BUNDLE_TITLE);
        String subtitle = getIntent().getStringExtra(BUNDLE_SUBTITLE);

        if(!TextUtils.isEmpty(title))
            getSupportActionBar().setTitle(title);
        if(!TextUtils.isEmpty(subtitle))
            getSupportActionBar().setSubtitle(subtitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            // This is temporary, this is a naive implementation of the up navigation.
            // It's in fact the back navigation.
            // Did this so I don't have the up button not working at all
            // I need to figure out how to handle complex situation such as activities with multiple parents
            // or parents which need data.
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID)
    {
        super.setContentView(layoutResID);
    }

    protected static Intent makeIntent(Activity a, Class<?> activityToLaunch, Bundle args)
    {
        Intent i = new Intent(a, activityToLaunch);
        if(args != null)
            i.putExtras(args);
        return i;
    }

    public static void launchActivity(Activity a, Class<?> activityToLaunch)
    {
        launchActivity(a, activityToLaunch, null);
    }

    public static void launchActivity(Activity a, Class<?> activityToLaunch, Bundle args)
    {
        a.startActivity(makeIntent(a, activityToLaunch, args));
    }

    public Toolbar getToolbar()
    {
        return toolbar;
    }

    // sometime the toolbar is not alone (if we have tabs or other stuff under)
    // in this case we might need the whole group and not just the toolbar
    public ViewGroup getToolbarGroup()
    {
        return getToolbar();
    }

    public void showToolbar(boolean show)
    {
        if(show == actionBarShown)
            return;

        actionBarShown = show;
        getToolbarGroup().animate()
                .translationY(show ? 0 : -getToolbarGroup().getBottom())
                .alpha(show ? 1 : 0)
                .setInterpolator(new DecelerateInterpolator());
    }
}
