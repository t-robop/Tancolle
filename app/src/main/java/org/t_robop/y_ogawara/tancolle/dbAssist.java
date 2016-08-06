package org.t_robop.y_ogawara.tancolle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by yuusuke on 16/06/14.
 */
//TODO このclassは極力弄らないこと、弄る必要がある場合はメソッドを作り、その中で完結させること
public class dbAssist {
    static SQLiteDatabase sqldb; //MySQLiteOpenHelperの変数
    static MySQLiteOpenHelper sqLiteOpenHelper;
    static ContentValues cv;
    static Cursor cursor;

    //orderで並び順を指定 ASCで昇順  DESCで降順
    static String openDB = "select * from users_table order by id asc";
    final static String TABLE_NAME = "users_table";


    public dbAssist(Context context) {
        sqLiteOpenHelper = new MySQLiteOpenHelper(context);
        sqldb = sqLiteOpenHelper.getWritableDatabase();
    }

    //データを追加するメソッド
    public static void insertData(Data data,Context context) {
        sqLiteOpenHelper = new MySQLiteOpenHelper(context.getApplicationContext());
        sqldb = sqLiteOpenHelper.getWritableDatabase();
        cv = new ContentValues();
        cv.put("name", data.getName());
        cv.put("kana", data.getKana());
        cv.put("birthday", data.getBirthday());
        cv.put("year", data.getYear());
        cv.put("month", data.getMonth());
        cv.put("day", data.getDay());
        cv.put("category", data.getCategory());
        cv.put("twitterID", data.getTwitterID());
        cv.put("memo", data.getMemo());
        cv.put("image", data.getImage());
        cv.put("smallImage", data.getSmallImage());
        cv.put("presentFlag", data.isPresentFlag());
        cv.put("yukarin", data.isYukarin());
        cv.put("notif_yest", data.isNotif_today());
        cv.put("notif_today", data.isNotif_today());
        cv.put("notif_day", data.getNotif_day());
        cv.put("notif_recy", data.getNotif_recy());

        sqldb.insert(TABLE_NAME, null, cv);
        sqldb.close();
    }

    //全てのデータを取得するメソッド
    public static ArrayList allSelect(Context context){
        ArrayList <Data> DataArray= new ArrayList<>();
        sqLiteOpenHelper = new MySQLiteOpenHelper(context);
        sqldb = sqLiteOpenHelper.getWritableDatabase();
        cursor = sqldb.rawQuery(openDB,null);
        while (cursor.moveToNext()) {
            Data data = new Data();
            data.setId(cursor.getInt(0));
            data.setName(cursor.getString(cursor.getColumnIndex("name")));
            data.setKana(cursor.getString(cursor.getColumnIndex("kana")));
            data.setBirthday(cursor.getInt(3));
            data.setYear(cursor.getInt(4));
            data.setMonth(cursor.getInt(5));
            data.setDay(cursor.getInt(6));
            data.setCategory(cursor.getString(cursor.getColumnIndex("category")));
            data.setTwitterID(cursor.getString(cursor.getColumnIndex("twitterID")));
            data.setMemo(cursor.getString(cursor.getColumnIndex("memo")));
            data.setImage(cursor.getString(cursor.getColumnIndex("image")));
            data.setSmallImage(cursor.getString(cursor.getColumnIndex("smallImage")));
            data.setPresentFlag(cursor.getInt(12));
            data.setYukarin(cursor.getInt(13));
            data.setNotif_yest(cursor.getInt(14));
            data.setNotif_today(cursor.getInt(15));
            data.setNotif_day(cursor.getInt(16));
            data.setNotif_recy(cursor.getInt(17));
            DataArray.add(data);
        }
        sqldb.close();
        return DataArray;
    }

