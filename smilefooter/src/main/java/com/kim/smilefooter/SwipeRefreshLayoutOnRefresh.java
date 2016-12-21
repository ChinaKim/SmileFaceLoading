package com.kim.smilefooter;

import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created by kim on 16-11-28.
 */

public class SwipeRefreshLayoutOnRefresh implements SwipeRefreshLayout.OnRefreshListener  {
    private LoadMoreRecyclerView mLoadMoreRecyclerView;

    public SwipeRefreshLayoutOnRefresh(LoadMoreRecyclerView loadMoreRecyclerView) {
        this.mLoadMoreRecyclerView = loadMoreRecyclerView;
    }

    @Override
    public void onRefresh() {
        mLoadMoreRecyclerView.refresh();
    }
}
