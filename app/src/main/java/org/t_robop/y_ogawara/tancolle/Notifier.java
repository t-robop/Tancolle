package org.t_robop.y_ogawara.tancolle;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

//TODO マニフェストのレシーバーうまく設定されとらんで！！！！！！！！！！
public class Notifier extends BroadcastReceiver {

        @Override
        public void onReceive(Context content, Intent intent) {
            if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
                Log.d("ファック","ふぁっきん！！！！！！！！！！！！！！！");
                Toast.makeText(content, "テスト", Toast.LENGTH_LONG).show();
                Calendar calendar;
                int id;
                int Mnotif,Wnotif,Ynotif,Tnotif; //通知が１ヶ月前１週間前１日前当日のフラグ
                int custum1,custum2,custum3;
                int year, month, day; //現在の年月日
                int birthmonth,birthday; //誕生日の年月日


                calendar = Calendar.getInstance(); //今日の年月日
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH) + 1; //0から11だから１個足す
                day = calendar.get(Calendar.DAY_OF_MONTH);
                ArrayList<Data> datas = new ArrayList<>();
                datas = dbAssist.allSelect(content);
                for (int i = 0; i<datas.size();i++) {
                    Data data = datas.get(i); //データの取得
                    id  = data.getId();
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
                    //通知再セット
                    alarm(id,name,year,month,day,birthmonth,birthday,Mnotif,Wnotif,Ynotif,Tnotif,custum1,custum2,custum3,content);
                }

            }

            //絶対にかぶらないID 通知の種類込み　ユーザーID + 9999 + notifId;
            int OriginalId = intent.getIntExtra("intentId", 99990);
            //後ろのintentの種類分けのみ
            int id = originalIdToId(OriginalId);
            String text = intent.getStringExtra("intentString");

            //通知がクリックされた時に発行されるIntentの生成
            Intent sendIntent = new Intent(content, AlarmActivity.class);

            PendingIntent sender = PendingIntent.getActivity(content, OriginalId, sendIntent, 0);

            //:todo idでどの通知かflag判定してるよ
            switch (id){
                //もしもputされてない時 まぁ普通はない
                case 0:

                break;
                //１ヶ月前
                case 1:
                    Notification noti = new NotificationCompat.Builder(content)
                            .setTicker(text)
                            .setContentTitle("たんこれ！")
                            .setContentText(text)
                            .setSmallIcon(R.mipmap.tancole_icon)
                            .setVibrate(new long[]{0, 200, 100, 200, 100, 200})
                            .setAutoCancel(true)
                            .setContentIntent(sender)
                            .build();

                    NotificationManager manager = (NotificationManager) content.getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(OriginalId, noti);

                    break;
                //1週間前
                case 2:
                    noti = new NotificationCompat.Builder(content)
                            .setTicker(text)
                            .setContentTitle("たんこれ！")
                            .setContentText(text)
                            .setSmallIcon(R.mipmap.tancole_icon)
                            .setVibrate(new long[]{0, 200, 100, 200, 100, 200})
                            .setAutoCancel(true)
                            .setContentIntent(sender)
                            .build();

                    manager = (NotificationManager) content.getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(OriginalId, noti);
                    break;
                //前日
                case 3:
                    noti = new NotificationCompat.Builder(content)
                            .setTicker(text)
                            .setContentTitle("たんこれ！")
                            .setContentText(text)
                            .setSmallIcon(R.mipmap.tancole_icon)
                            .setVibrate(new long[]{0, 200, 100, 200, 100, 200})
                            .setAutoCancel(true)
                            .setContentIntent(sender)
                            .build();

                    manager = (NotificationManager) content.getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(OriginalId, noti);
                    break;
                //当日
                case 4:
                    noti = new NotificationCompat.Builder(content)
                            .setTicker(text)
                            .setContentTitle("たんこれ！")
                            .setContentText(text)
                            .setSmallIcon(R.mipmap.tancole_icon)
                            .setVibrate(new long[]{0, 200, 100, 200, 100, 200})
                            .setAutoCancel(true)
                            .setContentIntent(sender)
                            .build();

                    manager = (NotificationManager) content.getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(OriginalId, noti);
                    break;
                //カスタム
                case 5:

                case 6:
                case 7:
                    noti = new NotificationCompat.Builder(content)
                            .setTicker(text)
                            .setContentTitle("たんこれ！")
                            .setContentText(text)
                            .setSmallIcon(R.mipmap.tancole_icon)
                            .setVibrate(new long[]{0, 200, 100, 200, 100, 200})
                            .setAutoCancel(true)
                            .setContentIntent(sender)
                            .build();

                    manager = (NotificationManager) content.getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(OriginalId, noti);
                    break;


            }


        }
    public static void alarm(int id, String name, int year, int month,int day,int birthmonth,int birthday,int Mnotif,int Wnotif,int Ynotif,int Tnotif,int custum1,int custum2,int custum3,Context context){
        int intentId;
        //IDを動的に生成する
        intentId = makeOriginalId(id);


        int cat,dog; //月日を４桁にするやつ（現在の日付と誕生日）

        int ms = 1000*60*60*24; //１日をミリ秒にしたやつ
        cat = birthmonth * 100 + birthday;  //誕生日を７月１４日を→７１４みたいな形に
        dog = month * 100 + day; //現在の日付を６月１５日→６１５みたいな形に

        int num = 0;//謎

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
            Intent alarmMonth = new Intent(context, Notifier.class);
            alarmMonth.setData(makeUri(Integer.parseInt(String.valueOf(intentId)+1)));
            alarmMonth.putExtra("intentId",Integer.parseInt(String.valueOf(intentId)+1));
            alarmMonth.putExtra("intentString",(name) + "さんの誕生日まで残り１ヶ月です");
            PendingIntent sender = PendingIntent.getBroadcast(context, Integer.parseInt(String.valueOf(intentId)+0), alarmMonth, PendingIntent.FLAG_UPDATE_CURRENT);

            //日時と発行するIntentをAlarmManagerにセットします
            AlarmManager manager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);

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
            //Calendar.SECONDで秒なのかdayなのか月なのか年なのか wDayがその変数
            triggerTime.add(Calendar.SECOND, Wday);	//
            //weekText = (name) + "さんの誕生日まで残り１週間です";
            //設定した日時で発行するIntentを生成
            Intent alarmWeek = new Intent(context, Notifier.class);
            alarmWeek.setData(makeUri(Integer.parseInt(String.valueOf(intentId)+2)));

            alarmWeek.putExtra("intentId",Integer.parseInt(String.valueOf(intentId)+2));
            alarmWeek.putExtra("intentString",(name) + "さんの誕生日まで残り１週間です");


            PendingIntent sender = PendingIntent.getBroadcast(context, Integer.parseInt(String.valueOf(intentId)+1), alarmWeek, PendingIntent.FLAG_UPDATE_CURRENT);
