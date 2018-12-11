package com.antelope.goodbrother.http.java.body;

import com.cylty.zhongmukeji.utils.SysInfoUtil;
import com.nirvana.zmkj.base.BaseApplication;

/**
 * Created by Administrator on 2017/11/17.
 */

public class BodyBuilder<T extends BaseBody> {
    public T createBody(Class<T> c) {
        T t = null;
        try {
            t = c.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        t.setVersion(SysInfoUtil.getVersion(BaseApplication.getInstance()));
        t.setPlatform("01");
        return t;
    }

}
