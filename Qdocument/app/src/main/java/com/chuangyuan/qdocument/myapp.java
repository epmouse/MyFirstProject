package com.chuangyuan.qdocument;

import android.app.Application;

import com.chuangyuan.qdocument.utils.FolderUtils;

/**
 * Created by Administrator on 2016/3/22 0022.
 */
public class myapp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        //初始化软件的时候先创建一个存储文件夹
        FolderUtils.createFolder(this,"");
    }
}