//日時と発行するIntentをAlarmManagerにセットします
            AlarmManager manager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);

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
            Intent alarmYest= new Intent(context, Notifier.class);
            alarmYest.setData(makeUri(Integer.parseInt(String.valueOf(intentId)+3)));

            alarmYest.putExtra("intentId",Integer.parseInt(String.valueOf(intentId)+3));
            alarmYest.putExtra("intentString",(name) + "さんの誕生日まで残り１日です");



            PendingIntent sender = PendingIntent.getBroadcast(context, Integer.parseInt(String.valueOf(intentId)+2), alarmYest, PendingIntent.FLAG_UPDATE_CURRENT);
//日時と発行するIntentをAlarmManagerにセットします
            AlarmManager manager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
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
            triggerTime.add(Calendar.SECOND, Tday);	//
            //todayText = "今日は" + (name) + "さんの誕生日です!";
            //設定した日時で発行するIntentを生成
            Intent alarmToday = new Intent(context, Notifier.class);
            alarmToday.setData(makeUri(Integer.parseInt(String.valueOf(intentId)+4)));
            alarmToday.putExtra("intentId",Integer.parseInt(String.valueOf(intentId)+4));
            alarmToday.putExtra("intentString","今日は" + (name) + "さんの誕生日です!");

            PendingIntent sender = PendingIntent.getBroadcast(context, Integer.parseInt(String.valueOf(intentId)+3), alarmToday, PendingIntent.FLAG_UPDATE_CURRENT);
