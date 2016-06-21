package org.t_robop.y_ogawara.tancolle;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends Activity implements SearchView.OnQueryTextListener {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


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
        testData.setTamura(1);
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
            item.setItemId(111);
            name.add(item);
        }
        dataListAdapter adapter = new dataListAdapter(this, R.layout.activity_search__item, name);
        listView = (ListView) findViewById(R.id.listView);


        //テキストフィルター(リアルタイム検索)
        listView.setTextFilterEnabled(true);

        //SearchView
        SearchView searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
        searchView.setIconifiedByDefault(false);
        //上のボックスに入れるテキスト
        searchView.setQueryHint("名前を入力");

        listView.setAdapter(adapter);
        
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    // 検索キーワードが入ると呼ばれる
//    @Override
//    public boolean onQueryTextChange(String newText){
//        if (newText == null || newText.equals("")) {
//            listView.clearTextFilter();
//        } else {
//            listView.setFilterText(newText); // ここで絞込み
//        }
//        return false;
//    }
    @Override
    public boolean onQueryTextChange(String newText) {
        // newText the new content of the query text field
        if (TextUtils.isEmpty(newText)) {
            // clear text filter so that list displays all items
            listView.clearTextFilter();
        } else {
            // apply filter so that list displays only
            // matching child items
            listView.setFilterText(newText.toString());
        }

        return true;
    }




}



