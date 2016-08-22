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
import android.widget.AbsListView;
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
    //選択されたカテゴリ名を格納するString型変数
    String choiceCategory;

    //preference用クラス
    PreferenceMethod PM;

    FloatingActionButton floatingBoth;
    //FloatingActionButtonの切り替えフラグ(false：追加モード,true：削除モード)
    boolean flagFloatingBtn;

    //キーボード制御
    InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_category);

        //ToolBar関連
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("表示設定");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //FloatingActionButtonの宣言
        floatingBoth = (FloatingActionButton) findViewById(R.id.floating_both);

        //preferenceクラス宣言
        PM=new PreferenceMethod();

        //キーボード表示を制御（出したり消したり）するためのオブジェクトの関連付け
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //カテゴリを表示するリストの初期設定
        listCategory=new ListView(this);
        //カテゴリを保存するリストの初期設定
        categorylist=new ArrayList<>();
        //カテゴリを格納するアダプターの初期設定
        categoryAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice);

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
        // アイテムクリック時のイベントを追加
            listCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(final AdapterView<?> parent,
                                        View view, final int position, long id) {

                    int cntFloating=0;
                    for (int i = 0; i < numCategory; i++) {
                        // 指定したアイテムがチェックされているか確認
                        boolean listCheck;
                        listCheck=listCategory.isItemChecked(i);
                        if(listCheck==true){
                            //ゴミ箱に
                            floatingBoth.setImageResource(R.drawable.ic_delete_white_48dp);
                            flagFloatingBtn=true;
                        }
                        else{
                            cntFloating++;
                        }
                    }
                    if(cntFloating==numCategory){
                        //+に
                        floatingBoth.setImageResource(R.drawable.ic_add_white_48dp);
                        flagFloatingBtn=false;
                    }

                }

            });
        /////

        // FloatingActionButton
        if (floatingBoth != null) {
            floatingBoth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //追加モード時(true)
                    if(!flagFloatingBtn) {
                        //キーボード表示
                        inputMethodManager.showSoftInput(viewDialog, InputMethodManager.SHOW_FORCED);
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
                        aldialogDeleCategory.setTitle("選択されたカテゴリを削除しますか");
                        //positiveボタン(今回はok)のリスナー登録
                        aldialogDeleCategory.setPositiveButton("決定", new DialogInterface.OnClickListener() {
                            //削除用ダイアログ内のokボタン押した時
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                int num=numCategory;
                                for (int i = 0; i < num; i++) {

                                        boolean listCheck;
                                        listCheck = listCategory.isItemChecked(i);

                                    String itemName=categoryAdapter.getItem(i);

                                    //true
                                        if (listCheck) {

                                            //最後のやつ以外
                                            if(i==numCategory-1&&i==0) {
                                                categorylist.clear();
                                                categoryAdapter.clear();
                                            }
                                            else{
                                                //選択されたカテゴリ名の要素をセット用アダプター・保存用リストから取り除く
                                                categorylist.remove(itemName);
                                                categoryAdapter.remove(itemName);
                                                /////
                                                i--;
                                                num--;
                                            }
                                        }

                                }
                                //listCategoryが空でない時（エラー回避）
                                if (listCategory != null) {
                                    //表示用リストにセット用アダプターをセット
                                    listCategory.setAdapter(categoryAdapter);
                                }
                                /////
                                    //プレファレンスに保存用カテゴリを保存
                                    PM.saveArray(categorylist, "StringItem", SettingCategoryActivity.this);
                                    //セット用アダプター・保存用リストに格納されている要素を全て消す
                                    categoryAdapter.clear();
                                    categorylist.clear();
                                    /////
                                    //preference読み込んでアダプターにセット
                                    loadPreference();
                                    //追加ボタンセットとlistセット
                                    addBtnListSet();

                                //+に
                                floatingBoth.setImageResource(R.drawable.ic_add_white_48dp);
                                flagFloatingBtn=false;

                                /////
                            }
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
                        /////
                    }
                }
            });
        }
    }

    //MenuItem(戻るボタン)の選択
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //フラグの保存
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
                numCategory=categoryItem.length;
                //保存されてるカテゴリ数だけループさせます
                    for (int n = 0; n < numCategory; n++) {
                        //読み込んだカテゴリを追加
                            //list表示用adaptor
                            categoryAdapter.add(categoryItem[n]);
                            //preference保存用adaptor
                            categorylist.add(categoryItem[n]);
                        /////
                    }
                /////
            }
            else{
                    numCategory=0;
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
                                PM.saveArray(categorylist, "StringItem",SettingCategoryActivity.this);

                                numCategory++;
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
        //listCategoryが空でない時（エラー回避）
            if (listCategory != null) {
                //listCategoryにadaptorセット
                listCategory.setAdapter(categoryAdapter);
            }
        /////
    }

}
