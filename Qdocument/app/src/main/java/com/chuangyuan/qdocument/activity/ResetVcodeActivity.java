package com.chuangyuan.qdocument.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chuangyuan.qdocument.R;

public class ResetVcodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_vcode);

        Button btn= (Button) findViewById(R.id.toMain);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ResetVcodeActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
