package com.chuangyuan.qdocument.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.chuangyuan.qdocument.R;

public class EmailRegistActivity extends AppCompatActivity {
    private TextView toPhoneRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_regist);
        TextView tv= (TextView) findViewById(R.id.title_text);
        tv.setText("邮箱注册");

        toPhoneRegister= (TextView) findViewById(R.id.toPhoneRegister);
        toPhoneRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmailRegistActivity.this,PhoneRegistActivity.class);
                startActivity(intent);
            }
        });
    }
}
