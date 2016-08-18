package org.t_robop.y_ogawara.tancolle;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class SettingCategoryActivity extends AppCompatActivity {

    //カテゴリ一覧を表示するlistの宣言
    ListView listCategory;
    //カテゴリ一覧読み込み用String[]型の宣言
    String categoryItem[];
    //カテゴリ用arraylist&arrayadapter
    ArrayAdapter<String> categoryAdapter;
    //カテゴリのList
    ArrayList<String> categorylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_category);

        listCategory=new ListView(this);

        categoryAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        // プリファレンスからカテゴリー一覧を取得
        categoryItem = getArray("StringItem");
        // カテゴリー追加処理
        //何かカテゴリが保存されてる時
            if(categoryItem!=null) {
                //保存されてるカテゴリ数だけループさせます
                    for (int n = 0; n < categoryItem.length; n++) {
                        //読み込んだカテゴリを追加
                            categoryAdapter.add(categoryItem[n]);
                            //categorylist.add(categoryItem[n]);
                        /////
                    }
                /////
            }
        /////
        //最後に「カテゴリを追加」を追加します
        categoryAdapter.add("カテゴリを追加");

        //カテゴリ一覧用リストの宣言
        listCategory=(ListView)findViewById(R.id.CategoryLst);
        if (listCategory != null) {
            listCategory.setAdapter(categoryAdapter);
        }
    }

    // プリファレンス保存
// aaa,bbb,ccc... の文字列で保存
    private void saveArray(ArrayList<String> array, String PrefKey){
        String str = new String("");
        for (int i =0;i<array.size();i++){
            str = str + array.get(i);
            if (i !=array.size()-1){
                str = str + ",";
            }
        }
        SharedPreferences prefs1 = getSharedPreferences("Array", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs1.edit();
        editor.putString(PrefKey, str).commit();
    }

    // プリファレンス取得
// aaa,bbb,ccc...としたものをsplitして返す
    private String[] getArray(String PrefKey){
        SharedPreferences prefs2 = getSharedPreferences("Array", Context.MODE_PRIVATE);
        String stringItem = prefs2.getString(PrefKey,"");
        if(stringItem != null && stringItem.length() != 0){
            return stringItem.split(",");
        }else{
            return null;
        }
    }
}
