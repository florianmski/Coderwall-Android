package com.florianmski.coderwall.framework.ui.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.florianmski.coderwall.R;
import com.florianmski.coderwall.framework.errors.Comportment;
import com.florianmski.coderwall.framework.errors.ErrorHandler;
import com.florianmski.coderwall.framework.errors.NoResultException;
import com.florianmski.coderwall.framework.errors.RetrofitComportment;
import com.florianmski.coderwall.framework.utils.ColorUtils;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.observables.AndroidObservable;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

public abstract class RxFragment<E, V extends View> extends BaseFragment implements Observer<E>, SwipeRefreshLayout.OnRefreshListener
{
    protected E data;
    protected V view;

    protected RelativeLayout root;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected FrameLayout frameLayoutLoadingWrapper;
    protected ProgressBar progressBar;
    protected FrameLayout frameLayoutErrorWrapper;
    protected TextView errorView;
    protected View.OnClickListener retryListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            refresh(true);
        }
    };

    protected CompositeSubscription subscriptions = new CompositeSubscription();
    protected Subscription subscription = Subscriptions.empty();
    protected ErrorHandler errorHandler;
    protected Comportment defaultComportment = new Comportment(
            null,
            "Unknown error\nA report has been send to the dev",
            "tap to retry",
            retryListener);

    protected boolean refreshAtStart = true;
    protected boolean instantLoad = false;
    protected AnimatorSet fadeAnim;
    protected View viewBeingAnimated;

    public RxFragment() {}

    protected abstract int getViewLayoutId();
    protected abstract Observable<E> createObservable();
    protected abstract void refreshView(E data);
    protected abstract boolean isEmpty(E data);

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if(fadeAnim != null)
        {
            fadeAnim.removeAllListeners();
            fadeAnim.cancel();
        }

        subscriptions.unsubscribe();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        // wait for the toolbar to be ready before setting paddingTop on the view
        getToolbarGroup().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                getToolbarGroup().getViewTreeObserver().removeGlobalOnLayoutListener(this);

                // don't touch anything if we already set a custom paddingTop
                if(view.getPaddingTop() == 0)
                {
                    setGroupViewPadding(
                            view.getPaddingLeft(),
                            getToolbarGroup().getHeight(),
                            view.getPaddingRight(),
                            view.getPaddingBottom());
                }
            }
        });

        errorHandler = new ErrorHandler(getActivity(), errorView, defaultComportment)
                .putComportment(new Comportment(NoResultException.class, "No result found :(", "tap to retry", retryListener))
                .putComportment(new RetrofitComportment(retryListener));

        // by default, pull to refresh is not possible
        setPullToRefresh(false);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(ColorUtils.fromAttribute(getActivity(), R.attr.colorPrimaryDark));

        if(refreshAtStart)
            refresh(!instantLoad);
        else
            showView();
    }

    protected View getLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_item_group, container, false);
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = getLayout(inflater, container, savedInstanceState);
        ViewStub vs = (ViewStub) view.findViewById(R.id.viewStub);
        vs.setLayoutResource(getViewLayoutId());
        vs.inflate();
        return view;
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState)
    {
        super.onViewCreated(v, savedInstanceState);

        root = (RelativeLayout) v.findViewById(R.id.root);
        view = (V) v.findViewById(android.R.id.list);
        frameLayoutLoadingWrapper = (FrameLayout) v.findViewById(R.id.frameLayoutLoadingWrapper);
        progressBar = (ProgressBar) v.findViewById(R.id.pb_loading);
        frameLayoutErrorWrapper = (FrameLayout) v.findViewById(R.id.frameLayoutErrorWrapper);
        errorView = (TextView) v.findViewById(R.id.error);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
    }

    public void refresh(boolean showProgressBar)
    {
        if(showProgressBar)
        {
            showProgressBar(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    refresh();
                }
            });
        }
        else
            refresh();
    }

    private void refresh()
    {
        // unsubscribe the subscription and add the new one
        subscriptions.remove(subscription);
        subscription = AndroidObservable.bindFragment(this, createObservable().subscribeOn(Schedulers.io()))
                .subscribe(RxFragment.this);
        subscriptions.add(subscription);
    }

    @Override
    public void onInsetsChanged(Rect insets)
    {
        super.onInsetsChanged(insets);

        setGroupViewPadding(
                view.getPaddingLeft(),
                view.getPaddingTop(),
                view.getPaddingRight(),
                insets.bottom);

        swipeRefreshLayout.setProgressViewEndTarget(false, view.getPaddingTop() * 2);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser)
            getBaseActivity().showToolbar(true);
    }

    protected void showProgressBar(Animator.AnimatorListener listener)
    {
        show(frameLayoutLoadingWrapper, listener, view, frameLayoutErrorWrapper);
    }

    protected void showView()
    {
        show(view, frameLayoutErrorWrapper, frameLayoutLoadingWrapper);
    }

    protected void showErrorView()
    {
        show(frameLayoutErrorWrapper, view, frameLayoutLoadingWrapper);
    }

    protected void show(View viewToShow, View... viewsToHide)
    {
        show(viewToShow, null, viewsToHide);
    }

    protected void show(View viewToShow, Animator.AnimatorListener listener, View... viewsToHide)
    {
        // if the view is already visible, nothing to do!
        if((viewBeingAnimated != null && viewBeingAnimated.getId() == viewToShow.getId()) || viewToShow.getVisibility() == View.VISIBLE)
            return;

        if(fadeAnim != null)
            fadeAnim.cancel();

        fadeAnim = new AnimatorSet();
        if(viewsToHide.length == 0)
            fadeAnim.play(changeToVisibility(View.VISIBLE, viewToShow));
        else
        {
            AnimatorSet.Builder builder = fadeAnim.play(changeToVisibility(View.GONE, viewsToHide[0]));
            if(viewsToHide.length > 1)
            {
                for(int i = 1; i < viewsToHide.length; i++)
                    builder.with(changeToVisibility(View.GONE, viewsToHide[i]));
            }

            builder.before(changeToVisibility(View.VISIBLE, viewToShow));
        }

        if(listener != null)
            fadeAnim.addListener(listener);

        fadeAnim.start();
    }

    private Animator changeToVisibility(final int toVisibility, final View v)
    {
        float startAlpha = toVisibility == View.VISIBLE ? 0f : 1f;
        float endAlpha = toVisibility == View.VISIBLE ? 1f : 0f;

        float startY = toVisibility == View.VISIBLE ? 50f : 0f;
        float endY = toVisibility == View.VISIBLE ? 0f : -50f;

        PropertyValuesHolder fadeAnimator = PropertyValuesHolder.ofFloat("alpha", startAlpha, endAlpha);
        PropertyValuesHolder translateAnimator = PropertyValuesHolder.ofFloat("y", startY, endY);

        ValueAnimator animator = ObjectAnimator.ofPropertyValuesHolder(v, fadeAnimator, translateAnimator);
        animator.addListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {
                viewBeingAnimated = v;
                if (toVisibility == View.VISIBLE)
                    v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation)
            {
                viewBeingAnimated = null;
                if (toVisibility != View.VISIBLE)
                    v.setVisibility(toVisibility);
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });

        return animator;
    }

    // avoid flickering of the progressbar when we get the results almost immediately
    protected void setInstantLoad(boolean instantLoad)
    {
        this.instantLoad = instantLoad;
    }

    protected void setRefreshOnStart(boolean refreshAtStart)
    {
        this.refreshAtStart = refreshAtStart;
    }

    public void setGroupViewPadding(int left, int top, int right, int bottom)
    {
        view.setPadding(left, top, right, bottom);

        // set padding on wrappers
        if(getInsets() != null)
        {
            top -= getInsets().bottom;
            frameLayoutLoadingWrapper.setPadding(left, top, right, 0);
            frameLayoutErrorWrapper.setPadding(left, top, right, 0);
        }
    }

    @Override
    public void onNext(E item)
    {
        data = item;

        if(isEmpty(item))
            // this way the observable do not call onCompleted so it is still active.
            // Useful when doing the first sync because the cursor can be reloaded and
            // display the item being synced for the first time
            onError(new NoResultException());
        else
        {
            refreshView(item);
            showView();
        }

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onCompleted() {}

    @Override
    public void onError(Throwable throwable)
    {
        errorHandler.handle(throwable, "Error while loading stuff");
        showErrorView();
    }

    @Override
    public void onRefresh()
    {
        refresh(false);
    }

    public void setPullToRefresh(boolean pullToRefresh)
    {
        swipeRefreshLayout.setEnabled(pullToRefresh);
    }
}
