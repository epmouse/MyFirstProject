package com.chuangyuan.qdocument.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/3/14 0014.
 */
public class ToastUtils {

    private static Toast toast;
    public static void showToast(Context context,String string){
        if(toast==null) {
            toast=toast.makeText(context, string, Toast.LENGTH_SHORT);
        }
            toast.setText(string);

        toast.show();
    }
}
