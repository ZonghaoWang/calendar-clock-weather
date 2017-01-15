package calendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.jierui.canvas_path.R;

import db.DegreeLevelDB;
import db.EventRecordDB;
import model.DegreeLevel;
import model.EventRecord;
import utility.EventAdapter;
import weatherHelper.Heweather5;

import static calendar.CalendarActivity.EventState.*;

/**
 * 日历显示activity
 *
 * @author Vincent Lee
 */
public class CalendarActivity extends Activity implements View.OnClickListener {

    private GestureDetector gestureDetector = null;
    private CalendarAdapter calV = null;
    private ViewFlipper flipper = null;
    private GridView gridView = null;   // 日历GridView
    private static int jumpMonth = 0; // 每次滑动，增加或减去一个月,默认为0（即显示当前月）
    private static int jumpYear = 0; // 滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
    private int year_c = 0;            // 当前的年份
    private int month_c = 0;
    private int day_c = 0;
    private String currentDate = "";
    /**
     * 每次添加gridview到viewflipper中时给的标记
     */
    private int gvFlag = 0;
    /**
     * 当前的年月，现在日历顶端
     */
    private TextView currentMonth;
    /**
     * 上个月
     */
    private ImageView prevMonth;   // 按钮
    /**
     * 下个月
     */
    private ImageView nextMonth;   // 按钮下一个月


    private int longClickedPosition;




    public CalendarActivity() {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        currentDate = sdf.format(date); // 当期日期
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        day_c = Integer.parseInt(currentDate.split("-")[2]);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        currentMonth = (TextView) findViewById(R.id.currentMonth);
        prevMonth = (ImageView) findViewById(R.id.prevMonth);
        nextMonth = (ImageView) findViewById(R.id.nextMonth);
        setListener();    // prevMonth 和 nextMonth 的listener

        gestureDetector = new GestureDetector(this, new MyGestureListener());
        flipper = (ViewFlipper) findViewById(R.id.flipper);
        flipper.removeAllViews();
        calV = new CalendarAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
        addGridView();
        gridView.setAdapter(calV);
        flipper.addView(gridView, 0);
        addTextToTopTextView(currentMonth);
        initPopWindow();
        initNongLi();
        initEventListPopWindow();
        history_today = (ListView) findViewById(R.id.list_history_today);
        spector_history_huodong = (ImageView) findViewById(R.id.spector_history_huodong);
        layout_planing = (LinearLayout) findViewById(R.id.planing);
        layout_history_today = (LinearLayout) findViewById(R.id.layout_history_today);
        initEvent();
        if (isExistHistory) {
            HistoryTodayHelper historyTodayHelper = new HistoryTodayHelper(mHandler);
        }

        //
        degreeLevelDB = DegreeLevelDB.getInstance(this);  //需要在oncreate中用this,不能声明变量时初始化
        EventRecord eventRecord = new EventRecord();
        eventRecord.setRecord("akdhakjdfha");
        eventRecord.setLevel(2);
        eventRecord.setTitle("wangzonghao");
        eventRecord.setStartDateTime(new Date(System.currentTimeMillis()));
        eventRecord.setProgress(23);
        eventRecord.setState(2);
        eventRecord.setEndDateTime(new Date(System.currentTimeMillis() + 1000));
        eventRecord.setInstant(30);
        int id = degreeLevelDB.saveEventRecord(eventRecord);

        initEventListView();
    }

