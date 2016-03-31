package com.chuangyuan.qdocument.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import com.chuangyuan.qdocument.R;

/**
 * Created by Administrator on 2016/3/21 0021.
 */
public class DialogUtils {
public static void showDialog(Context context,String title,String msg){
    Dialog alertDialog = new AlertDialog.Builder(context).
            setTitle("确定删除？").
            setMessage("您确定删除该条信息吗？").
            setIcon(R.mipmap.app_icon).
            setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                }
            }).
            setNegativeButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                }
            }).
            setNeutralButton("查看详情", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                }
            }).
            create();
    alertDialog.show();
}
}