    //仮名検索をするメソッド
    public static ArrayList kanaSelect(String kana,Context context){
        String sqlStr = "select *"
                +"from users_table "
                + "where kana like" +"'%"+kana+"%'"
                + "order by kana asc";
        ArrayList <Data> DataArray=new ArrayList<>();
        sqLiteOpenHelper = new MySQLiteOpenHelper(context);
        sqldb = sqLiteOpenHelper.getWritableDatabase();
        cursor = sqldb.rawQuery(sqlStr,null);
        while(cursor.moveToNext()) {
            Data data = new Data();
            data.setId(cursor.getInt(0));
            data.setName(cursor.getString(cursor.getColumnIndex("name")));
            data.setKana(cursor.getString(cursor.getColumnIndex("kana")));
            data.setBirthday(cursor.getInt(3));
            data.setYear(cursor.getInt(4));
            data.setMonth(cursor.getInt(5));
            data.setDay(cursor.getInt(6));
            data.setCategory(cursor.getString(cursor.getColumnIndex("category")));
            data.setTwitterID(cursor.getString(cursor.getColumnIndex("twitterID")));
            data.setMemo(cursor.getString(cursor.getColumnIndex("memo")));
            data.setImage(cursor.getString(cursor.getColumnIndex("image")));
            data.setSmallImage(cursor.getString(cursor.getColumnIndex("smallImage")));
            data.setPresentFlag(cursor.getInt(12));
            data.setYukarin(cursor.getInt(13));
            data.setNotif_yest(cursor.getInt(14));
            data.setNotif_today(cursor.getInt(15));
            data.setNotif_day(cursor.getInt(16));
            data.setNotif_recy(cursor.getInt(17));
            DataArray.add(data);
        }
        sqldb.close();

        return DataArray;
    }

    //何月で検索するメソッド
    public static ArrayList birthdaySelect(int month,Context context){
        String sqlStr = "select *"
                +"from users_table "
                + "where month ="+month
                + " order by day asc";
        ArrayList <Data> DataArray=new ArrayList<>();
        sqLiteOpenHelper = new MySQLiteOpenHelper(context);
        sqldb = sqLiteOpenHelper.getWritableDatabase();
        cursor = sqldb.rawQuery(sqlStr,null);
        while(cursor.moveToNext()) {
            Data data = new Data();
            data.setId(cursor.getInt(0));
            data.setName(cursor.getString(cursor.getColumnIndex("name")));
            data.setKana(cursor.getString(cursor.getColumnIndex("kana")));
            data.setBirthday(cursor.getInt(3));
            data.setYear(cursor.getInt(4));
            data.setMonth(cursor.getInt(5));
            data.setDay(cursor.getInt(6));
            data.setCategory(cursor.getString(cursor.getColumnIndex("category")));
            data.setTwitterID(cursor.getString(cursor.getColumnIndex("twitterID")));
            data.setMemo(cursor.getString(cursor.getColumnIndex("memo")));
            data.setImage(cursor.getString(cursor.getColumnIndex("image")));
            data.setSmallImage(cursor.getString(cursor.getColumnIndex("smallImage")));
            data.setPresentFlag(cursor.getInt(12));
            data.setYukarin(cursor.getInt(13));
            data.setNotif_yest(cursor.getInt(14));
            data.setNotif_today(cursor.getInt(15));
            data.setNotif_day(cursor.getInt(16));
            data.setNotif_recy(cursor.getInt(17));
            DataArray.add(data);
        }
        sqldb.close();

        return DataArray;
    }



    //idから一つの行を検索するメソッド
    public static Data idSelect(int id,Context context){
        String sqlstr = "select *"
                +"from users_table "
                + "where id = "+id;
        sqLiteOpenHelper = new MySQLiteOpenHelper(context);
        sqldb = sqLiteOpenHelper.getWritableDatabase();
        cursor = sqldb.rawQuery(sqlstr,null);
        cursor.moveToNext();
        Data data = new Data();
        data.setId(cursor.getInt(0));
        data.setName(cursor.getString(cursor.getColumnIndex("name")));
        data.setKana(cursor.getString(cursor.getColumnIndex("kana")));
        data.setBirthday(cursor.getInt(3));
        data.setYear(cursor.getInt(4));
        data.setMonth(cursor.getInt(5));
        data.setDay(cursor.getInt(6));
        data.setCategory(cursor.getString(cursor.getColumnIndex("category")));
        data.setTwitterID(cursor.getString(cursor.getColumnIndex("twitterID")));
        data.setMemo(cursor.getString(cursor.getColumnIndex("memo")));
        data.setImage(cursor.getString(cursor.getColumnIndex("image")));
        data.setSmallImage(cursor.getString(cursor.getColumnIndex("smallImage")));
        data.setPresentFlag(cursor.getInt(12));
        data.setYukarin(cursor.getInt(13));
        data.setNotif_yest(cursor.getInt(14));
        data.setNotif_today(cursor.getInt(15));
        data.setNotif_day(cursor.getInt(16));
        data.setNotif_recy(cursor.getInt(17));
        sqldb.close();
        return data;
    }


