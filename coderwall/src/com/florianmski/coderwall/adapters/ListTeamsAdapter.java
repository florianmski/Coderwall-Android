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
import com.florianmski.coderwall.models.Team;
import com.florianmski.coderwall.ui.widget.TeamRankDrawable;

public class ListTeamsAdapter extends RootAdapter<Team>
{
	public ListTeamsAdapter(Context context, List<Team> items) 
	{
		super(context, items);
	}

	@SuppressWarnings("deprecation")
	@Override
	public View doGetView(int position, View convertView, ViewGroup parent) 
	{
		final ViewHolder holder;

		if (convertView == null) 
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.list_item_leaderboard, null);
			holder = new ViewHolder();
			holder.ivRank = (ImageView)convertView.findViewById(R.id.imageViewRank);
			holder.ivTeam = (ImageView)convertView.findViewById(R.id.imageViewTeam);
			holder.tvTeam = (TextView)convertView.findViewById(R.id.textViewTeam);
			holder.tvScore = (TextView)convertView.findViewById(R.id.textViewScore);

			convertView.setTag(holder);
		} 
		else
			holder = (ViewHolder) convertView.getTag();

		Team t = getItem(position);

		final AQuery aq = listAq.recycle(convertView);
		BitmapAjaxCallback cb = new BitmapAjaxCallback()
		{
			@Override
			public void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status)
			{     
				aq.id(iv).image(bm).animate(android.R.anim.fade_in);
			}

		}.url(t.getAvatar()).fileCache(false).memCache(true);

		if(aq.shouldDelay(convertView, parent, t.getAvatar(), 0))
			aq.id(holder.ivTeam).image(0);
		else
			aq.id(holder.ivTeam).image(cb);
		
		//TODO optimize
		holder.ivRank.setImageDrawable(new TeamRankDrawable(t.getRank(), context.getResources().getColor(R.color.coderwall_blue)));
		holder.tvTeam.setText(t.getName());
		holder.tvScore.setText(t.getSize() + ""); //TODO

		return convertView;
	}

	private static class ViewHolder 
	{
		private ImageView ivRank;
		private ImageView ivTeam;
		private TextView tvTeam;
		private TextView tvScore;
	}

}
