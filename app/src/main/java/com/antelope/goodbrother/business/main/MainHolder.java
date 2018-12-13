package com.antelope.goodbrother.business.main;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.antelope.goodbrother.R;
import com.nirvana.zmkj.base.BaseRecyclerAdapter;

public class MainHolder extends BaseRecyclerAdapter.BaseHolder {


    ImageView icon;
    TextView titleTv;

    public MainHolder(View itemView) {
        super(itemView);
        icon = itemView.findViewById(R.id.icon_iv);
        titleTv = itemView.findViewById(R.id.title_tv);
    }
}
