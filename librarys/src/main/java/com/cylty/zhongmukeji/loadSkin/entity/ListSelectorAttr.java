package com.cylty.zhongmukeji.loadSkin.entity;

import android.view.View;
import android.widget.AbsListView;

import com.cylty.zhongmukeji.loadSkin.loader.SkinManager;


public class ListSelectorAttr extends SkinAttr {

	@Override
	public void apply(View view) {
		if(view instanceof AbsListView){
			AbsListView tv = (AbsListView)view;
			
			if(RES_TYPE_NAME_COLOR.equals(attrValueTypeName)){
				tv.setSelector(SkinManager.getInstance().getColor(attrValueRefId));
			}else if(RES_TYPE_NAME_DRAWABLE.equals(attrValueTypeName)){
				tv.setSelector(SkinManager.getInstance().getDrawable(attrValueRefId));
			}
		}
	}
}