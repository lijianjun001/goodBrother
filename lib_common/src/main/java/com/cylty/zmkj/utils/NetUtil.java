package com.cylty.zmkj.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Proxy;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by lijianjun on 2017-06-20.
 */
public class NetUtil {

    // 一般来说用一个生成一个UUID的话，会可靠很多，这里就不考虑这个了
    // 而且一般来说上传文件最好用BASE64进行编码，你只要用BASE64不用的符号就可以保证不冲突了。
    // 尤其是上传二进制文件时，其中很可能有\r、\n之类的控制字符，有时还可能出现最高位被错误处理的问题，所以必须进行编码。
    public static final String BOUNDARY = "--my_boundary--";

    public static final int NETWORN_NONE = 0;
    public static final int NETWORN_WIFI = 1;
    public static final int NETWORN_MOBILE = 2;

    /**
     * 获取网络状态
     *
     * @return
     */
    public static int getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // Wifi
        State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (state == State.CONNECTED || state == State.CONNECTING) {
            return NETWORN_WIFI;
        }
        // 2G、3G
        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if (state == State.CONNECTED || state == State.CONNECTING) {
            return NETWORN_MOBILE;
        }
        return NETWORN_NONE;
    }

    /**
     * 网络是否可用
     *
     * @return
     */
    public static boolean isNetworkAvaiable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            NetworkInfo activeNet = connectivityManager.getActiveNetworkInfo();
            if (activeNet == null) {
                return false;
            }
            return activeNet.isConnected();
        }
    }

    /**
     * 网络是否连接
     *
     * @return
     */
    public static boolean isNetConnected(Context context, int type) {
        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = mgr.getNetworkInfo(type);
        if (info != null) {
            return info.isConnected();
        }
        return false;
    }

    /**
     * 获取apn java代理
     *
     * @return
     */
    public static java.net.Proxy getApnJavaProxy(Context context) {
        String apnName = getApnName(context);
        if (apnName != null && apnName.length() > 0) {
            apnName = apnName.toLowerCase(Locale.CHINA);
            if (apnName.contains("uninet") || apnName.contains("3gnet") || apnName.contains("cmnet")
                    || apnName.contains("ctnet")) {
                return null;
            }
            String host = "";
            int port;
            if (apnName.contains("cmwap") || apnName.contains("3gwap") || apnName.contains("uniwap")) {
                host = "10.0.0.172";
                port = 80;
            } else if (apnName.contains("ctwap")) {
                host = "10.0.0.200";
                port = 80;
            } else {
                host = Proxy.getDefaultHost();
                port = Proxy.getDefaultPort();
            }
            if (host != null) {
                java.net.Proxy p = new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(host, port));
                return p;
            }

        }
        return null;
    }


    /**
     * 获取apn名字
     *
     * @return
     */
    private static String getApnName(Context context) {
        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = mgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiConn.isConnected()) { // 如果有wifi连接，不需要代理
            return null;
        }
        NetworkInfo net = mgr.getActiveNetworkInfo();
        if (net != null) {
            return net.getExtraInfo();
        }
        return null;
    }


    /**
     * 普通字符串数据
     *
     * @param textParams
     * @param ds
     * @throws Exception
     */
    public static void writeStringParams(Map<String, String> textParams,
                                         DataOutputStream ds) throws Exception {
        Set<String> keySet = textParams.keySet();
        for (Iterator<String> it = keySet.iterator(); it.hasNext(); ) {
            String name = it.next();
            String value = textParams.get(name);
            ds.writeBytes("--" + BOUNDARY + "\r\n");
            ds.writeBytes("Content-Disposition: form-data; name=\"" + name
                    + "\"\r\n");
            ds.writeBytes("\r\n");
            value = value + "\r\n";
            ds.write(value.getBytes());

        }
    }

    /**
     * 文件数据
     *
     * @param fileparams
     * @param ds
     * @throws Exception
     */
    public static void writeFileParams(Map<String, File> fileparams,
                                       DataOutputStream ds) throws Exception {
        Set<String> keySet = fileparams.keySet();
        for (Iterator<String> it = keySet.iterator(); it.hasNext(); ) {
            String name = it.next();
            File value = fileparams.get(name);
            ds.writeBytes("--" + BOUNDARY + "\r\n");
            ds.writeBytes("Content-Disposition: form-data; name=\"" + name
                    + "\"; filename=\""
                    + URLEncoder.encode(value.getName(), "UTF-8") + "\"\r\n");
            ds.writeBytes("Content-Type:application/octet-stream \r\n");
            ds.writeBytes("\r\n");
            ds.write(getBytes(value));
            ds.writeBytes("\r\n");
        }
    }

    // 把文件转换成字节数组
    private static byte[] getBytes(File f) throws Exception {
        FileInputStream in = new FileInputStream(f);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int n;
        while ((n = in.read(b)) != -1) {
            out.write(b, 0, n);
        }
        in.close();
        return out.toByteArray();
    }

    /**
     * 添加结尾数据
     *
     * @param ds
     * @throws Exception
     */
    public static void paramsEnd(DataOutputStream ds) throws Exception {
        ds.writeBytes("--" + BOUNDARY + "--" + "\r\n");
        ds.writeBytes("\r\n");
    }

    public static String readString(InputStream is) {
        return new String(readBytes(is));
    }

    public static byte[] readBytes(InputStream is) {
        try {
            byte[] buffer = new byte[1024];
            int len = -1;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
