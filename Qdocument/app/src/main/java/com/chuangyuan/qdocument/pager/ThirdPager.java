package com.chuangyuan.qdocument.pager;

import android.content.Context;
import android.view.View;

import com.chuangyuan.qdocument.R;
import com.chuangyuan.qdocument.activity.MainActivity;

/**
 * Created by Administrator on 2016/3/15 0015.
 */
public class ThirdPager extends BasePager {

    public ThirdPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view=View.inflate(context, R.layout.thirdpager_layout,null);

        return view;
    }
}
