package com.antelope.goodbrother;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.antelope.goodbrother.config.Constants;
import com.antelope.goodbrother.config.RouterConfig;
import com.antelope.goodbrother.http.net.ResultModel;
import com.antelope.goodbrother.utils.FileUriUtil;
import com.antelope.goodbrother.widget.CustomWebView;
import com.cylty.zhongmukeji.WeakHandler;
import com.cylty.zhongmukeji.myOkhttp.RxSchedulerHelper;
import com.cylty.zhongmukeji.utils.FileUtils;
import com.cylty.zhongmukeji.utils.GsonUtils;
import com.cylty.zhongmukeji.utils.NetUtil;
import com.cylty.zhongmukeji.utils.StringUtils;
import com.nirvana.lib_share.PopShare;
import com.nirvana.lib_share.ShareCache;
import com.nirvana.lib_share.ShareConstants;
import com.nirvana.lib_share.ShareModel;
import com.nirvana.lib_share.ShareSuccessDispatch;
import com.nirvana.zmkj.base.BaseActivity;
import com.nirvana.zmkj.widget.ShowMessageProxy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import androidx.annotation.NonNull;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Route(path = RouterConfig.ACTIVITY_WEB)
public class WebActivity extends BaseActivity implements OnClickListener, ShareSuccessDispatch.OnShareSuccessListener {
    public static final String WEB_URL = "webUrl";
    public static final String WEB_DATA = "webData";
    private static final int REQUEST_CODE_FILE_CHOOSER_PERMISSIONS = 1;
    private static final int REQUEST_CODE_CALL_PERMISSIONS = 2;
    private static final int REQUEST_CODE_STORAGE_PERMISSIONS = 3;
    private CustomWebView webView;
    private RelativeLayout titleLl;
    private LinearLayout backLl;
    private TextView titleTv;
    private FrameLayout videoViewLayout;// 全屏时视频加载Layout
    private View mediaView;// 全屏时视频加载view
    private LinearLayout noNetLayout;// 没有网络或者找不到网页的view
    private MyWebChromeClient myWebChromeClient;
    private WebChromeClient.CustomViewCallback customViewCallback;
    private View parentView;
    private WeakHandler handler = new WeakHandler();
    private String webUrl = "";
    private String webData = "";
    private String currentTitle = "";
    private ImageView noNetIv, closeIv, shareIv;
    private ValueCallback<Uri> valueCallback;// 单选文件回调<android4.4.1
    private ValueCallback<Uri[]> valueCallbacks;//多选文件回调
    private ProgressBar progressBar;
    private ShowMessageProxy showMessageProxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            webUrl = bundle.getString(WEB_URL);
            webData = bundle.getString(WEB_DATA);
        }
        parentView = LayoutInflater.from(this).inflate(R.layout.activity_web, null);
        setContentView(parentView);
        showMessageProxy = new ShowMessageProxy(this);
        ShareSuccessDispatch.addShareListener(this);
        findViewById();
        initView();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    protected void findViewById() {
        titleLl = findViewById(R.id.title_layout);
        backLl = findViewById(R.id.back_ll);
        titleTv = findViewById(R.id.title_tv);
        progressBar = findViewById(R.id.progress_pb);
        webView = findViewById(R.id.webView);
        videoViewLayout = findViewById(R.id.video_view);
        noNetLayout = findViewById(R.id.no_net_layout);
        noNetIv = findViewById(R.id.no_net_iv);
        closeIv = findViewById(R.id.close_iv);
        closeIv.setVisibility(View.VISIBLE);
        closeIv.setOnClickListener(this);
        shareIv = findViewById(R.id.right_iv);
        shareIv.setOnClickListener(this);
    }

    protected void initView() {
        backLl.setOnClickListener(this);
        myWebChromeClient = new MyWebChromeClient();
        webView.setWebChromeClient(myWebChromeClient);
        webView.setWebViewClient(new MyWebClient());
        webView.addJavascriptInterface(new NativeJs(), "android");
        webView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            // TODO Auto-generated method stub
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
//        长按
//        webView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                final WebView.HitTestResult htr = webView.getHitTestResult();//获取所点击的内容
//                if (htr.getType() == WebView.HitTestResult.IMAGE_TYPE || htr.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {//判断被点击的类型为图片
//                    decodeZxing(htr.getExtra());
//                }
//                return false;
//            }
//        });
        if (webData != null && !webData.equals("")) {
            webView.loadDataWithBaseURL("", webData, "text/html", "utf-8", "");
        } else {
            webView.loadUrl(webUrl);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_CALL_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callPhone(webUrl);
                } else {
                    showMessageProxy.displayToast("用户拒绝");
                }
                break;
            case REQUEST_CODE_STORAGE_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveImage(webUrl);
                } else {
                    showMessageProxy.displayToast("用户拒绝存储，无法继续下载");
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    public void callPhone(String url) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
        if (Build.VERSION.SDK_INT >= 23) {
            int hasCallPermission = checkSelfPermission(Manifest.permission.CALL_PHONE);
            if (hasCallPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                        REQUEST_CODE_CALL_PERMISSIONS);
            } else {
                startActivity(intent);
            }
        } else {
            startActivity(intent);
        }
    }

    /**
     * 判断是否是全屏
     */
    public boolean inCustomView() {
        return (mediaView != null);
    }

    /**
     * 全屏时按返加键执行退出全屏方法
     */
    public void hideCustomView() {
        myWebChromeClient.onHideCustomView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoViewLayout.setVisibility(View.GONE);
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
        ShareSuccessDispatch.removeShareListener(this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_FILE_CHOOSER_PERMISSIONS) {
            if (null == valueCallback && null == valueCallbacks)
                return;
            //content://
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (result == null && valueCallback != null) {
                valueCallback.onReceiveValue(null);
                valueCallback = null;
                return;
            }
            if (result == null && valueCallbacks != null) {
                valueCallbacks.onReceiveValue(null);
                valueCallbacks = null;
                return;
            }
            //文件存储位置
            String path = FileUtils.changePath(this, result);
            if (TextUtils.isEmpty(path) && valueCallback != null) {
                valueCallback.onReceiveValue(null);
                valueCallback = null;
                return;
            }
            if (TextUtils.isEmpty(path) && valueCallbacks != null) {
                valueCallbacks.onReceiveValue(null);
                valueCallbacks = null;
                return;
            }
            //file:///
            Uri uri = FileUriUtil.getFileUriByFile(WebActivity.this, new File(path));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                valueCallbacks.onReceiveValue(new Uri[]{uri});
            } else {
                valueCallback.onReceiveValue(uri);
            }
            valueCallback = null;
            valueCallbacks = null;
        }

    }

    public void goBack() {
        if (inCustomView()) {
            hideCustomView();
            return;
        }
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            WebActivity.this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_ll:
                goBack();
                break;
            case R.id.close_iv:
                WebActivity.this.finish();
                break;

        }
    }

    @Override
    public void onShareSuccess(String shareSuccessMessage) {
        showMessageProxy.showMessageDialog(shareSuccessMessage);
    }

    class MyWebClient extends WebViewClient {
        MyWebClient() {
            super();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Constants.DEBUG) {
                Log.d("url", url);
            }
            handleUrl(view, url);
            return true;
        }

        boolean containsIgnoreCase(String url, String message) {
            return url.toLowerCase().contains((message.toLowerCase()));
        }

        void handleUrl(WebView view, String url) {
            if (url.startsWith(CustomWebView.SAVE_IMAGE_FLAG)) {
                webUrl = url;
                checkAndSaveImage(url);
            } else if (containsIgnoreCase(url, "http://back")) {
                goBack();
            } else if (url.startsWith("tel:")) {
                webUrl = url;
                callPhone(webUrl);
            } else if (url.startsWith("mqqopensdkapi:") || url.startsWith("wtloginmqq://") || url.startsWith("mqqwpa://")) {// 加入qq群
                try {
                    Intent intent = new Intent();
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {
                    showMessageProxy.displayToast("没有安装QQ客户端，请下载后重试");
                }
            } else if (url.contains("sinaweibo://")) {
                try {
                    Intent intent = new Intent();
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {
                    showMessageProxy.displayToast("没有安装微博客户端，请下载后重试");
                }
            } else if (url.toLowerCase().startsWith("mailto:")) {
                Intent i = new Intent(Intent.ACTION_SEND);
                String[] urls = url.split(":");
                i.setType("message/rfc822"); // use from live device
                i.putExtra(Intent.EXTRA_EMAIL, urls[1]);
                startActivity(Intent.createChooser(i, "邮件"));
            } else if (url.toLowerCase().contains("login")) {
                ARouter.getInstance().build("/com/LoginByPasswordActivity").navigation();
            } else if (url.contains("#/Index") || url.contains("#/Found") || url.contains("#/PersonalCenter")) {
                ARouter.getInstance().build("/com/MainActivity").navigation();
                WebActivity.this.finish();
            } else if (url.contains("SinaSetPayPasswordSuccess")) {
                handler.post(() -> WebActivity.this.finish());
            } else {
                view.loadUrl(url);
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (url.contains("SinaCallBack")) {
                webView.loadUrl("javascript:android.getBody(document.body.innerHTML)");
            }
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
        }
    }

    class MyWebChromeClient extends WebChromeClient {
        // Android > 4.1.1 调用这个方法
        void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            valueCallback = uploadMsg;
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
//            acceptType=acceptType.replace(",","/");
            if (StringUtils.isEmpty(acceptType)) {
                intent.setType("*/*");
            } else {
                if (!acceptType.contains("/")) {
                    intent.setType("*/" + acceptType);
                } else {
                    intent.setType(acceptType);
                }
            }
            WebActivity.this.startActivityForResult(Intent.createChooser(intent, "File Chooser"),
                    REQUEST_CODE_FILE_CHOOSER_PERMISSIONS);
        }

        // 3.0 + 调用这个方法
        void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            openFileChooser(uploadMsg, acceptType, null);
        }

        // Android < 3.0 调用这个方法
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooser(uploadMsg, null);
        }

        // Android > 5.0 调用这个方法
        @SuppressLint("NewApi")
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                         FileChooserParams fileChooserParams) {
            if (valueCallbacks != null)
                return false;
            valueCallbacks = filePathCallback;
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            String[] types = fileChooserParams.getAcceptTypes();
            if (types != null && types.length > 0 && types[0].length() > 0) {
                if (!types[0].contains("/")) {
                    intent.setType("*/" + types[0]);
                } else {
                    intent.setType(types[0]);
                }
            } else {
                intent.setType("*/*");
            }
            CharSequence title = fileChooserParams.getTitle();
            if (title == null) {
                title = "File Chooser";
            }
            startActivityForResult(Intent.createChooser(intent, title), REQUEST_CODE_FILE_CHOOSER_PERMISSIONS);
            return true;
        }


        @Override
        public void onReceivedTitle(WebView view, String title) {
            if (!NetUtil.isNetworkAvaiable(WebActivity.this)) {
                webView.setVisibility(View.GONE);
                noNetLayout.setVisibility(View.VISIBLE);
                return;
            } else {
                webView.setVisibility(View.VISIBLE);
                noNetLayout.setVisibility(View.GONE);
            }
            webView.loadUrl("javascript:android.getUrl(document.location.href)");
            if (StringUtils.isEmpty(title)) {
                title = "众牧宝";
            }
            if ("找不到网页".equals(title) || title.contains("404") || title.contains("无法找到资源")) {
                webView.setVisibility(View.GONE);
                noNetLayout.setVisibility(View.VISIBLE);
                if ("找不到网页".equals(title)) {
                    noNetIv.setBackgroundResource(R.drawable.no_net);
                } else {
                    noNetIv.setBackgroundResource(R.drawable.net_404);
                }
                return;
            } else {
                webView.setVisibility(View.VISIBLE);
                noNetLayout.setVisibility(View.GONE);
            }
            if (title.equals(currentTitle)) {
                return;
            }
            currentTitle = title;
            String subtitle;
            if (title.length() > 10) {
                subtitle = title.substring(0, 9);
            } else {
                subtitle = title;
            }
            if ("我的卡包".equals(subtitle) || "签到规则".equals(subtitle)) {
                titleLl.setVisibility(View.GONE);
            } else {
                titleLl.setVisibility(View.VISIBLE);
            }
            if ("新手指导".equals(subtitle)) {
                shareIv.setVisibility(View.VISIBLE);
            } else {
                shareIv.setVisibility(View.GONE);
            }
            titleTv.setText(subtitle);
            super.onReceivedTitle(view, title);
        }

        // 播放网络视频时全屏会被调用的方法
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            WebActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
            if (customViewCallback != null) {
                customViewCallback.onCustomViewHidden();
                customViewCallback = null;
                return;
            }
            videoViewLayout.setVisibility(View.VISIBLE);
            videoViewLayout.addView(view);
            mediaView = view;
            // mediaView.setVisibility(View.VISIBLE);
            mediaView.findFocus();
            webView.setVisibility(View.GONE);
            // 如果一个视图已经存在，那么立刻终止并新建一个
            customViewCallback = callback;
        }

        @Override
        public void onHideCustomView() {
            WebActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            if (mediaView == null) // 不是全屏播放状态
                return;
            mediaView.setVisibility(View.GONE);
            videoViewLayout.removeView(mediaView);
            mediaView.destroyDrawingCache();
            mediaView = null;
            videoViewLayout.setVisibility(View.GONE);
            if (customViewCallback != null) {
                customViewCallback.onCustomViewHidden();
                customViewCallback = null;
            }
            webView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress != 100) {
                progressBar.setProgress(newProgress);
            } else {
                progressBar.setProgress(0);
            }
        }
    }

    public void showSharePop(String title, final String text, final String imageUrl, final String url) {
        ShareModel shareModel = new ShareModel();
        shareModel.setNeedReturn(true);
        shareModel.setType(ShareConstants.ShareTypeLink);
        ShareCache.setShareModel(shareModel);
        shareModel.setShareTitle(title);
        shareModel.setShareContent(text);
        shareModel.setShareIcon(imageUrl);
        shareModel.setShareUrl(url);
        PopShare popShare = new PopShare(WebActivity.this, null);
        popShare.showAtLocation(parentView, Gravity.CENTER, 0, 0);
    }

    final class NativeJs {
        @JavascriptInterface
        public void share(final String title, final String text, final String imageUrl, final String url) {
            handler.post(() -> showSharePop(title, text, imageUrl, url));
        }

        @JavascriptInterface
        public void getUrl(final String url) {
            handler.post(() -> {
                if (Constants.DEBUG) {
                    Log.d("getUrl", url);
                }
            });
        }

        @JavascriptInterface
        public void getBody(final String body) {
            Observable.unsafeCreate((Observable.OnSubscribe<ResultModel>) subscriber -> {
                ResultModel resultModel = GsonUtils.fromJson(body, ResultModel.class);
                subscriber.onNext(resultModel);
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResultModel>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onNext(ResultModel model) {
                            if (model.getResult() == 0) {
                                WebActivity.this.finish();
                            } else {
                                WebActivity.this.finish();
                            }
                        }
                    });
        }
    }

    private void checkAndSaveImage(String url) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int hasCallPermission = this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasCallPermission != PackageManager.PERMISSION_GRANTED) {
                this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_STORAGE_PERMISSIONS);
            } else {
                saveImage(url);
            }
        } else {
            saveImage(url);
        }
    }

    public static Observable<File> saveFileObservable(Context context, String imgUrl) {
        return Observable.unsafeCreate((Observable.OnSubscribe<File>) subscriber -> {
            File file = null;
            try {
                file = FileUtils.saveImageByUrl(context, imgUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
            subscriber.onNext(file);
        }).compose(RxSchedulerHelper.io_main());
    }

    private void saveImage(String url) {
        String imageUrl = url.split(CustomWebView.SAVE_IMAGE_FLAG)[1];
        try {
            imageUrl = URLDecoder.decode(imageUrl, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            showMessageProxy.displayToast("保存图片失败");
            return;
        }
        saveFileObservable(this, imageUrl).subscribe(new Observer<File>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                showMessageProxy.displayToast("保存图片失败");
            }

            @Override
            public void onNext(File file) {
                if (file != null) {
                    try {
                        MediaStore.Images.Media.insertImage(WebActivity.this.getContentResolver(),
                                file.getAbsolutePath(), file.getName(), null);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    WebActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, FileUriUtil.getFileUriByFile(WebActivity.this, file)));
                    showMessageProxy.displayToast("保存图片成功,在相册中查看");
                } else {
                    showMessageProxy.displayToast("保存图片失败");
                }
            }
        });
    }

}
