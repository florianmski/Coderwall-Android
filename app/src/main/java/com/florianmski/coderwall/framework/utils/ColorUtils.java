package com.florianmski.coderwall.framework.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public class ColorUtils
{
    public static int fromAttribute(Context context, int attr)
    {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(attr, typedValue, true);
        return typedValue.data;
    }
}
