package org.t_robop.y_ogawara.tancolle;

import android.app.ActionBar;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

public class SearchActivity extends Activity implements
        SearchView.OnQueryTextListener {

    private SearchView searchView;
    private ListView listView;
    private ArrayAdapter<String> adapter;



    //private String[] stateList = { "Nishimura", "にしむら", "ニシムラ", "西村", "Godiva", "ゴディバ", "ごでぃば" };

    //




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

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
        testData.setTamura(0,this);
        testData.setNotif_yest(1,this);
        testData.setNotif_today(1,this);
        testData.setNotif_day(3);
        testData.setNotif_recy(3);
        //dbに書き込み
        dbAssist.insertData(testData,this);


        //読み込み
        ArrayList<String> dataList=  dbAssist.kanaSelect(this,"にしむら");

        //String name = dataList.get(0);
        Log.d("","");


        searchView = (SearchView) findViewById(R.id.searchView1);
        listView = (ListView) findViewById(R.id.listView1);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        listView.setTextFilterEnabled(true);

        // Sets the default or resting state of the search field.
        // If true, a single search icon is shown by default and
        // expands to show the text field and other buttons when pressed
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);

        // Sets the hint text to display in the query text field
        searchView.setQueryHint("名前を入れてね！！");

//        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



    // Called when the query text is changed by the user.
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



    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }





}
