package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import model.DegreeLevel;
import model.EventRecord;

/**
 * Created by jierui on 2016/12/23.
 */

public class DegreeLevelDB {
    /**
     * 数据库名
     */
    public static final String DB_NAME = "degree_level";


    /**
     * 数据库版本
     */
    public static final int VERSION = 1;

    private static DegreeLevelDB degreeLevelDB;

    private SQLiteDatabase db;

    /**
    * 将构造方法实例化
    */
    private DegreeLevelDB(Context context){
        DegreeLevelOpenHelper degreeLevelOpenHelper = new DegreeLevelOpenHelper(context, DB_NAME, null, VERSION);
        db = degreeLevelOpenHelper.getWritableDatabase();
    }
    /**
     * 获取DegreeLevelDB的实例
     */
    public synchronized static DegreeLevelDB getInstance(Context context){
        if (degreeLevelDB == null){
            degreeLevelDB = new DegreeLevelDB(context);
        }
        return degreeLevelDB;
    }

    /**
     * 将某年某月数据更新到数据库
     */
    public void saveDegreeLevel(int year, int month, int[] degreeLevel){
        Cursor cursor = db.query("degree_level", new String[]{"degree"}, "year=? and month=?", new String[]{year+"", month+""}, null, null, null);
        ContentValues contentValues = new ContentValues();

        String s = "";
        for (int i = 0; i < degreeLevel.length; i++){
            s = s + degreeLevel[i];
        }
        contentValues.put("degree", s);
        if (cursor.moveToFirst() != false){
            db.update("degree_level", contentValues, "year=? and month=?", new String[]{year+"", month+""});
        }else {
            contentValues.put("year", year);
            contentValues.put("month", month);
            db.insert("degree_level", null, contentValues);
        }
    }

    /**
     * 读取某年某月的数据库信息
     */
    public DegreeLevel loadDegreeLevel(int year, int month){
        Cursor cursor = db.query("degree_level", new String[]{"degree"}, "year=? and month=?", new String[]{year+"", month+""}, null, null, null);
        String s = new String();
        int[] degrees = new int[42];
        if (cursor.moveToFirst()){
            s = cursor.getString(cursor.getColumnIndex("degree"));
        }

        if (s != null) {
            for (int i = 0; i < s.length(); i++) {
                degrees[i] = Integer.valueOf(s.charAt(i) - 48);
            }
            DegreeLevel degreeLevel = new DegreeLevel();
            degreeLevel.setDegree(degrees);
            degreeLevel.setMonth(month);
            degreeLevel.setYear(year);
            return degreeLevel;
        }else{
            return null;
        }
    }


    /***********************
     * 活动记录
     * ******************
     */
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
        contentValues.put("level", eventRecord.getLevel());
        contentValues.put("state", eventRecord.getState());
        contentValues.put("title", eventRecord.getTitle());
        contentValues.put("progress", eventRecord.getProgress() + "");
        contentValues.put("instant", eventRecord.getInstant() + "");
        int id = -1;
        if (eventRecord.getId() == -1){
            db.insert("degree_event_record", null, contentValues);
            Cursor cursor = db.rawQuery("select last_insert_rowid() from degree_event_record",null);
            if(cursor.moveToFirst()) {
                id = cursor.getInt(0);
            }
        }else{
            contentValues.put("_id", eventRecord.getId() + "");
            db.update("degree_event_record", contentValues, "_id=?", new String[]{eventRecord.getId() +""});
            id = eventRecord.getId();
        }
        return id;

    }



    /**
     * 读取state=0的数据
     */
    public List<EventRecord> loadEventRecord(int state) {
        Cursor cursor = db.query("degree_event_record", null, "state=?", new String[]{state + ""}, null, null, null);
        List<EventRecord> list = new ArrayList<EventRecord>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (cursor.moveToFirst()) {
            do {
                EventRecord eventRecord = new EventRecord();
                eventRecord.setLevel(Integer.parseInt(cursor.getString(cursor.getColumnIndex("level"))));
                eventRecord.setState(Integer.parseInt(cursor.getString(cursor.getColumnIndex("state"))));
                eventRecord.setProgress(Integer.parseInt(cursor.getString(cursor.getColumnIndex("progress"))));
                eventRecord.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))));
                eventRecord.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                eventRecord.setRecord(cursor.getString(cursor.getColumnIndex("title")));
                eventRecord.setInstant(Integer.parseInt(cursor.getString(cursor.getColumnIndex("instant"))));
                try {
                    eventRecord.setStartDateTime(format.parse(cursor.getString(cursor.getColumnIndex("start_date_time"))));
                    eventRecord.setEndDateTime(format.parse(cursor.getString(cursor.getColumnIndex("end_date_time"))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                list.add(eventRecord);
            } while (cursor.moveToNext());
        }
        return list;
    }


}
