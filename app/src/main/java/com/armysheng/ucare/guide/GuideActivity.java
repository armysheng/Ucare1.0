package com.armysheng.ucare.guide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.armysheng.ucare.Login_Activity;
import com.armysheng.ucare.R;

import java.util.ArrayList;

/**
 * Created by Armysheng on 2015/1/20.
 */
public  class GuideActivity extends Activity implements OnPageChangeListener {
    private ViewPager viewPager;

    private ViewPagerAdapter vpAdapter;

    private ArrayList<View> views;

    private View view1, view2, view3, view4;

    private Button startBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initView();

        initData();
    }

    /**
     * 锟斤拷始锟斤拷锟斤拷锟�
     */
    private void initView() {
        LayoutInflater mLi = LayoutInflater.from(this);
        view1 = mLi.inflate(R.layout.guide_view01, null);
        view2 = mLi.inflate(R.layout.guide_view02, null);
        view3 = mLi.inflate(R.layout.guide_view03, null);
        view4 = mLi.inflate(R.layout.guide_view04, null);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        views = new ArrayList<>();

        vpAdapter = new ViewPagerAdapter(views);

        startBt = (Button) view4.findViewById(R.id.startBtn);
    }

    /**
     */
    private void initData() {
        viewPager.setOnPageChangeListener(this);
        viewPager.setAdapter(vpAdapter);
        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);
        vpAdapter.notifyDataSetChanged();


        startBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startbutton();
            }
        });
    }

    public void onPageScrollStateChanged(int arg0) {

    }

    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    public void onPageSelected(int arg0) {

    }

    /**
     */
    private void startbutton() {
        Intent intent = new Intent();
        intent.setClass(GuideActivity.this,Login_Activity.class);
        startActivity(intent);
        this.finish();
    }


}
