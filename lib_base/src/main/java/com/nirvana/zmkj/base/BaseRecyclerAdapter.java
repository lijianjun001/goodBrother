package com.nirvana.zmkj.base;

import android.app.Activity;
import android.view.View;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.nirvana.zmkj.widget.ShowMessageProxy;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Administrator on 2018/4/4.
 */

public abstract class BaseRecyclerAdapter<D, H extends BaseRecyclerAdapter.BaseHolder> extends DelegateAdapter.Adapter<H> {
    protected Activity activity;
    protected LayoutHelper layoutHelper;
    protected RecyclerView.LayoutParams layoutParams;
    protected List<D> data;
    private OnItemClickListener itemClickListener;
    protected ShowMessageProxy showMessageProxy;

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public BaseRecyclerAdapter(Activity activity, LayoutHelper layoutHelper, List<D> data) {
        this(activity, layoutHelper, data, null);
    }

    public BaseRecyclerAdapter(Activity activity, LayoutHelper layoutHelper, List<D> data, @NonNull RecyclerView.LayoutParams layoutParams) {
        this.activity = activity;
        this.layoutHelper = layoutHelper;
        this.data = data;
        this.layoutParams = layoutParams;
        showMessageProxy = new ShowMessageProxy(activity);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    @Override
    public void onBindViewHolder(H holder, final int position) {
        covert(holder, layoutParams, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(position);
                }
            }
        });
    }

    protected abstract void covert(H holder, RecyclerView.LayoutParams layoutParams, int position);

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        }
        return 1;
    }


    static public class BaseHolder extends RecyclerView.ViewHolder {
        public BaseHolder(View itemView) {

            super(itemView);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
