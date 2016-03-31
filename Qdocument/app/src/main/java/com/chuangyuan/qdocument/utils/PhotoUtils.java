package com.chuangyuan.qdocument.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by Administrator on 2016/3/30 0030.
 */
public class PhotoUtils {
    /**
     * 压缩图片
     * @param photoPath
     */
    public static Bitmap compressImage(Activity act,String photoPath) {
        int screenWidth = act.getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight =act.getWindowManager().getDefaultDisplay().getHeight();
        BitmapFactory.Options opts= new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;//设置为true的目的，拿到图片的一些附属信息
        BitmapFactory.decodeFile(photoPath,opts);
        int imgWidth=opts.outWidth;
        int imgHeight=opts.outHeight;
        opts.inSampleSize=imgWidth/screenWidth>imgHeight/screenHeight?imgWidth/screenWidth:imgHeight/screenHeight;
        opts.inJustDecodeBounds = false;
        Bitmap mImage = BitmapFactory.decodeFile(photoPath, opts);
        return mImage;
    }
    /**
     * 调用拍照功能
     */
    public static void tackPhoto(Activity act,String spKEY,String path,String photoName,int requestCode) {
        SharedPreferences sp= act.getSharedPreferences("config",Context.MODE_PRIVATE);
        Intent mIntent = new Intent();
        mIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        mIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(path, photoName)));
        //直接把路径保存到sp中方便后边调用
        sp.edit().putString(spKEY,path+"/"+photoName).commit();

        act.startActivityForResult(mIntent, requestCode);
    }
}
