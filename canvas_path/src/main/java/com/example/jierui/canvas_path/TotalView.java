package com.example.jierui.canvas_path;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import toolbox.BasicTools;
import toolbox.MyPagerAdapter;
import weatherHelper.Heweather5;

public class TotalView extends Activity implements OnClickListener {

    /**
     * viewpager的变量
     */

    private List<View> views = new ArrayList<View>();
    private ViewPager viewPager;
    private LinearLayout lcalendar, lalarm, lweather;
    private ImageView icalendar, ialarm, iweather, ivCurrent;


    /**
     * weather的变量
     */
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_viewpager);

        initView();

        initData();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        lcalendar = (LinearLayout) findViewById(R.id.lcalendar);
        lalarm = (LinearLayout) findViewById(R.id.lalarm);
        lweather = (LinearLayout) findViewById(R.id.lweather);

        lcalendar.setOnClickListener(this);
        lalarm.setOnClickListener(this);
        lweather.setOnClickListener(this);

        icalendar = (ImageView) findViewById(R.id.icalendar);
        ialarm = (ImageView) findViewById(R.id.ialarm);
        iweather = (ImageView) findViewById(R.id.iweather);


        icalendar.setSelected(true);
        ivCurrent = icalendar;


        viewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                changeTab(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    private View carlendar;
    private View alarm;
    private View weather;
    private void initData() {
        LayoutInflater mInflater = LayoutInflater.from(this);
        carlendar = mInflater.inflate(R.layout.carlendar, null);
        alarm = mInflater.inflate(R.layout.alarm, null);
        weather = mInflater.inflate(R.layout.activity_main, null);
        views.add(carlendar);
        views.add(alarm);
        views.add(weather);

        MyPagerAdapter adapter = new MyPagerAdapter(views);
        viewPager.setAdapter(adapter);
        initWeather();
    }

    private void initWeather() {
        nowTmp = (TextView) weather.findViewById(R.id.now_tmp);
        cityTmp = (TextView) weather.findViewById(R.id.city_tmp);
        condTxt = (TextView) weather.findViewById(R.id.cond_txt);
        windInfo = (LinearLayout) weather.findViewById(R.id.wind_info);
        humInfo = (LinearLayout) weather.findViewById(R.id.hum_info);
        aqiInfo = (LinearLayout) weather.findViewById(R.id.aqi_info);
        visInfo = (LinearLayout) weather.findViewById(R.id.vis_info);
        head = (LinearLayout) weather.findViewById(R.id.head);

        wind_txt = (TextView) weather.findViewById(R.id.wind_txt);
        wind_degree = (TextView) weather.findViewById(R.id.wind_degree);
        hum_degree = (TextView) weather.findViewById(R.id.hum_degree);
        aqi_txt = (TextView) weather.findViewById(R.id.aqi_txt);
        aqi_degree = (TextView) weather.findViewById(R.id.aqi_degree);
        vis_degree = (TextView) weather.findViewById(R.id.vis_degree);
        spector1 = (TextView) weather.findViewById(R.id.spector1);
        spector2 = (TextView) weather.findViewById(R.id.spector2);
        spector3 = (TextView) weather.findViewById(R.id.spector3);


        drawCenterPath = (DrawCenterPath) weather.findViewById(R.id.drawCenter);
        drawCenterPath.setOnItemClickListener(new DrawCenterPath.OnItemClickListener() {
            @Override
            public void onClick(Heweather5 heweather5) {
                Toast.makeText(TotalView.this, heweather5.getCityName(), Toast.LENGTH_SHORT);
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
    }

    @Override
    public void onClick(View v) {
        changeTab(v.getId());
    }

    private void changeTab(int id) {
        ivCurrent.setSelected(false);
        switch (id) {
            case R.id.lcalendar:
                viewPager.setCurrentItem(0);
            case 0:
                icalendar.setSelected(true);
                ivCurrent = icalendar;

                break;
            case R.id.lalarm:
                viewPager.setCurrentItem(1);
            case 1:
                ialarm.setSelected(true);
                ivCurrent = ialarm;
                break;
            case R.id.lweather:
                viewPager.setCurrentItem(2);
            case 2:
                iweather.setSelected(true);
                ivCurrent = iweather;
                break;
            default:
                break;
        }
    }

}
