package com.example.jierui.canvas_path;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import toolbox.BasicTools;
import weatherHelper.Heweather5;

public class MainActivity extends AppCompatActivity{

    private DrawCenterPath drawCenterPath;
    private TextView nowTmp;
    private TextView cityTmp;
    private TextView condTxt;
    private LinearLayout windInfo;
    private LinearLayout humInfo;
    private LinearLayout aqiInfo;
    private LinearLayout visInfo;
    private LinearLayout head;

    private TextView wind_txt;
    private TextView wind_degree;
    private TextView hum_degree;
    private TextView aqi_txt;
    private TextView aqi_degree;
    private TextView vis_degree;
    private TextView spector1;
    private TextView spector2;
    private TextView spector3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nowTmp = (TextView) findViewById(R.id.now_tmp);
        cityTmp = (TextView) findViewById(R.id.city_tmp);
        condTxt = (TextView) findViewById(R.id.cond_txt);
        windInfo = (LinearLayout) findViewById(R.id.wind_info);
        humInfo = (LinearLayout) findViewById(R.id.hum_info);
        aqiInfo = (LinearLayout) findViewById(R.id.aqi_info);
        visInfo = (LinearLayout) findViewById(R.id.vis_info);
        head = (LinearLayout) findViewById(R.id.head);

        wind_txt = (TextView) findViewById(R.id.wind_txt);
        wind_degree = (TextView) findViewById(R.id.wind_degree);
        hum_degree = (TextView) findViewById(R.id.hum_degree);
        aqi_txt = (TextView) findViewById(R.id.aqi_txt);
        aqi_degree = (TextView) findViewById(R.id.aqi_degree);
        vis_degree = (TextView) findViewById(R.id.vis_degree);
        spector1 = (TextView) findViewById(R.id.spector1);
        spector2 = (TextView) findViewById(R.id.spector2);
        spector3 = (TextView) findViewById(R.id.spector3);

        drawCenterPath = (DrawCenterPath) findViewById(R.id.drawCenter);
        drawCenterPath.setOnItemClickListener(new DrawCenterPath.OnItemClickListener() {
            @Override
            public void onClick(Heweather5 heweather5) {
                Toast.makeText(MainActivity.this, heweather5.getCityName(), Toast.LENGTH_SHORT);
                head.setVisibility(View.VISIBLE);
                nowTmp.setText(heweather5.getNow().getTmp());
                cityTmp.setText(heweather5.getCityName());
                condTxt.setText(heweather5.getNow().getCondTxt());
                if (heweather5.getAqi()==null){
                    spector2.setVisibility(View.GONE);
                    aqiInfo.setVisibility(View.GONE);
                }else {
                    spector2.setVisibility(View.VISIBLE);
                    aqiInfo.setVisibility(View.VISIBLE);
                    aqi_txt.setText(heweather5.getAqi().getQlty());
                    aqi_degree.setText(heweather5.getAqi().getPm25());
                }
                wind_txt.setText(heweather5.getNow().getWind().getDir());
                wind_degree.setText(heweather5.getNow().getWind().getSc() + (BasicTools.hasDigit(heweather5.getNow().getWind().getSc())? " 级" : ""));
                hum_degree.setText(heweather5.getNow().getHum()+ "%");
                vis_degree.setText(heweather5.getNow().getVis() + " Km");
            }
        });
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
        drawCenterPath.addCity("若羌");
        drawCenterPath.addCity("哈尔滨");
        drawCenterPath.addCity("成都");
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
