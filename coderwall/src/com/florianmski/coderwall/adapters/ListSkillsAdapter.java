package com.florianmski.coderwall.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.florianmski.coderwall.R;

public class ListSkillsAdapter extends RootAdapter<String>
{
	public ListSkillsAdapter(Context context, List<String> items) 
	{
		super(context, items);
	}

	@Override
	public View doGetView(int position, View convertView, ViewGroup parent) 
	{
		final ViewHolder holder;

		if (convertView == null) 
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.list_item_skill, null);
			holder = new ViewHolder();
			holder.tvSkill = (TextView)convertView.findViewById(R.id.textViewSkill);

			convertView.setTag(holder);
		} 
		else
			holder = (ViewHolder) convertView.getTag();

		String s = getItem(position);

		holder.tvSkill.setText(s);

		return convertView;
	}

	private static class ViewHolder 
	{
		private TextView tvSkill;
	}

}
