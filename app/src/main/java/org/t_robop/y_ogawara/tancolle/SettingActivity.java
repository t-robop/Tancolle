package org.t_robop.y_ogawara.tancolle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

public class SettingActivity extends AppCompatActivity {

    //全体通知設定用チェックボックスの宣言
    CheckBox allNotif;
    //全体通知設定のフラグ(初期はonで)
    Boolean allNotifType=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //preferenceの「Setting」をプライベートモードで開く
        SharedPreferences pref = getSharedPreferences("Setting", MODE_PRIVATE);

        //ToolBar関連
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("設定");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    //MenuItem(戻るボタン)の選択
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //フラグの保存
            saveFlag();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //端末のバックボタンクリック時
    @Override
    public void onBackPressed() {
        //フラグの保存
        saveFlag();
        //Activity終了
        finish();
    }

    //設定クリック時
    public void drawsetting(View v){
        //表示設定用Activityに飛ぶ
            Intent intent = new Intent(SettingActivity.this, SettingDrawActivity.class);
            startActivity(intent);
        /////
    }
    //カテゴリ設定クリック時
    public void categorysetting(View v){
        //カテゴリ設定用Activityに飛ぶ
            Intent intent = new Intent(SettingActivity.this, SettingCategoryActivity.class);
            startActivity(intent);
        /////
    }
    //ライセンスクリック時
    public void license(View v){
        Intent intent = new Intent(SettingActivity.this, LicenseActivity.class);
        startActivity(intent);
    }
    //チェックボックスのリスナー登録
    public void checkBoxSet(){
        //チェックボックスがクリックされた時に呼び出されるコールバックリスナーを登録します
            allNotif.setOnClickListener(new View.OnClickListener() {
                @Override
                // チェックボックスがクリックされた時
                    public void onClick(View v) {
                        // チェックボックスのチェック状態を取得します
                        allNotifType=allNotif.isChecked();
                    }
                /////
            });
        /////
    }

    //設定の保存
    public void saveFlag(){
        //allNotifType(通知設定)の保存
        //preferenceの宣言
        SharedPreferences preferAllNotif;
        //preferenceの中の「Setting」をプライベートモードで開く
        preferAllNotif = getSharedPreferences("Setting", MODE_PRIVATE);
        //preferenceEditorの宣言
        SharedPreferences.Editor editor = preferAllNotif.edit();
        //開いたpreferenceの中の「allNotifType」の値を変更する
        editor.putBoolean("allNotifType", allNotifType);
        //editor終了
        editor.commit();
    }
}
