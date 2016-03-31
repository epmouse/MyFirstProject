package com.chuangyuan.qdocument.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chuangyuan.qdocument.R;
import com.chuangyuan.qdocument.utils.Constant;
import com.chuangyuan.qdocument.utils.ToastUtils;

public class LoginActivity extends AppCompatActivity {

    private ImageView image_pwd,image_username;
    /**
     * 记录密码是否显示的状态
     */
    private  boolean isDisplay_pwd =true;
    /**
     * 用户名输入历史是否显示的状态。
     */
    private boolean isSelected_username=false;
    private EditText et_userName,et_Pwd;
    private Button btn_login;
    private TextView noAccount,forgetPwd;
    private CheckBox rememberPwd,autoLogin;
    private Context ctx;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ctx=this;
        sp=getSharedPreferences("config",MODE_PRIVATE);
        initWidget();
        imageClick();
        toRegisterClick();
        login();

    }

    /**
     * 输入框状态选择切换
     */
    private void imageClick() {
        image_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(ctx, "被点了");
                if (isSelected_username) {
                    image_username.setSelected(false);
                    isSelected_username = false;

                } else {
                    image_username.setSelected(true);
                    isSelected_username = true;

                }
            }
        });


        image_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(ctx, "被点了");
                if (isDisplay_pwd) {
                    image_pwd.setSelected(false);
                    isDisplay_pwd = false;
                    et_Pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    // 使光标始终在最后位置
                    Editable etable = et_Pwd.getText();
                    Selection.setSelection(etable, etable.length());
                } else {
                    image_pwd.setSelected(true);
                    isDisplay_pwd = true;
                    et_Pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    // 使光标始终在最后位置
                    Editable etable = et_Pwd.getText();
                    Selection.setSelection(etable, etable.length());
                }
            }
        });
        image_pwd.setSelected(true);
        et_Pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    private void initWidget() {
        et_userName= (EditText) findViewById(R.id.et_username);
        et_Pwd= (EditText) findViewById(R.id.et_pwd);
        btn_login=(Button)findViewById(R.id.btn_login);
        noAccount=(TextView)findViewById(R.id.noAccount);
        forgetPwd= (TextView) findViewById(R.id.forgetPwd);
        rememberPwd= (CheckBox) findViewById(R.id.rememuberPwd);
        autoLogin= (CheckBox) findViewById(R.id.rememuberPwd);
        image_username= (ImageView) findViewById(R.id.img_longin_username);
        image_pwd = (ImageView) findViewById(R.id.image_pwd);

        //自动填充密码
        if(rememberPwd.isChecked()){
            et_userName.setText(sp.getString(Constant.USERNAME, ""));
            et_Pwd.setText(sp.getString(Constant.PASSWORD, ""));
        }
    }

    /**
     * 登录
     */
    private void login() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = et_userName.getText().toString();
                String pwd = et_Pwd.getText().toString();
                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(pwd)) {
                    ToastUtils.showToast(ctx, "用户名或密码不能为空");
                    return;
                }

                  //TODO-异步网络请求进行登陆逻辑

                //TODO-假如密码验证成功，则登录
                boolean pwdIsTrue = true;
                if(pwdIsTrue){
                    //记录登陆状态
                    sp.edit().putBoolean(Constant.ISLOGINED,true).commit();
                    //检查是否设置过vcode，如果设置跳进验证界面，如果没设置则进行设置
                   if(sp.getBoolean(Constant.ISSETSUCCESS,false)){
                       Intent intent = new Intent(ctx,VcodeVerifyActivity.class);
                       startActivity(intent);
                       finish();
                   }else{
                    Intent intent = new Intent(ctx,VcodeSetActivity.class);
                    startActivity(intent);
                       finish();
                   }
                }else{
                    ToastUtils.showToast(ctx,"用户名或密码错误");
                }

            }
        });
    }

    /**
     * 到注册页面
     */
    private void toRegisterClick() {
        noAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ctx,PhoneRegistActivity.class);
                startActivity(intent);
            }
        });
    }
}
