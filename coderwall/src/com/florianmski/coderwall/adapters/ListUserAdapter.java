package com.florianmski.coderwall.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.florianmski.coderwall.R;
import com.florianmski.coderwall.Utils;
import com.florianmski.coderwall.models.User;

public class ListUserAdapter extends RootAdapter<User>
{
	public ListUserAdapter(Context context, List<User> items) 
	{
		super(context, items);
	}

	@Override
	public View doGetView(int position, View convertView, ViewGroup parent) 
	{
		final ViewHolder holder;

		if (convertView == null) 
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.list_item_user, null);
			holder = new ViewHolder();
			holder.ivThumbnail = (ImageView)convertView.findViewById(R.id.imageViewThumbnail);
			holder.tvUsername = (TextView)convertView.findViewById(R.id.textViewUsername);

			convertView.setTag(holder);
		} 
		else
			holder = (ViewHolder) convertView.getTag();

		User u = getItem(position);

		final AQuery aq = listAq.recycle(convertView);
		BitmapAjaxCallback cb = new BitmapAjaxCallback()
		{
			@Override
			public void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status)
			{     
				aq.id(iv).image(Utils.roundBitmap(bm, 10)).animate(android.R.anim.fade_in);
			}

		}.url(u.getThumbnail()).animation(android.R.anim.fade_in).fileCache(true).memCache(true);

		aq.id(holder.ivThumbnail).image(cb);

		holder.tvUsername.setText(u.getUsername());

		return convertView;
	}

	private static class ViewHolder 
	{
		private ImageView ivThumbnail;
		private TextView tvUsername;
	}


}
