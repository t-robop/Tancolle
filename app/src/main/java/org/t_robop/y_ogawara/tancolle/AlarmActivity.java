package org.t_robop.y_ogawara.tancolle;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmActivity extends AppCompatActivity {

    static int Mnotif,Wnotif,Ynotif,Tnotif; //通知が１ヶ月前１週間前１日前当日のフラグ
    static int custum1,custum2,custum3;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Calendar calendar;
        int intentId;
        int year, month, day; //現在の年月日
        int birthmonth,birthday; //誕生日の年月日
        int cat,dog; //月日を４桁にするやつ（現在の日付と誕生日）
        int ms = 1000*60*60*24; //１日をミリ秒にしたやつ


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
        Log.d(String.valueOf(custum1),"test");
        custum2 = data.getNotif_cus2();
        custum3 = data.getNotif_cus3();
        String name = data.getName();
        int num =0; //numの初期値を０にする




        cat = birthmonth * 100 + birthday;  //誕生日を７月１４日を→７１４みたいな形に
        dog = month * 100 + day; //現在の日付を６月１５日→６１５みたいな形に
        //データのフォーマット
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        //date型の宣言
        Date dateTo = null; //次の誕生日
        Date dateFrom = null; //現在の日付
        Date dateBeforeMonth = null;//誕生日の一ヶ月前の日付
        Date datecus1=null;
        Date datecus2=null;
        Date datecus3=null;

        // 日付を作成します。
        try {
            //TODO ココらへんで今年の誕生日が過ぎていたら、来年の誕生日で計算させる
            dateFrom = sdf.parse(year + "/" + month + "/" + day); //現在の日付
            //指定フォーマットでデータを入力
            if (cat < dog) {  //誕生日より今日の日にちが大きかったら（もう誕生日がきていたら
                num = year + 1; //＋１して来年の誕生日の数値を割り出す
                dateTo = sdf.parse(num + "/" + birthmonth + "/" + birthday);
            } else {  //まだ誕生日がきていなかったら
                dateTo = sdf.parse(year + "/" + birthmonth + "/" + birthday); //今年の誕生日を作る
            }
            //エラーが起きた時
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 日付をミリ秒にしてlong値に変換します。
        long dateTimeTo = dateTo.getTime(); //次の誕生日のミリ秒
        long dateTimeFrom = dateFrom.getTime(); //現在の日付のミリ秒


    //TODO てすとおおおおおおおおおぶおおおおおおおおおおおおおおおおおおおおおお
        if(Mnotif==1){ //一ヶ月前にチェックがついていたら
            Calendar nextBirth = Calendar.getInstance(); //カレンダー型の宣言
            if(num==0){ //numを使ってなければ（まだ誕生日が来てなければ）
                nextBirth.set(year,birthmonth,birthday); //次の誕生日をセット
            }else{ //numを使っていたら（誕生日がもう過ぎていれば）
                nextBirth.set(num,birthmonth,birthday); //次の誕生日をセット（numの中身は次の誕生日の年）
            }
            nextBirth.add(Calendar.MONTH, -1); //セットした日付からマイナス1ヶ月する
            //ー１ヶ月されることにより年も日付もガラッと変わる場合があるので
            int aaa= nextBirth.get(Calendar.YEAR); //ー１ヶ月された時の年
            int bbb= nextBirth.get(Calendar.MONTH); //ー１ヶ月された時の月
            int ccc= nextBirth.get(Calendar.DATE);  //ー１ヶ月された時の日付 を取得
            try{
                dateBeforeMonth = sdf.parse(aaa + "/" + bbb + "/" + ccc); //1ヶ月前の日付を作る
            }catch(ParseException e) {
                e.printStackTrace();
            }
            long dateTimeBeforeMonth = dateBeforeMonth.getTime();//ミリ秒に変換
            long Mmsday = (dateTimeBeforeMonth-dateTimeFrom)/(ms);
            int Mday=(int)Mmsday;
            Calendar triggerTime = Calendar.getInstance();
            triggerTime.add(Calendar.MINUTE, Mday);	//

            //monthText = (name) + "さんの誕生日まで残り１ヶ月です";
            //設定した日時で発行するIntentを生成
            Intent alarmMonth = new Intent(AlarmActivity.this, Notifier.class);
            alarmMonth.putExtra("intentId",1);
            alarmMonth.putExtra("intentString",(name) + "さんの誕生日まで残り１ヶ月です");
            PendingIntent sender = PendingIntent.getBroadcast(AlarmActivity.this, 0, alarmMonth, PendingIntent.FLAG_UPDATE_CURRENT);

            //日時と発行するIntentをAlarmManagerにセットします
            AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);

            //Androidのバージョンが6.0以上(Dozeモードがあるバージョン)以上なら設定時間より15分遅れるかも
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,triggerTime.getTimeInMillis(),sender);
            }else{
                manager.set(AlarmManager.RTC_WAKEUP, triggerTime.getTimeInMillis(), sender);
            }
            //manager.set(AlarmManager.RTC_WAKEUP, triggerTime.getTimeInMillis(), sender);
        }
        if(Wnotif==1){ //一週間前のチェックがついていたら
            // 現在から見て何日後＝（次の誕生日のミリ秒ー７日をミリ秒にしたやつー現在のミリ秒）÷日付換算
            long Wmsday = (dateTimeTo - 7*ms - dateTimeFrom) / (ms);
            //int型に変換
            int Wday = (int) Wmsday;
            //呼び出す日時を設定する
            Calendar triggerTime = Calendar.getInstance();
            triggerTime.add(Calendar.SECOND, Wday);	//
            //weekText = (name) + "さんの誕生日まで残り１週間です";
            //設定した日時で発行するIntentを生成
            Intent alarmWeek = new Intent(AlarmActivity.this, Notifier.class);
            alarmWeek.putExtra("intentId",2);
            alarmWeek.putExtra("intentString",(name) + "さんの誕生日まで残り１週間です");


            PendingIntent sender = PendingIntent.getBroadcast(AlarmActivity.this, 1, alarmWeek, PendingIntent.FLAG_UPDATE_CURRENT);
//日時と発行するIntentをAlarmManagerにセットします
            AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);

            //Androidのバージョンが6.0以上(Dozeモードがあるバージョン)以上なら設定時間より15分遅れるかも
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,triggerTime.getTimeInMillis(),sender);
            }else{
                manager.set(AlarmManager.RTC_WAKEUP, triggerTime.getTimeInMillis(), sender);
            }



        }

        if(Ynotif==1){ //前日にチェックがついていたら
            // 現在から見て何日後＝（次の誕生日のミリ秒ー１日をミリ秒にしたやつー現在のミリ秒）÷日付換算
            long Ymsday = (dateTimeTo - ms - dateTimeFrom) / (ms);
            //int型に変換
            int Yday = (int) Ymsday;
            //呼び出す日時を設定する
            Calendar triggerTime = Calendar.getInstance();
            triggerTime.add(Calendar.SECOND, Yday);	//
            //yestText = (name) + "さんの誕生日まで残り１日です";
            //設定した日時で発行するIntentを生成
            Intent alarmYest= new Intent(AlarmActivity.this, Notifier.class);
            alarmYest.putExtra("intentId",3);
            alarmYest.putExtra("intentString",(name) + "さんの誕生日まで残り１日です");



            PendingIntent sender = PendingIntent.getBroadcast(AlarmActivity.this, 2, alarmYest, PendingIntent.FLAG_UPDATE_CURRENT);
//日時と発行するIntentをAlarmManagerにセットします
            AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
            //Androidのバージョンが6.0以上(Dozeモードがあるバージョン)以上なら設定時間より15分遅れるかも
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,triggerTime.getTimeInMillis(),sender);
            }else{
                manager.set(AlarmManager.RTC_WAKEUP, triggerTime.getTimeInMillis(), sender);
            }
            //manager.set(AlarmManager.RTC_WAKEUP, triggerTime.getTimeInMillis(), sender);
        }

        if(Tnotif==1){ //当日にチェックがついていたら
            // 現在から見て何日後＝（次の誕生日のミリ秒ー現在のミリ秒）÷日付換算
            long Tmsday = (dateTimeTo - dateTimeFrom) / (ms);
            //int型に変換
            int Tday = (int) Tmsday;
            //呼び出す日時を設定する
            Calendar triggerTime = Calendar.getInstance();
            triggerTime.add(Calendar.MINUTE, Tday);	//
            //todayText = "今日は" + (name) + "さんの誕生日です!";
            //設定した日時で発行するIntentを生成
            Intent alarmToday = new Intent(AlarmActivity.this, Notifier.class);
            alarmToday.putExtra("intentId",4);
            alarmToday.putExtra("intentString","今日は" + (name) + "さんの誕生日です!");

            PendingIntent sender = PendingIntent.getBroadcast(AlarmActivity.this, 3, alarmToday, PendingIntent.FLAG_UPDATE_CURRENT);
//日時と発行するIntentをAlarmManagerにセットします
            AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
            //Androidのバージョンが6.0以上(Dozeモードがあるバージョン)以上なら設定時間より15分遅れるかも
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,triggerTime.getTimeInMillis(),sender);
            }else{
                manager.set(AlarmManager.RTC_WAKEUP, triggerTime.getTimeInMillis(), sender);
            }
            //manager.set(AlarmManager.RTC_WAKEUP, triggerTime.getTimeInMillis(), sender);
        }
        if(custum1>0){
            int aaa=custum1/10000;
            int bbb=custum1%10000/100;
            int ccc=custum1%100;
            try{
                datecus1 = sdf.parse(aaa + "/" + bbb + "/" + ccc); //1ヶ月前の日付を作る
            }catch(ParseException e) {
                e.printStackTrace();
            }
            long dateTimeCus1 = datecus1.getTime();//ミリ秒に変換
            long cus1day = (dateTimeCus1-dateTimeFrom)/(ms);
            int custum1day=(int)cus1day;
            Calendar triggerTime = Calendar.getInstance();
            triggerTime.add(Calendar.MINUTE, custum1day);	//
            //custumText =String.valueOf(birthmonth)+"/"+String.valueOf(birthday)+"は"+(name)+"さんの誕生日です";
            //設定した日時で発行するIntentを生成
            Intent alarmCus1 = new Intent(AlarmActivity.this, Notifier.class);
            alarmCus1.putExtra("intentId",5);
            alarmCus1.putExtra("intentString",String.valueOf(birthmonth)+"/"+String.valueOf(birthday)+"は"+(name)+"さんの誕生日です");

            PendingIntent sender = PendingIntent.getBroadcast(AlarmActivity.this, 4, alarmCus1, PendingIntent.FLAG_UPDATE_CURRENT);

            //日時と発行するIntentをAlarmManagerにセットします
            AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
            //Androidのバージョンが6.0以上(Dozeモードがあるバージョン)以上なら設定時間より15分遅れるかも
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,triggerTime.getTimeInMillis(),sender);
            }else{
                manager.set(AlarmManager.RTC_WAKEUP, triggerTime.getTimeInMillis(), sender);
            }
            //manager.set(AlarmManager.RTC_WAKEUP, triggerTime.getTimeInMillis(), sender);
        }

        if(custum2>0){
            int aaa=custum2/10000;
            int bbb=custum2%10000/100;
            int ccc=custum2%100;
            try{
                datecus2 = sdf.parse(aaa + "/" + bbb + "/" + ccc); //日付を作る
            }catch(ParseException e) {
                e.printStackTrace();
            }
            long dateTimeCus2 = datecus2.getTime();//ミリ秒に変換
            long cus2day = (dateTimeCus2-dateTimeFrom)/(ms);
            int custum2day=(int)cus2day;
            Calendar triggerTime = Calendar.getInstance();
            triggerTime.add(Calendar.MINUTE, custum2day);	//
            //custumText =String.valueOf(birthmonth)+"/"+String.valueOf(birthday)+"は"+(name)+"さんの誕生日です";
            //設定した日時で発行するIntentを生成
            Intent alarmCus2 = new Intent(AlarmActivity.this, Notifier.class);
            alarmCus2.putExtra("intentId",5);
            alarmCus2.putExtra("intentString",String.valueOf(birthmonth)+"/"+String.valueOf(birthday)+"は"+(name)+"さんの誕生日です");

            PendingIntent sender = PendingIntent.getBroadcast(AlarmActivity.this, 5, alarmCus2, PendingIntent.FLAG_UPDATE_CURRENT);

            //日時と発行するIntentをAlarmManagerにセットします
            AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
            //Androidのバージョンが6.0以上(Dozeモードがあるバージョン)以上なら設定時間より15分遅れるかも
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,triggerTime.getTimeInMillis(),sender);
            }else{
                manager.set(AlarmManager.RTC_WAKEUP, triggerTime.getTimeInMillis(), sender);
            }
            //manager.set(AlarmManager.RTC_WAKEUP, triggerTime.getTimeInMillis(), sender);
        }

        if(custum3>0){
            int aaa=custum3/10000;
            int bbb=custum3%10000/100;
            int ccc=custum3%100;
            try{
                datecus3 = sdf.parse(aaa + "/" + bbb + "/" + ccc); //1ヶ月前の日付を作る
            }catch(ParseException e) {
                e.printStackTrace();
            }
            long dateTimeCus3 = datecus3.getTime();//ミリ秒に変換
            long cus3day = (dateTimeCus3-dateTimeFrom)/(ms);
            int custum3day=(int)cus3day;
            Calendar triggerTime = Calendar.getInstance();
            triggerTime.add(Calendar.MINUTE, custum3day);
            //custumText =String.valueOf(birthmonth)+"/"+String.valueOf(birthday)+"は"+(name)+"さんの誕生日です";
            //設定した日時で発行するIntentを生成
            Intent alarmCus3 = new Intent(AlarmActivity.this, Notifier.class);
            alarmCus3.putExtra("intentId",5);
            alarmCus3.putExtra("intentString",String.valueOf(birthmonth)+"/"+String.valueOf(birthday)+"は"+(name)+"さんの誕生日です");


            PendingIntent sender = PendingIntent.getBroadcast(AlarmActivity.this, 6, alarmCus3, PendingIntent.FLAG_UPDATE_CURRENT);

            //日時と発行するIntentをAlarmManagerにセットします
            AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
            //Androidのバージョンが6.0以上(Dozeモードがあるバージョン)以上なら設定時間より15分遅れるかも
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,triggerTime.getTimeInMillis(),sender);
            }else{
                manager.set(AlarmManager.RTC_WAKEUP, triggerTime.getTimeInMillis(), sender);
            }
            //manager.set(AlarmManager.RTC_WAKEUP, triggerTime.getTimeInMillis(), sender);
        }

    }
}