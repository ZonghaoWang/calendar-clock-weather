package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.DegreeLevel;
import model.EventRecord;

/**
 * Created by jierui on 2016/12/28.
 */

public class EventRecordDB {
    /**
     * 数据库名
     */
    public static final String DB_NAME = "degree_event_record";


    /**
     * 数据库版本
     */
    public static final int VERSION = 1;

    private static EventRecordDB eventRecordDB;

    private SQLiteDatabase db;

    /**
     * 将构造方法实例化
     */
    private EventRecordDB(Context context){
        EventRecordOpenHelper eventRecordOpenHelper = new EventRecordOpenHelper(context, DB_NAME, null, VERSION);
        db = eventRecordOpenHelper.getWritableDatabase();
    }
    /**
     * 获取DegreeLevelDB的实例
     */
    public synchronized static EventRecordDB getInstance(Context context){
        if (eventRecordDB == null){
            eventRecordDB = new EventRecordDB(context);
        }
        return eventRecordDB;
    }

    /**
     * 添加数据
     */
    public int saveEventRecord(EventRecord eventRecord){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str_start_date = format.format(eventRecord.getStartDateTime());
        String str_end_date = format.format(eventRecord.getEndDateTime());
        ContentValues contentValues = new ContentValues();
        contentValues.put("start_date_time", str_start_date);
        contentValues.put("end_date_time", str_end_date);
        contentValues.put("record", eventRecord.getRecord());
        contentValues.put("level", eventRecord.getLevel() + "");
        contentValues.put("state", eventRecord.getState());
        contentValues.put("title", eventRecord.getTitle());
        contentValues.put("progress", eventRecord.getProgress());
        int id = -1;
        if (eventRecord.getId() == -1){
            db.insert("degree_event_record", null, contentValues);
            Cursor cursor = db.rawQuery("select last_insert_rowid() from degree_event_record",null);
            if(cursor.moveToFirst()) {
                id = cursor.getInt(0);
            }
        }else{
            contentValues.put("_id", eventRecord.getId());
            db.update("degree_event_record", contentValues, "id=?", new String[]{eventRecord.getId() +""});
            id = eventRecord.getId();
        }
        return id;
        
    }



    /**
     * 读取state=0的数据
     */
    public List<EventRecord> loadDegreeLevel(int state) {
        Cursor cursor = db.query("degree_event_record", null, "where state=?", new String[]{state + ""}, null, null, null);
        List<EventRecord> list = new ArrayList<EventRecord>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (cursor.moveToFirst()) {
            EventRecord eventRecord = new EventRecord();
            eventRecord.setLevel(Integer.parseInt(cursor.getString(cursor.getColumnIndex("level"))));
            eventRecord.setState(Integer.parseInt(cursor.getString(cursor.getColumnIndex("state"))));
            eventRecord.setProgress(Integer.parseInt(cursor.getString(cursor.getColumnIndex("progress"))));
            eventRecord.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))));
            eventRecord.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            eventRecord.setRecord(cursor.getString(cursor.getColumnIndex("title")));
            try {
                eventRecord.setStartDateTime(format.parse(cursor.getString(cursor.getColumnIndex("start_date_time"))));
                eventRecord.setEndDateTime(format.parse(cursor.getString(cursor.getColumnIndex("end_date_time"))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            list.add(eventRecord);
        }
        return list;
    }
}