//日時と発行するIntentをAlarmManagerにセットします
            AlarmManager manager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
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
            Intent alarmCus1 = new Intent(context, Notifier.class);
            alarmCus1.setData(makeUri(Integer.parseInt(String.valueOf(intentId)+5)));
            alarmCus1.putExtra("intentId",Integer.parseInt(String.valueOf(intentId)+5));
            alarmCus1.putExtra("intentString",String.valueOf(birthmonth)+"/"+String.valueOf(birthday)+"は"+(name)+"さんの誕生日です");

            PendingIntent sender = PendingIntent.getBroadcast(context, Integer.parseInt(String.valueOf(intentId)+4), alarmCus1, PendingIntent.FLAG_UPDATE_CURRENT);

            //日時と発行するIntentをAlarmManagerにセットします
            AlarmManager manager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
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
            Intent alarmCus2 = new Intent(context, Notifier.class);
            alarmCus2.putExtra("intentId",Integer.parseInt(String.valueOf(intentId)+6));
            alarmCus2.setData(makeUri(Integer.parseInt(String.valueOf(intentId)+6)));
            alarmCus2.putExtra("intentString",String.valueOf(birthmonth)+"/"+String.valueOf(birthday)+"は"+(name)+"さんの誕生日です");

            PendingIntent sender = PendingIntent.getBroadcast(context, Integer.parseInt(String.valueOf(intentId)+5), alarmCus2, PendingIntent.FLAG_UPDATE_CURRENT);

            //日時と発行するIntentをAlarmManagerにセットします
            AlarmManager manager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
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
            Intent alarmCus3 = new Intent(context, Notifier.class);
            alarmCus3.setData(makeUri(Integer.parseInt(String.valueOf(intentId)+7)));
            alarmCus3.putExtra("intentId",Integer.parseInt(String.valueOf(intentId)+7));
            alarmCus3.putExtra("intentString",String.valueOf(birthmonth)+"/"+String.valueOf(birthday)+"は"+(name)+"さんの誕生日です");


            PendingIntent sender = PendingIntent.getBroadcast(context, Integer.parseInt(String.valueOf(intentId)+6), alarmCus3, PendingIntent.FLAG_UPDATE_CURRENT);

            //日時と発行するIntentをAlarmManagerにセットします
            AlarmManager manager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);


            //Androidのバージョンが6.0以上(Dozeモードがあるバージョン)以上なら設定時間より15分遅れるかも
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,triggerTime.getTimeInMillis(),sender);
            }else{
                manager.set(AlarmManager.RTC_WAKEUP, triggerTime.getTimeInMillis(), sender);
            }
            //manager.set(AlarmManager.RTC_WAKEUP, triggerTime.getTimeInMillis(), sender);
        }

    }
    static int makeOriginalId(int id){
        int makeId;
        makeId = Integer.parseInt(id +"9999");
        Log.d("makeInt",String.valueOf(makeId));
        return makeId;
    }
    static int originalIdToId(int makeId){
        int id;
        String temp = String.valueOf(makeId);
        id = Integer.parseInt(temp.substring(temp.length()-1,temp.length()));
        return id;
    }
    static Uri makeUri(int originalId) {
        StringBuffer buffer = new StringBuffer();
        long nowElapsedRealtime = SystemClock.elapsedRealtime();
        buffer.append("SCHEME" + "://")
                .append("HOSTNAME" + "/")
                .append(originalId)
                .append(Long.toString(nowElapsedRealtime));
        Uri uri = Uri.parse(buffer.toString());
        return uri;
    }


}



