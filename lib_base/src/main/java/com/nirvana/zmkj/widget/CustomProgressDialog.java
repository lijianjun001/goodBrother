package com.nirvana.zmkj.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.nirvana.ylmc.lib_base.R;

public class CustomProgressDialog extends Dialog {

    private TextView messageTv;

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getWindow() != null) {
            getWindow().getAttributes().gravity = Gravity.CENTER;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        ImageView imageView = findViewById(R.id.loading_iv);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView
                .getBackground();
        animationDrawable.start();
    }

    public static class Builder {
        private CustomProgressDialog dialog;

        private String message;

        public Builder(Context context) {
            dialog = new CustomProgressDialog(context, R.style.CustomProgressDialog);
            dialog.setContentView(R.layout.custom_progress_dialog);
            dialog.messageTv = dialog.findViewById(R.id.message_tv);
        }

        public CustomProgressDialog create() {
            if (message != null && dialog.messageTv != null) {      //设置提示内容
                dialog.messageTv.setText(message);
            }
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            return dialog;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }
    }
}
