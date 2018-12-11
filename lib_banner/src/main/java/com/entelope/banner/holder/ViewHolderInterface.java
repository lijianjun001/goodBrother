package com.entelope.banner.holder;

import android.content.Context;
import android.view.View;

public interface ViewHolderInterface<D> {
    View createView(Context context);

    void onBind(Context context, int position, D data);
}
