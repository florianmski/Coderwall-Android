package com.florianmski.coderwall.framework.ui.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class ScrollFragment<E, V extends ViewGroup> extends RxFragment<E, V>
{
    protected List<GenericScrollListener<V>> scrollListeners = new ArrayList<>();
    protected GenericScrollListener<V> scrollListener = new GenericScrollListener<V>()
    {
        @Override
        public void onScrollChanged(V who, int dx, int dy)
        {
            for(GenericScrollListener<V> listener : scrollListeners)
                listener.onScrollChanged(who, dx, dy);
        }
    };

    public ScrollFragment() {}

    protected abstract void plugScrollListenerToView(V view, GenericScrollListener<V> scrollListener);

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        plugScrollListenerToView(view, scrollListener);
        addScrollListener(new GenericScrollListener<V>()
        {
            @Override
            public void onScrollChanged(V who, int dx, int dy)
            {
                View v = who.getChildAt(0);
                if(v == null)
                    return;

                if(dy > 0 && v.getTop() < 0)
                    getBaseActivity().showToolbar(false);
                else if(dy < 0)
                    getBaseActivity().showToolbar(true);
            }
        });
    }

    @Override
    public void onInsetsChanged(Rect insets)
    {
        super.onInsetsChanged(insets);

        view.setClipToPadding(false);
    }

    public void addScrollListener(GenericScrollListener<V> listener)
    {
        scrollListeners.add(listener);
    }

    public void removeScrollListener(GenericScrollListener<V> listener)
    {
        scrollListeners.remove(listener);
    }

    public interface GenericScrollListener<V>
    {
        public void onScrollChanged(V who, int dx, int dy);
    }
}
