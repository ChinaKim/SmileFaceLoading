package com.kim.smilefooter;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by kim on 16-11-28.
 */

public class LoadMoreRecyclerView extends LinearLayout {
    private Context mContext;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;

    private int totalItemCount;
    private int lastVisibleItemPosition;
    private int visibleThreshold = 1;


    private LoadMoreListener loadMoreListener;

    public LoadMoreRecyclerView(Context context) {
        super(context);
        initView(context);
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }


    private void initView(Context context) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.loadmore_layout, null);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.smile_green));

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setVerticalScrollBarEnabled(true);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayoutOnRefresh(this));
        this.addView(view);
    }


    public void setLinearLayout() {
        linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter != null) {
            mRecyclerView.setAdapter(adapter);
            SetaddOnScrollListener();
        }
    }

    public LinearLayoutManager getLinearLayoutManager() {
        return linearLayoutManager;
    }

    public void SetaddOnScrollListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = getLinearLayoutManager().getItemCount();
                lastVisibleItemPosition = getLinearLayoutManager().findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= lastVisibleItemPosition + visibleThreshold) {
                    loadMore();
                    isLoading = true;
                }
            }
        });
    }

    private boolean isLoading;
    public void setLoaded() {
        isLoading = false;
    }

    public void setRefreshing(boolean refreshing){
        swipeRefreshLayout.setRefreshing(refreshing);
    }

    public void setOnLoadMoreDataListener(LoadMoreListener loadMoreDataListener) {
        this.loadMoreListener = loadMoreDataListener;
    }

    public void refresh() {
        if (loadMoreListener != null) {
            loadMoreListener.onRefresh();
        }
    }

    public void loadMore() {
        if (loadMoreListener != null) {
            loadMoreListener.onLoadMore();
        }
    }
}
