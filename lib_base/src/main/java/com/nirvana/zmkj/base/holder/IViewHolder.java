package com.nirvana.zmkj.base.holder;

import android.content.Context;
import android.view.View;

public interface IViewHolder<D> {
    void onFindView(View convertView);

    void onBind(Context context, int position, D data);
}
