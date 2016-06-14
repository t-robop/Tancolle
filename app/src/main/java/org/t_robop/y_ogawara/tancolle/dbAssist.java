package org.t_robop.y_ogawara.tancolle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by yuusuke on 16/06/14.
 */
public class dbAssist {
    static SQLiteDatabase mydb; //MySQLiteOpenHelperの変数
    static MySQLiteOpenHelper hlpr;
    static ContentValues cv;
    static Cursor cursor;

    static String openDB = "select * from users_table order by name desc, kana desc, birthday desc,category desc," +
            "twitterID desc,memo desc,image desc,smallImage desc,presentFlag desc,tamura desc,notif_yest desc," +
            "notif_today desc,notif_day desc,notif_recy desc";;
    final static String TABLE_NAME = "users_table";


    public dbAssist(Context context) {
        hlpr = new MySQLiteOpenHelper(context);
        mydb = hlpr.getWritableDatabase();
    }

    //データを追加するメソッド
    public static void insertData(Data data, Context context) {
        hlpr = new MySQLiteOpenHelper(context);
        mydb = hlpr.getWritableDatabase();
        cv = new ContentValues();
        cv.put("name", data.getName());
        cv.put("kana", data.getKana());
        cv.put("birthday", data.getBirthday());
        cv.put("category", data.getCategory());
        cv.put("twitterID", data.getTwitterID());
        cv.put("memo", data.getMemo());
        cv.put("image", data.getTwitterID());
        cv.put("smallImage", data.getSmallImage());
        cv.put("presentFlag", data.isPresentFlag());
        cv.put("tamura", data.isTamura());
        cv.put("notif_yest", data.isNotif_today());
        cv.put("notif_today", data.isNotif_today());
        cv.put("notif_day", data.getNotif_day());
        cv.put("notif_recy", data.getNotif_recy());

        mydb.insert(TABLE_NAME, null, cv);
        mydb.close();
    }

    //全てのデータを取得するメソッド
    public static ArrayList allSelect(Context context){
        ArrayList <Data> DataArray=new ArrayList<Data>();
        hlpr = new MySQLiteOpenHelper(context);
        mydb = hlpr.getWritableDatabase();
        cursor = mydb.rawQuery(openDB,null);
        while (cursor.moveToNext()) {
            Data data = new Data();
            data.setId(cursor.getInt(0));
            data.setName(cursor.getString(cursor.getColumnIndex("name")));
            data.setKana(cursor.getString(cursor.getColumnIndex("kana")));
            data.setBirthday(cursor.getColumnIndex("birthday"));
            data.setCategory(cursor.getString(cursor.getColumnIndex("category")));
            data.setTwitterID(cursor.getString(cursor.getColumnIndex("twitterID")));
            data.setMemo(cursor.getString(cursor.getColumnIndex("memo")));
            data.setImage(cursor.getString(cursor.getColumnIndex("image")));
            data.setSmallImage(cursor.getString(cursor.getColumnIndex("smallImage")));
            data.setPresentFlag(cursor.getInt(9));
            data.setTamura(cursor.getInt(10));
            data.setNotif_yest(cursor.getInt(11));
            data.setNotif_today(cursor.getInt(12));
            data.setNotif_day(cursor.getInt(13));
            data.setNotif_recy(cursor.getInt(14));
            DataArray.add(data);
        }
        mydb.close();
        return DataArray;
    }

    //仮名検索をするメソッド
    public static ArrayList kanaSelect(Context context ,String kana){
        String sqlstr = "select *"
                +"from users_table "
                + "where kana like" +"'%"+kana+"%'"
                + "order by name desc, kana desc, birthday desc,category desc," +
                "twitterID desc,memo desc,image desc,smallImage desc,presentFlag desc,tamura desc,notif_yest desc," +
                "notif_today desc,notif_day desc,notif_recy desc";

        //String sql    = "SELECT * FROM users_table WHERE `tag_name` = '?";
        ArrayList <Data> DataArray=new ArrayList<Data>();
        hlpr = new MySQLiteOpenHelper(context);
        mydb = hlpr.getWritableDatabase();
        cursor = mydb.rawQuery(sqlstr,null);
        while(cursor.moveToNext()) {
            Data data = new Data();
            data.setId(cursor.getInt(0));
            data.setName(cursor.getString(cursor.getColumnIndex("name")));
            data.setKana(cursor.getString(cursor.getColumnIndex("kana")));
            data.setBirthday(cursor.getColumnIndex("birthday"));
            data.setCategory(cursor.getString(cursor.getColumnIndex("category")));
            data.setTwitterID(cursor.getString(cursor.getColumnIndex("twitterID")));
            data.setMemo(cursor.getString(cursor.getColumnIndex("memo")));
            data.setImage(cursor.getString(cursor.getColumnIndex("image")));
            data.setSmallImage(cursor.getString(cursor.getColumnIndex("smallImage")));
            data.setPresentFlag(cursor.getInt(9));
            data.setTamura(cursor.getInt(10));
            data.setNotif_yest(cursor.getInt(11));
            data.setNotif_today(cursor.getInt(12));
            data.setNotif_day(cursor.getInt(13));
            data.setNotif_recy(cursor.getInt(14));
            DataArray.add(data);
        }
        mydb.close();

        return DataArray;
    }


