package rsu.siriwimon.pakdeeporn.alertbusstop;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ADMIN on 1/11/2559.
 */

public class MyManage {
    //Explicit
    private Context context;
    private MyOpenHelper myOpenHelper;
    private SQLiteDatabase sqLiteDatabase;

    public static final String tableName = "busTABLE";
    public static final String column_id = "_id";
    public static final String column_NameBusStop = "NameBusStop";
    public static final String column_PathBusStop = "PathBusStop";
    public static final String column_Lat = "Lat";
    public static final String column_Lng = "Lng";
    public static final String column_Destination = "Destination";

    public MyManage(Context context) {
        this.context = context;
        myOpenHelper = new MyOpenHelper(context);
        sqLiteDatabase = myOpenHelper.getWritableDatabase();
    }

    public long addValueToSQLite(String strName,
                                 String strPath,
                                 String strLat,
                                 String strLng,
                                 String strDestiation) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(column_NameBusStop, strName);
        contentValues.put(column_PathBusStop, strPath);
        contentValues.put(column_Lat, strLat);
        contentValues.put(column_Lng, strLng);
        contentValues.put(column_Destination, strDestiation);

        return sqLiteDatabase.insert(tableName, null, contentValues);
    }

} // main class
