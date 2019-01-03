package com.antelope.goodbrother.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.vlayout.LayoutHelper;
import com.antelope.goodbrother.R;
import com.nirvana.zmkj.base.BaseRecyclerAdapter;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class FootViewAdapter extends BaseRecyclerAdapter<String, FootViewAdapter.FootViewHolder> {
    public static final String LOADING = "loading";
    public static final String LOAD_COMPLETE = "loadComplete";

    public FootViewAdapter(Context context, LayoutHelper layoutHelper, List<String> data) {
        super(context, layoutHelper, data);
    }

    @Override
    protected void covert(FootViewHolder holder, RecyclerView.LayoutParams layoutParams, int position) {
        if (LOADING.equals(data.get(position))) {
            holder.footerTv.setText("加载更多");
        } else if (LOAD_COMPLETE.equals(data.get(position))) {
            holder.footerTv.setText("已经到底了");
        }

    }

    @Override
    public FootViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FootViewHolder(LayoutInflater.from(context).inflate(R.layout.recycview_footer, parent, false));
    }

    public static class FootViewHolder extends BaseRecyclerAdapter.BaseHolder {
        TextView footerTv;

        public FootViewHolder(View itemView) {
            super(itemView);
            footerTv = itemView.findViewById(R.id.footer_tv);
        }
    }


}
