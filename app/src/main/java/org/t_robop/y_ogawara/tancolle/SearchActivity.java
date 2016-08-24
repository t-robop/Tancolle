package org.t_robop.y_ogawara.tancolle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    ListView listView;
    SearchView searchView;
    View view;

    //ArrayList<ListItem> items;
    dataListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        listView = (ListView) findViewById(R.id.listView);
        searchView = (SearchView) findViewById(R.id.searchView);

        ArrayList<Data> dataList = dbAssist.allSelect(this);


        ArrayList<ListItem> name = new ArrayList<>();
        for (int i = 0; dataList.size() > i; i++) {
            ListItem item = new ListItem();
            item.setName(dataList.get(i).getName());
            item.setKana(dataList.get(i).getKana());
            item.setItemId(dataList.get(i).getId());
            item.setSmallImage(dataList.get(i).getSmallImage());
            Log.d("aaaaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaaaaaaaaa");
            Log.d("search", dataList.get(i).getImage());
            name.add(item);
        }

        //adapterをセット
        adapter = new dataListAdapter(getApplicationContext(), name);
        listView.setAdapter(adapter);

        //searchViewに関する設定
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("名前を入力");
        searchView.setIconifiedByDefault(false);

        // ListView がクリックされた時に呼び出されるコールバックを登録
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                //ここに書く
                //listViewのitemを取得してadapterからItemをもらってくる
                ListView listView = (ListView) parent;
                listView.getItemAtPosition(position);

                ListItem listItem = (ListItem) listView.getAdapter().getItem(position);

                //Intentで飛ばす＆idをキーにする
                Intent intent = new Intent(SearchActivity.this, UserDetailActivity.class);
                intent.putExtra("id", listItem.getItemId());
                startActivity(intent);

                //トースト
                //Toast.makeText(SearchActivity.this, "Click: " + item, Toast.LENGTH_LONG).show();

            }
        });

    }

    //Textが変わった時に呼び出される
    @Override
    public boolean onQueryTextChange(String newText) {
        //adapterにfilterを送ってfilterされたのをもらってくる
        adapter.getFilter().filter(newText);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    //アクションバー処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // アクションバーアイテム上の押下を処理します。
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}



