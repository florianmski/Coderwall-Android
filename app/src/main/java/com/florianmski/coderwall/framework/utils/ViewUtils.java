package com.florianmski.coderwall.framework.utils;

import android.view.View;

public class ViewUtils
{
    public static void setPaddingLeft(View v, int paddingLeft)
    {
        v.setPadding(paddingLeft, v.getPaddingTop(), v.getPaddingRight(), v.getPaddingBottom());
    }

    public static void setPaddingTop(View v, int paddingTop)
    {
        v.setPadding(v.getPaddingLeft(), paddingTop, v.getPaddingRight(), v.getPaddingBottom());
    }

    public static void setPaddingRight(View v, int paddingRight)
    {
        v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), paddingRight, v.getPaddingBottom());
    }

    public static void setPaddingBottom(View v, int paddingBottom)
    {
        v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), paddingBottom);
    }
}
