package com.example.jierui.canvas_path;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import weatherHelper.Heweather5;

public class MainActivity extends AppCompatActivity {

    DrawCenterPath drawCenterPath;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawCenterPath = (DrawCenterPath) findViewById(R.id.drawCenter);
        drawCenterPath.addCity("北京");
        drawCenterPath.addCity("上海");
        drawCenterPath.addCity("天津");
        drawCenterPath.addCity("重庆");
        drawCenterPath.addCity("乌鲁木齐");
        drawCenterPath.addCity("濮阳");
        drawCenterPath.addCity("邯郸");
        drawCenterPath.addCity("菏泽");
        drawCenterPath.addCity("新乡");
        drawCenterPath.addCity("郑州");
        drawCenterPath.addCity("呼和浩特");
        drawCenterPath.addCity("巴音郭楞");
        drawCenterPath.addCity("哈尔滨");
        drawCenterPath.addCity("四川");
        drawCenterPath.addCity("开封");
        drawCenterPath.addCity("清丰");




//        cityName.clear();
//        cityName.add("北京");
//        cityName.add("上海");
//        cityName.add("天津");
//        cityName.add("重庆");
//        cityName.add("乌鲁木齐");
//        cityName.add("濮阳");
//        cityName.add("邯郸");
//        cityName.add("菏泽");
//        cityName.add("新乡");
//        cityName.add("郑州");
//        cityName.add("呼和浩特");
//        cityName.add("巴音郭楞");
//        cityName.add("哈尔滨");
//        cityName.add("纽约");

//        init();
        int i;
        i = 9;

    }







    private void init() {
        // 得到屏幕尺寸
        DisplayMetrics dm =getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        Log.d("tag", "屏幕尺寸2：宽度 = " + w_screen + "高度 = " + h_screen + "密度 = " + dm.densityDpi);


        RelativeLayout layout=(RelativeLayout) findViewById(R.id.root);
        final DrawCenter view=new DrawCenter(this);
        //view.setMinimumHeight(500);
        //view.setMinimumWidth(300);
        //通知view组件重绘
        view.invalidate();
        layout.addView(view);

    }
}
