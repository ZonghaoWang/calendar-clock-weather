package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import model.DegreeLevel;

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
}
