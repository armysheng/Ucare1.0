package com.armysheng.ucare;

import com.armysheng.ucare.guide.GuideActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;


public class Welcome extends Activity implements Runnable {

    //判断是否是第一次用
    private boolean isFirstUse;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getActionBar().hide();
        /**
         * 新建线程
         */
        new Thread(this).start();
    }

    public void run() {
        try {
            /**
             * 欢迎页面停留2000ms
             */
            Thread.sleep(2000);

            //第一次使用后更新SharedPreferences
            SharedPreferences preferences = getSharedPreferences("isFirstUse",MODE_WORLD_READABLE);

            isFirstUse = preferences.getBoolean("isFirstUse", true);

            /**
             *判断是否是第一次启动
             */
            if (isFirstUse) {
                startActivity(new Intent(Welcome.this, GuideActivity.class));
            } else {
                startActivity(new Intent(Welcome.this, Login_Activity.class));
            }
            finish();

            //创建Editor来编辑preferences
            Editor editor = preferences.edit();
            //update editor
            editor.putBoolean("isFirstUse", false);
            //
            editor.commit();


        } catch (InterruptedException e) {

        }
    }
}
