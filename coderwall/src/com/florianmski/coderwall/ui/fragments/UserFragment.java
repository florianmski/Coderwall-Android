package com.florianmski.coderwall.ui.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.florianmski.coderwall.AsyncLoader;
import com.florianmski.coderwall.Constants;
import com.florianmski.coderwall.R;
import com.florianmski.coderwall.adapters.PagerUserAdapter;
import com.florianmski.coderwall.api.service.CoderwallApiProvider;
import com.florianmski.coderwall.models.User;
import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitlePageIndicator.IndicatorStyle;

public class UserFragment extends BaseFragment implements OnPageChangeListener, LoaderCallbacks<User>
{
	private TitlePageIndicator pageIndicator;
	private ViewPager viewPager;

	private PagerUserAdapter pagerAdapter;

	private MenuItem refreshItem;

	private User u;
	private String username;

	public static UserFragment newInstance(Bundle args)
	{
		UserFragment f = new UserFragment();
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

		if(savedInstanceState != null)
			u = (User) savedInstanceState.get(Constants.BUNDLE_USER);

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getSherlockActivity());
		username = prefs.getString(Constants.PREF_USERNAME, null);

		if(username == null)
			createAlertDialog();
		else
		{
			String username = u == null ? this.username : u.getUsername();
			//			CoderwallApiProvider.getClient().user(new UserAPIDelegate(LoadPolicy.ENABLED), username);
			getLoaderManager().initLoader(0, getArguments(), this);
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View v = inflater.inflate(R.layout.fragment_user, null);

		viewPager = (ViewPager) v.findViewById(R.id.paged_view);
		pageIndicator = (TitlePageIndicator) v.findViewById(R.id.page_indicator_title);

		return v;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		super.onCreateOptionsMenu(menu, inflater);

		menu.add(Menu.NONE, R.id.action_bar_invite, 0, "Search")
		.setIcon(R.drawable.ic_menu_invite)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		MenuItem refreshItem = menu.add(Menu.NONE, R.id.action_bar_refresh, 1, "Refresh");
		refreshItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		refreshItem.setIcon(R.drawable.ic_menu_refresh);

		this.refreshItem = refreshItem;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch(item.getItemId()) 
		{	
		case R.id.action_bar_refresh:
			if(u != null)
				executeTask(u.getUsername(), true);
			break;
		case R.id.action_bar_invite:
			createAlertDialog();
			break;
		case R.id.action_bar_about:
			new AlertDialog.Builder(getSherlockActivity())
			.setTitle("About")
			.setMessage("When life gives you lemons, don't make lemonade. " +
					"Make life take the lemons back! Get mad! I don't want your damn lemons! " +
					"What am I supposed to do with these?! Demand to see life's manager! " +
					"Make life rue the day it thought it could give Cave Johnson lemons! " +
					"Do you know who I am? I'm the man who's gonna burn your house down! " +
					"With the lemons! I'm gonna get my engineers to invent a combustible lemon that burns your house down!")
					.setPositiveButton("Dev profile", new DialogInterface.OnClickListener() 
					{
						public void onClick(DialogInterface dialog, int whichButton) 
						{
							executeTask("florianmski", true);
						}
					})
					.setNeutralButton("Close", new DialogInterface.OnClickListener() 
					{
						public void onClick(DialogInterface dialog, int whichButton) {}
					}).show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void executeTask(String username, boolean refresh)
	{		
		//TODO
		//		LoadPolicy lp;
		//		
		//		if(refresh)
		//		{
		//			setRefresh(true);
		//			if(u != null && !u.getUsername().equals(username))
		//				lp = LoadPolicy.NETWORK_ENABLED;
		//			else
		//				lp = LoadPolicy.NEVER;
		//		}
		//		else
		//			lp = LoadPolicy.ENABLED;
		//		
		//		CoderwallApiProvider.getClient().user(new UserAPIDelegate(lp), username);
	}

	public void setRefresh(boolean on)
	{
		if(refreshItem == null)
			return;

		if(on)
		{
			// based on http://stackoverflow.com/questions/9731602/animated-icon-for-actionitem/9732235#9732235
			LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			ImageView iv = (ImageView) inflater.inflate(R.layout.view_refresh, null);

			Animation rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.refresh);
			rotation.setRepeatCount(Animation.INFINITE);
			iv.startAnimation(rotation);

			refreshItem.setActionView(iv);
		}
		else
		{
			if(refreshItem.getActionView() != null)
				refreshItem.getActionView().clearAnimation();
			refreshItem.setActionView(null);
		}
	}

	public void createAlertDialog()
	{
		final EditText edtUsername = new EditText(getSherlockActivity());
		edtUsername.setSingleLine();
		edtUsername.setImeOptions(EditorInfo.IME_ACTION_DONE);

		final AlertDialog alert = new AlertDialog.Builder(getSherlockActivity())
		.setTitle("Coderwall")
		.setMessage("Please enter " + (username == null ? "your" : "a") + " username")
		.setView(edtUsername)
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int whichButton) 
			{
				executeTask(edtUsername.getText().toString().trim(), true);
			}
		}).show();

		edtUsername.setOnEditorActionListener(new OnEditorActionListener() 
		{
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) 
			{
				alert.getButton(AlertDialog.BUTTON_POSITIVE).performClick();
				return false;
			}
		});
	}

	@Override
	public void onPageScrollStateChanged(int state) {}

	@Override
	public void onPageScrolled(int position, float positionOffset,	int positionOffsetPixels) {}

	@Override
	public void onPageSelected(int position) {}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);

		outState.putSerializable(Constants.BUNDLE_USER, u);
	}

	@Override
	public Loader<User> onCreateLoader(int arg0, Bundle arg1) 
	{
		return new ProfileLoader(getActivity(), username);
	}

	@Override
	public void onLoadFinished(Loader<User> arg0, User user) 
	{
		if(user == null)
			UserFragment.this.setRefresh(false);
		else
		{
			UserFragment.this.setRefresh(false);

			if(UserFragment.this.username == null)
			{
				UserFragment.this.username = u.getUsername();
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getSherlockActivity());
				prefs.edit().putString(Constants.PREF_USERNAME, u.getUsername()).commit();
			}

			UserFragment.this.u = u;

			if(pagerAdapter == null || viewPager.getAdapter() == null)
			{
				viewPager.setAdapter(pagerAdapter = new PagerUserAdapter(getFragmentManager(), u));
				pageIndicator.setViewPager(viewPager);
				pageIndicator.setOnPageChangeListener(UserFragment.this);
				pageIndicator.setBackgroundColor(getResources().getColor(R.color.coderwall_blue));
				pageIndicator.setFooterColor(Color.WHITE);
				pageIndicator.setFooterIndicatorStyle(IndicatorStyle.Triangle);
				pageIndicator.setFooterLineHeight(0);
			}
			else
				pagerAdapter.refreshUser(u);
		}
	}

	@Override
	public void onLoaderReset(Loader<User> arg0) {}

	public static class ProfileLoader extends AsyncLoader<User>
	{
		private String username;

		public ProfileLoader(Context context, String username) 
		{
			super(context);

			this.username = username;
		}

		@Override
		public User loadInBackground() 
		{
			User user;
			try
			{
				user = CoderwallApiProvider.getClient().user(username);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				//TODO
//				Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
				return null;
			}

			return user;
		}

	}
}
