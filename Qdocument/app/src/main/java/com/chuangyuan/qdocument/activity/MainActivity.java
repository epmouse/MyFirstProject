package com.chuangyuan.qdocument.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;

import com.chuangyuan.qdocument.R;
import com.chuangyuan.qdocument.fragment.LeftFragment;
import com.chuangyuan.qdocument.fragment.RightFragment;
import com.chuangyuan.qdocument.pager.BasePager;
import com.chuangyuan.qdocument.pager.FirstPager;
import com.chuangyuan.qdocument.pager.SecondPager;
import com.chuangyuan.qdocument.pager.ThirdPager;
import com.chuangyuan.qdocument.utils.Constant;
import com.chuangyuan.qdocument.utils.Dp2pxUtils;
import com.chuangyuan.qdocument.utils.FolderUtils;
import com.chuangyuan.qdocument.utils.LogUtils;
import com.chuangyuan.qdocument.utils.ToastUtils;
import com.chuangyuan.qdocument.view.CircleImageView;
import com.chuangyuan.qdocument.view.MyViewPager;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActionBar actionbar;
    private DrawerLayout mDrawerLayout;
    private SlidingPaneLayout mPaneLayout;
    public View mCustomView;
    private PopupWindow popupWindow;
    private View popview;
    private ActionBar.LayoutParams mP;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* //使用slidingPaneLayout
        setContentView(R.layout.activity_main_slidindpane);*/
        //使用drawerLayout
        setContentView(R.layout.activity_main_drawerlayout);
        ctx = this;
        sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        //初始化actionBar
        initActionBar();

        //初始化drawerlayout实现侧滑
        initDrawerLayout();

        /*//初始化slidingPaneLayout实现侧滑
        initSlidingPaneLayout();*/

        initPopupwindow();
    }

    /**
     * 初始化popupwindow
     */
    private void initPopupwindow() {

        popupWindow = new PopupWindow(popview, 200,
                300);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popwindow_bg));


    }

    /**
     * 抽屉在下面 SlidingPaneLayout
     */
    private void initSlidingPaneLayout() {
        //抽屉在下面 SlidingPaneLayout
        mPaneLayout = (SlidingPaneLayout) findViewById(R.id.slidingpanellayout);
        Fragment leftfragment = new LeftFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.left, leftfragment).commit();
        Fragment rightfragment = new RightFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.right, rightfragment).commit();
    }

    /**
     * 初始化actionBar
     */
    private void initActionBar() {
        actionbar = getSupportActionBar();
        actionbar.setDisplayUseLogoEnabled(false);//显示应用图标
//        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setDisplayShowCustomEnabled(true);//自定义图标的显示
        actionbar.setDisplayShowHomeEnabled(false);//activity图标的显示
        actionbar.setDisplayShowTitleEnabled(false);//应用名称的显示
        mCustomView = getLayoutInflater().inflate(R.layout.actionbar_custom_layout, null);
        actionbar.setCustomView(mCustomView);
        //下面这句跟这句相同的作用actionbar.setDisplayShowCustomEnabled(true);//自定义图标的显示
        //actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//      actionbar.setCustomView(mCustomView, new
//               ActionBar.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT));
        //一下三行代码使mCustomview居中显示。
        //mP = (ActionBar.LayoutParams) mCustomView.getLayoutParams();
//        mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK | Gravity.CENTER_HORIZONTAL;
//        actionbar.setCustomView(mCustomView, mP);


    }


    private RadioGroup radioGroup;
    private MyViewPager viewPager;
    private ArrayList<BasePager> pagerList;
    private Context ctx;

    /**
     * 初始化策划菜单
     */
    private void initDrawerLayout() {
        //抽屉在上面 DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        final CircleImageView circleImageView = (CircleImageView) mCustomView.findViewById(R.id.circleImageview);
        //点击控制drawerlayout的开合。
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断drawerlayout的开合状态
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.closeDrawers();
                    // StarAnimation(-0.5f, 0, 0, 0);//在点击的时候就开始做动画，效果比较好，但滑动时无法跟随。
                } else {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                    //StarAnimation(0, -0.5f, 0, 0);//在点击的时候就开始做动画，效果比较好，但滑动时无法跟随。
                }
            }
        });


        Fragment leftfragment = new LeftFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!leftfragment.isAdded()) {
            transaction.add(R.id.left, leftfragment).commit();
        }
        viewPager = (MyViewPager) findViewById(R.id.viewpager);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        initPager();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.first:
                        viewPager.setCurrentItem(0);
                        //让actionbar显示
                        //MainActivity.this.getSupportActionBar().show();
                        break;
                    case R.id.second:
                        viewPager.setCurrentItem(1);
                        //让actionbar隐藏
                        //MainActivity.this.getSupportActionBar().hide();
                        break;
                    case R.id.third:
                        viewPager.setCurrentItem(2);
                        break;
                }
            }
        });
        radioGroup.check(R.id.first);
        adapter = new MyPagerAdapter();
        viewPager.setAdapter(adapter);
    }

    /**
     * 开启一个平移动画
     */
    private void StarAnimation(float xStar, float xEnd, float yStar, float yEnd) {
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, xStar,
                Animation.RELATIVE_TO_SELF, xEnd, Animation.RELATIVE_TO_SELF, yStar,
                Animation.RELATIVE_TO_SELF, yEnd);
        animation.setDuration(300);
        animation.setFillAfter(true);
        mCustomView.startAnimation(animation);
    }

    private void initPager() {
        pagerList = new ArrayList<>();
        pagerList.add(new FirstPager(ctx));
        pagerList.add(new SecondPager(ctx));
        pagerList.add(new ThirdPager(ctx));
    }

    private PagerAdapter adapter;

    class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return pagerList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = pagerList.get(position).initView();
            container.addView(view);
            return view;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                ToastUtils.showToast(this, "搜索");
                break;
            case R.id.action_settings:
                popview = View.inflate(ctx, R.layout.popwindow_layout, null);
                AnimationSet animationSet = new AnimationSet(false);
                AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 1.0f);
                alphaAnimation.setDuration(300);
                ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f);
                scaleAnimation.setDuration(300);
                animationSet.addAnimation(alphaAnimation);
                animationSet.addAnimation(scaleAnimation);
                popview.startAnimation(animationSet);

                initPopWindowListview();
                int width = Dp2pxUtils.Dp2Px(ctx, 150f);
                //int heigth=Dp2pxUtils.Dp2Px(ctx,200f);
                //popupWindow = new PopupWindow(popview, width, heigth);
                popupWindow = new PopupWindow(popview, width, LinearLayout.LayoutParams.WRAP_CONTENT);

                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                View parent = View.inflate(ctx, R.layout.activity_main_drawerlayout, null);
                Rect frame = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                int x = Dp2pxUtils.Dp2Px(ctx, 20f);
                popupWindow.showAtLocation(parent, Gravity.TOP + Gravity.RIGHT, x, frame.top + actionbar.getHeight());
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);
                popupWindow.update();


                break;
        }
        return true;
    }

    /**
     * 控制popwindow的布局显示
     */
    private void initPopWindowListview() {
        ListView listview = (ListView) popview.findViewById(R.id.popwindow_listview);
        String[] strArray = new String[]{"创建文件夹", "浏览"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_list_item_1, strArray);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //弹出对话输入框，输入文件名，输入成功后点击确定创建文件夹
                        View dialogView = View.inflate(ctx, R.layout.createfolder_dialog_layout, null);
                        showMyDialog(dialogView, "创建文件夹", "创建", "取消");
                        popupWindow.dismiss();
                        break;
                    case 1:
                        FolderUtils.showFileChooser(MainActivity.this);
                        popupWindow.dismiss();
                        break;

                }
            }
        });
        listview.setAdapter(adapter);

    }

    /**
     * 弹出自定义对话框--创建文件夹
     */
    private void showMyDialog(View dialogView, String title, String positive, String negative) {

        final EditText et_fileName = (EditText) dialogView.findViewById(R.id.et_folderName);
        Dialog alertDialog = new AlertDialog.Builder(this).
                setTitle(title).
                setIcon(R.mipmap.app_icon).
                setView(dialogView).
                setPositiveButton(positive, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String fileName = et_fileName.getText().toString();
                        //TODO-在此处添加打开的文件夹的路径

                        String currentPath = sp.getString(Constant.currFolderPath, null);
                        LogUtils.logInfoStar(currentPath + "当前所在路径");
                        if (currentPath != null) {
                            if (currentPath.equals(Constant.BASEPATH)) {
                                FolderUtils.createFolder(ctx, "/" + fileName);

                                if (folderChangedListener != null) {
                                    folderChangedListener.onFolderChanged("/"+fileName);
                                }
                            } else {
                                String currFolderName = currentPath.substring(currentPath.lastIndexOf("/"));
                                LogUtils.logInfoStar(currFolderName + "当前文件夹名字");
                                FolderUtils.createFolder(ctx, currFolderName + "/" + fileName);
                                if (folderChangedListener != null) {
                                    folderChangedListener.onFolderChanged(currFolderName + "/" + fileName);
                                }
                            }
                        }
//                        if (folderChangedListener != null) {
//                            folderChangedListener.onFolderChanged(fileName);
//                        }
                    }
                }).
                setNegativeButton(negative, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                }).
                create();
        alertDialog.show();


    }

    //文件夹状态改变的监听回调
    private FolderChangedListener folderChangedListener;

    public void setOnFolderChangedListener(FolderChangedListener folderChangedListener) {
        this.folderChangedListener = folderChangedListener;
    }

    public interface FolderChangedListener {
        void onFolderChanged(String fileName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constant.FILE_SELECT_CODE:
                // TODO Auto-generated method stub
                if (resultCode == Activity.RESULT_OK) {
                    // Get the Uri of the selected file

                    Uri uri = data.getData();
                    String filePath = FolderUtils.uri2Path(ctx, uri);
                    if(filePath==null){
                        ToastUtils.showToast(this,"请从系统图库中选择");
                        return;
                    }
                    File file = new File(filePath);
                    String  currFolderPath= sp.getString(Constant.currFolderPath, null);
                    if(currFolderPath.equals(Constant.BASEPATH)){
                    FolderUtils.copyFile(filePath, Constant.BASEPATH + "/" + file.getName());//复制文件到app
                    }else{
                    String currFolder = currFolderPath.substring(currFolderPath.lastIndexOf("/"));
                    FolderUtils.copyFile(filePath, Constant.BASEPATH + currFolder+"/" + file.getName());//复制文件到app
                    }
                    if (folderChangedListener != null) {
                        folderChangedListener.onFolderChanged(file.getName());
                    }
                    LogUtils.logInfoStar(uri + "---------------...>>>>>>" + filePath + "=+=====");
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


}
