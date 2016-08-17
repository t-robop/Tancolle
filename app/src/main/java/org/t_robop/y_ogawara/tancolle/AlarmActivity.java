package org.t_robop.y_ogawara.tancolle;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Calendar calendar;
        int intentId;
        int year, month, day; //現在の年月日
        int birthyear, birthmonth,birthday; //誕生日の年月日
        int cat,dog; //月日を４桁にするやつ（現在の日付と誕生日）
        int Mnotif,Wnotif,Ynotif,Tnotif; //通知が１ヶ月前１週間前１日前当日のフラグ
        int ms = 1000*60*60*24; //１日をミリ秒にしたやつ


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        Intent intent = getIntent();
        intentId = intent.getIntExtra("id", 1);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1; //0から11までしかないから１個足す
        day = calendar.get(Calendar.DAY_OF_MONTH);

        Data data = dbAssist.idSelect(intentId, this);
        birthyear = data.getYear();
        birthmonth = data.getMonth();
        birthday = data.getDay();
        Mnotif = data.getNotif_month();
        Wnotif = data.getNotif_week();
        Ynotif = data.isNotif_yest();
        Tnotif = data.isNotif_today();

        cat = birthmonth * 100 + birthday;  //誕生日を７月１４日を→７１４みたいな形に
        dog = month * 100 + day; //現在の日付を６月１５日→６１５みたいな形に
        //データのフォーマット
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        //date型の宣言
        Date dateTo = null;
        Date dateFrom = null;

        // 日付を作成します。
        try {
            //TODO ココらへんで今年の誕生日が過ぎていたら、来年の誕生日で計算させる
            dateFrom = sdf.parse(year + "/" + month + "/" + day); //現在の日付
            //指定フォーマットでデータを入力
            if (cat < dog) {  //誕生日より今日の日にちが大きかったら（もう誕生日がきていたら
                int num;
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


        if(Mnotif==1){ //一ヶ月前にチェックがついていたら

            Calendar c = Calendar.getInstance();
            c.set(year,birthmonth,birthday); //次の誕生日
            c.add(Calendar.MONTH, -1); // 1ヶ月とする
            int aaa= c.get(Calendar.YEAR);
            int bbb= c.get(Calendar.MONTH);
            int ccc= c.get(Calendar.DATE);






        }
        if(Wnotif==1){ //一週間前のチェックがついていたら
            // 現在から見て何日後＝（次の誕生日のミリ秒ー７日をミリ秒にしたやつー現在のミリ秒）÷日付換算
            long Wmsday = (dateTimeTo - 7*ms - dateTimeFrom) / (ms);
            //int型に変換
            int Wday = (int) Wmsday;
            //呼び出す日時を設定する
            Calendar triggerTime = Calendar.getInstance();
            triggerTime.add(Calendar.DATE, Wday);	//
            //設定した日時で発行するIntentを生成
            Intent intenta = new Intent(AlarmActivity.this, Notifier.class);
            PendingIntent sender = PendingIntent.getBroadcast(AlarmActivity.this, 0, intenta, PendingIntent.FLAG_UPDATE_CURRENT);

//日時と発行するIntentをAlarmManagerにセットします
            AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
            manager.set(AlarmManager.RTC_WAKEUP, triggerTime.getTimeInMillis(), sender);

        }

        if(Ynotif==1){ //前日にチェックがついていたら
            // 現在から見て何日後＝（次の誕生日のミリ秒ー１日をミリ秒にしたやつー現在のミリ秒）÷日付換算
            long Ymsday = (dateTimeTo - ms - dateTimeFrom) / (ms);
            //int型に変換
            int Yday = (int) Ymsday;
            //呼び出す日時を設定する
            Calendar triggerTime = Calendar.getInstance();
            triggerTime.add(Calendar.DATE, Yday);	//
            //設定した日時で発行するIntentを生成
            Intent intentb = new Intent(AlarmActivity.this, Notifier.class);
            PendingIntent sender = PendingIntent.getBroadcast(AlarmActivity.this, 0, intentb, PendingIntent.FLAG_UPDATE_CURRENT);

//日時と発行するIntentをAlarmManagerにセットします
            AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
            manager.set(AlarmManager.RTC_WAKEUP, triggerTime.getTimeInMillis(), sender);

        }

        if(Tnotif==1){ //当日にチェックがついていたら
            // 現在から見て何日後＝（次の誕生日のミリ秒ー現在のミリ秒）÷日付換算
            long Tmsday = (dateTimeTo - dateTimeFrom) / (ms);
            //int型に変換
            int Tday = (int) Tmsday;
            //呼び出す日時を設定する
            Calendar triggerTime = Calendar.getInstance();
            triggerTime.add(Calendar.DATE, Tday);	//
            //設定した日時で発行するIntentを生成
            Intent intentc = new Intent(AlarmActivity.this, Notifier.class);
            PendingIntent sender = PendingIntent.getBroadcast(AlarmActivity.this, 0, intentc, PendingIntent.FLAG_UPDATE_CURRENT);

//日時と発行するIntentをAlarmManagerにセットします
            AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
            manager.set(AlarmManager.RTC_WAKEUP, triggerTime.getTimeInMillis(), sender);

        }




    }
}
