package com.florianmski.coderwall.framework.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.florianmski.coderwall.R;
import com.florianmski.coderwall.framework.adapters.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class RecyclerFragment<E, A extends RecyclerAdapter<E, ?>> extends ScrollFragment<List<E>, RecyclerView>
{
    protected A recyclerAdapter;

    protected abstract RecyclerView.LayoutManager getLayoutManager();
    protected abstract A createAdapter(List<E> items);

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        view.setHasFixedSize(hasFixedSize());
        view.setLayoutManager(getLayoutManager());
        view.setAdapter(getAdapter());
    }

    @Override
    protected void plugScrollListenerToView(RecyclerView view, final GenericScrollListener<RecyclerView> scrollListener)
    {
        view.setOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {}

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                scrollListener.onScrollChanged(recyclerView, dy, dy);
            }
        });
    }

    @Override
    protected int getViewLayoutId()
    {
        return R.layout.view_recycler;
    }

    @Override
    protected void refreshView(List<E> data)
    {
        getAdapter().refresh(data);
    }

    protected A getAdapter()
    {
        if(recyclerAdapter == null)
            recyclerAdapter = createAdapter(new ArrayList<E>());

        return recyclerAdapter;
    }

    @Override
    protected boolean isEmpty(List<E> data)
    {
        return data == null || data.isEmpty();
    }

    protected boolean hasFixedSize()
    {
        return true;
    }
}
