package com.kim.recyclerfooter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kim.smilefooter.SmileView;
import java.util.List;

/**
 * Created by kim on 16-11-28.
 */

public class LoadMoreAdapter extends RecyclerView.Adapter{
    private Context context;

    protected static final int VIEW_ITEM = 0;
    protected static final int VIEW_FOOT = 1;

    protected List<String> mData;

    LoadMoreAdapter(Context context){
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (viewType == VIEW_ITEM) {
            holder = new MyItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent, false));
        }else{
            holder = new MyFooterHolder(LayoutInflater.from(context).inflate(R.layout.item_footer, parent, false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyItemViewHolder) {
            if (((MyItemViewHolder) holder).tv_name != null)
                ((MyItemViewHolder) holder).tv_name.setText(mData.get(position));
        }else if (holder instanceof MyFooterHolder) {
            if (((MyFooterHolder) holder).footview != null){
                ((MyFooterHolder) holder).footview.start();
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position) != null ? VIEW_ITEM : VIEW_FOOT;
    }

    //设置数据的方法
    public void setData(List<String> data) {
        mData = data;
    }

    public class MyItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_name;
        public MyItemViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

    public class MyFooterHolder extends RecyclerView.ViewHolder {
        private final SmileView footview;
        public MyFooterHolder(View itemView) {
            super(itemView);
            footview = (SmileView)itemView.findViewById(R.id.footview);
        }
    }

}
