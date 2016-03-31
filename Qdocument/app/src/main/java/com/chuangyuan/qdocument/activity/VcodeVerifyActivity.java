package com.chuangyuan.qdocument.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chuangyuan.qdocument.R;
import com.chuangyuan.qdocument.utils.ToastUtils;

/**
 * 验证Vcode码的页面
 */
public class VcodeVerifyActivity extends AppCompatActivity {
    private EditText et_vcode;
    private Button btn_deblock;
    private Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vcode_verify);

        et_vcode= (EditText) findViewById(R.id.et_vcode);
        btn_deblock= (Button) findViewById(R.id.btn_deblock);
        btn_deblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String VerifyVcode = et_vcode.getText().toString();
                if(TextUtils.isEmpty(VerifyVcode)){
                    ToastUtils.showToast(ctx,"口令不能为空");
                    return;
                }
                //TODO-验证vcode，验证通过调到主界面
                if(true){
                    Intent intent = new Intent(ctx,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    ToastUtils.showToast(ctx,"口令错误，请核实后重新输入");
                    return;
                }
            }
        });
    }
}
