package com.chuangyuan.qdocument.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.chuangyuan.qdocument.R;
import com.chuangyuan.qdocument.utils.Constant;

public class SplashActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ctx=this;
        sp = getSharedPreferences("config", MODE_PRIVATE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(2000);
                Intent intent = null;

                //检查登陆状态
                if (sp.getBoolean(Constant.ISLOGINED, false)) {
                    //已登录过的话 ，检查vcode有没有设置
                    //TODO-检查vcode是否设置
                    boolean isSetVcode=false;
                    if(isSetVcode){
                        //TODO-跳转到验证vcode的界面。
                        //验证完成，跳转到登陆界面，此逻辑应该在验证界面写，
                        intent = new Intent(ctx, VcodeVerifyActivity.class);
                    }else{//未设置
                      //TODO-跳转到设置界面,设置完成直接跳转到主页面
                        intent = new Intent(ctx, VcodeSetActivity.class);
                    }

                } else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }).start();
    }
}
