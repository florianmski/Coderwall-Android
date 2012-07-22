package com.florianmski.coderwall.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.florianmski.coderwall.R;

public class ListAccomplishmentsAdapter extends RootAdapter<String>
{
	public ListAccomplishmentsAdapter(Context context, List<String> items) 
	{
		super(context, items);
	}

	@Override
	public View doGetView(int position, View convertView, ViewGroup parent) 
	{
		final ViewHolder holder;

		if (convertView == null) 
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.list_item_accomplishment, null);
			holder = new ViewHolder();
			holder.tvAccomplishment = (TextView)convertView.findViewById(R.id.textViewAccomplishment);

			convertView.setTag(holder);
		} 
		else
			holder = (ViewHolder) convertView.getTag();

		String s = getItem(position);

		holder.tvAccomplishment.setText(s);

		return convertView;
	}

	private static class ViewHolder 
	{
		private TextView tvAccomplishment;
	}

}
