package org.t_robop.y_ogawara.tancolle;

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
        menu.add(0, MENU_SELECT_A, 0, "Menu A");
        menu.add(0, MENU_SELECT_B, 0, "Menu B");
        menu.add(0, MENU_SELECT_C, 0, "Menu C");
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
                testData.setBirthday(19970714);
                testData.setCategory("友達");
                testData.setTwitterID("Taiga_Natto");
                testData.setMemo("教科書を見て実装して欲しい");
                testData.setImage("Imageデータ");
                testData.setSmallImage("Imageデータ");
                testData.setPresentFlag(0,this);
                testData.setTamura(2,this);
                testData.setNotif_yest(1,this);
                testData.setNotif_today(1,this);
                testData.setNotif_day(3);
                testData.setNotif_recy(3);
                //dbに書き込み
                dbAssist.insertData(testData,this);
                return true;

            case MENU_SELECT_B:
                Log.d("Menu","DBをアップデートします");
                testData = new Data();
                testData.setName("aaaaa");
                testData.setBirthday(19970714);
                testData.setNotif_yest(1,this);
                //DataBase.updateData(1,testData);
                //ArrayList<Data> test=  dbAssist.openData(this);
                return true;

            case MENU_SELECT_C:
                Log.d("Menu","全データ検索と削除");
                ArrayList<Data> dataList=  dbAssist.openData(this);
                Log.d("","");
                //idを指定して削除
                //dbAssist.deleteData(1);
                return true;

        }
        return false;

        //return super.onOptionsItemSelected(item);
    }
}
