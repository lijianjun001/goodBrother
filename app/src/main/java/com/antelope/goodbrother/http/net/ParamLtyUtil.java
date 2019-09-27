package com.antelope.goodbrother.http.net;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lijianjun on 2017-06-15.
 */

public class ParamLtyUtil {


    public static String GetLtyWxCodePopState() {
        Map<String, Object> params = new HashMap<>();
        params.put("Action", "GetLtyWxCodePopState");
        return buildParamStr(params);
    }

    public static String GetLtyWxCode() {
        Map<String, Object> params = new HashMap<>();
        params.put("Action", "GetLtyWxCode");
        return buildParamStr(params);
    }

    public static String ReceiveLibrary(String Code) {
        Map<String, Object> params = new HashMap<>();
        params.put("Code", Code);
        params.put("Action", "ReceiveLibrary");
        return buildParamStr(params);
    }

    public static String LtyScanning(String Code) {
        Map<String, Object> params = new HashMap<>();
        params.put("Code", Code);
        params.put("Action", "LtyScanning");
        return buildParamStr(params);
    }

    public static String ReceiveLibraryLog(String Phone) {
        Map<String, Object> params = new HashMap<>();
        params.put("Phone", Phone);
        params.put("Action", "ReceiveLibraryLog");
        return buildParamStr(params);
    }

    private static String buildParamStr(Map<String, Object> params) {
        return ParamUtil.buildParamStr(params, "Lty");
    }
}
