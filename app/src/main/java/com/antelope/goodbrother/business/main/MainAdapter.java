package com.antelope.goodbrother.business.main;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.antelope.goodbrother.R;
import com.antelope.goodbrother.config.RouterConfig;
import com.bumptech.glide.Glide;
import com.nirvana.zmkj.base.BaseRecyclerAdapter;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class MainAdapter extends BaseRecyclerAdapter<MainEntity, MainHolder> {

    public MainAdapter(Activity activity, LayoutHelper layoutHelper, List<MainEntity> data) {
        super(activity, layoutHelper, data);
    }

    @Override
    protected void covert(MainHolder holder, RecyclerView.LayoutParams layoutParams, int position) {

        MainEntity mainEntity = data.get(position);
        Glide.with(activity).load(mainEntity.getIcon()).placeholder(R.drawable.default_square).into(holder.icon);
        holder.titleTv.setText(mainEntity.getTitle());
        holder.titleTv.setOnClickListener(v -> {
            ARouter.getInstance().build(RouterConfig.ACTIVITY_WEB).withString(RouterConfig.KEY_WEB_URL, mainEntity.getUrl()).navigation();
        });

    }

    @Override
    public MainHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainHolder(LayoutInflater.from(activity).inflate(R.layout.item_main, parent, false));
    }
}
