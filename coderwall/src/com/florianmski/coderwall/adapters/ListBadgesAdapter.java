package com.florianmski.coderwall.adapters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import net.caseydunham.coderwall.data.Badge;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.florianmski.coderwall.R;

public class ListBadgesAdapter extends RootAdapter<Badge>
{
	private SimpleDateFormat sdfParse;
	private java.text.DateFormat sdfFormat;

	public ListBadgesAdapter(Context context, List<Badge> items) 
	{
		super(context, items);

		sdfParse = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		sdfFormat = DateFormat.getDateFormat(context);
	}

	@SuppressWarnings("deprecation")
	@Override
	public View doGetView(int position, View convertView, ViewGroup parent) 
	{
		final ViewHolder holder;

		if (convertView == null) 
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.list_item_badge, null);
			holder = new ViewHolder();
			holder.ivBadge = (ImageView)convertView.findViewById(R.id.imageViewBadge);
			holder.tvName = (TextView)convertView.findViewById(R.id.textViewName);
			holder.tvDescription = (TextView)convertView.findViewById(R.id.textViewDescription);
			holder.tvCreated = (TextView)convertView.findViewById(R.id.textViewCreated);

			convertView.setTag(holder);
		} 
		else
			holder = (ViewHolder) convertView.getTag();

		Badge b = getItem(position);

		final AQuery aq = listAq.recycle(convertView);
		BitmapAjaxCallback cb = new BitmapAjaxCallback()
		{
			@Override
			public void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status)
			{     
				aq.id(iv).image(bm).animate(android.R.anim.fade_in);
			}

		}.url(b.getBadge()).fileCache(true).memCache(true);

		if(aq.shouldDelay(convertView, parent, b.getBadge(), 0))
			aq.id(holder.ivBadge).image(0);
		else
			aq.id(holder.ivBadge).image(cb);

		holder.tvName.setText(b.getName());
		holder.tvDescription.setText(b.getDescription());
		try 
		{
			holder.tvCreated.setText(sdfFormat.format(sdfParse.parse(b.getCreated())));
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
		}

		return convertView;
	}

	private static class ViewHolder 
	{
		private ImageView ivBadge;
		private TextView tvName;
		private TextView tvDescription;
		private TextView tvCreated;
	}

}
