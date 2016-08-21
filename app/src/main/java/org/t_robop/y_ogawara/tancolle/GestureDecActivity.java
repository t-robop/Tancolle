package org.t_robop.y_ogawara.tancolle;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class GestureDecActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    //横スクロールの宣言
    private HorizontalScrollView horizontalScrollView;
    //
    private GestureDetector gestureDetector;

    private int page = 0; // ページ数
    private int displayWidth; // 画面サイズ：X
    private int displayHeight; // 画面サイズ：Y
    private int pageCount = 0; // 画面数 (要するに最後の画面)

    private boolean scrollFlg = false; // スクロールチェック
    private static final int SCROLL_NONE = 0; // スライド距離が一定量を超えない
    private static final int SCROLL_LEFT = 2; //
    private static final int SCROLL_RIGHT = 1; //
    private int slideLimitFlg = SCROLL_NONE; // スライドの状態判定

    int MONTH; //端末の月


    static int num2;

    //カテゴリの値をいれる箱
    String Category = "aa";


    MainListAdapter[] mainListAdapter;

    ListView[] listView;

    static boolean flag;

    //preference用クラス
    PreferenceMethod PM;
    //データ読み書き
    SharedPreferences pref;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_dec);
        setViewSize();


        //preferenceクラス宣言
        PM=new PreferenceMethod();

        //Permission確認 Android6.0以上
        permissionAcquisition();

        // FloatingActionButton
        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.add);
        if (add != null) {
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(GestureDecActivity.this, UserRegisterActivity.class);
                    intent.putExtra("month", page + 1);
                    startActivity(intent);
                }
            });
        }

        // GestureDetectorの生成
        gestureDetector = new GestureDetector(getApplicationContext(), this);
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.hsv_main);
        horizontalScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // GestureDetectorにイベントを委譲する
                boolean result = gestureDetector.onTouchEvent(event);

                // スクロールが発生した後に画面から指を離した時
                if ((event.getAction() == MotionEvent.ACTION_UP) && scrollFlg) {
                    switch (slideLimitFlg) {
                        case SCROLL_NONE:
                            break;
                        case SCROLL_LEFT:
                            setPage(true);
                            break;
                        case SCROLL_RIGHT:
                            setPage(false);
                            break;
                    }
                    // 指定ページへスクロールする
                    horizontalScrollView.scrollTo(page * displayWidth,displayHeight);
                    TextView textView = (TextView) findViewById(R.id.current_month);
                    textView.setText(String.valueOf(page + 1) + "月");
                }
                return result;
            }
        });

    }




    @Override
    protected void onStart() {
        super.onStart();
        MONTH =  Calendar.getInstance().get(Calendar.MONTH);
        page = MONTH;
        //idの関連付け
        idSet();



        //12ヶ月分セットするために12回ループさせます。
        for (int fullReturn = 0; fullReturn < 12; fullReturn++) {

            ArrayList<Data> monthTurnData;//ArrayListの宣言

            //:TODO 中里見がspinnerを実装し終わっていないため未検証

            //prefの設定 細かいところはSettingDrawに準じているので不明
            SharedPreferences pref = getSharedPreferences("Setting", MODE_PRIVATE);


            if (Category == "すべて") {
                monthTurnData = dbAssist.birthdaySelect(fullReturn + 1, this);//ArrayListに月検索したデータをぶち込む
            }else{
                //ArrayListに月とCategory条件の一致したデータを入れる
                monthTurnData = dbAssist.birthdayAndCategorySelect(fullReturn + 1,Category, this);
            }


//誕生日表示(false)か残日表示(true)かを取得
            boolean drawType = pref.getBoolean("drawType", false);


            MainAdapterData Mad;//自分で作成したclassの宣言

            ArrayList<MainAdapterData> adapterData = new ArrayList<>();//classのArrayListの作成


            int num = monthTurnData.size();//int型変数numにmonthTurnDataの配列数を入れる

            //欠番している数
            num2 = 3 - (num % 3);

            //読み込んだ月のデータの数だけ回す。（3分の1でいいのと、後述のListデータの取得に使うため+3）
            for (int j = 0; j < monthTurnData.size(); j = j + 3) {
                //三人分だけ保存するため3回回す。
                // Mad.startMad();//クラスの変数の初期化
                Mad = new MainAdapterData();//自分で作成したclassの宣言

                for (int i = 0; i < 3; i++) {
                    Data getData;//monthTurnData取得用のデータ型

                    Log.d("aaaa", String.valueOf(i + j));

                    if (i + j + 1 <= num)//iとnumを比較してiの方が低い時だけ（データ無いのに取得しようとして落ちるやつの修正）
                    {
                        getData = monthTurnData.get(i + j);//読み込んだListの要素を取得

                        Mad.setId(i, getData.getId());//idのセット
                        Mad.setName(i, getData.getName());//名前のセット
                        Mad.setBirthMonth(i, getData.getMonth());//誕生月のセット
                        Mad.setBirthDay(i, getData.getDay());//誕生日のセット
                        Mad.setPresentFlag(i, getData.isPresentFlag());//プレゼントフラグのセット
                    }
                }

                //この辺に書き込み処理書いてくらさい。

                Mad.setAllSize(num);
                adapterData.add(Mad);//三人のデータの追加

            }

            //Adapterをセット
            mainListAdapter[fullReturn] = new MainListAdapter(this, 0, adapterData);
            //listView.setEmptyView(findViewById(R.id.listView));
            listView[fullReturn].setAdapter(mainListAdapter[fullReturn]);
            SpinnerSetting();
            horizontalScrollView.scrollTo(page * displayWidth,displayHeight);

        }

    }
    public void SpinnerSetting(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /********spinnerの設定関連**********/
        String spinnerItems[];
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        //adaptor全てふっとばす
        adapter.clear();
        //まず"すべて"を追加
        adapter.add("すべて");
        // プリファレンスからカテゴリー一覧を取得
        spinnerItems = PM.getArray("StringItem",this);
        //何かカテゴリが保存されてる時
        if(spinnerItems!=null) {
            //保存されてるカテゴリ数だけループさせます
            for (int n = 0; n < spinnerItems.length; n++) {
                //adaptorに読み込んだカテゴリを追加
                adapter.add(spinnerItems[n]);
                /////
            }
            /////
        }
        /////
        Spinner spinner =  (Spinner) toolbar.getChildAt(0);
        spinner.setAdapter(adapter);
        /*****選択されてたカテゴリの読み込みとセット*****/
        //"Setting"をプライベートモードで開く
        SharedPreferences pref = getSharedPreferences("Setting", MODE_PRIVATE);
        //String型に"choiseCategory"の文字列を代入(何も保存されてない時はnull)
        String choiseCategory = pref.getString("choiseCategory", null);
        //nullでない(何か保存されてる)時
        if(choiseCategory!=null){
            //spinnerに取得したカテゴリをセット
            setSelection(spinner,choiseCategory);
        }
        /********************/

        // リスナーを登録
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //　アイテムが選択された時
            public void onItemSelected(AdapterView<?> parent, View viw, int arg2, long arg3) {
                Spinner spinner = (Spinner) parent;
                Category = (String) spinner.getSelectedItem();
                //
                if (Category.equals("すべて") ){

                }

                /*****選択したカテゴリの保存*****/
                SharedPreferences preferCategory;
                //"Setting"をプライベートモードで開く
                preferCategory = getSharedPreferences("Setting", MODE_PRIVATE);
                //editorを展開
                SharedPreferences.Editor editor = preferCategory.edit();
                //"choiseCategory"に選択されたカテゴリを保存
                editor.putString("choiseCategory", Category);
                editor.commit();
                /********************/

// 画面セットしなおし


                //12ヶ月分セットするために12回ループさせます。
                for (int fullReturn = 0; fullReturn < 12; fullReturn++) {
                    mainListAdapter[fullReturn].clear();
                    mainListAdapter[fullReturn].notifyDataSetChanged();


                    ArrayList<Data> monthTurnData;//ArrayListの宣言

                    //:TODO 中里見がspinnerを実装し終わっていないため未検証

                    //prefの設定 細かいところはSettingDrawに準じているので不明
                    SharedPreferences pref = getSharedPreferences("Setting", MODE_PRIVATE);




                    if (Category.equals("すべて")) {
                        monthTurnData = dbAssist.birthdaySelect(fullReturn + 1, getApplicationContext());//ArrayListに月検索したデータをぶち込む
                    }else{
                        //ArrayListに月とCategory条件の一致したデータを入れる
                        monthTurnData = dbAssist.birthdayAndCategorySelect(fullReturn + 1,Category, getApplicationContext());
                    }


//誕生日表示(false)か残日表示(true)かを取得
                    boolean drawType = pref.getBoolean("drawType", false);


                    MainAdapterData Mad;//自分で作成したclassの宣言

                    ArrayList<MainAdapterData> adapterData = new ArrayList<>();//classのArrayListの作成


                    int num = monthTurnData.size();//int型変数numにmonthTurnDataの配列数を入れる

                    //欠番している数
                    num2 = 3 - (num % 3);

                    //読み込んだ月のデータの数だけ回す。（3分の1でいいのと、後述のListデータの取得に使うため+3）
                    for (int j = 0; j < monthTurnData.size(); j = j + 3) {
                        //三人分だけ保存するため3回回す。
                        // Mad.startMad();//クラスの変数の初期化
                        Mad = new MainAdapterData();//自分で作成したclassの宣言

                        for (int i = 0; i < 3; i++) {
                            Data getData;//monthTurnData取得用のデータ型

                            Log.d("aaaa", String.valueOf(i + j));

                            if (i + j + 1 <= num)//iとnumを比較してiの方が低い時だけ（データ無いのに取得しようとして落ちるやつの修正）
                            {
                                getData = monthTurnData.get(i + j);//読み込んだListの要素を取得

                                Mad.setId(i, getData.getId());//idのセット
                                Mad.setName(i, getData.getName());//名前のセット
                                Mad.setBirthMonth(i, getData.getMonth());//誕生月のセット
                                Mad.setBirthDay(i, getData.getDay());//誕生日のセット
                                Mad.setPresentFlag(i, getData.isPresentFlag());//プレゼントフラグのセット
                            }
                        }

                        //この辺に書き込み処理書いてくらさい。

                        Mad.setAllSize(num);
                        adapterData.add(Mad);//三人のデータの追加

                    }

                    //Adapterをセット
                    mainListAdapter[fullReturn].addAll(adapterData);
                    //listView.setEmptyView(findViewById(R.id.listView));

                    flag = true;
                    mainListAdapter[fullReturn].notifyDataSetChanged();

                    //listView[fullReturn].invalidateViews();
                }
            }

            //　アイテムが選択されなかった
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /********spinnerの設定　終わり************/

        Log.d("onResume", "onResume");
        pref = getSharedPreferences("temp", Context.MODE_PRIVATE);
        //page = pref.getInt("page",0);

        TextView textView = (TextView) findViewById(R.id.current_month);
        textView.setText(String.valueOf(page + 1 +"月"));

    }


    //spinnerの中から文字列を探してセットするメソッド
    public static void setSelection(Spinner spinner, String item) {
        //spinnerにセットされてるadaptorを取得
        SpinnerAdapter adapter = spinner.getAdapter();
        //position取得用変数の宣言
        int index = 0;
        //adaptorの要素数だけ回す
        for (int i = 0; i < adapter.getCount(); i++) {
            //adaptorの要素に指定された文字列があった時
            if (adapter.getItem(i).equals(item)) {
                //positionを取得してbreak
                index = i; break;
            }
        }
        //取得したpositionの要素をspinnerにセット
        spinner.setSelection(index);
    }

    // ページ設定用 true;次のページ false:前のページ
    private void setPage(boolean check) {
        if (check) {
            if (page < pageCount) {
                page++;
            }
            //もし一番右にいた時に、右側に行こうとした時
            else {
                page = 0;
            }
        } else {
            if (page > 0) {
                page--;
            }
            //もし一番左側にいた時に、左に行こうとした時
            else {
                page = pageCount;
            }
        }
    }

    // 各ImageViewを画面サイズと同じサイズに設定
    private void setViewSize() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        displayWidth = display.getWidth();
        displayHeight = display.getHeight();

        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(
                displayWidth, displayHeight);
        ViewGroup layout = (ViewGroup) findViewById(R.id.ll_main);

        // ページ数の設定
        pageCount = layout.getChildCount() - 1;

        for (int i = 0; i <= pageCount; i++) {
            layout.getChildAt(i).setLayoutParams(layoutParam);
        }
    }

    // フリック入力の検出
    @Override
    public boolean onFling(MotionEvent envent1, MotionEvent envent2,
                           float velocityX, float velocityY) {
        Log.d("onFling", "onFling");
        // スクロールが一定距離を超えていない時のフリック操作は有効とする
//        if (slideLimitFlg == SCROLL_NONE) {
//            if (velocityX < 0) {
//                // 左フリック
//                setPage(true);
//            } else if (velocityX > 0) {
//                // 右フリック
//                setPage(false);
//            }
//            horizontalScrollView.scrollTo(page * displayWidth, displayHeight);
//        }
        return true;
    }

    // スライド入力検出
    @Override
    public boolean onScroll(MotionEvent envent1, MotionEvent envent2,
                            float distanceX, float distanceY) {
        Log.d("onScroll", "onScroll");
        scrollFlg = true;
        int rangeX = 0;
        //envent1がnullの時(１月データの時に12月に行こうとするとでる)
        if (envent1 == null) {
            rangeX = (int) (0 - envent2.getRawX());
        } else {
            // スライド距離の計算
            rangeX = (int) (envent1.getRawX() - envent2.getRawX());
        }


        if (rangeX < -displayWidth * 0.15) {
            // 右に一定距離のスライド
            slideLimitFlg = SCROLL_RIGHT;
        } else if (rangeX > displayWidth * 0.15) {
            // 左に一定距離のスライド
            slideLimitFlg = SCROLL_LEFT;
        } else {
            slideLimitFlg = SCROLL_NONE;
        }
        return false;
    }


    //ここでmenuを作る
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gesturedec_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //ActionBarにあるボタン関連
    //@menuにあるやつから持ってきてる
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                Intent intent_setting = new Intent(GestureDecActivity.this, SettingActivity.class);
                startActivity(intent_setting);
                return true;

            case R.id.action_search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    //戻るキーで終了するやつ(コピペ)
    //たぶんhandlerでflagを変えているだけ
    private boolean FinishFlag;
    @Override
    public void onBackPressed() {
        if (FinishFlag) {
            finish();
        } else {
            Toast.makeText(this, "もう一度押すと終了します", Toast.LENGTH_SHORT).show();
            FinishFlag = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    FinishFlag = false;
                }
            }, 2000);
        }
    }

    /***
     * 今回未使用のOnGestureListener関連イベント
     *********************/
    @Override
    public boolean onDown(MotionEvent envent) {
        Log.d("onDown", "onDown");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent envent) {
        Log.d("onShowPress", "onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent envent) {
        Log.d("onSingleTapUp", "onSingleTapUp");
        return false;
    }

    @Override
    public void onLongPress(MotionEvent envent) {
        Log.d("onLongPress", "onLongPress");
    }

    /*******************************************************************/
    //リストをクリックした時のイベント
    public void listClick(View view) {
        Log.d("hello", String.valueOf(view.getTag()));


//        //listViewのitemを取得してadapterからItemをもらってくる
//        ListView listView = (ListView) parent;
//        listView.getItemAtPosition(position);

        int numData = new Integer((Integer) view.getTag());

        if (numData != 0) {
            //Intentで飛ばす＆idをキーにする
            Intent intent = new Intent(GestureDecActivity.this, UserDetailActivity.class);
            intent.putExtra("id", numData);

            //page番号を保存
            //SharedPreferences.Editor editor = pref.edit();
            //editor.putInt("page", page).apply();
            //intent.putExtra("page",page);
            startActivity(intent);
        }
    }
    private void permissionAcquisition() {

        // Android6.0以降でのPermissionの確認
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // 許可されている時の処理
            Log.d("Accept", "Accept");
        } else {
            //拒否されている時の処理
            Log.d("Deny", "Deny");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //拒否された時 Permissionが必要な理由を表示して再度許可を求めたり、機能を無効にしたりします。
                Log.d("Alert", "Alert");
                //AlertDialog
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                //alertDialog.setTitle("");          //タイトル
                alertDialog.setMessage("顔写真を追加する際にストレージへのアクセスが必要です。" + "\n" + "次の画面で許可を押してください。");      //内容
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("AlertDialog", "Positive which :" + which);
                        ActivityCompat.requestPermissions(GestureDecActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                    }
                });
                alertDialog.create();
                alertDialog.show();

            } else {
                //まだ許可を求める前の時、許可を求めるダイアログを表示します。
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                Log.d("else", "else");
            }
        }
    }
    private void idSet() {
        mainListAdapter = new MainListAdapter[12];

        //配列で12ヶ月分のlistView作ります
        listView = new ListView[12];
        listView[0] = (ListView) findViewById(R.id.list1).findViewById(R.id.listView1);
        listView[1] = (ListView) findViewById(R.id.list2).findViewById(R.id.listView1);
        listView[2] = (ListView) findViewById(R.id.list3).findViewById(R.id.listView1);
        listView[3] = (ListView) findViewById(R.id.list4).findViewById(R.id.listView1);
        listView[4] = (ListView) findViewById(R.id.list5).findViewById(R.id.listView1);
        listView[5] = (ListView) findViewById(R.id.list6).findViewById(R.id.listView1);
        listView[6] = (ListView) findViewById(R.id.list7).findViewById(R.id.listView1);
        listView[7] = (ListView) findViewById(R.id.list8).findViewById(R.id.listView1);
        listView[8] = (ListView) findViewById(R.id.list9).findViewById(R.id.listView1);
        listView[9] = (ListView) findViewById(R.id.list10).findViewById(R.id.listView1);
        listView[10] = (ListView) findViewById(R.id.list11).findViewById(R.id.listView1);
        listView[11] = (ListView) findViewById(R.id.list12).findViewById(R.id.listView1);

    }
    public void onResume(){
        super.onResume();
        //horizontalScrollView.scrollTo(page * displayWidth,displayHeight);
        horizontalScrollView.post(new Runnable() {
            public void run() {
                horizontalScrollView.scrollTo(page * displayWidth,displayHeight);
            }
        });
    }

}
