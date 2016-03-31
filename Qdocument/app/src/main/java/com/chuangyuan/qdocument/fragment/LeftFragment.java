package com.chuangyuan.qdocument.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chuangyuan.qdocument.R;
import com.chuangyuan.qdocument.activity.MainActivity;
import com.chuangyuan.qdocument.activity.SetSecurityActivity;
import com.chuangyuan.qdocument.utils.Constant;
import com.chuangyuan.qdocument.utils.FolderUtils;
import com.chuangyuan.qdocument.utils.LogUtils;
import com.chuangyuan.qdocument.utils.PhotoUtils;
import com.chuangyuan.qdocument.utils.ToastUtils;
import com.chuangyuan.qdocument.view.CircleImageView;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LeftFragment extends Fragment {
    private SharedPreferences sp;
    @InjectView(R.id.night)
    TextView tv_night;
    @InjectView(R.id.set)
    TextView tv_set;
    @InjectView(R.id.drawerView)
    LinearLayout drawerView;
    @InjectView(R.id.slid_list)
    ListView slid_list;
    @InjectView(R.id.userphoto)
    CircleImageView userPhoto;
    private View parent;
    private View leftFragmentView;
    private MainActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtils.logInfoStar("oncreateview执行了");
        sp = getActivity().getSharedPreferences("config", getActivity().MODE_PRIVATE);
        leftFragmentView = View.inflate(getActivity(), R.layout.left_fragment_layout, null);
        ButterKnife.inject(this, leftFragmentView);
        activity = (MainActivity) getActivity();


        //初始化的时候查看用户是否设置了头像，有的话直接设置用户头像，否则设置默认头像
        String photoPath = sp.getString(Constant.TAKE_PHOTO_PATH, null);
        if (!TextUtils.isEmpty(photoPath)) {
            Bitmap bitmap = PhotoUtils.compressImage(getActivity(), photoPath);
            userPhoto.setImageBitmap(bitmap);
        } else {
            userPhoto.setImageResource(R.drawable.head);
        }

        setDrawerLayoutListener();//给drawerlayout设置打开和关闭的监听。
        slid_list_show();//侧滑菜单listview的相关处理
        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomPop(leftFragmentView);
            }
        });

        return leftFragmentView;
    }

    /**
     * 从底部弹出popupwindow
     */
    private void showBottomPop(View parent) {
        final View popView = View.inflate(getActivity(), R.layout.bottom_pop_layout, null);
        showAnimation(popView);//开启动画
        PopupWindow mPopWindow = new PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        clickPopItem(popView, mPopWindow);//条目的点击
        mPopWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopWindow.showAtLocation(parent,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        mPopWindow.setOutsideTouchable(true);
        mPopWindow.setFocusable(true);
        mPopWindow.update();
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.7f;
        getActivity().getWindow().setAttributes(lp);
        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
    }

    /**
     * geipopupwindow添加动画
     *
     * @param popView
     */
    private void showAnimation(View popView) {
        AnimationSet animationSet = new AnimationSet(false);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1.0f);
        alphaAnimation.setDuration(300);
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f
        );
        translateAnimation.setDuration(300);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(translateAnimation);
        popView.startAnimation(animationSet);
    }

    /**
     * popupWindow的条目点击逻辑
     *
     * @param popView
     * @param mPopWindow
     */
    private void clickPopItem(View popView, final PopupWindow mPopWindow) {

        TextView takePhoto = (TextView) popView.findViewById(R.id.takePhoto);
        TextView openPhotos = (TextView) popView.findViewById(R.id.openPhotos);
        TextView cancel = (TextView) popView.findViewById(R.id.cancel);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这里不能调用photoutils中的相机方法，因为那里面启动相机使用activity实现，所以回掉的，onActivityResult（）
                //方法也是activity中的，而此处我们的毁掉逻辑是在fragment中的，onActivityResult（）方法中处理，所以不能用。
                //记录图片的路径
                //TODO-由于每次拍照的路径都是一样的，所以照片会被覆盖，所以即便设置一个prephotopath取出来还是同一张照片，待解决。
//                String prePhotoPath = sp.getString(Constant.TAKE_PHOTO_PATH, "");
//                sp.edit().putString(Constant.PREPHOTOPATH, prePhotoPath).commit();
//                PhotoUtils.tackPhoto(activity, Constant.TAKE_PHOTO_PATH,
//                        Environment.getExternalStorageDirectory().getPath(),
//                        Constant.PHOTONAME,
//                        Constant.TAKEPHOTO_CODE);

////                //调用相机逻辑
                Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //下面这句指定调用相机拍照后的照片存储的路径
                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory(), Constant.PHOTONAME)));
                //记录图片的路径
                String prePhotoPath = sp.getString(Constant.TAKE_PHOTO_PATH, "");
                //TODO-由于每次拍照的路径都是一样的，所以照片会被覆盖，所以即便设置一个prephotopath取出来还是同一张照片，待解决。
                sp.edit().putString(Constant.PREPHOTOPATH, prePhotoPath).commit();

                sp.edit().putString(Constant.TAKE_PHOTO_PATH, Environment.getExternalStorageDirectory() + "/" + Constant.PHOTONAME).commit();
                startActivityForResult(takeIntent, Constant.TAKEPHOTO_CODE);
                mPopWindow.dismiss();
            }
        });
        openPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用系统相册功能逻辑

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/jpeg");
                startActivityForResult(intent, Constant.SELECT_PIC);
                mPopWindow.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        View dialogView = View.inflate(getActivity(), R.layout.show_photo_dialog_layout, null);
        ImageView iv_userIcon = (ImageView) dialogView.findViewById(R.id.iv_userIcon);
        showMyDialog(dialogView, "头像设置", "设置", "取消");
        switch (requestCode) {
            case Constant.TAKEPHOTO_CODE:
                iv_userIcon.setImageBitmap(BitmapFactory.decodeFile(sp.getString(Constant.TAKE_PHOTO_PATH, "")));
                break;
            case Constant.SELECT_PIC:
                if (data == null) {
                    return;
                }
                Uri uri = data.getData();
                iv_userIcon.setImageURI(uri);//直接使用setImageURI即可。//TODO-有空余时间可做此优化
                String path = FolderUtils.uri2Path(getActivity(), uri);
                LogUtils.logInfoStar(path + "路径");
                //这两句由于使用的是一个图片地址，所以没作用
                String prePhotoPath = sp.getString(Constant.TAKE_PHOTO_PATH, "");
                sp.edit().putString(Constant.PREPHOTOPATH, prePhotoPath).commit();
                sp.edit().putString(Constant.TAKE_PHOTO_PATH, path).commit();
               //TODO-下面这行可用 setImageURI（uri）代替，节省代码。
               // iv_userIcon.setImageBitmap(BitmapFactory.decodeFile(path));

                break;
        }
        //TODO-此处完成图片上传到服务器的逻辑
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 弹出自定义对话框--创建文件夹
     */
    private void showMyDialog(View dialogView, String title, String positive, String negative) {

        final EditText et_fileName = (EditText) dialogView.findViewById(R.id.et_folderName);
        Dialog alertDialog = new AlertDialog.Builder(getActivity()).
                setTitle(title).
                setIcon(R.mipmap.app_icon).
                setView(dialogView).
                setPositiveButton(positive, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newPhotoPath = sp.getString(Constant.TAKE_PHOTO_PATH, null);
                        Bitmap bitmap = PhotoUtils.compressImage(getActivity(), newPhotoPath);
                        userPhoto.setImageBitmap(bitmap);
                    }
                }).
                setNegativeButton(negative, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String prePhotoPath = sp.getString(Constant.PREPHOTOPATH, "");
                        Bitmap bitmap = PhotoUtils.compressImage(getActivity(), prePhotoPath);
                        userPhoto.setImageBitmap(bitmap);

                    }
                }).
                create();
        alertDialog.show();


    }

    /**
     * 侧滑菜单listview的相关处理
     */
    private void slid_list_show() {
        final String[] slid_item = new String[]{"设置安全头像", "闪电互传", "退出"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                0, slid_item) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv = new TextView(getActivity());
                tv.setTextColor(Color.WHITE);
                tv.setTextSize(18);
                tv.setText(slid_item[position]);
                tv.setPadding(0, 10, 0, 10);
                return tv;
            }
        };

        slid_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0://设置安全头像
                        Intent intent = new Intent(getActivity(), SetSecurityActivity.class);
                        intent.putExtra(Constant.ISRESETVCODE,false);//不是从重置vcode处跳转。
                        startActivity(intent);
                        break;
                    case 1:
                        break;
                    case 2:
                        break;

                }
            }
        });


        slid_list.setAdapter(adapter);
    }

    /**
     * drawerLayout的监听事件
     */
    private void setDrawerLayoutListener() {
        //drawerlayout的布局在activity中，所以要通过activity才能获取到它
        DrawerLayout parent = (DrawerLayout) getActivity().findViewById(R.id.drawerlayout);
        parent.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                LogUtils.logInfoStar(slideOffset + "");
                //slideOffset的值为偏移的百分比。
                //  mP.setMargins((int)(width*slideOffset),5,5,
                //  ActionBar.
                //以下代码可实现mcustomview随着drawerlayout的滑动而等比例移动。
                activity.mCustomView.scrollTo((int) (20 * slideOffset), 0);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                drawerView.setClickable(true);//让侧边栏获取点击焦点，不然点击事件会直接传到下面被覆盖的布局中。
                //StarAnimation(0, -0.5f, 0, 0);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // StarAnimation(-0.5f, 0, 0, 0);
            }

            private void StarAnimation(float xStar, float xEnd, float yStar, float yEnd) {
                TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, xStar,
                        Animation.RELATIVE_TO_SELF, xEnd, Animation.RELATIVE_TO_SELF, yStar,
                        Animation.RELATIVE_TO_SELF, yEnd);
                animation.setDuration(50);
                animation.setFillAfter(true);
                activity.mCustomView.startAnimation(animation);
            }
        });
    }

}
