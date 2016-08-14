package org.t_robop.y_ogawara.tancolle;

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
        int notif_m,notif_w,notif_y,notif_t; //通知が１ヶ月前１週間前１日前当日のフラグ


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
        //notif_m
        //notif_w
        notif_y = data.isNotif_yest();
        notif_t = data.isNotif_today();

        cat = birthmonth * 100 + birthday;  //誕生日を７月１４日を→７１４みたいな形に
        dog = month * 100 + day; //現在の日付を６月１５日→６１５みたいな形に
        //データのフォーマット
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        //date型の宣言
        Date dateTo = null;
        Date dateFrom = null;

        // 日付を作成します。
        try {
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

        // 日付をlong値に変換します。
        long dateTimeTo = dateTo.getTime(); //次の誕生日
        long dateTimeFrom = dateFrom.getTime(); //現在の日付

        if(notif_y==1){
            // 差分の日数を算出します。
            long dayDiff = (dateTimeTo - 1000*60*60*24 - dateTimeFrom) / (1000 * 60 * 60 * 24);
            //int型に変換
            int remDay = (int) dayDiff;
        }




    }
}
