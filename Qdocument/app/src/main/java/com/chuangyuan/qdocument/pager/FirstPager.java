package com.chuangyuan.qdocument.pager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.FileObserver;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chuangyuan.qdocument.R;
import com.chuangyuan.qdocument.activity.MainActivity;
import com.chuangyuan.qdocument.utils.Constant;
import com.chuangyuan.qdocument.utils.Dp2pxUtils;
import com.chuangyuan.qdocument.utils.FolderUtils;
import com.chuangyuan.qdocument.utils.LogUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/15 0015.
 */
public class FirstPager extends BasePager {

    private ListView listview;
    private ArrayList<String> names;
    private ArrayList<String> paths;

    public FirstPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.firstpager_layout, null);
        listview = (ListView) view.findViewById(R.id.listview);

        names = new ArrayList<>();
        paths = new ArrayList<>();
        FolderUtils.showFileList(Constant.BASEPATH, names, paths);
        ListviewClick();//listview的条目点击事件（长按+短按）

        adapter = new MyAdapter();
        listview.setAdapter(adapter);
        showNemFolder();//刷新显示刚添加的文件夹
        return view;
    }

    /**
     * 刷新显示刚添加的文件夹
     */
    private void showNemFolder() {
        MainActivity act = (MainActivity) context;
        act.setOnFolderChangedListener(new MainActivity.FolderChangedListener() {
            @Override
            public void onFolderChanged(String fileName) {
                names.add(fileName);
                paths.add(Constant.BASEPATH+"/" + fileName);
                adapter.notifyDataSetChanged();
                listview.smoothScrollToPosition(listview.getCount() - 1);//移动到尾部
            }
        });
    }

    /**
     * listview的点击事件
     */
    private void ListviewClick() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String path = paths.get(position);

                File file = new File(path);
                //如果是文件夹就继续分解
                if (file.isDirectory()) {
                    names = new ArrayList<>();
                    paths = new ArrayList<>();
                    FolderUtils.showFileList(path, names, paths);
                    adapter.notifyDataSetChanged();
                } else {
                    new AlertDialog.Builder(context).setTitle("提示")
                            .setMessage(file.getName() + " 是一个文件！")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {

                                }

                            }).show();
                }
            }

        });


        //长按事件
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                View popview = View.inflate(context, R.layout.folder_popwindow_layout, null);
                TextView tv_del = (TextView) popview.findViewById(R.id.tv_del);


                //PopupWindow popupWindow = new PopupWindow(popview, -2,-2);
                final PopupWindow popupWindow = new PopupWindow(popview, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                if (popupWindow!=null&&popupWindow.isShowing()) {
//                    popupWindow.dismiss();
//                }

                tv_del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        File file=new File(paths.get(position));
                        file.delete();
                        if(popupWindow.isShowing()&&popupWindow!=null){
                           popupWindow.dismiss();
                        }
                        names.remove(position);
                        paths.remove(position);
                        adapter.notifyDataSetChanged();

                    }
                });
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                int[] location=new int[2];
                view.getLocationInWindow(location);
                int x= Dp2pxUtils.Dp2Px(context,180);

                popupWindow.showAtLocation(parent, Gravity.TOP + Gravity.LEFT, x, location[1]);
                AnimationSet animationSet=new AnimationSet(false);
                AlphaAnimation alphaAnimation=new AlphaAnimation(0.5f,1.0f);
                alphaAnimation.setDuration(300);
                ScaleAnimation scaleAnimation=new ScaleAnimation(0.5f,1.0f,0.5f,1.0f);
                scaleAnimation.setDuration(300);
                animationSet.addAnimation(alphaAnimation);
                animationSet.addAnimation(scaleAnimation);
                popview.startAnimation(animationSet);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);
                popupWindow.update();




                return true;
            }
        });
    }


    private MyAdapter adapter;

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return names.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            viewHolder vh = null;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.firstpager_listview_item_layout, null);
                vh = new viewHolder(convertView);
                convertView.setTag(vh);
            } else {
                vh = (viewHolder) convertView.getTag();
            }
            String filepath=paths.get(position);
            vh.tv_name.setText(names.get(position));
            vh.tv_path.setText(filepath);
            if(filepath.contains(".")){

            String type = filepath.substring(filepath.lastIndexOf(".")+1, filepath.length());
            LogUtils.logInfoStar(type+"------>>>>typetypetype");
            if("jpg".equalsIgnoreCase(type)||"png".equalsIgnoreCase(type)){

                     //显示copy进来的图片
                    Bitmap decodeFile = BitmapFactory.decodeFile(filepath);
                    vh.imager.setImageBitmap(decodeFile);

               // vh.imager.setImageResource(R.drawable.img_type);
            }else if("txt".equalsIgnoreCase(type)){
                vh.imager.setImageResource(R.drawable.txt_type);
            }else if("doc".equalsIgnoreCase(type)){
                vh.imager.setImageResource(R.drawable.doc_type);
            }else{
                vh.imager.setImageResource(R.mipmap.app_icon);
            }
            }else {
                vh.imager.setImageResource(R.drawable.myfolder);

            }
            return convertView;
        }
    }

    class viewHolder {
        TextView tv_name, tv_path;
        ImageView imager;
        public viewHolder(View convertView) {
            tv_name = (TextView) convertView.findViewById(R.id.text_folderName);
            tv_path = (TextView) convertView.findViewById(R.id.text_folderSize);
            imager= (ImageView) convertView.findViewById(R.id.image);
        }
    }

}
