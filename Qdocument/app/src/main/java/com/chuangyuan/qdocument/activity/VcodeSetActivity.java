package com.chuangyuan.qdocument.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chuangyuan.qdocument.R;
import com.chuangyuan.qdocument.utils.Constant;
import com.chuangyuan.qdocument.utils.ToastUtils;

/**
 * 设置Vcode码的界面
 */
public class VcodeSetActivity extends AppCompatActivity {
    private EditText et_vcode1,et_vcode2;
    private Button btn_ok;
    private Context ctx;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vcode_set);
        ctx=this;
        sp=getSharedPreferences("config",MODE_PRIVATE);

        initWidget();

    }

    private void initWidget() {
        et_vcode1= (EditText) findViewById(R.id.et_vcode);
        et_vcode2= (EditText) findViewById(R.id.et_vcode2);
        btn_ok= (Button) findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vcode1=et_vcode1.getText().toString();
                String vcode2=et_vcode2.getText().toString();
                if(TextUtils.isEmpty(vcode1)||TextUtils.isEmpty(vcode2)){
                    ToastUtils.showToast(ctx,"安全口令不能为空");
                    return;
                }

                if(!vcode1.equals(vcode2)){
                    ToastUtils.showToast(ctx,"两次输入不一致");
                    return;
                }
                //TODO-此处需要进行vcode格式的验证，
                //TODO-异步任务把设置好的vcode上传到服务器
                //记录vcode的设置状态
                //if(服务器返回设置成功的状态)，则记录
                sp.edit().putBoolean(Constant.ISSETSUCCESS,true).commit();
                        //设置完毕后直接跳转到主页面
                       Intent intent = new Intent(ctx, MainActivity.class);
                       startActivity(intent);
                       finish();
            }
        });

    }
}
