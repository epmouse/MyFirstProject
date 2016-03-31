package com.chuangyuan.qdocument.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.chuangyuan.qdocument.R;
import com.chuangyuan.qdocument.utils.Constant;
import com.chuangyuan.qdocument.utils.LogUtils;
import com.chuangyuan.qdocument.utils.PhotoUtils;
import com.chuangyuan.qdocument.utils.ToastUtils;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.FaceRequest;
import com.iflytek.cloud.RequestListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SetSecurityActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences sp;
    @InjectView(R.id.securityICON)
    ImageView image;
    @InjectView(R.id.take_securityIcon)
    Button take_securityIcon;
    @InjectView(R.id.upload_securityIcon)
    Button upload_securityIcon;
    @InjectView(R.id.verify)
    Button verify;
    private Context act;
    private byte[] mImageData;
    private FaceRequest face;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_security);
        act = this;
        //初始化人脸识别（科大讯飞），该功能用户需要时才启动，所以在该类中初始化，不在application初始化。
        SpeechUtility.createUtility(act, SpeechConstant.APPID + "=56fa48cc");
        // 注意：此接口在非主进程调用会返回null对象，如需在非主进程使用语音功能，
        // 请使用参数：SpeechConstant.APPID +"=56fa48cc," + SpeechConstant.FORCE_LOGIN +"=true"。
        LogUtils.logInfoStar("初始化完成");
        face = new FaceRequest(act);

        ButterKnife.inject(this);
        verify.setOnClickListener(this);
        take_securityIcon.setOnClickListener(this);
        upload_securityIcon.setOnClickListener(this);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        image.setImageResource(R.drawable.head);//初始化照片
    }

    private boolean isTakePhoto;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_securityIcon:
                //拍照
                PhotoUtils.tackPhoto(this, Constant.SECURITY_ICON_PATH,
                        Environment.getExternalStorageDirectory().getPath(),
                        Constant.SECURITY_ICONNAME,
                        Constant.OPEN_CAMERA_FOR_SECURITYICON);
                isTakePhoto = true;
                break;
            case R.id.upload_securityIcon:
                if (mImageData != null && isTakePhoto) {
                    // 设置业务类型为注册
                    face.setParameter(SpeechConstant.WFR_SST, "reg");
                    // 设置auth_id根据每台机器的imei号
                    face.setParameter(SpeechConstant.AUTH_ID, "create224");
                    // 调用sendRequest(byte[] img, RequestListener listener)方法发送头像注册请求，img为图片的二进制数据，listener为处理注册结果的回调对象
                    face.sendRequest(mImageData, mRequestListener);
                    isTakePhoto = false;
                } else {
                    ToastUtils.showToast(act, "请先拍照");
                }
                break;
            case R.id.verify:
                if (mImageData != null && isTakePhoto) {
                    // 设置业务类型为验证
                    face.setParameter(SpeechConstant.WFR_SST, "verify");
                    // 设置auth_id
                    face.setParameter(SpeechConstant.AUTH_ID, "create224");
                    // 设置gid，由于一个auth_id下只有一个gid，所以设置了auth_id时则可以不用设置gid。但是当
                    // 没有设置auth_id时，必须设置gid
                    // 调用sendRequest(byte[] img, RequestListener listener)方法发送注册请求，img为图片的二进制数据，listener为处理注册结果的回调对象
                    face.sendRequest(mImageData, mRequestListener);
                    isTakePhoto = false;
                } else {
                    ToastUtils.showToast(act, "请先拍照");
                }
                break;
        }
    }

    /**
     * 联网请求后回调
     */
    RequestListener mRequestListener = new RequestListener() {

        // 获得结果时返回，JSON格式。
        public void onBufferReceived(byte[] buffer) {
            try {
                String result = new String(buffer, "utf-8");
                LogUtils.logInfoStar(result + "已返回结果");
                JSONObject object = new JSONObject(result);
                //optString()会在取不到值时返回空字符串，而getString会抛出异常。
                String type = object.optString("sst");
                if ("reg".equals(type)) {
                    register(object);
                } else if ("verify".equals(type)) {
                    verify(object);
                } else if ("detect".equals(type)) {//留待扩展
                    // detect(object);
                } else if ("align".equals(type)) {//留待扩展
                    // align(object);
                }
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO: handle exception
            }

        }

        // 流程结束时返回，error不为空则表示发生错误。
        public void onCompleted(SpeechError error) {
            if (error != null) {
                switch (error.getErrorCode()) {
                    case ErrorCode.MSP_ERROR_ALREADY_EXIST:
                        ToastUtils.showToast(act, "authid已经被注册，请更换后再试");
                        break;
                    default:
                        ToastUtils.showToast(act, error.getPlainDescription(true));
                        break;
                }
            }
        }

        // 保留接口，扩展用。
        public void onEvent(int eventType, Bundle params) {
        }
    };

    /**
     * 验证结果的提示信息
     *
     * @param obj
     * @throws JSONException
     */
    private void verify(JSONObject obj) throws JSONException {
        int ret = obj.getInt("ret");
        if (ret != 0) {
            ToastUtils.showToast(this, "验证失败");
            return;
        }
        if ("success".equals(obj.get("rst"))) {
            if (obj.getBoolean("verf")) {
                ToastUtils.showToast(this, "通过验证，欢迎回来");
                image.setImageResource(R.drawable.head);//成功后恢复默认头像。
            } else {
                ToastUtils.showToast(this, "验证不通过");
            }
        } else {
            ToastUtils.showToast(this, "验证失败");
        }
    }

    /**
     * 注册结果的提示信息
     *
     * @param obj
     * @throws JSONException
     */
    private void register(JSONObject obj) throws JSONException {
        int ret = obj.getInt("ret");
        if (ret != 0) {
            ToastUtils.showToast(this, "注册失败");
            return;
        }
        if ("success".equals(obj.get("rst"))) {
            ToastUtils.showToast(this, "注册成功");
            image.setImageResource(R.drawable.head);//恢复默认头像
        } else {
            ToastUtils.showToast(this, "注册失败");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.OPEN_CAMERA_FOR_SECURITYICON) {
            Bitmap bitmap = PhotoUtils.compressImage(this, sp.getString(Constant.SECURITY_ICON_PATH, ""));
            if (bitmap != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                //可根据流量及网络状况对图片进行压缩，100-->0  100表最高质量
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                mImageData = baos.toByteArray();
                image.setImageBitmap(bitmap);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
