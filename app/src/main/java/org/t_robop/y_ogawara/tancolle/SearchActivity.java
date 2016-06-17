package org.t_robop.y_ogawara.tancolle;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.NameList;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.jar.Attributes;

public class SearchActivity extends Activity implements SearchView.OnQueryTextListener {

    private SearchView searchView;
    private ListView listView;
    private ArrayAdapter<String> adapter;


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
            //Data allData = new Data();

            //allData = dataList.get(0);
            List<ListItem> name = new ArrayList<>();
            for (int i = 0; dataList.size() > i; i++) {
                ListItem item = new ListItem();
                item.setName(dataList.get(i).getName());
                item.setItemId(111);
                name.add(item);
            }
            dataListAdapter adapter = new dataListAdapter(this, R.layout.activity_search_view_item, name);

            //searchView = (SearchView) findViewById(R.id.searchView1);
            listView = (ListView) findViewById(R.id.listView);




//            int padding = (int) (getResources().getDisplayMetrics().density * 8);
//            ListView listView = (ListView) findViewById(R.id.);
//            listView.setPadding(padding, 0, padding, 0);
//            listView.setScrollBarStyle(ListView.SCROLLBARS_OUTSIDE_OVERLAY);
//            listView.setDivider(null);
//            listView.setAdapter(adapter);
            //テキストフィルター(リアルタイム検索)
//            listView.setTextFilterEnabled(true);

            // Sets the default or resting state of the search field.
            // If true, a single search icon is shown by default and
            // expands to show the text field and other buttons when pressed
 //           searchView.setIconifiedByDefault(false);
            //searchView.setOnQueryTextListener(this);

            // 入力するところに最初に書いてあるやーつ
            //searchView.setQueryHint("名前を入力");

//            // ListView がクリックされた時に呼び出されるコールバックを登録
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                public void onItemClick(AdapterView parent, View view, int position, long id) {
//                    //ここに書く
//                    ListView listView = (ListView) parent;
//                    String item = (String) listView.getItemAtPosition(position);
//
//                    //トースト
//                    Toast.makeText(SearchActivity.this, "Click: " + item, Toast.LENGTH_LONG).show();
//
//                }
//            });
           // listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(adapter);

        }


    // 検索キーワードが入ると呼ばれる
    @Override
    public boolean onQueryTextChange(String newText) {
        // 表示設定
        if (TextUtils.isEmpty(newText)) {
            // 何も入ってないとフィルターしない
            listView.clearTextFilter();
        } else {
            //トーストでない書き方
            Filter filter = ((Filterable) listView.getAdapter()).getFilter();
            filter.filter(newText.toString());

            //こっちにするとトーストっぽいのがでる
            //listView.setFilterText(newText.toString());
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
}



