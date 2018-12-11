package com.nirvana.zmkj.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.cylty.zhongmukeji.utils.StringUtils;
import com.nirvana.ylmc.lib_base.R;

public class CustomProgressDialog extends Dialog {

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_progress_dialog);
        if (getWindow() != null) {
            getWindow().getAttributes().gravity = Gravity.CENTER;
        }
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        ImageView imageView = findViewById(R.id.loading_iv);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView
                .getBackground();
        animationDrawable.start();
    }

    public void setMessage(String strMessage) {
        TextView messageTv = findViewById(R.id.loading_tv);
        if (!StringUtils.isEmpty(strMessage)&&messageTv!=null) {
            messageTv.setText(strMessage);
        }
    }
}
