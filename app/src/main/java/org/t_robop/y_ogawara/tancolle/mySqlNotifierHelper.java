package org.t_robop.y_ogawara.tancolle;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yuusuke on 16/08/23.
 */
public class mySqlNotifierHelper extends SQLiteOpenHelper {
    static final String DB_NAME = "tancolle1.db";  //データベースの名前
    static final int DB_VERSION = 1;             //データベースのバージョン
    static final String DROP = "drop table notifier_table";   //データベースを下ろすSQL文
    /**
     * データベースファイル初回使用時に実行される処理

     //通知の種類
     int flagId; 7桁連結
     1111111 だったら全部on

     //誕生日の時間
     int birthdaySecond;
     //誕生日の月 日
     int month;
     int day;
     //通知
     String alarmText;

     //Customの日付
     int CustomYear1;
     int CustomMonth1;
     int CustomDay1;
     int CustomYear2;
     int CustomMonth2;
     int CustomDay2;
     int CustomYear3;
     int CustomMonth3;
     int CustomDay3;
     int flag1;
     int flag2;
     int flag3;
     int flag4;
     int flag5;
     int flag6;
     int flag7;

     */

    public mySqlNotifierHelper(Context c) {
        super(c, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // テーブル作成のクエリを発行
        String sql = "CREATE TABLE notifier_table ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "flagId INTEGER,"
                +"birthdaySecond INTEGER,"
                +"month INTEGER,"
                +"day INTEGER,"
                +"alarmText TEXT,"
                +"CustomYear1 INTEGER,"
                +"CustomMonth1 INTEGER,"
                +"CustomDay1 INTEGER,"
                +"CustomYear2 INTEGER,"
                +"CustomMonth2 INTEGER,"
                +"CustomDay2 INTEGER,"
                +"CustomYear3 INTEGER,"
                +"CustomMonth3 INTEGER,"
                +"CustomDay3 INTEGER,"
                +"flag1 INTEGER,"
                +"flag2 INTEGER,"
                +"flag3 INTEGER,"
                +"flag4 INTEGER,"
                +"flag5 INTEGER,"
                +"flag6 INTEGER,"
                +"flag7 INTEGER"
                + ");";
        db.execSQL(sql);
    }

    /**
     * データベースのバージョンアップ時に実行される処理
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // テーブルの破棄と再作成
        //db.execSQL("drop table notifier_table;");
        db.execSQL(DROP);
        onCreate(db);
    }

}
