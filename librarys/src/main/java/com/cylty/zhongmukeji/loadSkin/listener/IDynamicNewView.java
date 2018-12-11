package com.cylty.zhongmukeji.loadSkin.listener;

import android.view.View;

import com.cylty.zhongmukeji.loadSkin.entity.DynamicAttr;

import java.util.List;


public interface IDynamicNewView {
    void dynamicAddView(View view, List<DynamicAttr> pDAttrs);
}
