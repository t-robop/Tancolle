package org.t_robop.y_ogawara.tancolle;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class SettingCategoryActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_setting_category);

        //カテゴリを表示するリストの初期設定
        listCategory=new ListView(this);
        //カテゴリを保存するリストの初期設定
        categorylist=new ArrayList<>();
        //カテゴリを格納するアダプターの初期設定
        categoryAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        //カテゴリ一覧用リストの関連付け
        listCategory=(ListView)findViewById(R.id.list_category);

        //DiaLog用のxmlとの連携の関連付け
            inflaterDialog = LayoutInflater.from(SettingCategoryActivity.this);
            viewDialog = inflaterDialog.inflate(R.layout.dialog_user_register, null);
            editDialog = (EditText) viewDialog.findViewById(R.id.editText1);
        /////
        //preference読み込んでアダプターにセット
        loadPreference();
        //追加ボタンセットとアダプターをリストに反映
        addBtnListSet();
        //読み込んだカテゴリの数の取得
            //カテゴリに何か入ってる時限定
                if(categoryItem!=null) {
                    //カテゴリ一覧の配列数からカテゴリ数を取得
                    numCategory = categoryItem.length;
                }
            /////
            //カテゴリが何もない時（表示されてるのが「カテゴリの追加」のみの場合）
                else{
                    //この処理を書かないとnullになってしまうので
                    numCategory=0;
                }
        /////
        // アイテムクリック時のイベントを追加
            listCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(final AdapterView<?> parent,
                                        View view, final int position, long id) {
                    //追加ボタンクリック時
                        if(position==numCategory){
                            //カテゴリ追加用ダイアログの作成
                            CategoryAdd();
                            //作成したダイアログの表示
                            addCategoryDialog.show();
                        }
                    /////
                    //他の要素クリック時(削除処理)
                        else{
                            //選んだカテゴリの名前を取得
                            choiceCategory = String.valueOf(parent.getItemAtPosition(position));
                            //このアクティビティに表示する削除確認ダイアログの宣言
                            AlertDialog.Builder aldialogDeleCategory=new AlertDialog.Builder(SettingCategoryActivity.this);
                            //ダイアログタイトルの決定
                            aldialogDeleCategory.setTitle(choiceCategory+"を削除しますか？");
                            //positiveボタン(今回はok)のリスナー登録
                                aldialogDeleCategory.setPositiveButton("決定", new DialogInterface.OnClickListener() {
                                    //削除用ダイアログ内のokボタン押した時
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //選択されたカテゴリ名の要素をセット用アダプター・保存用リストから取り除く
                                                categoryAdapter.remove(choiceCategory);
                                                categorylist.remove(choiceCategory);
                                            /////
                                            //listCategoryが空でない時（エラー回避）
                                                if (listCategory != null) {
                                                    //表示用リストにセット用アダプターをセット
                                                    listCategory.setAdapter(categoryAdapter);
                                                }
                                            /////
                                            //プレファレンスに保存用カテゴリを保存
                                            saveArray(categorylist, "StringItem");
                                            //セット用アダプター・保存用リストに格納されている要素を全て消す
                                                categoryAdapter.clear();
                                                categorylist.clear();
                                            /////
                                            //preference読み込んでアダプターにセット
                                            loadPreference();
                                            //追加ボタンセットとlistセット
                                            addBtnListSet();
                                            //読み込んだカテゴリの数の取得
                                                //カテゴリに何か入ってる時限定
                                                    if(categoryItem!=null) {
                                                        //カテゴリ一覧の配列数からカテゴリ数を取得
                                                        numCategory = categoryItem.length;
                                                    }
                                                /////
                                                //カテゴリが何もない時（表示されてるのが「カテゴリの追加」のみの場合）
                                                    else{
                                                        //この処理を書かないとnullになってしまうので
                                                        numCategory=0;
                                                    }
                                                /////
                                            /////
                                        }
                                    /////
                                });
                            /////
                            //negativeボタン(今回はキャンセル)のリスナー登録
                            aldialogDeleCategory.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                            //設定したダイアログの表示
                            aldialogDeleCategory.show();
                        }
                    /////
                }
            });
        /////
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

    //preferenceからカテゴリ一覧を読み込む処理
    public void loadPreference(){
        // プリファレンスからカテゴリー一覧を取得
        categoryItem = getArray("StringItem");
        //何かカテゴリが保存されてる時
            if(categoryItem!=null) {
                //保存されてるカテゴリ数だけループさせます
                    for (int n = 0; n < categoryItem.length; n++) {
                        //読み込んだカテゴリを追加
                            //list表示用adaptor
                            categoryAdapter.add(categoryItem[n]);
                            //preference保存用adaptor
                            categorylist.add(categoryItem[n]);
                        /////
                    }
                /////
            }
        /////
    }

    //Category追加処理
    public void CategoryAdd() {
        //Adddialogが作成されていない時
        if(addCategoryDialog == null)
        {
            //追加用ダイアログの設定
                addCategoryDialog =
                        //このアクティビティに表示
                        new AlertDialog.Builder(SettingCategoryActivity.this)
                        //DiaLogタイトル
                        .setTitle("カテゴリー入力")
                        //設定したダイアログ用xmlのView指定
                        .setView(viewDialog)
                        //ボタン作成
                        .setPositiveButton("決定", new DialogInterface.OnClickListener()
                        {
                            //DiaLog内の決定をクリックした時
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //セット用アダプター・保存用リストに格納されている要素を全て消す
                                    categoryAdapter.clear();
                                    categorylist.clear();
                                /////
                                //preference読み込んでアダプターにセット
                                loadPreference();
                                //入力されたカテゴリ名を取得する変数
                                String addcategory;
                                //editTextのテキストを全選択します
                                editDialog.selectAll();
                                //editTextに何も入力されてない時
                                    if (editDialog.getText().toString().equals("")) {
                                        //nullとする
                                        addcategory = null;
                                    }
                                /////
                                //editTextに何か入力されてる時
                                    else {
                                        //editTextのテキストを取得します
                                        addcategory = editDialog.getText().toString();
                                    }
                                /////
                                //入力された
                                    if(addcategory!=null) {
                                        //セット用adaptorに入力された物を追加
                                        categoryAdapter.add(addcategory);
                                        //保存用listに入力された物を追加
                                        categorylist.add(addcategory);
                                    }
                                /////
                                //adapter更新
                                categoryAdapter.notifyDataSetChanged();
                                //editTextの初期化
                                editDialog.getEditableText().clear();
                                //追加ボタンセットとlistセット
                                addBtnListSet();
                                //プレファレンスにカテゴリの保存
                                saveArray(categorylist, "StringItem");
                                //読み込んだカテゴリの数の取得
                                    //カテゴリに何かある時
                                        if(categoryItem!=null) {
                                            //配列数からカテゴリ数を取得
                                            numCategory = categoryItem.length;
                                            //今増やした分
                                            numCategory++;
                                        }
                                    /////
                                    //カテゴリが何も無い時
                                        else{
                                            //今追加した分を加算
                                            numCategory=1;
                                        }
                                    /////
                                /////
                            }
                        })
                        .setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
                                    //DiaLog内の決定をクリックした時
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                        })
                        .create();//初回AddDiaLog制作
            /////
        }
        /////
    }

    //追加ボタンセットとlistセット
    public void addBtnListSet(){
        //「カテゴリを追加」を追加します
        categoryAdapter.add("カテゴリを追加");
        //listCategoryが空でない時（エラー回避）
            if (listCategory != null) {
                //listCategoryにadaptorセット
                listCategory.setAdapter(categoryAdapter);
            }
        /////
    }

}
