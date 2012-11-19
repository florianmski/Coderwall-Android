package com.florianmski.coderwall.ui.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;

public class TeamRankDrawable extends Drawable
{
//	private PorterDuffXfermode duffXMode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
	
    private Paint circlePaint;
    private Paint rankPaint;
    
    private int size = 100;
    private int position;

    public TeamRankDrawable(int position, int color)
    {
    	this.position = position;
    	
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(color);
        circlePaint.setStyle(Style.FILL);
        
        rankPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rankPaint.setColor(Color.WHITE);
        rankPaint.setTextAlign(Align.CENTER);
        rankPaint.setTextSize(size/2);
    }

    @Override
    public void draw(Canvas canvas)
    {
        canvas.drawCircle(size/2, size/2, size/2, circlePaint);
//        rankPaint.setXfermode(duffXMode);
        canvas.drawText(String.valueOf(position), size/2, size/2, rankPaint);
//        rankPaint.setXfermode(null);
//        canvas.drawText(String.valueOf(position), size/2, size/2, rankPaint);
    }

    @Override
    public void setAlpha(int alpha) {}

    @Override
    public void setColorFilter(ColorFilter cf) {}

    @Override
    public int getOpacity() 
    {
        return PixelFormat.TRANSLUCENT;
    }
    
    @Override
    public int getIntrinsicWidth() 
    {
        return size;
    }

    @Override
    public int getIntrinsicHeight() 
    {
    	return size;
    }
}

