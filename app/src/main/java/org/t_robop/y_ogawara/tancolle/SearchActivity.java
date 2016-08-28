package org.t_robop.y_ogawara.tancolle;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    ListView listView;
    private Toolbar toolbar;
    private SearchView searchView;
    View view;

    //ArrayList<ListItem> items;
    dataListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.search);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }


        listView = (ListView) findViewById(R.id.listView);

        ArrayList<Data> dataList = dbAssist.allSelect(this);


        ArrayList<ListItem> name = new ArrayList<>();
        for (int i = 0; dataList.size() > i; i++) {
            ListItem item = new ListItem();
            item.setName(dataList.get(i).getName());
            item.setKana(dataList.get(i).getKana());
            item.setItemId(dataList.get(i).getId());
            item.setSmallImage(dataList.get(i).getSmallImage());
            name.add(item);
        }

        //adapterをセット
        adapter = new dataListAdapter(getApplicationContext(), name);
        listView.setAdapter(adapter);

        // ListView がクリックされた時に呼び出されるコールバックを登録
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                //ここに書く
                //listViewのitemを取得してadapterからItemをもらってくる
                ListView listView = (ListView) parent;
                listView.getItemAtPosition(position);

                ListItem listItem = (ListItem) listView.getAdapter().getItem(position);

                //Intentで飛ばす＆idをキーにする
                Intent intent = new Intent(SearchActivity.this, ReUserDetailActivity.class);
                intent.putExtra("id", listItem.getItemId());
                startActivity(intent);

                //トースト
                //Toast.makeText(SearchActivity.this, "Click: " + item, Toast.LENGTH_LONG).show();

            }
        });

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        MenuItem searchItem = menu.findItem(R.id.searchView);

        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView = (SearchView) toolbar.getMenu().findItem(R.id.searchView).getActionView();

        SearchView.SearchAutoComplete searchAutoComplete =
                (SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        //searchAutoCompleteを呼んでおくことで色の変更が可能になる
        searchAutoComplete.setTextColor(Color.BLACK);
        searchAutoComplete.setHintTextColor(Color.WHITE);
        searchView.setIconified(false);
        searchView.setQueryHint("名前を入力");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        });


        return true;
    }


}



