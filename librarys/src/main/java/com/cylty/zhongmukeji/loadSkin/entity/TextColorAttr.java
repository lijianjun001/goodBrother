package com.cylty.zhongmukeji.loadSkin.entity;

import android.view.View;
import android.widget.TextView;

import com.cylty.zhongmukeji.loadSkin.loader.SkinManager;


public class TextColorAttr extends SkinAttr {

	@Override
	public void apply(View view) {
		if(view instanceof TextView){
			TextView tv = (TextView)view;
			if(RES_TYPE_NAME_COLOR.equals(attrValueTypeName)){
				tv.setTextColor(SkinManager.getInstance().convertToColorStateList(attrValueRefId));
			}
		}
	}
}
