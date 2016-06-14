package org.t_robop.y_ogawara.tancolle;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yuusuke on 16/06/13.
 */
class MySQLiteOpenHelper extends SQLiteOpenHelper {


    static final String DB_NAME = "tancolle.db";  //データベースの名前
    static final int DB_VERSION = 1;             //データベースのバージョン
    static final String DROP = "drop table users_table";   //データベースを下ろすSQL文


    /**
     * データベースファイル初回使用時に実行される処理
    //名前
    String name;
    //ふりがな
    String kana;
    //誕生日
    long birthday;
    //カテゴリ
    String category;
    //twitterのID
    String twitterID;
    //メモ欄
    String memo;
    //画像(詳細画面用)
    String image;
    //画像(検索画面)
    String smallImage;
    //presentを買ったか
    int presentFlag;
    //年齢を固定するか
    int tamura;
    //通知を前日にするか
    int notif_yest;
    //通知を当日にするか
    int notif_today;
    //通知を何日前にするか
    int notif_day;
    //通知を繰り返すか
    int notif_recy;
     */

    // コンストラクタ
    public MySQLiteOpenHelper(Context c) {
        super(c, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // テーブル作成のクエリを発行
        String sql = "CREATE TABLE users_table ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT,"
                +"kana TEXT,"
                +"birthday long,"
                +"category TEXT,"
                +"twitterID TEXT,"
                +"memo TEXT,"
                +"image TEXT,"
                +"smallImage TEXT,"
                +"presentFlag INTEGER,"
                +"tamura INTEGER,"
                +"notif_yest INTEGER,"
                +"notif_today INTEGER,"
                +"notif_day INTEGER,"
                +"notif_recy INTEGER"
                + ");";
        db.execSQL(sql);
    }

    /**
     * データベースのバージョンアップ時に実行される処理
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // テーブルの破棄と再作成
        //db.execSQL("drop table users_table;");
        db.execSQL(DROP);
        onCreate(db);
    }
}
