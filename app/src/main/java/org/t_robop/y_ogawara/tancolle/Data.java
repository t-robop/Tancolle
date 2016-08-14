package org.t_robop.y_ogawara.tancolle;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by yuusuke on 16/06/10.
 */
public class Data {


    //固有ID
    private int id;
    //名前
    private String name;
    //ふりがな
    private String kana;
    //誕生日
    private int birthday;
    //年
    private int year = Integer.MIN_VALUE;
    //月
    private int month = Integer.MIN_VALUE;
    //日
    private int day = Integer.MIN_VALUE;
    //カテゴリ
    private String category;
    //twitterのID
    private String twitterID;
    //メモ欄
    private String memo;
    //画像(詳細画面用)
    private String image;
    //画像(検索画面)
    private String smallImage;
    //presentを買ったか
    //ここで、int型の最小値を初期値に設定、そうしないと0や-の値が使えなくなる
    private int presentFlag = Integer.MIN_VALUE;
    //年齢を固定するか
    private int yukarin = Integer.MIN_VALUE;
    //通知を前日にするか
    private int notif_yest = Integer.MIN_VALUE;
    //通知を当日にするか
    private int notif_today = Integer.MIN_VALUE;
    //通知を何日前にするか
    private int notif_month = Integer.MIN_VALUE;
    //通知を何回繰り返すか
    private int notif_week = Integer.MIN_VALUE;

    private int notif_cus1 = Integer.MIN_VALUE;
    private int notif_cus2 = Integer.MIN_VALUE;
    private int notif_cus3 = Integer.MIN_VALUE;

    public int getNotif_cus2() {
        return notif_cus2;
    }

    public void setNotif_cus2(int notif_cus2) {
        this.notif_cus2 = notif_cus2;
    }

    public int getNotif_cus1() {
        return notif_cus1;
    }

    public void setNotif_cus1(int notif_cus1) {
        this.notif_cus1 = notif_cus1;
    }

    public int getNotif_cus3() {
        return notif_cus3;
    }

    public void setNotif_cus3(int notif_cus3) {
        this.notif_cus3 = notif_cus3;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNotif_week() {
        return notif_week;
    }

    public void setNotif_week(int notif_week) {
        this.notif_week = notif_week;
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

    public int getBirthday() {
        return birthday;
    }

    public void setBirthday(int birthday) {
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

    public void setPresentFlag(int presentFlag) {
        this.presentFlag = presentFlag;
    }

    public int isYukarin() {
        return yukarin;
    }

    public void setYukarin(int yukarin) {

        this.yukarin = yukarin;
    }

    public int isNotif_yest() {
        return notif_yest;
    }

    public void setNotif_yest(int notif_yest) {

        this.notif_yest = notif_yest;
    }

    public int isNotif_today() {
        return notif_today;
    }

    public void setNotif_today(int notif_today) {

        this.notif_today = notif_today;
    }

    public int getNotif_month() {
        return notif_month;
    }

    public void setNotif_month(int notif_month) {
        this.notif_month = notif_month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }



    void error(Context context){
        Toast.makeText(context, "0か1を入力させてね", Toast.LENGTH_LONG).show();

    }

}