package org.t_robop.y_ogawara.tancolle;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


    public class Notifier extends BroadcastReceiver {

        @Override
        public void onReceive(Context content, Intent intent) {

            //通知がクリックされた時に発行されるIntentの生成
            Intent sendIntent = new Intent(content, AlarmActivity.class);
            PendingIntent sender = PendingIntent.getActivity(content, 0, sendIntent, 0);

        }
    }
