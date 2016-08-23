package org.t_robop.y_ogawara.tancolle;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SettingDrawActivity extends AppCompatActivity {

    //表記変更用スピナー
    Spinner spinnerDraw;
    String itemDrawSpinner[] = {"日付", "残日"};
    boolean drawType=false;//false:日付
    //テーマ変更用スピナー
    Spinner spinnerTheme;
    String itemThemeSpinner[]={"白","黒"};
    int themeType=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_draw);

        /*****関連付け*****/
        //表記変更用スピナーの関連付け
        spinnerDraw=(Spinner)findViewById(R.id.draw_spinner);
        //テーマ変更用スピナーの関連付け
        spinnerTheme=(Spinner)findViewById(R.id.theme_spinner);
        /********************/

        /*****preference展開*****/
        //preference"Setting"をプライベートモードで開く
        SharedPreferences pref = getSharedPreferences("Setting", MODE_PRIVATE);
        //表記変更の値を取得（未設定の場合は日付表示(false)）
        drawType = pref.getBoolean("drawType", false);
        //テーマ変更の値を取得（未設定の場合は初期テーマ(0)）
        themeType=pref.getInt("themeType",0);
        /********************/

        //スピナーのイベント設定
        spinnerSet(spinnerDraw, itemDrawSpinner,0);
        spinnerSet(spinnerTheme,itemThemeSpinner,1);

        /*****取得した値からスピナーの要素を変更*****/
        //表記設定
        if (drawType==false) {
            spinnerDraw.setSelection(0);
        }
        else{
            spinnerDraw.setSelection(1);
        }
        //テーマ設定
        spinnerTheme.setSelection(themeType);
        /********************/

        /*****ToolBar関連*****/
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("表示設定");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        /********************/
    }

    //MenuItem(戻るボタン)の選択
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //preferenceに設定値を保存
            savePreference();
            //Activity終了
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //バックしたときの処理
    @Override
    public void onBackPressed() {
        //preferenceに設定値を保存
        savePreference();
        //Activity終了
        finish();
    }

    //数値で場合分けして何の処理か分ける
    public void spinnerSet(Spinner spinner, final String spinnerItems[], final int ifnum){
        // ArrayAdapterの宣言
        ArrayAdapter<String> adapter
                = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // spinner に adapter をセット
        spinner.setAdapter(adapter);
        // リスナーを登録
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //　アイテムが選択された時
            public void onItemSelected(AdapterView<?> parent, View viw, int arg2, long arg3) {
                Spinner spinner = (Spinner) parent;
                String item = (String) spinner.getSelectedItem();

                //日付変更時
                if(ifnum==0) {
                    //それぞれの選択肢が選択された時の処理
                    if (item.equals(spinnerItems[1])) {
                        //残日
                        drawType=true;
                    } else {//初期値
                        //日付
                        drawType=false;
                    }
                }
                //テーマ変更時
                else {
                    //それぞれの選択肢が選択された時の処理
                    //黒テーマ
                    if (item.equals(spinnerItems[1])) {
                        //setTheme(R.style.AppThemeBlack);
                        themeType=1;
                    }
                    //白テーマ
                    else {//初期値
                        setTheme(R.style.AppTheme);
                        themeType=0;
                    }
                }
            }
            //　アイテムが選択されなかった
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    //設定値の保存
    public void savePreference(){
        //preferenceの宣言
        SharedPreferences prefType;
        //preference"Setting"をプライベートモードで開く
        prefType = getSharedPreferences("Setting", MODE_PRIVATE);
        //編集用editorを展開
        SharedPreferences.Editor editor = prefType.edit();
        //"drawType"の値をboolean型変数drawTypeに
        editor.putBoolean("drawType", drawType);
        //"themeType"の値をint型変数themeTypeに
        editor.putInt("themeType",themeType);
        //editor終了
        editor.apply();
    }
}
