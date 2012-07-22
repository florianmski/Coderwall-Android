package com.florianmski.coderwall;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;

public class Utils 
{
	public static final boolean isOnline(Context context) 
	{
		if(context == null)
			return false;

		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting())
			return true;
		return false;
	}

	public static String getExtFolderPath()
	{
		return Environment.getExternalStorageDirectory() + "/Android/data/com.florianmski.coderwall/";
	}

	public static String getAPICacheFolderPath()
	{
		return getExtFolderPath() + "/api/";
	}

	public static String convertStreamToString(InputStream is) 
	{
		try 
		{
			return new java.util.Scanner(is).useDelimiter("\\A").next();
		} 
		catch (java.util.NoSuchElementException e) 
		{
			return "";
		}
	}

	public static boolean isStringEmpty(String s)
	{
		return s == null || s.trim().equals("");
	}

	public static Bitmap roundBitmap(Bitmap bm, int radius)
	{
		if(bm == null)
			return null;

		Bitmap output = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bm.getWidth(), bm.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, radius, radius, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bm, rect, rect, paint);

		return output;
	}

	public static void openBrowser(Context context, String url)
	{
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		context.startActivity(browserIntent);
	}

	public static String getThumbnail(String url, int size)
	{
		return url + "?s=" + size;
	}

	public static String readInputStream(InputStream in) throws IOException 
	{
		StringBuffer stream = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;)
			stream.append(new String(b, 0, n));

		return stream.toString();
	}
}
