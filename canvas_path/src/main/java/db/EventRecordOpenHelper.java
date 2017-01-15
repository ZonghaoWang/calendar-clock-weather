package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jierui on 2016/12/28.
 */

public class EventRecordOpenHelper extends SQLiteOpenHelper {

    public static final String CREATE_EVENT_RECORD = "create table degree_event_record (" +
            "_id integer PRIMARY KEY autoincrement," +
            "start_date_time text," +
            "end_date_time text," +
            "progress integer," +
            "level integer," +
            "state integer," +
            "instant integer," +
            "record text" +
            "title text)";

    public EventRecordOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_EVENT_RECORD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
