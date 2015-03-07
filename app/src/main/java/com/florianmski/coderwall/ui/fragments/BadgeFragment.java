package com.florianmski.coderwall.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.florianmski.coderwall.R;
import com.florianmski.coderwall.data.models.Badge;
import com.squareup.picasso.Picasso;

public class BadgeFragment extends DialogFragment
{
    private final static String BUNDLE_BADGE = "badge";
    private final static String BUNDLE_NAME = "name";
    private final static String BUNDLE_DESCRIPTION = "description";

    private ImageView ivBadge;
    private TextView tvName;
    private TextView tvDescription;

    public static BadgeFragment newInstance(Badge badge)
    {
        Bundle args = new Bundle();
        args.putString(BUNDLE_BADGE, badge.badge);
        args.putString(BUNDLE_NAME, badge.name);
        args.putString(BUNDLE_DESCRIPTION, badge.description);

        BadgeFragment f = new BadgeFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        String badge = getArguments().getString(BUNDLE_BADGE);
        String name = getArguments().getString(BUNDLE_NAME);
        String description = getArguments().getString(BUNDLE_DESCRIPTION);

        Picasso.with(getActivity()).load(badge).into(ivBadge);

        tvName.setText(name);
        tvDescription.setText(description);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.fragment_badge, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        ivBadge = (ImageView)view.findViewById(R.id.imageViewBadge);
        tvName = (TextView)view.findViewById(R.id.textViewName);
        tvDescription = (TextView)view.findViewById(R.id.textViewDescription);
    }
}