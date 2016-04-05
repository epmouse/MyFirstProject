package com.chuangyuan.qdocument.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by Administrator on 2016/4/5 0005.
 */
public class PhotoInfoUtils {

    /**
     * * 获取手机imei串号
     */
    public static String getImei(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        return imei;
    }
}
