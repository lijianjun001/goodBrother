package com.cylty.zhongmukeji.loadSkin.entity;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cylty.zhongmukeji.loadSkin.loader.SkinManager;
import com.cylty.zhongmukeji.views.MyRadioButton;


public class SizeAttr extends SkinAttr {
	public static final String WIDTH = "layout_width";
	public static final String HEIGHT = "layout_height";
	public static final String DRAWABLE_WIDTH = "drawableWidth";
	public static final String DRAWABLE_HEIGHT = "drawableheight";
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void apply(View view) {
		if(RES_TYPE_NAME_DIMEN.equals(attrValueTypeName)){
			if (WIDTH.equals(attrName)){
				//设置控件宽高不起作用
				LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(Float.valueOf(SkinManager.getInstance().getSize(attrValueRefId)).intValue(),0);
				view.setLayoutParams(params);
				view.invalidate();
			}else if(HEIGHT.equals(attrName)){
				LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) view.getLayoutParams();
				params.height=Float.valueOf(SkinManager.getInstance().getSize(attrValueRefId)).intValue();
				view.setLayoutParams(params);
				view.invalidate();
			}else if(DRAWABLE_WIDTH.equals(attrName)){
				MyRadioButton myRadioButton=(MyRadioButton)view;
				myRadioButton.setDrawableWidth(Float.valueOf(SkinManager.getInstance().getSize(attrValueRefId)).intValue());
			}else if(DRAWABLE_HEIGHT.equals(attrName)){
				MyRadioButton myRadioButton=(MyRadioButton)view;
				myRadioButton.setDrawableHeight(Float.valueOf(SkinManager.getInstance().getSize(attrValueRefId)).intValue());
			}
		}
	}
}
