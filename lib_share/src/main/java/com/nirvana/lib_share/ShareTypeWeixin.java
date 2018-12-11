package com.nirvana.lib_share;

import android.content.Context;

/**
 * Created by Administrator on 2018/2/27.
 */

public class ShareTypeWeixin implements ShareInterface {

    private Context context;

    public ShareTypeWeixin(Context context) {
        this.context = context;
    }

    @Override
    public void shareLink() {
        ShareTextTask shareTask = new ShareTextTask(context);
        shareTask.execute();
    }

    @Override
    public void shareImage() {
        ShareImageTask shareImageTask = new ShareImageTask(context, null);
        shareImageTask.execute();
    }

    @Override
    public void shareLinkAndImage() {

    }

}
