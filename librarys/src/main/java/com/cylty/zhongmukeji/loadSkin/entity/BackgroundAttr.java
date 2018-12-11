package com.cylty.zhongmukeji.loadSkin.entity;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;

import com.cylty.zhongmukeji.loadSkin.loader.SkinManager;
import com.cylty.zhongmukeji.views.MyRadioButton;


public class BackgroundAttr extends SkinAttr {

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void apply(View view) {
		
		if(RES_TYPE_NAME_COLOR.equals(attrValueTypeName)){
			view.setBackgroundColor(SkinManager.getInstance().getColor(attrValueRefId));
		}else if(RES_TYPE_NAME_DRAWABLE.equals(attrValueTypeName)){
			Drawable bg = SkinManager.getInstance().getDrawable(attrValueRefId);
			if (view instanceof MyRadioButton){
				MyRadioButton temp=(MyRadioButton)view;
				temp.setCompoundDrawablesWithIntrinsicBounds(null,bg,null,null);
			}else{
				view.setBackground(bg);
			}
		}
	}
}
