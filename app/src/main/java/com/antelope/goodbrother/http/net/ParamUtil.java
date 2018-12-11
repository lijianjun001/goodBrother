package com.antelope.goodbrother.http.net;

import android.util.Log;

import com.antelope.goodbrother.account.AccountManager;
import com.antelope.goodbrother.config.Constants;
import com.antelope.goodbrother.manager.KeyManager;
import com.cylty.zhongmukeji.utils.CryptLib;
import com.cylty.zhongmukeji.utils.GsonUtils;
import com.cylty.zhongmukeji.utils.StringUtils;

import java.util.Map;
import java.util.UUID;

/**
 * Created by lijianjun on 2017-06-15.
 */

public class ParamUtil {

    static {
        System.loadLibrary("native-lib");
    }

    static String buildParamStr(Map<String, Object> params, String serviceName) {
        params.put("Service", serviceName);
        params.put("Token", AccountManager.getInstance().getToken());
        params.put("Platform", "01");
        params.put("UniqueId", UUID.randomUUID());
        String output = "";
        CryptLib crypt;
        try {
            crypt = new CryptLib();
            String plainText = GsonUtils.mapToJson(params);
            if (Constants.DEBUG) {
                Log.d("BaseParamUtil", plainText);
            }
            plainText = StringUtils.gbEncoding(plainText);
            output = crypt.encrypt(plainText, KeyManager.getInstance().getKey(), KeyManager.getInstance().getInitVector()); // encrypt
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
}
