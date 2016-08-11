package org.t_robop.y_ogawara.tancolle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class SettingActivity extends AppCompatActivity {

    CheckBox allNotif;
    Boolean allNotifType=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        allNotif=(CheckBox)findViewById(R.id.allnotif);

        checkBoxSet();

        SharedPreferences pref = getSharedPreferences("Setting", MODE_PRIVATE);
        allNotifType = pref.getBoolean("allNotifType", false);

        allNotif.setChecked(allNotifType);
    }

    public void drawsetting(View v){
        Intent intent = new Intent(this, SettingDrawActivity.class);
        startActivity(intent);
    }

    public void license(View v){

    }

    //バックしたときの処理
    @Override
    public void onBackPressed() {

        //allNotifType(通知設定)の保存
        SharedPreferences preferAllNotif;
        preferAllNotif = getSharedPreferences("Setting", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferAllNotif.edit();
        editor.putBoolean("allNotifType", allNotifType);
        editor.commit();

        finish();
    }

    public void checkBoxSet(){
        // チェックボックスがクリックされた時に呼び出されるコールバックリスナーを登録します
        allNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            // チェックボックスがクリックされた時に呼び出されます
            public void onClick(View v) {

                // チェックボックスのチェック状態を取得します
                allNotifType=allNotif.isChecked();

            }
        });
    }
}
