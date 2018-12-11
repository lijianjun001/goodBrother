package com.antelope.goodbrother.http.net;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lijianjun on 2017-06-15.
 */

public class ParamSystemUtil {

    public static String getVcode(String phone) {
        Map<String, Object> params = new HashMap<>();
        params.put("Action", "SendSms");
        params.put("Phone", phone);
        params.put("TemplateCode", "00");
        return buildParamStr(params);
    }


    public static String getDistrict(String ParentCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("Action", "GetDistrict");
        params.put("ParentCode", ParentCode);
        return buildParamStr(params);
    }

    public static String getDistrictStr(String ParentCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("Action", "GetDistrict");
        params.put("ParentCode", ParentCode);
        return buildParamStr(params);
    }

    public static String getAbout() {
        Map<String, Object> params = new HashMap<>();
        params.put("Action", "About");
        return buildParamStr(params);
    }

    public static String getVersion(String versionCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("Action", "Version");
        params.put("Version", versionCode);
        return buildParamStr(params);
    }

    public static String GetNotice() {
        Map<String, Object> params = new HashMap<>();
        params.put("Action", "GetNotice");
        return buildParamStr(params);
    }

    public static String GetNoticeTopLine() {
        Map<String, Object> params = new HashMap<>();
        params.put("Action", "GetNoticeTopLine");
        return buildParamStr(params);
    }

    public static String GetAppVersion() {
        Map<String, Object> params = new HashMap<>();
        params.put("Action", "GetAppVersion");
        return buildParamStr(params);
    }

    public static String GetBanner() {
        Map<String, Object> params = new HashMap<>();
        params.put("Action", "GetBanner");
        return buildParamStr(params);
    }

    public static String GetBannerStr() {
        Map<String, Object> params = new HashMap<>();
        params.put("Action", "GetBanner");
        return buildParamStr(params);
    }

    public static String SendSmsImg() {
        Map<String, Object> params = new HashMap<>();
        params.put("Action", "SendSmsImg");
        return buildParamStr(params);
    }

    public static String SendSmsAndImg(String Phone, String ImgGuid, String ImgCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("Action", "SendSmsAndImg");
        params.put("Phone", Phone);
        params.put("TemplateCode", "00");
        params.put("ImgGuid", ImgGuid);
        params.put("ImgCode", ImgCode);
        return buildParamStr(params);
    }

    public static String SendSmsCode(String TemplateCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("Action", "SendSmsCode");
        params.put("TemplateCode", TemplateCode);
        return buildParamStr(params);
    }



    private static String buildParamStr(Map<String, Object> params) {
        return ParamUtil.buildParamStr(params, "System");
    }
}
