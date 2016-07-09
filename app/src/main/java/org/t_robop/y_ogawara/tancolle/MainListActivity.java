package org.t_robop.y_ogawara.tancolle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by iris on 2016/07/06.
 */
public class MainListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);
        ListView listView = (ListView) findViewById(R.id.listView1);


        ArrayList<MainListData> MainListArray = new ArrayList<>();


        // データを突っ込む
        for (int i =1;i<3;i=i+3){
            MainListData MainList = new MainListData();
            MainList.setBirth1("6/10");
            MainList.setBirth2("1/2");
            MainList.setBirth3("12/25");
            MainList.setName1("西村");
            MainList.setName2("いか");
            MainList.setName3("キリスト");


            MainListArray.add(MainList);

        }

        //Adapterをセット
        MainListAdapter adapter = new MainListAdapter(this, 0, MainListArray);
        //listView.setEmptyView(findViewById(R.id.listView));
        listView.setAdapter(adapter);
    }

    //リストをクリックした時のイベント
    public void listClick(View view) {
        Log.d("hello","aaaaaaaaaaaaaa");
    }
}
