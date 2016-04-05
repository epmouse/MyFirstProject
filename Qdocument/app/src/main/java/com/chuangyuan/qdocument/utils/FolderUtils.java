package com.chuangyuan.qdocument.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/21 0021.
 */
public class FolderUtils {


    /** 调用文件选择软件来选择文件 **/
    public static void showFileChooser(Activity mActivity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            mActivity.startActivityForResult(Intent.createChooser(intent, "请选择一个要上传的文件"),
                    Constant.FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(mActivity, "请安装文件管理器", Toast.LENGTH_SHORT)
                    .show();
        }


    }

    /**
     * 创建文件夹
     * @param fileName
     */
    public static void createFolder(Context context,String fileName){
        //判断是否存在sd卡
        if(!Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED)){
            return;
        }
     String basePath = Environment.getExternalStorageDirectory().getPath()+"/ChuangYuan";
       //TODO-启动的时候加载到第一页上 showFileList(basePath);

        File destDir = new File(basePath+fileName);
        if (!destDir.exists()) {
            destDir.mkdirs();
            ToastUtils.showToast(context, "创建成功");
        }
    }



    /**
     * 显示文件夹下所有文件和目录
     * @param path
     */
    public static void showFileList(String path,ArrayList<String> names,ArrayList<String> paths) {
//        File file=new File(path);
//        File[] files = file.listFiles();
//        for(File f:files){
//            LogUtils.logInfoStar(f.getName() + "\n");
//        }
        String basePath = Environment.getExternalStorageDirectory().getPath()+"/ChuangYuan";
        File f = new File(path);
        File[] files = f.listFiles();// 列出所有文件
        // 如果不是根目录,则列出返回根目录和上一目录选项
        if (!path.equals(basePath)) {
            names.add("返回根目录");
            paths.add(basePath);
            names.add("返回上一层目录");
            paths.add(f.getParent());
        }
        // 将所有文件存入list中
       if (files != null) {
            int count = files.length;// 文件个数
            for (int i = 0; i < count; i++) {
                File file = files[i];
                names.add(file.getName());
                paths.add(file.getPath());

            }
        }
    }


    /**
         * Try to return the absolute file path from the given Uri
         *
         * @param context
         * @param uri
         * @return the file path or null
         */
        public static String uri2Path( final Context context,final Uri uri ) {
            if ( null == uri ) return null;
            final String scheme = uri.getScheme();
            String data = null;
            if ( scheme == null )
                data = uri.getPath();
            else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
                data = uri.getPath();
            } else if ( ContentResolver.SCHEME_CONTENT.equals(scheme) ) {
                Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
                if ( null != cursor ) {
                    if ( cursor.moveToFirst() ) {
                        int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                        if(index==-1){
                        index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        }
                        if ( index > -1 ) {
                            data = cursor.getString( index );
                        }
                    }
                    cursor.close();
                }
            }
            return data;
        }


    /**
     * 获取文件名
     * @param pathandname
     * @return
     */
    public static String getFileName(String pathandname){

        int start=pathandname.lastIndexOf("/");
        int end=pathandname.lastIndexOf(".");
        if(start!=-1 && end!=-1){
            return pathandname.substring(start+1,end);
        }else{
            return null;
        }

    }
    /**
     * 获取文件夹名
     * @param pathandname
     * @return
     */
    public static String getFolderName(String pathandname){

        int start=pathandname.lastIndexOf("/");
        int end=pathandname.lastIndexOf(".");
        if(start!=-1 && end!=-1){
            String substring = pathandname.substring(start + 1, end);
            return getFileName(substring);
        }else{
            return null;
        }

    }


    /**
     * 复制单个文件
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     *
     */
//    public static void copyFile(String oldPath, String newPath) {
//        try {
////            int bytesum = 0;
////            int byteread = 0;
//            File oldfile = new File(oldPath);
//            LogUtils.logInfoStar("流上面");
//            if (oldfile.exists()) { //文件存在时
//                LogUtils.logInfoStar("文件存在");
//                InputStream inStream = new FileInputStream(oldPath); //读入原文件
//                FileOutputStream fs = new FileOutputStream(newPath);
//                byte[] buffer = new byte[1444];
//                int length;
//                while ( (length = inStream.read(buffer)) != -1) {
//                    //bytesum += byteread; //字节数 文件大小
//                    fs.write(buffer, 0, length);
//                    LogUtils.logInfoStar("复制陈功--bytesum");
//                }
//                inStream.close();
//            }
//        }
//        catch (Exception e) {
//            LogUtils.logInfoStar("复制出错");
//            e.printStackTrace();
//
//        }
//
//    }





    /**
     * copy file
     *
     * @param sourceFilePath    资源路径
     * @param destFilePath  删除的文件
     * @throws RuntimeException if an error occurs while operator
     * FileOutputStream
     * @return  返回是否成功
     */
    public static boolean copyFile(String sourceFilePath, String destFilePath) {

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(sourceFilePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        }
        return writeFile(destFilePath, inputStream);
    }

    /**
     * write file, the bytes will be written to the begin of the file
     *
     * @param filePath   路径
     * @param stream  输入流
     * @return 返回是否写入成功
     */
    public static boolean writeFile(String filePath, InputStream stream) {
        return writeFile(filePath, stream, false);

    }
    /**
     * write file
     *
     * @param filePath 路径
     * @param stream the input stream
     * @param append if <code>true</code>, then bytes will be written to the
     * end
     * of the file rather than the beginning
     * @return return true
     * FileOutputStream
     */
    public static boolean writeFile(String filePath, InputStream stream, boolean append) {

        return writeFile(filePath != null ? new File(filePath) : null, stream,
                append);
    }

    /**
     * write file
     *
     * @param file the file to be opened for writing.
     * @param stream the input stream
     * @param append if <code>true</code>, then bytes will be written to the
     * end
     * of the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator
     * FileOutputStream
     */
    public static boolean writeFile(File file, InputStream stream, boolean append) {
        OutputStream o = null;
        try {
            makeDirs(file.getAbsolutePath());
            o = new FileOutputStream(file, append);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
            }
            o.flush();
            return true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            try {
                o.close();
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    /**
     *
     * @param filePath 路径
     * @return 是否创建成功
     */
    public static boolean makeDirs(String filePath) {

        String folderName = getFolderName(filePath);
        if (TextUtils.isEmpty(folderName)) {
            return false;
        }

        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory())
                ? true
                : folder.mkdirs();
    }


    /**
     * 递归删除文件和文件夹
     *
     * @param file
     *            要删除的根目录
     */
    public static void DeleteFile(File file) {
       if(!file.exists()){
           return;
       }
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    DeleteFile(f);
                }
                file.delete();
            }
        }


}
