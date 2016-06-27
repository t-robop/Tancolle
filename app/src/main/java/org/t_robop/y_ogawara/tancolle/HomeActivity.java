package org.t_robop.y_ogawara.tancolle;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ListView listView = (ListView) findViewById(R.id.listView);
        // データを準備
        ArrayList<ListCellData> cellDataArray = new ArrayList<>();


        for (int i =1;i<9;i=i+3){
            ListCellData cellData = new ListCellData();
            cellData.setBirthday1("6/10");
            cellData.setBirthday2("6/12");
            cellData.setBirthday3("6/20");
            cellData.setName1("西村");
            cellData.setName2("西");
            cellData.setName3("村");

            cellData.setBitmap1(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
            cellData.setBitmap2(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
            cellData.setBitmap3(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));

            cellData.setId1(i);
            cellData.setId2(i+1);
            cellData.setId3(i+2);
            cellDataArray.add(cellData);
        }

        // Adapter - ArrayAdapter - UserAdapter
        ListCellAdapter adapter = new ListCellAdapter(this, 0, cellDataArray);


        // ListViewに表示
        listView.setEmptyView(findViewById(R.id.listView));
        listView.setAdapter(adapter);
    }
    public void cellClick(View view) {
        Log.v("yorosiku!",  String.valueOf(view.getTag()));
    }
}
