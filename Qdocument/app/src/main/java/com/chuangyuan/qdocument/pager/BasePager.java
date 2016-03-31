package com.chuangyuan.qdocument.pager;


import android.content.Context;
import android.view.View;

/**
 * Created by Administrator on 2016/3/15 0015.
 */
public abstract class BasePager {
    public Context context;
    public BasePager(Context context) {
        this.context=context;
    }
    public abstract View initView();

    public void initData(){

    }
}
