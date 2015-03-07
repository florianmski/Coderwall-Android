package com.florianmski.coderwall.framework.utils;

import android.content.Context;
import android.content.res.TypedArray;

public class ToolbarUtils
{
    public static int getHeight(Context context)
    {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(new int[]{android.support.v7.appcompat.R.attr.actionBarSize});
        int height = styledAttributes.getDimensionPixelSize(0, 0);
        styledAttributes.recycle();
        return height;
    }
}