    private DegreeLevelDB degreeLevelDB;
    private class MyGestureListener extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            int gvFlag = 0; // 每次添加gridview到viewflipper中时给的标记
            if (e1.getX() - e2.getX() > 120) {
                // 像左滑动
                enterNextMonth(gvFlag);
                return true;
            } else if (e1.getX() - e2.getX() < -120) {
                // 向右滑动
                enterPrevMonth(gvFlag);
                return true;
            }
            return false;
        }
    }


    /**
     * 移动到下一个月
     *
     * @param gvFlag
     */
    private void enterNextMonth(int gvFlag) {
        addGridView(); // 添加一个gridView
        jumpMonth++; // 下一个月

        calV = new CalendarAdapter(this, this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
        gridView.setAdapter(calV);
        addTextToTopTextView(currentMonth); // 移动到下一月后，将当月显示在头标题中
        gvFlag++;
        flipper.addView(gridView, gvFlag);
        flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
        flipper.showNext();
        flipper.removeViewAt(0);
    }

    /**
     * 移动到上一个月
     *
     * @param gvFlag
     */
    private void enterPrevMonth(int gvFlag) {
        addGridView(); // 添加一个gridView
        jumpMonth--; // 上一个月

        calV = new CalendarAdapter(this, this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
        gridView.setAdapter(calV);
        gvFlag++;
        addTextToTopTextView(currentMonth); // 移动到上一月后，将当月显示在头标题中
        flipper.addView(gridView, gvFlag);

        flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
        flipper.showPrevious();
        flipper.removeViewAt(0);
    }

    /**
     * 添加头部的年份 闰哪月等信息
     *
     * @param view
     */
    public void addTextToTopTextView(TextView view) {
        StringBuffer textDate = new StringBuffer();
        // draw = getResources().getDrawable(R.drawable.top_day);
        // view.setBackgroundDrawable(draw);
        textDate.append(calV.getShowYear()).append("年").append(calV.getShowMonth()).append("月").append("\t");
        view.setText(textDate);
    }


    private void addGridView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        // 取得屏幕的宽度和高度
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int Width = display.getWidth();
        int Height = display.getHeight();

        gridView = new GridView(this);
        gridView.setNumColumns(7);
        gridView.setGravity(Gravity.CENTER_VERTICAL);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        // 去除gridView边框
        gridView.setVerticalSpacing(1);
        gridView.setHorizontalSpacing(1);
        gridView.setOnTouchListener(new OnTouchListener() {
            // 将gridview中的触摸事件回传给gestureDetector

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                return CalendarActivity.this.gestureDetector.onTouchEvent(event);
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(CalendarActivity.this, "longlong", 2000).show();
                longClickedPosition = i;
                gridView.getWidth();
                gridView.getHeight();
                degree_calendar.setText(calV.getDateFromPosition(i));
                mPopWindow.showAsDropDown(view, -view.getWidth(), 0);
                return true;  // true 会震动,flase不震动
            }
        });
        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // TODO Auto-generated method stub
                // 点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
                int startPosition = calV.getStartPositon();
                int endPosition = calV.getEndPosition();
                if (startPosition <= position + 7 && position <= endPosition - 7) {
                    String scheduleDay = calV.getDateByClickItem(position).split("\\.")[0]; // 这一天的阳历
                    // String scheduleLunarDay =
                    // calV.getDateByClickItem(position).split("\\.")[1];
                    // //这一天的阴历
                    String scheduleYear = calV.getShowYear();
                    String scheduleMonth = calV.getShowMonth();
                    Toast.makeText(CalendarActivity.this, scheduleYear + "-" + scheduleMonth + "-" + scheduleDay, 2000).show();
                    // Toast.makeText(CalendarActivity.this, "点击了该条目",
                    // Toast.LENGTH_SHORT).show();
                }
            }
        });
        gridView.setLayoutParams(params);
    }

    private void setListener() {
        prevMonth.setOnClickListener(this);
        nextMonth.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {    // filpper 没有setOnClickListener(this), 点击flipper时不会响应该该事件
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.nextMonth: // 下一个月
                enterNextMonth(gvFlag);
                break;
            case R.id.prevMonth: // 上一个月
                enterPrevMonth(gvFlag);
                break;
            case R.id.degree0:
                calV.setDaysDegree(longClickedPosition, 0);
                v.setBackground(getResources().getDrawable(R.drawable.pop_window_bg));
                mPopWindow.dismiss();
                break;
            case R.id.degree1:
                calV.setDaysDegree(longClickedPosition, 1);
                mPopWindow.dismiss();
                break;
            case R.id.degree2:
                calV.setDaysDegree(longClickedPosition, 2);
                mPopWindow.dismiss();
                break;
            case R.id.degree3:
                calV.setDaysDegree(longClickedPosition, 3);
                mPopWindow.dismiss();
                break;
            case R.id.degree4:
                calV.setDaysDegree(longClickedPosition, 4);
                mPopWindow.dismiss();
                break;
            case R.id.degree5:
                calV.setDaysDegree(longClickedPosition, 5);
                mPopWindow.dismiss();
                break;
            // 描述活动列表状态的textview
            case R.id.state_of_event:
                currentEventState = getNextState();
                state_of_event.setText(getAddressAndChangeAdapter(currentEventState));
                break;
            default:
                break;
        }
        calV.notifyDataSetChanged();
    }

    private PopupWindow mPopWindow;
    private LinearLayout degree0, degree1, degree2, degree3, degree4, degree5;
    private TextView degree_calendar;
    // 黄历
    private TextView text_nongli, text_jieri, text_lifa, nongli_spector;
    private ListView history_today;

    private void initPopWindow(){
        View contentView = LayoutInflater.from(CalendarActivity.this).inflate(R.layout.pop_window_lay, null);
        mPopWindow = new PopupWindow(contentView,
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setTouchable(true);
        mPopWindow.setFocusable(true);
        mPopWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        mPopWindow.setOutsideTouchable(true);
        //设置各个控件的点击响应
        degree0 = (LinearLayout) contentView.findViewById(R.id.degree0);
        degree1 = (LinearLayout) contentView.findViewById(R.id.degree1);
        degree2 = (LinearLayout) contentView.findViewById(R.id.degree2);
        degree3 = (LinearLayout) contentView.findViewById(R.id.degree3);
        degree4 = (LinearLayout) contentView.findViewById(R.id.degree4);
        degree5 = (LinearLayout) contentView.findViewById(R.id.degree5);
        degree_calendar = (TextView) contentView.findViewById(R.id.degree_calendar);
        degree0.setOnClickListener(this);
        degree1.setOnClickListener(this);
        degree2.setOnClickListener(this);
        degree3.setOnClickListener(this);
        degree4.setOnClickListener(this);
        degree5.setOnClickListener(this);
    }
    private void initNongLi(){
        text_nongli = (TextView) findViewById(R.id.text_nongli);
        text_jieri = (TextView) findViewById(R.id.text_jieri);
        text_lifa = (TextView) findViewById(R.id.text_lifa);
        nongli_spector = (TextView) findViewById(R.id.nongli_spector);
        text_nongli.setText("农历腊月二十");
        text_jieri.setText("圣诞节");
        text_jieri.setVisibility(View.GONE);
        nongli_spector.setVisibility(View.GONE);
        text_lifa.setText("丙申猴年");
    }
    private LinearLayout layout_planing, layout_history_today;
    private ImageView spector_history_huodong;

    private boolean isExistPlan = true;
    private boolean isExistHistory = true;
    private void initEvent(){
        if (isExistPlan){
            layout_planing.setVisibility(View.VISIBLE);
            if (isExistHistory){
                layout_history_today.setVisibility(View.VISIBLE);
                spector_history_huodong.setVisibility(View.VISIBLE);
            }else{
                layout_history_today.setVisibility(View.GONE);
                spector_history_huodong.setVisibility(View.GONE);
            }
        }else{
            spector_history_huodong.setVisibility(View.GONE);
        }

    }
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //完成主界面更新,拿到数据
                    HistoryTodayHelper historyTodayHelper = (HistoryTodayHelper) msg.obj;
                    ArrayList<HashMap<String, String>> list_history_today = historyTodayHelper.getList();
                    SimpleAdapter adapter_history_today, adapter_event;
                    if (isExistPlan){
                        layout_planing.setVisibility(View.VISIBLE);
                        spector_history_huodong.setVisibility(View.VISIBLE);
                        adapter_history_today = new SimpleAdapter(CalendarActivity.this, list_history_today, R.layout.history_today_item_dual, new String[]{"year", "event"}, new int[]{R.id.year, R.id.event});

                    }else {
                        layout_planing.setVisibility(View.GONE);
                        spector_history_huodong.setVisibility(View.GONE);
                        adapter_history_today = new SimpleAdapter(CalendarActivity.this, list_history_today, R.layout.history_today_item_single, new String[]{"year", "event"}, new int[]{R.id.year, R.id.event});
                    }
                    history_today.setAdapter(adapter_history_today);
                    break;
                default:
                    break;
            }

        }

    };

    /**
     * event_list，配置活动列表list
     */
    private ListView huodong_list;
    private TextView state_of_event;
    private List<EventRecord> eventRecords_todo, eventRecords_doing, eventRecords_done, eventRecords_droped;
    private EventAdapter eventAdapter_todo, eventAdapter_doing, eventAdapter_done, eventAdapter_droped;
    public enum EventState{todo, doing, done, droped};
    private EventState currentEventState = doing;
    private EventState getNextState(){
            switch(currentEventState){
            case todo: return EventState.doing;
            case doing: return EventState.done;
            case done: return EventState.droped;
            case droped: return EventState.todo;
        }
        return EventState.done;
    }
    private void initEventListView(){
        huodong_list = (ListView) findViewById(R.id.huodong_list);
        state_of_event = (TextView) findViewById(R.id.state_of_event);
        eventRecords_todo = degreeLevelDB.loadEventRecord(1);
        eventRecords_doing = degreeLevelDB.loadEventRecord(2);
        eventRecords_done = degreeLevelDB.loadEventRecord(3);
        eventRecords_droped = degreeLevelDB.loadEventRecord(4);
        // 设置adapter
        eventAdapter_todo = new EventAdapter(this, eventRecords_todo);
        eventAdapter_doing = new EventAdapter(this, eventRecords_doing);
        eventAdapter_done = new EventAdapter(this, eventRecords_done);
        eventAdapter_droped = new EventAdapter(this, eventRecords_droped);

        // 设置显示状态的textview的显示值
        state_of_event.setOnClickListener(this);
        state_of_event.setText(getAddressAndChangeAdapter(currentEventState));
        huodong_list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.setClass(CalendarActivity.this, CreateRecord.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("EventRecord", getCurrentEventRecord(i));
                bundle.putInt("index", i);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);


            }
        });
        huodong_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(CalendarActivity.this, "long", 2000).show();
                return true;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:
                Bundle b = data.getExtras(); //data为B中回传的Intent
                int index = b.getInt("index");//str即为回传的值
                EventRecord eventRecord = (EventRecord) b.getSerializable("EventRecord");
                degreeLevelDB.saveEventRecord(eventRecord);
                if (index != -1){
                    setCurrentEventRecord(index, eventRecord);
                }
                break;
            default:
                break;
        }
    }
    private void setCurrentEventRecord(int i, EventRecord eventRecord){
        switch (currentEventState){
            case todo:
                eventAdapter_todo.changeItem(i, eventRecord);
                break;
            case doing:
                eventAdapter_doing.changeItem(i, eventRecord);
                break;
            case done:
                eventAdapter_done.changeItem(i, eventRecord);
                break;
            case droped:
                eventAdapter_droped.changeItem(i, eventRecord);
            default: eventRecords_doing.set(i, eventRecord);
        }

    }
    private EventRecord getCurrentEventRecord(int i){
        switch (currentEventState){
            case todo: return eventRecords_todo.get(i);
            case doing: return eventRecords_doing.get(i);
            case done: return eventRecords_done.get(i);
            case droped: return eventRecords_droped.get(i);
            default: return eventRecords_doing.get(i);
        }
    }
    /**
     * @param  currentEventState 当前输入状态
     * @deprecated 返回状态textview的描述至，并设置adapter
     */

    private int getAddressAndChangeAdapter(EventState currentEventState) {
        switch (currentEventState){
            case todo: huodong_list.setAdapter(eventAdapter_todo); return R.string.todo;
            case doing: huodong_list.setAdapter(eventAdapter_doing);return R.string.doing;
            case done: huodong_list.setAdapter(eventAdapter_done);return R.string.done;
            case droped: huodong_list.setAdapter(eventAdapter_droped);return R.string.droped;
            default: return R.string.todo;
        }
    }


    /**
     * 初始化活动列表
     */
    private TextView swithc_to_doing, swithc_to_todo, swithc_to_done, swithc_to_droped, swithc_delete;
    private LinearLayout swithc_layout;
    private PopupWindow stateEventSwitchPopWindow;
    private void initEventListPopWindow(){
        View contentView = LayoutInflater.from(CalendarActivity.this).inflate(R.layout.state_of_event_switch, null);
        stateEventSwitchPopWindow = new PopupWindow(contentView,
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
        stateEventSwitchPopWindow.setTouchable(true);
        stateEventSwitchPopWindow.setFocusable(true);
        stateEventSwitchPopWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        stateEventSwitchPopWindow.setOutsideTouchable(true);


        swithc_layout = (LinearLayout) contentView.findViewById(R.id.swithc_layout);
        swithc_to_doing = (TextView) contentView.findViewById(R.id.swithc_to_doing);
        swithc_to_todo = (TextView) contentView.findViewById(R.id.swithc_to_todo);
        swithc_to_done = (TextView) contentView.findViewById(R.id.swithc_to_done);
        swithc_to_droped = (TextView) contentView.findViewById(R.id.swithc_to_droped);
        swithc_delete = (TextView) contentView.findViewById(R.id.switch_delete);
        swithc_to_doing.setOnClickListener(this);
        swithc_to_todo.setOnClickListener(this);
        swithc_to_done.setOnClickListener(this);
        swithc_to_droped.setOnClickListener(this);
        swithc_delete.setOnClickListener(this);
    }
    private void impactState(int index){

        index = index - 1;
        for (int i = 0; i < swithc_layout.getChildCount() - 1; i++){
            if (i == index){
                swithc_layout.getChildAt(i).setVisibility(View.GONE);
            }else{
                swithc_layout.getChildAt(i).setVisibility(View.VISIBLE);
            }
        }
    }



}