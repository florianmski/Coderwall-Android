package com.florianmski.coderwall.ui.widgets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.florianmski.coderwall.framework.ui.widgets.NotifyingScrollView;

public class HeaderScrollView extends NotifyingScrollView
{
    public HeaderScrollView(Context context)
    {
        super(context);
    }

    public HeaderScrollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public HeaderScrollView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event)
    {
        // if we are touching the paddingTop zone of the scrollView, do not handle touch events
        int viewTop = getPaddingTop() - getScrollY();
        if(event.getAction() == MotionEvent.ACTION_DOWN && event.getY() < viewTop)
            return false;

        return super.onTouchEvent(event);
    }
}
