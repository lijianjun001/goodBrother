package com.nirvana.zmkj.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.cylty.zmkj.utils.DensityUtil;
import com.nirvana.ylmc.lib_base.R;


public class CustomDialog extends Dialog {
    private View layout;
    private TextView positiveTv, negativeTv;
    private View dividerLine;
    private Context context;
    public CustomDialog(Context context, int theme) {
        super(context, theme);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window dialogWindow = this.getWindow();
        if (dialogWindow==null){
            throw new RuntimeException("Window service not available");
        }
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
        lp.y = DensityUtil.dipToPx(context,64); // 新位置Y坐标
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT; // 宽度
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度
        dialogWindow.setAttributes(lp);
    }
    public interface OnDiaLogClickListener {
        //左下面的按钮或者单个按钮点击调用
        void onPositive();

        //右下面的按钮点击调用
        void onNegative();
    }
    public void setOnDiaLogClickListener(final OnDiaLogClickListener onDiaLogClickListener) {
        positiveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDiaLogClickListener.onPositive();
            }
        });
        negativeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDiaLogClickListener.onNegative();
            }
        });

    }


    public static class Builder {
        private String message;
        private CustomDialog dialog;
        private String positiveText;
        private String negativeText;

        public Builder(Context context) {
            dialog = new CustomDialog(context, R.style.MyDialog);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (inflater==null){
                throw new RuntimeException("LayoutInflater service not available");
            }
            dialog.layout = inflater.inflate(R.layout.custom_dialog, null);
            dialog.positiveTv = dialog.layout.findViewById(R.id.positive_tv);
            dialog.negativeTv = dialog.layout.findViewById(R.id.negative_tv);
            dialog.dividerLine = dialog.layout.findViewById(R.id.divider_line);
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setPositive(String positiveText) {
            this.positiveText = positiveText;
            return this;
        }

        public Builder setNegative(String negativeText) {
            this.negativeText = negativeText;
            return this;
        }

        public CustomDialog createSingleButtonDialog() {
            dialog.negativeTv.setVisibility(View.GONE);
            dialog.dividerLine.setVisibility(View.GONE);
            if (positiveText != null) {
                dialog.positiveTv.setText(positiveText);
            } else {
                dialog.positiveTv.setText("确定");
            }
            create();
            return dialog;
        }

        public CustomDialog createTwoButtonDialog() {
            //如果传入的按钮文字为空，则使用默认的“是”和“否”
            if (positiveText != null) {
                dialog.positiveTv.setText(positiveText);
            } else {
                dialog.positiveTv.setText("确定");
            }
            if (negativeText != null) {
                dialog.negativeTv.setText(negativeText);
            } else {
                dialog.negativeTv.setText("取消");
            }
            create();
            return dialog;
        }

        private void create() {
            if (message != null) {      //设置提示内容
                ((TextView) dialog.layout.findViewById(R.id.dialog_content_tv)).setText(message);
            }
            dialog.addContentView(dialog.layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            dialog.setCancelable(true);     //用户可以点击手机Back键取消对话框显示
            dialog.setCanceledOnTouchOutside(false);        //用户不能通过点击对话框之外的地方取消对话框显示
        }
    }
}

