package com.florianmski.coderwall.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.florianmski.coderwall.R;
import com.florianmski.coderwall.data.models.Badge;
import com.florianmski.coderwall.framework.adapters.RecyclerAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerBadgeAdapter extends RecyclerAdapter<Badge, RecyclerBadgeAdapter.BadgeViewHolder>
{
    public RecyclerBadgeAdapter(Context context, List<Badge> data, OnItemClickListener listener)
    {
        super(context, data, listener);
    }

    @Override
    public BadgeViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_badge, parent, false);
        return new BadgeViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(BadgeViewHolder holder, int position)
    {
        Badge badge = getItem2(position);

        Picasso.with(context).load(badge.badge).into(holder.ivBadge);
    }

    protected static class BadgeViewHolder extends RecyclerAdapter.ViewHolder
    {
        public ImageView ivBadge;

        public BadgeViewHolder(View itemView, final OnItemClickListener listener)
        {
            super(itemView, listener);

            ivBadge = (ImageView)itemView.findViewById(R.id.imageViewBadge);
        }
    }

}