package com.kim.recyclerfooter;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.kim.smilefooter.LoadMoreListener;
import com.kim.smilefooter.LoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<String> list = new ArrayList<>();
    private Handler handler = new Handler();
    private LoadMoreAdapter myAdapter;
    private List<String> refreshData = new ArrayList<>();
    private List<String> moreData = new ArrayList<>();

    private LoadMoreRecyclerView loadmore_recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        initMoreData();
        initRefreshData();
    }

    private void init() {
        initData();
        loadmore_recyclerview = (LoadMoreRecyclerView) findViewById(R.id.loadmore_recyclerview);
        loadmore_recyclerview.setLinearLayout();

        //创建adapter对象
        myAdapter = new LoadMoreAdapter(this);
        loadmore_recyclerview.setAdapter(myAdapter);
        myAdapter.setData(list);//设置数据

        initListener();
    }


    //初始化数据
    private void initData() {
        for (int i = 0; i < 8; i++) {
            list.add("this is test data " + i);
        }
    }

    //初始化加载更多数据
    private void initMoreData() {
        for (int i = 0; i < 5; i++) {
            moreData.add("next page data " + i);
        }
    }

    private void initRefreshData() {
        for (int i = 0; i < 2; i++) {
            refreshData.add("pull down data " + i);
        }
    }

    //初始化监听
    private void initListener() {

        loadmore_recyclerview.setOnLoadMoreDataListener(new LoadMoreListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        list.addAll(0,refreshData);
                        myAdapter.notifyDataSetChanged();
                        loadmore_recyclerview.setRefreshing(false);
                    }
                },1000);
            }

            @Override
            public void onLoadMore() {
                //加入null值此时adapter会判断item的type
                list.add(null);
                Log.i("ViewType","loadMoreData()");
                myAdapter.notifyDataSetChanged();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //移除刷新的footer
                        list.remove(list.size() - 1);
                        myAdapter.notifyDataSetChanged();
                        list.addAll(moreData);
                        myAdapter.notifyDataSetChanged();
                        loadmore_recyclerview.setLoaded();
                    }
                }, 5000);
            }
        });
    }

}
