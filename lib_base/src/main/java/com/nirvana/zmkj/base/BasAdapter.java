package com.nirvana.zmkj.base;

import android.content.Context;
import android.widget.BaseAdapter;

import com.nirvana.zmkj.widget.ShowMessageProxy;

import java.util.List;

public abstract class BasAdapter<T> extends BaseAdapter {

    protected Context context;
    protected List<T> list;
    protected ShowMessageProxy showMessageProxy;

    public BasAdapter(Context context, List<T> list) {
        this.context = context;
        showMessageProxy = new ShowMessageProxy(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list!=null&&!list.isEmpty()) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
