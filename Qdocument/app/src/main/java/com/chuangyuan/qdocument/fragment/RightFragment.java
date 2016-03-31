package com.chuangyuan.qdocument.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.chuangyuan.qdocument.R;
import com.chuangyuan.qdocument.pager.BasePager;
import com.chuangyuan.qdocument.pager.FirstPager;
import com.chuangyuan.qdocument.pager.SecondPager;
import com.chuangyuan.qdocument.pager.ThirdPager;
import com.chuangyuan.qdocument.view.MyViewPager;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/14 0014.
 */
public class RightFragment extends Fragment {
    private MyViewPager viewPager;
    private RadioGroup radiogroup;
    private ArrayList<BasePager> pagerList;
    private Context ctx;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ctx=getActivity();
        View view=View.inflate(getActivity(), R.layout.right_fragment_layout,null);
        viewPager= (MyViewPager) view.findViewById(R.id.viewpager);
        radiogroup= (RadioGroup) view.findViewById(R.id.radioGroup);
        initPager();

        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.first:

                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.second:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.third:
                        viewPager.setCurrentItem(2);
                        break;


                }

            }
        });
        radiogroup.check(R.id.first);
        adapter=new MyPagerAdapter();
        viewPager.setAdapter(adapter);
        return view;
    }

    private void initPager() {
        pagerList=new ArrayList<>();
        pagerList.add(new FirstPager(ctx));
        pagerList.add(new SecondPager(ctx));
        pagerList.add(new ThirdPager(ctx));


    }

    private PagerAdapter adapter;
    class MyPagerAdapter extends PagerAdapter{

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


            return view==object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((View) object);
        }
    }
}
