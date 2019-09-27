package com.antelope.goodbrother.config;

public class Constants {

    private static final boolean NATIVE_IP = false;

    private static String APP_URL_CLIENT_IP_JAVA;//java新请求

    public static String getAppUrlClientIpJava() {
        return APP_URL_CLIENT_IP_JAVA;
    }

    private static String APP_URL_CLIENT_IP_PORT;//net接口地址

    public static String getAppUrlClientIpPort() {
        return APP_URL_CLIENT_IP_PORT;
    }

    private static String APP_URL_WEB_IP_PORT;//网页地址

    public static String getAppUrlWebIpPort() {
        return APP_URL_WEB_IP_PORT;
    }

    private static String APP_URL_WEB;//Emubao/WebApp地址

    public static String getAppUrlWeb() {
        return APP_URL_WEB;
    }

    private static String APP_URL_WEB_VUE;

    public static String getAppUrlWebVue() {
        return APP_URL_WEB_VUE;
    }

    static {
        if (NATIVE_IP) {
            APP_URL_CLIENT_IP_JAVA = "http://192.168.31.188:8089/api/";//阿龙
//            APP_URL_CLIENT_IP_JAVA = "http://192.168.31.110:8089/api/";//祥子
            APP_URL_CLIENT_IP_PORT = "http://192.168.31.188:8001/";//阿龙
//            APP_URL_CLIENT_IP_PORT = "http://192.168.31.110:31146/";//祥子
            APP_URL_WEB_IP_PORT = "http://192.168.31.188:8004/";
            APP_URL_WEB_VUE = "http://192.168.31.133:9081/";
        } else {
            APP_URL_CLIENT_IP_JAVA = "https://japiv2.emubao.com/api/";
            APP_URL_CLIENT_IP_PORT = "https://androidapi.emubao.com/";
            APP_URL_WEB_IP_PORT = "https://android2.emubao.com/";
            APP_URL_WEB_VUE = "https://v.emubao.com/";
        }
        APP_URL_WEB = APP_URL_WEB_IP_PORT + "/Emubao/WebApp";
    }

    public static final int SUCCESS_CODE = 0;
    public static final String IS_FIRST_IN = "isFirstIn_2";//如果需要用户看引导图，这个值需要变

    public static final String IS_READ_GUIDANCE = "isReadGuidance";
    public static final String IS_SET_SIGN_REMIND = "isSetSignRemind";
    public static final String TOKEN_INVALID_CODE = "-401";
    public static final String MAIN_CODE = "-2038";
    public static final String REAL_NAME_CODE = "-2034";
    public static final int DEFAULT_TIMEOUT = 60;
    public static final String IS_UPLOAD_LOCATION = "isUploadLocation";
    public static final String TELEPHONE = "telephone";
    public static final String USER_INFO = "userInfo";
    public static final String SERVICE_TEL = "400-8923-020";


}
