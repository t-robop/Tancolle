package org.t_robop.y_ogawara.tancolle;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final int MENU_SELECT_A = 0;
    public static final int MENU_SELECT_B = 1;
    public static final int MENU_SELECT_C = 2;
    public static final int MENU_SELECT_D = 3;
    public static final int MENU_SELECT_E = 4;
    public static final int MENU_SELECT_F = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.add);
        if (add != null) {
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.add(0, MENU_SELECT_A, 0, "データ書き込み");
        menu.add(0, MENU_SELECT_B, 0, "データupdate");
        menu.add(0, MENU_SELECT_C, 0, "全データ取得");
        menu.add(0, MENU_SELECT_D, 0, "仮名検索");
        menu.add(0, MENU_SELECT_E, 0, "id検索");
        menu.add(0, MENU_SELECT_F, 0, "データ削除");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case MENU_SELECT_A:
                Log.d("Menu","SQliteに書き込み");
                //Data型の宣言
                Data testData =new Data();
                //Data型にデータをセット
                testData.setName("西村");
                testData.setKana("にしむら");
                testData.setBirthday(19970616);
                testData.setCategory("友達");
                testData.setTwitterID("Taiga_Natto");
                testData.setMemo("教科書を見て実装して欲しい");
                testData.setImage("Imageデータ");
                testData.setSmallImage("Imageデータ");
                testData.setPresentFlag(0);
                testData.setYukarin(1);
                testData.setNotif_yest(1);
                testData.setNotif_today(1);
                testData.setNotif_day(3);
                testData.setNotif_recy(3);
                //dbに書き込み
                dbAssist.insertData(testData,this);

                //Data型の宣言
                Data testData1 =new Data();
                //Data型にデータをセット
                testData1.setName("西");
                testData1.setKana("にら");
                testData1.setBirthday(19970512);
                testData1.setCategory("友達");
                testData1.setTwitterID("Taiga_Natvaevto");
                testData1.setMemo("教科書を見てvesva実装して欲しい");
                testData1.setImage("Imageデータ");
                testData1.setSmallImage("Imadgeデータ");
                testData1.setPresentFlag(10);
                testData1.setYukarin(11);
                testData1.setNotif_yest(11);
                testData1.setNotif_today(11);
                testData1.setNotif_day(31);
                testData1.setNotif_recy(31);
                //dbに書き込み
                //引数はData,場所(this)
                dbAssist.insertData(testData1,this);
                return true;

            case MENU_SELECT_B:
                Log.d("Menu","DBをアップデートします");
                Data updateData =new Data();
                updateData.setName("aaaaa");
                updateData.setBirthday(19970714);
                updateData.setNotif_yest(1);
                updateData.setNotif_recy(0);
                //引数はid,data,場所
                dbAssist.updateData(1,updateData,this);
                return true;

            case MENU_SELECT_C:
                Log.d("Menu","全データ検索");
                //引数は場所
                //返り値は配列
                ArrayList<Data> dataList=  dbAssist.allSelect(this);
                Data allData =new Data();
                allData = dataList.get(1);

                Log.d("aaaaaaaaaaaa", dataList.get(1).getKana());
                return true;

            case MENU_SELECT_D:
                Log.d("Menu","仮名検索");
                //引数はkana(String),場所
                //返り値は配列
                ArrayList<Data> kanaList=  dbAssist.kanaSelect("にしむら",this);
                Log.d("","");
                return true;

            case MENU_SELECT_E:
                Log.d("Menu","id検索");
                //引数はid(int),場所
                //idは固有値なので、返り値はData型
                Data idList=  dbAssist.idSelect(1,this);
                Log.d("","");
                return true;

            case MENU_SELECT_F:
                Log.d("Menu","削除");
                //引数はid,場所
                dbAssist.deleteData(1,this);
                return true;
        }
        return false;

    }
    // プリファレンス保存
// aaa,bbb,ccc... の文字列で保存
    private void saveArray(String[] array,String PrefKey){
        StringBuffer buffer = new StringBuffer();
        String stringItem = null;
        for(String item : array){
            buffer.append(item+",");
        };
        if(buffer != null){
            String buf = buffer.toString();
            stringItem = buf.substring(0, buf.length() - 1);

            SharedPreferences prefs1 = getSharedPreferences("Array", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs1.edit();
            editor.putString(PrefKey, stringItem).commit();
        }
    }

    // プリファレンス取得
// aaa,bbb,ccc...としたものをsplitして返す
    private String[] getArray(String PrefKey) {
        SharedPreferences prefs2 = getSharedPreferences("Array", Context.MODE_PRIVATE);
        String stringItem = prefs2.getString(PrefKey, "");
        if (stringItem != null && stringItem.length() != 0) {
            return stringItem.split(",");
        } else {
            return null;
        }
    }
    public void testBtn(View view) {
        Intent intent = new Intent(this,UserDetailActivity.class);
        startActivity(intent);

    }
}
