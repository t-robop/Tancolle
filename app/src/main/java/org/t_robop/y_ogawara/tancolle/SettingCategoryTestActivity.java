package org.t_robop.y_ogawara.tancolle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SettingCategoryTestActivity extends AppCompatActivity {

    //カテゴリ一覧を表示するlistの宣言
    ListView listCategory;
    //カテゴリ一覧読み込み用String[]型の宣言
    String categoryItem[];
    //カテゴリを格納するアダプターの宣言
    ArrayAdapter<String> categoryAdapter;
    //カテゴリ保存用のリスト
    ArrayList<String> categorylist;
    //カテゴリ数
    int numCategory=1;
    //カテゴリ追加ダイアログ
    AlertDialog addCategoryDialog;
    //EditText用変数群
    View viewDialog;
    //DiaLog用xmlから来るやつ
    LayoutInflater inflaterDialog;
    //DiaLog用xmlのEditText
    EditText editDialog;
    //選択されたカテゴリ名を格納するString型変数
    String choiceCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_category_test);

        categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        // アイテムを追加します
        categoryAdapter.add("red");
        categoryAdapter.add("green");
        categoryAdapter.add("blue");
        ListView listView = (ListView) findViewById(R.id.list_category);
        // アダプターを設定します
        listView.setAdapter(categoryAdapter);
        // リストビューのアイテムがクリックされた時に呼び出されるコールバックリスナーを登録します
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ListView listView = (ListView) parent;
                // クリックされたアイテムを取得します
                String item = (String) listView.getItemAtPosition(position);
                Toast.makeText(SettingCategoryTestActivity.this, item, Toast.LENGTH_LONG).show();
            }
        });
        // リストビューのアイテムが選択された時に呼び出されるコールバックリスナーを登録します
        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                ListView listView = (ListView) parent;
                // 選択されたアイテムを取得します
                String item = (String) listView.getSelectedItem();
                Toast.makeText(SettingCategoryTestActivity.this, item, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
