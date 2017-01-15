package calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.jierui.canvas_path.R;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.EventRecord;
import utility.SandyClock;

/**
 * Created by jierui on 2017/1/2.
 */

public class CreateRecord extends Activity {


    private EventRecord eventRecord;
    private int index = 0;


    /**
     * 控件
    * */
    private Date startDateTime, endDateTime;
    private int state;
    private int level;
    private int progress;
    private int instant;
    private int id = -1;
    private String title, record;
    private TextView event_record_page_title, event_record_page_start_time, event_record_page_end_time, event_record_page_record;
    private ratingbar.RatingBar android_important_star;
    private SeekBar android_process_seekBar;
    private SandyClock android_instant_sandyClock;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");

    private String startTime, endTime;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_record_page);
        //获得意图
        Intent intent = getIntent();
        //读取数据
        Bundle bundle  = intent.getExtras();
        index = intent.getIntExtra("index", index);
        if (index != -1) {
            eventRecord = (EventRecord) bundle.getSerializable("EventRecord");
            state = eventRecord.getState();
            level = eventRecord.getLevel();
            progress = eventRecord.getProgress();
            instant = eventRecord.getInstant();
            id = eventRecord.getId();
            title = eventRecord.getTitle();
            record = eventRecord.getRecord();
            startDateTime = eventRecord.getStartDateTime();
            endDateTime = eventRecord.getEndDateTime();
        }else {
            eventRecord = new EventRecord();
            /**
             * 设计progress level instant
             */

        }


        // 初始化控件
        initView();
        startTime = sdf.format(startDateTime);
        endTime = sdf.format(endDateTime);
        event_record_page_title.setText(title);
        event_record_page_record.setText(record);
        event_record_page_start_time.setText(startTime);
        event_record_page_end_time.setText(endTime);
        android_instant_sandyClock.setPercent(instant);
        android_important_star.setRatingValue(level);
        android_process_seekBar.setProgress(progress);




    }

    private void initView() {
        event_record_page_title = (TextView) findViewById(R.id.event_record_page_title);
        event_record_page_start_time = (TextView) findViewById(R.id.event_record_page_start_time);
        event_record_page_end_time = (TextView) findViewById(R.id.event_record_page_end_time);
        android_important_star = (ratingbar.RatingBar) findViewById(R.id.android_important_star);
        android_process_seekBar = (SeekBar) findViewById(R.id.android_process_seekBar);
        android_instant_sandyClock = (SandyClock) findViewById(R.id.android_instant_sandyClock);
        event_record_page_record = (TextView) findViewById(R.id.event_record_page_record);
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        eventRecord.setProgress(android_process_seekBar.getProgress());
        eventRecord.setLevel((int) android_important_star.getRating());
        eventRecord.setInstant(android_instant_sandyClock.getPercent());
        eventRecord.setTitle(event_record_page_title.getText().toString());
        eventRecord.setRecord(event_record_page_record.getText().toString());
        try {
            eventRecord.setStartDateTime(sdf.parse(event_record_page_start_time.getText().toString()));
            eventRecord.setEndDateTime(sdf.parse(event_record_page_end_time.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        bundle.putSerializable("EventRecord", eventRecord);
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        super.finish();
    }



}
