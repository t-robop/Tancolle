package org.t_robop.y_ogawara.tancolle;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends Activity implements
        SearchView.OnQueryTextListener {

    ListView listView;
    SearchView searchView;

    String[] name_items, kana_items;

    ArrayList<ListItem> items;
    dataListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        listView = (ListView) findViewById(R.id.listView);
        searchView = (SearchView) findViewById(R.id.searchView);

        //Data型の宣言
        Data testData = new Data();
        //Data型にデータをセット
        testData.setName("西村");
        testData.setKana("にしむら");
        testData.setBirthday(19970714);
        testData.setCategory("友達");
        testData.setTwitterID("Taiga_Natto");
        testData.setMemo("教科書を見て実装して欲しい");
        testData.setImage("Imageデータ");
        testData.setSmallImage("Imageデータ");
        testData.setPresentFlag(0);
        testData.setNotif_yest(1);
        testData.setNotif_today(1);
        testData.setNotif_day(3);
        testData.setNotif_recy(3);
        //dbに書き込み
        dbAssist.insertData(testData, this);

        //TODO 自分でListに送りたいデータ用のクラスを準備しないと動きません
        ArrayList<Data> dataList = dbAssist.allSelect(this);


        List<ListItem> name = new ArrayList<>();
        for (int i = 0; dataList.size() > i; i++) {
            ListItem item = new ListItem();
            item.setName(dataList.get(i).getName());
            item.setKana(dataList.get(i).getKana());
            name.add(item);
        }


        adapter = new dataListAdapter(getApplicationContext(), items);
        listView.setAdapter(adapter);

        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


}



