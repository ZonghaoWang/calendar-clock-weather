package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jierui on 2016/12/23.
 */

public class DegreeLevelOpenHelper extends SQLiteOpenHelper {

    public static final String CREATE_DEGREE = "create table degree_level (" +
            "year integer," +
            "month integer," +
            "degree varchar(42))";

    public DegreeLevelOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_DEGREE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
