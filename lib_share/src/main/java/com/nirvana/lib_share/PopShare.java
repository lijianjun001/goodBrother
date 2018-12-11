package com.nirvana.lib_share;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.nirvana.zmkj.base.BasePopupWindow3;
import com.sina.weibo.sdk.share.WbShareHandler;

public class PopShare extends BasePopupWindow3<WbShareHandler> implements OnClickListener {


    private ShareModel shareModel;
    private String shareToType;
    private LinearLayout WXSessionLl, WXTimelineLl, weiboLL;

    public PopShare(Activity context, WbShareHandler wbShareHandler) {
        super(context, wbShareHandler);
    }

    @Override
    protected void initView(View rootView, WbShareHandler wbShareHandler) {
        this.shareModel = ShareCache.getShareModel();
        rootView.findViewById(R.id.cancel_tv).setOnClickListener(this);
        WXSessionLl = rootView.findViewById(R.id.wx_session_ll);
        WXSessionLl.setOnClickListener(this);
        WXTimelineLl = rootView.findViewById(R.id.wx_timeline_ll);
        WXTimelineLl.setOnClickListener(this);
        weiboLL = rootView.findViewById(R.id.weibo_ll);
        weiboLL.setOnClickListener(this);
        if (shareModel != null) {
            String shareTo = shareModel.getShareTo();
            if (ShareConstants.ShareToWXSceneSessionAndWXSceneTimeline.equals(shareTo)) {
                weiboLL.setVisibility(View.GONE);
            } else if (ShareConstants.ShareToWXSceneSessionAndWeiBo.equals(shareTo)) {
                WXTimelineLl.setVisibility(View.GONE);
            } else if (ShareConstants.ShareToWXSceneTimelineAndWeiBo.equals(shareTo)) {
                WXSessionLl.setVisibility(View.GONE);
            }
        }
    }

    private void share() {
        if (ShareCache.isIsShareInBackGround()) {
            showMessageProxy.displayToast("分享正在进行。。。");
        } else {
            ShareContext shareContext;
            if (ShareConstants.ShareToWXSceneSession.equals(shareToType)) {
                ShareCache.getShareModel().setShareTo(ShareConstants.ShareToWXSceneSession);
                shareContext = new ShareContext(new ShareTypeWeixin(activity));
            } else if (ShareConstants.ShareToWXSceneTimeline.equals(shareToType)) {
                ShareCache.getShareModel().setShareTo(ShareConstants.ShareToWXSceneTimeline);
                shareContext = new ShareContext(new ShareTypeWeixin(activity));
            } else {
                ShareCache.getShareModel().setShareTo(ShareConstants.ShareToWeiBo);
                shareContext = new ShareContext(new ShareTypeWeibo(activity, model));
            }
            shareContext.shareByType(shareModel.getType());
            PopShare.this.dismiss();
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.pop_share;
    }


    public void show(View parentView){

        showAtLocation(parentView, Gravity.BOTTOM,0,0);
    }

    @Override
    protected boolean maskLLShow() {
        return true;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.cancel_tv) {
            dismiss();

        } else if (i == R.id.wx_session_ll) {
            shareToType = ShareConstants.ShareToWXSceneSession;
            share();

        } else if (i == R.id.wx_timeline_ll) {
            shareToType = ShareConstants.ShareToWXSceneTimeline;
            share();

        } else if (i == R.id.weibo_ll) {
            shareToType = ShareConstants.ShareToWeiBo;
            share();

        }
    }
}
