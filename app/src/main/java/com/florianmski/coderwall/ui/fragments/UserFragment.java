package com.florianmski.coderwall.ui.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.florianmski.coderwall.R;
import com.florianmski.coderwall.adapters.RecyclerBadgeAdapter;
import com.florianmski.coderwall.data.CoderwallClient;
import com.florianmski.coderwall.data.CoderwallPrefs;
import com.florianmski.coderwall.data.models.Badge;
import com.florianmski.coderwall.data.models.User;
import com.florianmski.coderwall.framework.adapters.RecyclerAdapter;
import com.florianmski.coderwall.framework.ui.fragments.ScrollViewFragment;
import com.florianmski.coderwall.framework.ui.widgets.BezelImageView;
import com.florianmski.coderwall.framework.ui.widgets.NotifyingScrollView;
import com.florianmski.coderwall.framework.utils.SearchUtils;
import com.florianmski.coderwall.framework.utils.TextUtils;
import com.florianmski.coderwall.framework.utils.ViewUtils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class UserFragment extends ScrollViewFragment<UserFragment.UserLatLng>
{
    private final static LatLng MOUNTAIN_VIEW = new LatLng(37.3861111, -122.0827778);

    private SearchUtils search = new SearchUtils(this);

    private View header;
    private LinearLayout llUserInfos;
    private BezelImageView ivAvatar;
    private BezelImageView ivTwitter;
    private BezelImageView ivGithub;
    private TextView tvName;
    private TextView tvTitle;
    private MapView mapView;

    private TextView tvLocation;
    private TextView tvWork;
    private TextView tvEndorsements;

    private TextView tvAbout;

    private RecyclerView recyclerView;

    private GoogleMap map;

    public static UserFragment newInstance()
    {
        return new UserFragment();
    }

    @Override
    protected int getContentLayoutId()
    {
        return R.layout.fragment_user;
    }

    @Override
    protected int getViewLayoutId()
    {
        return R.layout.view_headerscrollview;
    }

    @Override
    protected Observable<UserLatLng> createObservable()
    {
        return CoderwallClient.INSTANCE.get()
                .user(search.getQuery())
                .flatMap(new Func1<User, Observable<UserLatLng>>()
                {
                    @Override
                    public Observable<UserLatLng> call(final User user)
                    {
                        return createGeocoderObservable(user)
                                .map(new Func1<LatLng, UserLatLng>()
                                {
                                    @Override
                                    public UserLatLng call(LatLng latLng)
                                    {
                                        return new UserLatLng(user, latLng);
                                    }
                                })
                                .onErrorResumeNext(Observable.just(new UserLatLng(user, MOUNTAIN_VIEW)));
                    }
                });
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        MapsInitializer.initialize(getActivity());
        search.onCreate(savedInstanceState, CoderwallPrefs.getInstance().getLastUsernameEntered());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        //        getBaseActivity().showToolbar(false);
//                        view.getChildAt(0).setBackgroundColor(Color.TRANSPARENT);
//                getToolbar().setBackgroundColor(Color.TRANSPARENT);

        map = mapView.getMap();
        map.getUiSettings().setAllGesturesEnabled(false);

        mapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int height = mapView.getHeight();
                // at most, 70% of the screen is covered by the map
                ViewUtils.setPaddingTop(view, (int) (height * 0.7));
                // the 30 last percent doesn't need to display the map
                ViewUtils.setPaddingBottom(mapView, (int) (height * 0.3));
                // don't want to display the map behind the toolbar
                ViewUtils.setPaddingTop(mapView, getToolbar().getHeight());

                //                view.scrollTo(0, (int) (mapView.getHeight() * 0.3));

                updateUserInfosLayoutPosition();
                updateMapPadding();
            }
        });

        // zoom in when we scroll up, zoom out when scrolling down
        addScrollListener(new GenericScrollListener<NotifyingScrollView>()
        {
            @Override
            public void onScrollChanged(NotifyingScrollView who, int dx, int dy)
            {
                updateUserInfosLayoutPosition();
                updateMapPadding();

                //                CameraUpdate cuScroll = CameraUpdateFactory.scrollBy(0, dy * 0.3f);
                //                map.moveCamera(cuScroll);
                CameraUpdate cuZoom = CameraUpdateFactory.zoomBy(dy * 0.001f, new Point(mapView.getWidth()/2, (int) llUserInfos.getY()));
                map.moveCamera(cuZoom);
            }
        });

        addScrollListener(new GenericScrollListener<NotifyingScrollView>()
        {
            @Override
            public void onScrollChanged(NotifyingScrollView who, int dx, int dy)
            {
                if(llUserInfos.getTag(R.id.animation_in_progress) == true)
                    return;

                int mapHeight = view.getPaddingTop() - view.getScrollY() - getToolbar().getHeight();
                boolean show = mapHeight >= llUserInfos.getHeight();

                // check that we are going in the right direction
                if(show && dy > 0 || !show && dy < 0)
                    return;

                float scale = show ? 1f : 0f;

                // hide header if we haven't enough space
                llUserInfos.animate()
                        .scaleX(scale).scaleY(scale)
                        .setListener(new AnimatorListenerAdapter()
                        {
                            @Override
                            public void onAnimationStart(Animator animation)
                            {
                                llUserInfos.setTag(R.id.animation_in_progress, true);
                            }

                            @Override
                            public void onAnimationEnd(Animator animation)
                            {
                                llUserInfos.setTag(R.id.animation_in_progress, false);
                            }
                        });
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    private void updateUserInfosLayoutPosition()
    {
        int viewTop = view.getPaddingTop() - view.getScrollY();
        int y = (viewTop - getToolbar().getHeight()) / 2;
        llUserInfos.setY(y);
    }

    private void updateMapPadding()
    {
        map.setPadding(
                mapView.getPaddingLeft(),
                mapView.getPaddingTop(),
                mapView.getPaddingRight(),
                view.getScrollY());
    }

    @Override
    protected View getLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup v = (ViewGroup) super.getLayout(inflater, container, savedInstanceState);
        header = inflater.inflate(R.layout.view_header, v, false);
        v.addView(header, 0);
        return v;
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState)
    {
        super.onViewCreated(v, savedInstanceState);

        llUserInfos = (LinearLayout) v.findViewById(R.id.linearLayoutUserInfos);
        ivAvatar = (BezelImageView) v.findViewById(R.id.imageViewAvatar);
        ivTwitter = (BezelImageView) v.findViewById(R.id.imageViewTwitter);
        ivGithub = (BezelImageView) v.findViewById(R.id.imageViewGithub);
        tvName = (TextView) v.findViewById(R.id.name);
        tvTitle = (TextView) v.findViewById(R.id.title);
        mapView = (MapView) v.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        tvLocation = (TextView) v.findViewById(R.id.location);
        tvWork = (TextView) v.findViewById(R.id.work);
        tvEndorsements = (TextView) v.findViewById(R.id.endorsements);

        tvAbout = (TextView) v.findViewById(R.id.about);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
    }

    @Override
    protected void refreshView(UserLatLng userLatLng)
    {
        final User user = userLatLng.user;

        String username = user.username;
        // if refresh was a success set username in prefs
        CoderwallPrefs.getInstance().putLastUsernameEntered(username);
        getToolbar().setSubtitle(username);

        // if we have a location, move camera
        map.moveCamera(CameraUpdateFactory.newLatLng(userLatLng.latLng));

        // before animating the view, we want them invisible
        ivAvatar.setVisibility(View.INVISIBLE);
        ivTwitter.setVisibility(View.INVISIBLE);
        ivGithub.setVisibility(View.INVISIBLE);
        tvName.setVisibility(View.INVISIBLE);
        tvTitle.setVisibility(View.INVISIBLE);

        // set content in the views
        Picasso.with(getActivity()).load(user.thumbnail).into(ivAvatar);
        tvName.setText(user.name);
        tvTitle.setText(user.title);

        TextUtils.setTextAndVisibility(tvLocation, user.location, View.INVISIBLE);
        TextUtils.setTextAndVisibility(tvWork, user.company, View.INVISIBLE);
        TextUtils.setTextAndVisibility(tvEndorsements, String.valueOf(user.endorsements), View.INVISIBLE);

        tvAbout.setText(user.about);

        recyclerView.setAdapter(new RecyclerBadgeAdapter(getActivity(), user.badges, new RecyclerAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View v, int position)
            {
                Badge badge = ((RecyclerBadgeAdapter)recyclerView.getAdapter()).getItem2(position);
                DialogFragment badgeFragment = BadgeFragment.newInstance(badge);
                badgeFragment.show(getChildFragmentManager(), null);
            }
        }));

        boolean twitter = user.accounts != null && user.accounts.twitter != null;
        boolean github = user.accounts != null && user.accounts.github != null;

        if(twitter)
            setAccountClickListener(ivTwitter, "https://www.twitter.com/%s", user.accounts.twitter);
        if(github)
            setAccountClickListener(ivGithub, "https://www.github.com/%s", user.accounts.github);

        // get the position of the avatar on screen
        int[] location = new int[2];
        ivAvatar.getLocationInWindow(location);
        int avatarX = location[0] + ivAvatar.getWidth() / 2;
        int avatarY = location[1] - getInsets().top + ivAvatar.getHeight() / 2;
        int finalRadius = Math.max(mapView.getWidth(), mapView.getHeight());

        // creating animators
        ObjectAnimator avatarAnimator = ObjectAnimator.ofPropertyValuesHolder(
                ivAvatar,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 0f, 1f),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f, 1f));
        avatarAnimator.setInterpolator(new BounceInterpolator());
        setViewVisibleWhenAnimationStart(avatarAnimator, ivAvatar);

        ObjectAnimator twitterAnimator = ObjectAnimator.ofPropertyValuesHolder(
                ivTwitter,
                PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0f, ivAvatar.getWidth()));
        twitterAnimator.setInterpolator(new AccelerateInterpolator());
        if(twitter)
            setViewVisibleWhenAnimationStart(twitterAnimator, ivTwitter);

        ObjectAnimator githubAnimator = ObjectAnimator.ofPropertyValuesHolder(
                ivGithub,
                PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0f, -ivAvatar.getWidth()));
        githubAnimator.setInterpolator(new AccelerateInterpolator());
        if(github)
            setViewVisibleWhenAnimationStart(githubAnimator, ivGithub);

        ObjectAnimator nameAnimator = ObjectAnimator.ofPropertyValuesHolder(
                tvName,
                PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 50f, 0f),
                PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f));
        setViewVisibleWhenAnimationStart(nameAnimator, tvName);
        ObjectAnimator titleAnimator = ObjectAnimator.ofPropertyValuesHolder(
                tvTitle,
                PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 50f, 0f),
                PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f));
        setViewVisibleWhenAnimationStart(titleAnimator, tvTitle);

        AnimatorSet titleSet = new AnimatorSet();
        titleSet.playTogether(nameAnimator, titleAnimator, twitterAnimator, githubAnimator);
        titleSet.setDuration(500);

        // fallback if we aren't on lollipop
        Animator mapViewAnimator;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            mapViewAnimator = ViewAnimationUtils.createCircularReveal(
                    mapView,
                    avatarX,
                    avatarY,
                    0,
                    finalRadius);
        }
        else
            mapViewAnimator = ObjectAnimator.ofFloat(mapView, View.ALPHA, 0f, 1f);

        mapViewAnimator.setDuration(700);
        setViewVisibleWhenAnimationStart(mapViewAnimator, mapView);

        // start animation
        AnimatorSet as = new AnimatorSet();
        as.setStartDelay(500);
        as.play(avatarAnimator).with(mapViewAnimator).before(titleSet);
        as.start();
    }

    private void setAccountClickListener(ImageView v, final String url, final String account)
    {
        v.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format(url, account)));
                startActivity(browserIntent);
            }
        });
    }

    private void setViewVisibleWhenAnimationStart(Animator animator, final View view)
    {
        animator.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {
                view.setVisibility(View.VISIBLE);
            }
        });
    }

    public Observable<LatLng> createGeocoderObservable(final User user)
    {
        return Observable.create(new Observable.OnSubscribe<LatLng>()
        {
            @Override
            public void call(Subscriber<? super LatLng> subscriber)
            {
                Geocoder geocoder = new Geocoder(getActivity());
                try
                {
                    Address address = geocoder.getFromLocationName(user.location, 1).get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    subscriber.onNext(latLng);
                    subscriber.onCompleted();
                } catch (IOException e)
                {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);

        search.onCreateOptionsMenu(menu, "Who are you looking for?", false);
    }

    @Override
    public void onInsetsChanged(Rect insets)
    {
        super.onInsetsChanged(insets);

        // handle the fact that we can see the view bg behind the nav bar
        View lastChild = view.getChildAt(view.getChildCount()-1);
        ViewUtils.setPaddingBottom(lastChild, insets.bottom);
        ViewUtils.setPaddingBottom(view, 0);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mapView.onDestroy();
        search.onDestroy();
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        mapView.onSaveInstanceState(outState);
        search.onSaveInstanceState(outState);
    }

    protected class UserLatLng
    {
        public LatLng latLng;
        public User user;

        public UserLatLng(User user, LatLng latLng)
        {
            this.user = user;
            this.latLng = latLng;
        }
    }
}
