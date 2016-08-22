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

    Spinner spinnerDraw;
    String spinnerDrawItems[] = {"日付", "残日"};
    boolean drawType=false;//false:日付

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_draw);

        spinnerDraw=(Spinner)findViewById(R.id.drawspinner) ;

        SharedPreferences pref = getSharedPreferences("Setting", MODE_PRIVATE);
        drawType = pref.getBoolean("drawType", false);

        spinnerSet(spinnerDraw,spinnerDrawItems,0);

        if (drawType==false) {
            spinnerDraw.setSelection(0);
        }
        else{
            spinnerDraw.setSelection(1);
        }
        //ToolBar関連
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("表示設定");
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
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //バックしたときの処理
    @Override
    public void onBackPressed() {

        //drawType(表記設定)の保存
        SharedPreferences preferDrawType;
        preferDrawType = getSharedPreferences("Setting", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferDrawType.edit();
        editor.putBoolean("drawType", drawType);
        editor.commit();

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
                else {

                }
            }
            //　アイテムが選択されなかった
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


}
