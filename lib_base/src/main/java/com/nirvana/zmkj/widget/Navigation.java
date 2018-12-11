package com.nirvana.zmkj.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nirvana.ylmc.lib_base.R;

import androidx.annotation.DrawableRes;


public class Navigation extends RelativeLayout {

    private LinearLayout backLL;
    private Button backBtn;
    private TextView titleTv;
    private OnBackListener onBackListener;
    private View rootView;
    private ImageView rightIv;

    public void setOnBackListener(OnBackListener onBackListener) {
        this.onBackListener = onBackListener;
    }

    public Navigation(Context context) {
        super(context);
        initView();
    }

    public Navigation(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public Navigation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        rootView = layoutInflater.inflate(R.layout.activity_title, null);
        addView(rootView);
        backLL = rootView.findViewById(R.id.back_ll);
        backLL.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackListener.OnBack();
            }
        });
        backBtn = rootView.findViewById(R.id.back_btn);
        titleTv = rootView.findViewById(R.id.title_tv);
        rightIv = rootView.findViewById(R.id.right_iv);
    }

    public void setTitle(String title) {
        if (titleTv != null && title != null) {
            titleTv.setText(title);
        }
    }

    public void setBackground(int color) {
        rootView.setBackgroundColor(getResources().getColor(color));
    }

    public void setBackBg(int backBg) {
        backBtn.setBackgroundResource(backBg);
    }

    public void setNavigationTitleColor(int color) {
        titleTv.setTextColor(getResources().getColor(color));
    }

    public interface OnBackListener {
        void OnBack();
    }

    public void setRightIvSrc(@DrawableRes int src) {
        rightIv.setVisibility(VISIBLE);
        rightIv.setBackgroundResource(src);
    }

    public void setRightIvOnclickListener(OnClickListener onclickListener) {
        rightIv.setOnClickListener(onclickListener);
    }

}
