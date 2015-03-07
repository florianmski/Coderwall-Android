package com.florianmski.coderwall.framework.utils;

import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.florianmski.coderwall.R;
import com.florianmski.coderwall.framework.ui.fragments.RxFragment;

public class SearchUtils implements SearchView.OnQueryTextListener
{
    private final static String BUNDLE_QUERY = "query";

    private RxFragment f;
    private SearchView searchView;
    private MenuItem searchMenuItem;
    private String query;

    public SearchUtils(RxFragment f)
    {
        this.f = f;
    }

    public String getQuery()
    {
        return query;
    }

    public SearchView getView()
    {
        return searchView;
    }

    public void onCreate(Bundle savedInstanceState, String queryByDefault)
    {
        f.setHasOptionsMenu(true);

        if(savedInstanceState != null)
            query = savedInstanceState.getString(BUNDLE_QUERY);
        else
            query = queryByDefault;
    }

    public void onCreateOptionsMenu(Menu menu, String queryHint, boolean shouldExpandNow)
    {
        // create the search view
        searchView = new SearchView(f.getActionBar().getThemedContext());
        searchView.setQueryHint(queryHint);
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(false);

        searchMenuItem = menu.add(Menu.NONE, R.id.action_bar_search, Menu.NONE, f.getString(android.R.string.search_go));
        searchMenuItem
                .setIcon(R.drawable.ic_search_white_24dp)
                .setActionView(searchView)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

        searchView.setOnSearchClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                searchView.setQuery(query, false);
            }
        });

        if(shouldExpandNow)
            searchMenuItem.expandActionView();
        else
            searchView.clearFocus();
    }

    public void onSaveInstanceState(Bundle outState)
    {
        outState.putString(BUNDLE_QUERY, query);
    }

    public void onDestroy()
    {
        f = null;
    }

    @Override
    public boolean onQueryTextSubmit(String newText)
    {
        query = newText;
        f.refresh(true);
        searchMenuItem.collapseActionView();

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
        return true;
    }
}
