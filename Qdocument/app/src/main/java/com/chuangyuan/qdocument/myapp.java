package com.chuangyuan.qdocument;

import android.app.Application;

import com.chuangyuan.qdocument.utils.FolderUtils;

/**
 * Created by Administrator on 2016/4/5 0005.
 */
public class Myapp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FolderUtils.createFolder(this,"");

    }
}
