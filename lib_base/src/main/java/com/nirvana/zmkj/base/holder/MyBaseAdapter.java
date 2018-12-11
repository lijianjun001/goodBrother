package com.nirvana.zmkj.base.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nirvana.zmkj.widget.ShowMessageProxy;

import java.util.List;

public abstract class MyBaseAdapter<T, VH extends IViewHolder> extends BaseAdapter {

    protected Context mContext;
    protected List<T> mList;
    protected ShowMessageProxy mShowMessageProxy;
    private HolderCreator mholderCreator;

    public MyBaseAdapter(Context context, List<T> list, HolderCreator holderCreator) {
        this.mContext = context;
        mShowMessageProxy = new ShowMessageProxy(context);
        this.mList = list;
        this.mholderCreator = holderCreator;
    }

    @Override
    public int getCount() {
        if (mList != null && !mList.isEmpty()) {
            return mList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VH iViewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(mholderCreator.getLayout(), null);
            iViewHolder = (VH) mholderCreator.createViewHolder();
            iViewHolder.onFindView(convertView);
            convertView.setTag(iViewHolder);
        } else {
            iViewHolder = (VH) convertView.getTag();
        }
        iViewHolder.onBind(mContext, position, mList.get(position));
        return convertView;
    }

}
