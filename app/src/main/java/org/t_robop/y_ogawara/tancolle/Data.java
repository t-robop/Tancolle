package org.t_robop.y_ogawara.tancolle;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by yuusuke on 16/06/10.
 */
public class Data {


    //固有ID
    static int id;
    //名前
    static String name;
    //ふりがな
    static String kana;
    //誕生日
    static long birthday;
    //カテゴリ
    static String category;
    //twitterのID
    static String twitterID;
    //メモ欄
    static String memo;
    //画像(詳細画面用)
    static String image;
    //画像(検索画面)
    static String smallImage;
    //presentを買ったか
    static int presentFlag;
    //年齢を固定するか
    static int tamura;
    //通知を前日にするか
    static int notif_yest;
    //通知を当日にするか
    static int notif_today;
    //通知を何日前にするか
    int notif_day;
    //通知を何回繰り返すか
    int notif_recy;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNotif_recy() {
        return notif_recy;
    }

    public void setNotif_recy(int notif_recy) {
        this.notif_recy = notif_recy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getKana() {
        return kana;
    }

    public void setKana(String kana) {
        this.kana = kana;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTwitterID() {
        return twitterID;
    }

    public void setTwitterID(String twitterID) {
        this.twitterID = twitterID;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }

    public int isPresentFlag() {
        return presentFlag;
    }

    public void setPresentFlag(int presentFlag,Context context) {
        if (presentFlag !=0||presentFlag!=1){
            error(context);
        }
        this.presentFlag = presentFlag;
    }

    public int isTamura() {
        return tamura;
    }

    public void setTamura(int tamura, Context context) {
        if (tamura !=0||tamura!=1){
            error(context);
        }
        this.tamura = tamura;
    }

    public int isNotif_yest() {
        return notif_yest;
    }

    public void setNotif_yest(int notif_yest,Context context) {
        if (notif_yest !=0||notif_yest!=1){
            error(context);
        }
        this.notif_yest = notif_yest;
    }

    public int isNotif_today() {
        return notif_today;
    }

    public void setNotif_today(int notif_today,Context context) {
        if (notif_today !=0||notif_today!=1){
            error(context);
        }
        this.notif_today = notif_today;
    }

    public int getNotif_day() {
        return notif_day;
    }

    public void setNotif_day(int notif_day) {
        this.notif_day = notif_day;
    }


    void error(Context context){
        Toast.makeText(context, "0か1を入力させてね", Toast.LENGTH_LONG).show();

    }

}
