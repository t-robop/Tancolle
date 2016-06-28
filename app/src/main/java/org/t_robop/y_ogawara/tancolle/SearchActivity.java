package org.t_robop.y_ogawara.tancolle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchActivity extends Activity implements
        SearchView.OnQueryTextListener {

    ListView listView;
    SearchView searchView;

    String[] name_items, kana_items;

    //ArrayList<ListItem> items;
    dataListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        listView = (ListView) findViewById(R.id.listView);
        searchView = (SearchView) findViewById(R.id.searchView);

        //Data型の宣言
//        Data testData = new Data();
//        //Data型にデータをセット
//        testData.setName("西村");
//        testData.setKana("にしむら");
//        testData.setBirthday(19970714);
//        testData.setYear(1997);
//        testData.setMonth(6);
//        testData.setDay(20);
//        testData.setCategory("友達");
//        testData.setTwitterID("Taiga_Natto");
//        testData.setMemo("教科書を見て実装して欲しい");
//        testData.setImage("Imageデータ");
//        testData.setSmallImage("Imageデータ");
//        testData.setPresentFlag(0);
//        testData.setNotif_yest(1);
//        testData.setNotif_today(1);
//        testData.setNotif_day(3);
//        testData.setNotif_recy(3);
//        //dbに書き込み
//        dbAssist.insertData(testData, this);
//
//        //Data型の宣言
//        Data testData1 = new Data();
//        //Data型にデータをセット
//        testData1.setName("西村");
//        testData1.setKana("なかさとみ");
//        testData1.setBirthday(19970714);
//        testData1.setYear(1997);
//        testData1.setMonth(6);
//        testData1.setDay(20);
//        testData1.setCategory("友達");
//        testData1.setTwitterID("Taiga_Natto");
//        testData1.setMemo("教科書を見て実装して欲しい");
//        testData1.setImage("Imageデータ");
//        testData1.setSmallImage("Imageデータ");
//        testData1.setPresentFlag(0);
//        testData1.setNotif_yest(1);
//        testData1.setNotif_today(1);
//        testData1.setNotif_day(3);
//        testData1.setNotif_recy(3);
//        //dbに書き込み
//        dbAssist.insertData(testData1, this);
//
//        //Data型の宣言
//        Data testData2 = new Data();
//        //Data型にデータをセット
//        testData2.setName("西村");
//        testData2.setKana("まりか");
//        testData2.setBirthday(19970714);
//        testData2.setYear(1997);
//        testData2.setMonth(6);
//        testData2.setDay(20);
//        testData2.setCategory("友達");
//        testData2.setTwitterID("Taiga_Natto");
//        testData2.setMemo("教科書を見て実装して欲しい");
//        testData2.setImage("Imageデータ");
//        testData2.setSmallImage("Imageデータ");
//        testData2.setPresentFlag(0);
//        testData2.setNotif_yest(1);
//        testData2.setNotif_today(1);
//        testData2.setNotif_day(3);
//        testData2.setNotif_recy(3);
//        //dbに書き込み
//        dbAssist.insertData(testData2, this);


        //TODO 自分でListに送りたいデータ用のクラスを準備しないと動きません
        ArrayList<Data> dataList = dbAssist.allSelect(this);


        ArrayList<ListItem> name = new ArrayList<>();
        for (int i = 0; dataList.size() > i; i++) {
            ListItem item = new ListItem();
            item.setName(dataList.get(i).getName());
            item.setKana(dataList.get(i).getKana());
            item.setItemId(dataList.get(i).getId());
            //TODO 画像のパス追加
            item.setSmallImage(dataList.get(i).getSmallImage());
            Log.d("aaaaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaaaaaaaaa");
            Log.d("search", dataList.get(i).getImage());
            name.add(item);
        }


        adapter = new dataListAdapter(getApplicationContext(), name);
        listView.setAdapter(adapter);

        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("名前を入力");
        searchView.setIconifiedByDefault(false);

        // ListView がクリックされた時に呼び出されるコールバックを登録
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                //ここに書く
                ListView listView = (ListView) parent;
                listView.getItemAtPosition(position);

                ListItem listItem = (ListItem) listView.getAdapter().getItem(position);


                Intent intent = new Intent(SearchActivity.this, UserDetailActivity.class);
                intent.putExtra("id", listItem.getItemId());
                startActivity(intent);

                //トースト
                //Toast.makeText(SearchActivity.this, "Click: " + item, Toast.LENGTH_LONG).show();

            }
        });

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



