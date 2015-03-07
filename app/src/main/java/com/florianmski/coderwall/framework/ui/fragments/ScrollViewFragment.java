package com.florianmski.coderwall.framework.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.florianmski.coderwall.R;
import com.florianmski.coderwall.framework.ui.widgets.NotifyingScrollView;

public abstract class ScrollViewFragment<E> extends ScrollFragment<E, NotifyingScrollView>
{
    protected abstract int getContentLayoutId();

    @Override
    protected void plugScrollListenerToView(NotifyingScrollView view, final GenericScrollListener<NotifyingScrollView> scrollListener)
    {
        view.setOnScrollChangedListener(new NotifyingScrollView.OnScrollChangedListener()
        {
            @Override
            public void onScrollChanged(NotifyingScrollView who, int l, int t, int oldl, int oldt)
            {
                scrollListener.onScrollChanged(who, l - oldl, t - oldt);
            }
        });
    }

    @Override
    protected int getViewLayoutId()
    {
        return R.layout.view_scrollview;
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState)
    {
        super.onViewCreated(v, savedInstanceState);

        LayoutInflater.from(getActivity()).inflate(getContentLayoutId(), view, true);
    }

    @Override
    protected boolean isEmpty(E data)
    {
        return data == null;
    }
}
