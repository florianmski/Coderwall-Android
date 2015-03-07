package com.florianmski.coderwall.framework.utils;

import android.widget.TextView;

public class TextUtils
{
    public static void setTextAndVisibility(TextView tv, CharSequence text, int visibilityIfEmptyText)
    {
        if(android.text.TextUtils.isEmpty(text))
            tv.setVisibility(visibilityIfEmptyText);
        else
            tv.setText(text);
    }
}
