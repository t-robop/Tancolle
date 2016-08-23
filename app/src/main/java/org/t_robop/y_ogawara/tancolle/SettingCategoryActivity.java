package org.t_robop.y_ogawara.tancolle;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
    //カテゴリ追加ダイアログ
    AlertDialog addCategoryDialog;
    //カテゴリ数
    int numCategory;
    //EditText用変数群
    View viewDialog;
    //DiaLog用xmlから来るやつ
    LayoutInflater inflaterDialog;
    //DiaLog用xmlのEditText
    EditText editDialog;
    //preference用クラス
    PreferenceMethod PM;
    //FloatingActionButtonの宣言
    FloatingActionButton floatingBoth;
    //FloatingActionButtonの切り替えフラグ(false：追加モード,true：削除モード)
    boolean flagFloatingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_category);

        /*****ToolBar関連*****/
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("カテゴリ設定");
        }
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        /********************/
        //FloatingActionButtonの宣言
        floatingBoth = (FloatingActionButton) findViewById(R.id.floating_both);
        //preferenceクラス宣言
        PM = new PreferenceMethod();
        //カテゴリを表示するリストの初期設定
        listCategory=new ListView(this);
        //カテゴリ一覧用リストの関連付け
        listCategory=(ListView)findViewById(R.id.list_category);
        //カテゴリを保存するリストの初期設定
        categorylist=new ArrayList<>();
        //カテゴリを格納するアダプターの初期設定
        categoryAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice);
        /*****DiaLog用のxmlとの連携の関連付け*****/
        inflaterDialog = LayoutInflater.from(SettingCategoryActivity.this);
        viewDialog = inflaterDialog.inflate(R.layout.dialog_user_register, null);
        editDialog = (EditText) viewDialog.findViewById(R.id.editText1);
        /********************/
        //preference読み込んでアダプターにセット
        loadPreference();
        //アダプターをリストに反映
        addBtnListSet();
        /*****アイテムクリック時*****/
            listCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(final AdapterView<?> parent,
                                        View view, final int position, long id) {
                    /*****FloatingActionButtonの表示切替*****/
                    //チェック無しをカウントするための変数の宣言
                    int cntFloating=0;
                    //リストの要素の数だけループ
                    for (int i = 0; i < numCategory; i++) {
                        //要素のチェック状況取得用変数
                        boolean listCheck;
                        //指定したアイテムがチェック状態を取得
                        listCheck=listCategory.isItemChecked(i);
                        //チェックが入っていた時(true)
                        if(listCheck){
                            //ゴミ箱アイコンに変える
                            floatingBoth.setImageResource(R.drawable.ic_delete_white_48dp);
                            //FloatingActionButtonのアイコン状況をtrueに
                            flagFloatingBtn=true;
                        }
                        //チェックが入ってなかった時(false)
                        else{
                            //チェック無しをカウント
                            cntFloating++;
                        }
                    }
                    //チェックが入ってなかった要素が全要素数と同じ時(全てチェック無しの時)
                    if(cntFloating==numCategory){
                        //+アイコンに変える
                        floatingBoth.setImageResource(R.drawable.ic_add_white_48dp);
                        //FloatingActionButtonのアイコン状況をfalseに
                        flagFloatingBtn=false;
                    }
                    /********************/
                }
            });
        /********************/

        /*****FloatingActionButtonの設定*****/
        if (floatingBoth != null) {
            floatingBoth.setOnClickListener(new View.OnClickListener() {
                //クリックされた時
                @Override
                public void onClick(View view) {
                    //追加モード時(true)
                    if(!flagFloatingBtn) {
                        //カテゴリ追加用ダイアログの作成
                        CategoryAdd();
                        //作成したダイアログの表示
                        addCategoryDialog.show();
                    }
                    //削除モード時
                    else{
                        //このアクティビティに表示する削除確認ダイアログの宣言
                        AlertDialog.Builder aldialogDeleCategory=new AlertDialog.Builder(SettingCategoryActivity.this);
                        //ダイアログタイトルの決定
                        aldialogDeleCategory.setTitle("選択したカテゴリを削除しますか");
                        //positiveボタン(今回はok)のリスナー登録
                        aldialogDeleCategory.setPositiveButton("決定", new DialogInterface.OnClickListener() {
                            //削除用ダイアログ内のokボタン押した時
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //ループ用変数に現在の要素数を代入
                                int num=numCategory;
                                //要素のズレによってチェック判定がズレるのを直すための変数
                                int cnt=0;
                                //要素を上から数えるためのループ
                                for (int i = 0; i < num; i++) {
                                    //要素のチェック状況を取得
                                    boolean listCheck=listCategory.isItemChecked(i+cnt);
                                    //要素名を取得
                                    String itemName=categoryAdapter.getItem(i);
                                    //チェック入ってる時
                                    if (listCheck) {
                                        //選択されたカテゴリ名の要素を保存用リストから取り除く
                                        categorylist.remove(itemName);
                                        //選択されたカテゴリ名の要素をセット用アダプターから取り除く
                                        categoryAdapter.remove(itemName);
                                        //ズレを直す
                                        i--;
                                        num--;
                                        cnt++;
                                    }

                                }
                                //listCategoryが空でない時（エラー回避）
                                if (listCategory != null) {
                                    //表示用リストにセット用アダプターをセット
                                    listCategory.setAdapter(categoryAdapter);
                                }
                                //プレファレンスに保存用カテゴリを保存
                                PM.saveArray(categorylist, "StringItem", SettingCategoryActivity.this);
                                //セット用アダプターに格納されている要素を全て消す
                                categoryAdapter.clear();
                                //保存用リストに格納されている要素を全て消す
                                categorylist.clear();
                                //preference読み込んでアダプターにセット
                                loadPreference();
                                //追加ボタンセットとlistセット
                                addBtnListSet();
                                //+アイコンに変える
                                floatingBoth.setImageResource(R.drawable.ic_add_white_48dp);
                                //FloatingActionButtonのアイコン状況をfalseに
                                flagFloatingBtn=false;
                            }
                        });
                        //negativeボタン(今回はキャンセル)のリスナー登録
                        aldialogDeleCategory.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        //設定したダイアログの表示
                        aldialogDeleCategory.show();
                    }
                }
            });
        }
        /********************/
    }

    //MenuItem(戻るボタン)の選択
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //Activityの終了
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //端末のバックボタンクリック時
    @Override
    public void onBackPressed() {
        //Activity終了
        finish();
    }

    //preferenceからカテゴリ一覧を読み込む処理
    public void loadPreference(){
        // プリファレンスからカテゴリー一覧を取得
        categoryItem = PM.getArray("StringItem",this);
        //何かカテゴリが保存されてる時
        if(categoryItem!=null) {
            //要素数を取得
            numCategory=categoryItem.length;
            //保存されてるカテゴリ数だけループさせます
            for (int n = 0; n < numCategory; n++) {
                //list表示用adaptorにカテゴリを追加
                categoryAdapter.add(categoryItem[n]);
                //preference保存用adaptorにカテゴリを追加
                categorylist.add(categoryItem[n]);
            }
        }
        else{
            //nullを防ぐための0
            numCategory=0;
        }
    }

    //Category追加処理
    public void CategoryAdd() {
        //Adddialogが作成されていない時
        if(addCategoryDialog == null)
        {
            /*****追加用ダイアログの設定*****/
            addCategoryDialog =
                    //このアクティビティに表示
                    new AlertDialog.Builder(SettingCategoryActivity.this)
                        //DiaLogタイトル
                        .setTitle("カテゴリを追加")
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
                                //editTextに何か入力されてる時
                                else {
                                    //editTextのテキストを取得します
                                    addcategory = editDialog.getText().toString();
                                }
                                //入力された
                                if(addcategory!=null) {
                                    //セット用adaptorに入力された物を追加
                                    categoryAdapter.add(addcategory);
                                    //保存用listに入力された物を追加
                                    categorylist.add(addcategory);
                                }
                                //adapter更新
                                categoryAdapter.notifyDataSetChanged();
                                //editTextの初期化
                                editDialog.getEditableText().clear();
                                //追加ボタンセットとlistセット
                                addBtnListSet();
                                //プレファレンスにカテゴリの保存
                                PM.saveArray(categorylist, "StringItem",SettingCategoryActivity.this);
                                //追加した分を増加
                                numCategory++;
                            }
                        })
                        .setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
                            //DiaLog内の決定をクリックした時
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        //初回AddDiaLog制作
                        .create();
            /********************/
        }
    }

    //追加ボタンセットとlistセット
    public void addBtnListSet(){
        //listCategoryが空でない時（エラー回避）
            if (listCategory != null) {
                //listCategoryにadaptorセット
                listCategory.setAdapter(categoryAdapter);
            }
    }
}
