package com.florianmski.coderwall.ui.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.florianmski.coderwall.Constants;
import com.florianmski.coderwall.R;
import com.florianmski.coderwall.Utils;
import com.florianmski.coderwall.models.CWUser;

public class ProfileFragment extends BaseFragment
{
	private RelativeLayout rlBusinessCard;
	private ImageView ivProfile;
	private ImageView ivSpecial;
	private ImageView ivGithub;
	private ImageView ivTwitter;
	private TextView tvName;
	private TextView tvLocation;
	private TextView tvTitle;
	private TextView tvAbout;
	private TextView tvEndorsements;

	public static ProfileFragment newInstance(Bundle args)
	{
		ProfileFragment f = new ProfileFragment();
		f.setArguments(args);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);

		final CWUser u = (CWUser) getArguments().get(Constants.BUNDLE_USER);
		final String thumbnail = Utils.getThumbnail(u.getThumbnail(), 200);

		final AQuery aq = new AQuery(getSherlockActivity());

		BitmapAjaxCallback cbProfile = new BitmapAjaxCallback()
		{
			@Override
			public void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status)
			{     
				aq.id(iv).image(Utils.roundBitmap(bm, 10)).animate(android.R.anim.fade_in);
			}

		}.url(thumbnail).fileCache(true).memCache(false);

		BitmapAjaxCallback cbSpecial = new BitmapAjaxCallback()
		{
			@Override
			public void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status)
			{     
				aq.id(iv).image(bm).animate(android.R.anim.fade_in);
			}

		}.url(u.getSpecialImage()).fileCache(true).memCache(false);

		aq.id(ivProfile).image(cbProfile);
		aq.id(ivSpecial).image(cbSpecial);

		tvName.setText(Utils.isStringEmpty(u.getName()) ? u.getUsername() : u.getName());

		if(!Utils.isStringEmpty(u.getLocation()))
			tvLocation.setText(u.getLocation());

		if(!Utils.isStringEmpty(u.getTitle()) || !Utils.isStringEmpty(u.getTitle()))
		{
			if(!Utils.isStringEmpty(u.getTitle()))
				tvTitle.setText(u.getTitle() + (Utils.isStringEmpty(u.getCompany()) ? "" : " at " + u.getCompany()));
			else
				tvTitle.setText("work at " + u.getCompany());
		}

		tvAbout.setText(Utils.isStringEmpty(u.getAbout()) ? "404 not found" : u.getAbout());
		tvEndorsements.setText(u.getEndorsements()+"");

		rlBusinessCard.startAnimation(AnimationUtils.makeInAnimation(getSherlockActivity(), false));
		rlBusinessCard.setVisibility(View.VISIBLE);

		if(u.getAccounts() != null)
		{
			if(u.getAccounts().getGithub() != null)
				ivGithub.setVisibility(View.VISIBLE);

			if(u.getAccounts().getTwitter() != null)
				ivTwitter.setVisibility(View.VISIBLE);

			ivGithub.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					Utils.openBrowser(getSherlockActivity(), "http://www.github.com/" + u.getAccounts().getGithub());
				}
			});

			ivTwitter.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					Utils.openBrowser(getSherlockActivity(), "http://www.twitter.com/" + u.getAccounts().getTwitter());
				}
			});
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View v = inflater.inflate(R.layout.fragment_profile, null);

		rlBusinessCard = (RelativeLayout)v.findViewById(R.id.relativeLayoutBusinessCard);
		rlBusinessCard.setVisibility(View.INVISIBLE);

		ivProfile = (ImageView)v.findViewById(R.id.imageViewProfile);
		ivSpecial = (ImageView)v.findViewById(R.id.imageViewSpecial);
		ivGithub = (ImageView)v.findViewById(R.id.imageViewGithub);
		ivTwitter = (ImageView)v.findViewById(R.id.imageViewTwitter);

		tvName = (TextView)v.findViewById(R.id.textViewName);
		tvLocation = (TextView)v.findViewById(R.id.textViewLocation);
		tvTitle = (TextView)v.findViewById(R.id.textViewTitle);
		tvAbout = (TextView)v.findViewById(R.id.textViewAboutContent);
		tvEndorsements = (TextView)v.findViewById(R.id.textViewEndorsements);

		return v;
	}
}
