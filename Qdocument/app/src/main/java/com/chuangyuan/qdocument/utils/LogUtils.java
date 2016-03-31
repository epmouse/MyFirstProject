package com.chuangyuan.qdocument.utils;

import android.util.Log;

/**
 * Created by Administrator on 2016/3/21 0021.
 */
public class LogUtils {
        private static boolean isOpen = true;//log开关

        public static void logInfoStar(String msg){
            if(isOpen){
                Log.i("star_xing", msg);
            }
        }

}