    //idから一つの行を検索するメソッド
    public static Data idSelect(Context context ,int id){
        String sqlstr = "select *"
                +"from users_table "
                + "where _id like" +"'%"+id+"%'"
                + "order by name desc, kana desc, birthday desc,category desc," +
                "twitterID desc,memo desc,image desc,smallImage desc,presentFlag desc,tamura desc,notif_yest desc," +
                "notif_today desc,notif_day desc,notif_recy desc";
        hlpr = new MySQLiteOpenHelper(context);
        mydb = hlpr.getWritableDatabase();
        cursor = mydb.rawQuery(sqlstr,null);
        cursor.moveToNext();
        Data data = new Data();
        data.setId(cursor.getInt(0));
        data.setName(cursor.getString(cursor.getColumnIndex("name")));
        data.setKana(cursor.getString(cursor.getColumnIndex("kana")));
        data.setBirthday(cursor.getColumnIndex("birthday"));
        data.setCategory(cursor.getString(cursor.getColumnIndex("category")));
        data.setTwitterID(cursor.getString(cursor.getColumnIndex("twitterID")));
        data.setMemo(cursor.getString(cursor.getColumnIndex("memo")));
        data.setImage(cursor.getString(cursor.getColumnIndex("image")));
        data.setSmallImage(cursor.getString(cursor.getColumnIndex("smallImage")));
        data.setPresentFlag(cursor.getInt(9));
        data.setTamura(cursor.getInt(10));
        data.setNotif_yest(cursor.getInt(11));
        data.setNotif_today(cursor.getInt(12));
        data.setNotif_day(cursor.getInt(13));
        data.setNotif_recy(cursor.getInt(14));
        mydb.close();
        return data;
    }


    //SQL文を使いたい時のメソッド
    public static ArrayList rawSelect(String dbString,Context context){
        ArrayList <Data> DataArray=new ArrayList<Data>();
        hlpr = new MySQLiteOpenHelper(context);
        mydb = hlpr.getWritableDatabase();
        cursor = mydb.rawQuery(dbString,null);
        while(cursor.moveToNext()) {
            Data data = new Data();
            data.setId(cursor.getInt(0));
            data.setName(cursor.getString(cursor.getColumnIndex("name")));
            data.setKana(cursor.getString(cursor.getColumnIndex("kana")));
            data.setBirthday(cursor.getColumnIndex("birthday"));
            data.setCategory(cursor.getString(cursor.getColumnIndex("category")));
            data.setTwitterID(cursor.getString(cursor.getColumnIndex("twitterID")));
            data.setMemo(cursor.getString(cursor.getColumnIndex("memo")));
            data.setImage(cursor.getString(cursor.getColumnIndex("image")));
            data.setSmallImage(cursor.getString(cursor.getColumnIndex("smallImage")));
            data.setPresentFlag(cursor.getInt(9));
            data.setTamura(cursor.getInt(10));
            data.setNotif_yest(cursor.getInt(11));
            data.setNotif_today(cursor.getInt(12));
            data.setNotif_day(cursor.getInt(13));
            data.setNotif_recy(cursor.getInt(14));
            DataArray.add(data);
        }
        mydb.close();

        return DataArray;
    }

    //データを削除したい時のメソッド
    public static void deleteData(int id){
        mydb.delete(TABLE_NAME, "_id = " + id, null);
    }

    //行のデータを更新したい時のメソッド
    public static void updateData(int id,Data data){
        if (data.getName()!=null){
            cv.put("name", data.getName());
        }
        if (data.getKana()!=null){
            cv.put("kana", data.getKana());
        }
        if (data.getBirthday()!=0){
            cv.put("birthday", data.getBirthday());
        }
        if (data.getCategory()!=null){
            cv.put("category", data.getCategory());
        }
        if (data.getTwitterID()!=null){
            cv.put("twitterID", data.getTwitterID());
        }
        if (data.getMemo()!=null){
            cv.put("memo", data.getMemo());
        }
        if (data.getImage()!=null){
            cv.put("image", data.getTwitterID());
        }
        if (data.getSmallImage()!=null){
            cv.put("smallImage", data.getSmallImage());
        }
        if (data.isPresentFlag()!=0){
            cv.put("presentFlag", data.isPresentFlag());
        }
        if (data.isPresentFlag()!=0){
            cv.put("tamura", data.isTamura());
        }
        if (data.isNotif_yest()!=0){
            cv.put("notif_yest", data.isNotif_today());
        }
        if (data.isNotif_today()!=0){
            cv.put("notif_today", data.isNotif_today());
        }
        if (data.getNotif_day()!=0){
            cv.put("notif_day", data.getNotif_day());
        }
        if (data.getNotif_recy()!=0){
            cv.put("notif_recy", data.getNotif_recy());
        }

        mydb.update(TABLE_NAME, cv, "_id ="+id, null);
    }
}
