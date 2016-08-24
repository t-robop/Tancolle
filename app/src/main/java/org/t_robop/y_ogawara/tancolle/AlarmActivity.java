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
        int Mnotif, Wnotif, Ynotif, Tnotif; //通知が１ヶ月前１週間前１日前当日のフラグ
        int custum1, custum2, custum3;
        int year, month, day; //現在の年月日
        int birthmonth, birthday; //誕生日の年月日


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        Intent intent = getIntent();
        intentId = intent.getIntExtra("usagi", 1);

        calendar = Calendar.getInstance(); //今日の年月日
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1; //0から11だから１個足す
        day = calendar.get(Calendar.DAY_OF_MONTH);


        Data data = dbAssist.idSelect(intentId, this); //データの取得
        birthmonth = data.getMonth();
        birthday = data.getDay();
        Mnotif = data.getNotif_month();  //フラグ
        Wnotif = data.getNotif_week();
        Ynotif = data.isNotif_yest();
        Tnotif = data.isNotif_today();
        custum1 = data.getNotif_cus1();
        custum2 = data.getNotif_cus2();
        custum3 = data.getNotif_cus3();
        String name = data.getName();

        //
        Notifier.alarm(intentId, name, year, month, day, birthmonth, birthday, Mnotif, Wnotif, Ynotif, Tnotif, custum1, custum2, custum3, this);

    }
}