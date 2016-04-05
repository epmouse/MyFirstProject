package com.chuangyuan.qdocument.utils;

import android.os.Environment;

/**
 * Created by Administrator on 2016/3/16 0016.
 */
public class Constant {
    /**
     * 记录有没登陆过
     */
    public final static String ISLOGINED="isLogined";
    /**
     * 记录用户名
     */
    public static final String USERNAME = "username";
    /**
     * 记录密码
     */
    public static final String PASSWORD = "password";
    /**
     * 记录vcode设置是否成功
     */
    public static final String ISSETSUCCESS = "isSetSuccess";
    /**
     * 文件选择器的 requestCode
      */
    public static final int FILE_SELECT_CODE =10 ;



    public static final String BASEPATH = Environment.getExternalStorageDirectory().getPath()+"/ChuangYuan";

    /**
     * 打开相机的requestCode
     */
    public static final int TAKEPHOTO_CODE = 11 ;
    public static final String PHOTONAME = "head";
    /**
     * 用户调用系统相机拍照后照片的储存路径。
     */
    public static final String TAKE_PHOTO_PATH = "take_photoPath";
    /**
     * 打开相册的requestCode
     */
    public static final int SELECT_PIC =18 ;
    /**
     * 上一次的头像图片路径
     */
    public static final String PREPHOTOPATH = "prePhotoPath" ;
    /**
     * 打开摄像头拍摄安全头像requestCode
     */
    public static final int OPEN_CAMERA_FOR_SECURITYICON = 12;
    /**
     * 安全头像拍完后保存的位置。
     */
    public static final String SECURITY_ICON_PATH = "security_icon_path";
    /**
     * 安全头像名称
     */
    public static final String SECURITY_ICONNAME = "securityIcon";
    /**
     * 表示是要修改vcode
     */
    public static final String ISRESETVCODE = "isResetVcode";
    /**
     * 记录当前打开的文件夹路径
     */
    public static String currFolderPath="currentFolderPath";
}
