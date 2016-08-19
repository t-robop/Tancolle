package org.t_robop.y_ogawara.tancolle;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;


public class Notifier extends BroadcastReceiver {

        @Override
        public void onReceive(Context content, Intent intent) {

            //通知がクリックされた時に発行されるIntentの生成
            Intent sendIntent = new Intent(content, AlarmActivity.class);
            PendingIntent sender = PendingIntent.getActivity(content, 0, sendIntent, 0);


            //通知オブジェクトの生成
            if(AlarmActivity.Mnotif==1) {
                Notification noti = new NotificationCompat.Builder(content)
                        .setTicker(AlarmActivity.monthText)
                        .setContentTitle("たんこれ！")
                        .setContentText(AlarmActivity.monthText)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setVibrate(new long[]{0, 200, 100, 200, 100, 200})
                        .setAutoCancel(true)
                        .setContentIntent(sender)
                        .build();

                NotificationManager manager = (NotificationManager) content.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(0, noti);
            }
            //通知オブジェクトの生成
            if(AlarmActivity.Wnotif==1) {
                Notification noti = new NotificationCompat.Builder(content)
                        .setTicker(AlarmActivity.weekText)
                        .setContentTitle("たんこれ！")
                        .setContentText(AlarmActivity.weekText)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setVibrate(new long[]{0, 200, 100, 200, 100, 200})
                        .setAutoCancel(true)
                        .setContentIntent(sender)
                        .build();

                NotificationManager manager = (NotificationManager) content.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(0, noti);
            }

            //通知オブジェクトの生成
            if(AlarmActivity.Ynotif==1) {
                Notification noti = new NotificationCompat.Builder(content)
                        .setTicker(AlarmActivity.yestText)
                        .setContentTitle("たんこれ！")
                        .setContentText(AlarmActivity.yestText)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setVibrate(new long[]{0, 200, 100, 200, 100, 200})
                        .setAutoCancel(true)
                        .setContentIntent(sender)
                        .build();

                NotificationManager manager = (NotificationManager) content.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(0, noti);
            }

            //通知オブジェクトの生成
            if(AlarmActivity.Tnotif==1) {
                Notification noti = new NotificationCompat.Builder(content)
                        .setTicker(AlarmActivity.todayText)
                        .setContentTitle("たんこれ！")
                        .setContentText(AlarmActivity.todayText)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setVibrate(new long[]{0, 200, 100, 200, 100, 200})
                        .setAutoCancel(true)
                        .setContentIntent(sender)
                        .build();

                NotificationManager manager = (NotificationManager) content.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(0, noti);
            }

            //通知オブジェクトの生成
            if((AlarmActivity.custum1>0)||(AlarmActivity.custum2>0)||(AlarmActivity.custum3>0)) {
                Notification noti = new NotificationCompat.Builder(content)
                        .setTicker(AlarmActivity.custumText)
                        .setContentTitle("たんこれ！")
                        .setContentText(AlarmActivity.custumText)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setVibrate(new long[]{0, 200, 100, 200, 100, 200})
                        .setAutoCancel(true)
                        .setContentIntent(sender)
                        .build();

                NotificationManager manager = (NotificationManager) content.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(0, noti);
            }

        }
    }
