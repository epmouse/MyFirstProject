package com.chuangyuan.qdocument.activity;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chuangyuan.qdocument.R;
import com.chuangyuan.qdocument.utils.Constant;
import com.chuangyuan.qdocument.utils.FolderUtils;
import com.chuangyuan.qdocument.utils.LogUtils;
import com.chuangyuan.qdocument.utils.PhotoUtils;
import com.chuangyuan.qdocument.utils.ToastUtils;
import com.chuangyuan.qdocument.view.CircleImageView;

/**
 * 验证Vcode码的页面
 */
public class VcodeVerifyActivity extends AppCompatActivity {
    private EditText et_vcode;
    private Button btn_deblock;
    private Context ctx;
    private SharedPreferences sp;
    private TextView tv_forgetVcode;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vcode_verify);

        //接收系统分享功能打开后传的值
        Intent intent = getIntent();
        String type = intent.getType();
        LogUtils.logInfoStar(type+"文件类型");
        ClipData clipData = intent.getClipData();
        if(clipData!=null){
           String clipStr= clipData.toString();
            LogUtils.logInfoStar(clipStr.toString());
            String realUri = null;
            if("image/*".equalsIgnoreCase(type)){
            realUri= clipStr.substring(clipStr.indexOf("content"), clipStr.indexOf("} }"));
            }else if("text/plain".equalsIgnoreCase(type)){
            realUri = clipStr.substring(clipStr.indexOf("file"), clipStr.indexOf("} }"));
            }
            Uri uri = Uri.parse(realUri);
            String path = FolderUtils.uri2Path(this, uri);
            LogUtils.logInfoStar(path + "返回的路径");
            String fileName = path.substring(path.lastIndexOf("/"));
            LogUtils.logInfoStar(fileName+"文件名");
            FolderUtils.copyFile(path, Constant.BASEPATH + "/" + fileName);
            LogUtils.logInfoStar("返回的值" + uri);
        }

//TODO-取别的应用穿过来的值

        ctx=this;
        sp=getSharedPreferences("config",MODE_PRIVATE);
        CircleImageView userPhoto= (CircleImageView) findViewById(R.id.userphoto);
        //初始化的时候查看用户是否设置了头像，有的话直接设置用户头像，否则设置默认头像
        String photoPath = sp.getString(Constant.TAKE_PHOTO_PATH, null);
        if (!TextUtils.isEmpty(photoPath)) {
            Bitmap bitmap = PhotoUtils.compressImage(this,photoPath);
            userPhoto.setImageBitmap(bitmap);
        } else {
            userPhoto.setImageResource(R.drawable.head);
        }

        tv_forgetVcode= (TextView) findViewById(R.id.forgetVcode);
        et_vcode= (EditText) findViewById(R.id.et_vcode);
        btn_deblock= (Button) findViewById(R.id.btn_deblock);

        tv_forgetVcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(VcodeVerifyActivity.this,SetSecurityActivity.class);
                intent.putExtra(Constant.ISRESETVCODE,true);
                startActivity(intent);
                finish();
            }
        });

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
