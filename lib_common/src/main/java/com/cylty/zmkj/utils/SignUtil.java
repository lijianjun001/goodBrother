package com.cylty.zmkj.utils;

import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class SignUtil {
    /**
     * @param params 传递的参数，是一个map
     * @param key    签名时候用的key,客户端和服务端要一致
     * @return
     */
    public static String sign(Map<String, String> params, String key) {
        params = sortMapByKey(params);
        String paramStr = "{";
        for (Map.Entry<String, String> entry : params.entrySet()) {
            paramStr = paramStr + "\"" + entry.getKey() + "\":" + "\"" + entry.getValue() + "\",";
        }
        paramStr = paramStr + "\"key\":\"" + key + "\"}";
        String sign = null;
        try {
            sign = Md5Util.getMD5(paramStr);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sign;
    }

    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sortMap = new TreeMap<String, String>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }
}

//比较器类
class MapKeyComparator implements Comparator<String> {
    public int compare(String str1, String str2) {
        return str1.compareTo(str2);
    }
}
