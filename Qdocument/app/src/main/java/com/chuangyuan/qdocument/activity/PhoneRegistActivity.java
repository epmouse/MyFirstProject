package com.chuangyuan.qdocument.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.chuangyuan.qdocument.R;
import com.chuangyuan.qdocument.utils.Constant;
import com.chuangyuan.qdocument.utils.ToastUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PhoneRegistActivity extends AppCompatActivity {
    @InjectView(R.id.et_phoneNum)
    EditText et_phoneNum;
    @InjectView(R.id.et_password)
    EditText et_password;
    @InjectView(R.id.et_password_again)
    EditText et_password_again;
    @InjectView(R.id.et_verify_code)
    EditText et_verify_code;
    @InjectView(R.id.send_verificationCode)
    Button send_verificationCode;
    @InjectView(R.id.btn_register)
    Button btn_register;
    @InjectView(R.id.verify_time)
    TextView verify_time;
    @InjectView(R.id.to_emailRegister)
    TextView to_emailRegister;
    @InjectView(R.id.cb_read_protocol)
    CheckBox cb_read_protocol;
    private SharedPreferences sp;
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ctx=this;
        sp=getSharedPreferences("config",MODE_PRIVATE);
        ButterKnife.inject(this);

        sendVerifyCode();
        register();
        to_emailRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, EmailRegistActivity.class);
                startActivity(intent);
            }
        });
    }
    int i = 60;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            i--;
            verify_time.setText("" + i + "S");
            if(i>0){
            handler.sendEmptyMessageDelayed(99,1000);
            }else{
                i=60;
                verify_time.setText("" + i + "S");
            }
        }
    };
   private boolean isTiming=false;
    /**
     * 发送短信验证码的触发
     */
    private void sendVerifyCode() {
        send_verificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO-发送短信验证码
                //触发计时器
                if(!isTiming){
                         handler.sendEmptyMessage(99);
                    isTiming=true;
                }

            }
        });
    }

    /**
     * 注册按钮的点击触发
     */
    private void register() {
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum = et_phoneNum.getText().toString();
                String password = et_password.getText().toString();
                String password_again = et_password_again.getText().toString();
                String verificationCode = et_verify_code.getText().toString();
                if (TextUtils.isEmpty(phoneNum) || TextUtils.isEmpty(password) || TextUtils.isEmpty(password_again)) {
                    ToastUtils.showToast(ctx, "手机号或密码不能为空");
                    return;
                }
                if (!password.equals(password_again)) {
                    ToastUtils.showToast(ctx, "两次输入的密码不一致");
                    return;
                }
                //TODO-此处添加核对验证码的代码。如果验证码无误，跳转到登陆界面
                //验证验证成功后，把用户名和密码存入sp中
                sp.edit().putString(Constant.USERNAME,phoneNum).commit();
                sp.edit().putString(Constant.PASSWORD,password).commit();
                i=0;//停止计时器
                isTiming=false;
                Intent intent = new Intent(ctx,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
