package org.t_robop.y_ogawara.tancolle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Calendar calendar;
        int intentId;
        int mNotif, wNotif, yNotif, tNotif; //通知が１ヶ月前１週間前１日前当日のフラグ
        int custom1, custom2, custom3;
        int year, month, day; //現在の年月日
        int birthMonth, birthday; //誕生日の年月日


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        Intent intent = getIntent();
        intentId = intent.getIntExtra("usagi", 1);

        calendar = Calendar.getInstance(); //今日の年月日
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1; //0から11だから１個足す
        day = calendar.get(Calendar.DAY_OF_MONTH);


        Data data = dbAssist.idSelect(intentId, this); //データの取得
        birthMonth = data.getMonth();
        birthday = data.getDay();
        mNotif = data.getNotif_month();  //フラグ
        wNotif = data.getNotif_week();
        yNotif = data.isNotif_yest();
        tNotif = data.isNotif_today();
        custom1 = data.getNotif_cus1();
        custom2 = data.getNotif_cus2();
        custom3 = data.getNotif_cus3();
        String name = data.getName();

        //通知を出す
        Notifier.alarm(intentId, name, year, month, day, birthMonth, birthday, mNotif, wNotif, yNotif, tNotif, custom1, custom2, custom3, this);

    }
}