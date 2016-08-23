package org.t_robop.y_ogawara.tancolle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by yuusuke on 16/08/23.
 */
public class notifierDbAssist {
    static SQLiteDatabase sqldb; //MySQLiteOpenHelperの変数
    static mySqlNotifierHelper sqLiteOpenHelper;
    static ContentValues cv;
    static Cursor cursor;

    //orderで並び順を指定 ASCで昇順  DESCで降順
    static String openDB = "select * from users_table order by id asc";
    final static String TABLE_NAME = "users_table";


    public notifierDbAssist(Context context) {
        sqLiteOpenHelper = new mySqlNotifierHelper(context);
        sqldb = sqLiteOpenHelper.getWritableDatabase();
    }
    //データを追加するメソッド
    public static void insertData(alarmData data,Context context) {
        sqLiteOpenHelper = new mySqlNotifierHelper(context.getApplicationContext());
        sqldb = sqLiteOpenHelper.getWritableDatabase();
        cv = new ContentValues();
        cv.put("flagId", data.getFlagId());
        cv.put("birthdaySecond", data.getBirthdaySecond());
        cv.put("month", data.getMonth());
        cv.put("day", data.getDay());
        cv.put("alarmText", data.getAlarmText());
        cv.put("CustomYear1", data.getCustomDay1());
        cv.put("CustomMonth1", data.getCustomMonth1());
        cv.put("CustomDay1", data.getCustomDay1());
        cv.put("CustomYear2", data.getCustomYear2());
        cv.put("CustomMonth2", data.getCustomMonth2());
        cv.put("CustomDay2", data.getCustomDay2());
        cv.put("CustomYear3", data.getCustomYear3());
        cv.put("CustomMonth3", data.getCustomMonth3());
        cv.put("CustomDay3", data.getCustomDay3());
        cv.put("flag1", data.getFlag1());
        cv.put("flag2", data.getFlag2());
        cv.put("flag3", data.getFlag3());
        cv.put("flag4", data.getFlag4());
        cv.put("flag5", data.getFlag5());
        cv.put("flag6", data.getFlag6());
        cv.put("flag7", data.getFlag7());



        sqldb.insert(TABLE_NAME, null, cv);
        sqldb.close();
    }
    //全てのデータを取得するメソッド
    public static ArrayList allSelect(Context context){
        ArrayList <alarmData> DataArray= new ArrayList<>();
        sqLiteOpenHelper = new mySqlNotifierHelper(context);
        sqldb = sqLiteOpenHelper.getWritableDatabase();
        cursor = sqldb.rawQuery(openDB,null);
        while (cursor.moveToNext()) {
            alarmData data = new alarmData();
            data.setFlagId(cursor.getInt(0));
            data.setBirthdaySecond(cursor.getInt(1));
            data.setMonth(cursor.getInt(2));
            data.setDay(cursor.getInt(3));
            data.setAlarmText(cursor.getString(cursor.getColumnIndex("alarmText")));
            data.setCustomDay1(cursor.getInt(5));
            data.setCustomMonth1(cursor.getInt(6));
            data.setCustomYear1(cursor.getInt(7));
            data.setCustomDay2(cursor.getInt(8));
            data.setCustomMonth2(cursor.getInt(9));
            data.setCustomYear2(cursor.getInt(10));
            data.setCustomDay3(cursor.getInt(11));
            data.setCustomMonth3(cursor.getInt(12));
            data.setCustomYear3(cursor.getInt(13));
            data.setFlag1(cursor.getInt(14));
            data.setFlag2(cursor.getInt(15));
            data.setFlag3(cursor.getInt(16));
            data.setFlag4(cursor.getInt(17));
            data.setFlag5(cursor.getInt(18));
            data.setFlag6(cursor.getInt(19));
            data.setFlag7(cursor.getInt(20));

            DataArray.add(data);
        }
        sqldb.close();
        return DataArray;
    }
}