    //SQL文を使いたい時のメソッド
    public static ArrayList rawSelect(String dbString,Context context){
        ArrayList <Data> DataArray= new ArrayList<>();
        sqLiteOpenHelper = new MySQLiteOpenHelper(context);
        sqldb = sqLiteOpenHelper.getWritableDatabase();
        cursor = sqldb.rawQuery(dbString,null);
        while(cursor.moveToNext()) {
            Data data = new Data();
            data.setId(cursor.getInt(0));
            data.setName(cursor.getString(cursor.getColumnIndex("name")));
            data.setKana(cursor.getString(cursor.getColumnIndex("kana")));
            data.setBirthday(cursor.getInt(3));
            data.setYear(cursor.getInt(4));
            data.setMonth(cursor.getInt(5));
            data.setDay(cursor.getInt(6));
            data.setCategory(cursor.getString(cursor.getColumnIndex("category")));
            data.setTwitterID(cursor.getString(cursor.getColumnIndex("twitterID")));
            data.setMemo(cursor.getString(cursor.getColumnIndex("memo")));
            data.setImage(cursor.getString(cursor.getColumnIndex("image")));
            data.setSmallImage(cursor.getString(cursor.getColumnIndex("smallImage")));
            data.setPresentFlag(cursor.getInt(12));
            data.setYukarin(cursor.getInt(13));
            data.setNotif_yest(cursor.getInt(14));
            data.setNotif_today(cursor.getInt(15));
            data.setNotif_day(cursor.getInt(16));
            data.setNotif_recy(cursor.getInt(17));
            DataArray.add(data);
        }
        sqldb.close();

        return DataArray;
    }

    //データを削除したい時のメソッド
    public static void deleteData(int id,Context context){
        sqLiteOpenHelper = new MySQLiteOpenHelper(context);
        sqldb = sqLiteOpenHelper.getWritableDatabase();
        sqldb.delete(TABLE_NAME, "id = " + id, null);
    }

    //行のデータを更新したい時のメソッド
    public static void updateData(int id,Data data,Context context){
        sqLiteOpenHelper = new MySQLiteOpenHelper(context);
        sqldb = sqLiteOpenHelper.getWritableDatabase();
        cv = new ContentValues();
        if (data.getName()!=null){
            cv.put("name", data.getName());
        }
        if (data.getKana()!=null){
            cv.put("kana", data.getKana());
        }
        if (data.getBirthday()!=0){
            cv.put("birthday", data.getBirthday());
        }
        if (data.getYear()!=Integer.MIN_VALUE){
            cv.put("year", data.getYear());
        }
        if (data.getMonth()!=Integer.MIN_VALUE){
            cv.put("month", data.getMonth());
        }
        if (data.getDay()!=Integer.MIN_VALUE){
            cv.put("day", data.getDay());
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
            cv.put("image", data.getImage());
        }
        if (data.getSmallImage()!=null){
            cv.put("smallImage", data.getSmallImage());
        }
        if (data.isPresentFlag()!=Integer.MIN_VALUE){
            cv.put("presentFlag", data.isPresentFlag());
        }
        if (data.isYukarin()!=Integer.MIN_VALUE){
            cv.put("yukarin", data.isYukarin());
        }
        if (data.isNotif_yest()!=Integer.MIN_VALUE){
            cv.put("notif_yest", data.isNotif_today());
        }
        if (data.isNotif_today()!=Integer.MIN_VALUE){
            cv.put("notif_today", data.isNotif_today());
        }
        if (data.getNotif_day()!=Integer.MIN_VALUE){
            cv.put("notif_day", data.getNotif_day());
        }
        if (data.getNotif_recy()!=Integer.MIN_VALUE){
            cv.put("notif_recy", data.getNotif_recy());
        }
        sqldb.update(TABLE_NAME, cv, "id = "+id, null);
    }
}