package org.t_robop.y_ogawara.tancolle;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by iris on 2016/08/23.
 */
public class ReUserDetailActivity extends AppCompatActivity {


    int intentId;
    TextView nameTV;
    TextView kanaTV;
    TextView birthTV;
    TextView ageTV;
    TextView remaTV;
    TextView memoTV;
    TextView cateTV;
    Calendar calendar;
    int year, month, day; //現在の日付
    int a, b;
    int age;//年齢の計算結果を入れる箱
    ImageView image;
    int imagecount; //プレゼントボタンの判定
    int yukarin;
    String memo;
    String TwitterID;
    //人の顔写真が入るとこ
    ImageView photoImageView;
    //画像データを一時的に蓄えるとこ
    Bitmap bitmap;

    //前のpage番号
    int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        //Toolbar関連
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setTitle("title");

//        // 文字色(縮小時)
//        toolbarLayout.setCollapsedTitleTextColor("");
//        // 文字色(展開時)
//        toolbarLayout.setExpandedTitleColor(文字色(展開時));

        Intent intent = getIntent();
        intentId = intent.getIntExtra("id", 1);
        Data data = dbAssist.idSelect(intentId, this);
        String category = data.getCategory();

        //初期設定
        PreferenceMethod PM;
        PM = new PreferenceMethod();
        //配列を読み込み (保存のkey,場所)
        String[] categoryItem = PM.getArray("StringItem", this);
        int count = 0;
        if (categoryItem != null) {
            for (int i = 0; i < categoryItem.length; i++) { //0からカテゴリリストの最大値まで繰り返す
                if (!(categoryItem[i].equals(category))) { //もしもカテゴリリストのi個目と今読み込んだカテゴリの名前が一致しなかったら
                    count++; //カウントを足していく
                }
                if (count == categoryItem.length) { //もし一致しなかった数＝カテゴリの最大値だったら（一個も一致しない 存在しなかったら）
                    Data updateData = new Data(); //そのカテゴリは存在しないのでSQLに未選択で書き換える
                    updateData.setCategory("<未選択>");
                    dbAssist.updateData(intentId, updateData, this);
                }
            }

        } else {
            if (!category.equals("<未選択>")) {
                Data updateData = new Data(); //そのカテゴリは存在しないのでSQLに未選択で書き換える
                updateData.setCategory("<未選択>");
                dbAssist.updateData(intentId, updateData, this);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        intentId = intent.getIntExtra("id", 1);
        page = intent.getIntExtra("page",0);
        nameTV = (TextView) findViewById(R.id.Name);
        kanaTV = (TextView) findViewById(R.id.Kana);
        birthTV = (TextView) findViewById(R.id.Birthay);
        ageTV = (TextView) findViewById(R.id.age);
        remaTV = (TextView) findViewById(R.id.nokori);
        memoTV = (TextView) findViewById(R.id.chou);
        cateTV = (TextView) findViewById(R.id.category);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1; //0から11までしかないから１個足す
        day = calendar.get(Calendar.DAY_OF_MONTH);




        Data data = dbAssist.idSelect(intentId, this);
        String name = data.getName(); //SQliteからもってくる
        String kana = data.getKana();
        String smallImage = data.getImage(); //TODO
        String category = data.getCategory();
        TwitterID = data.getTwitterID();
        memo = data.getMemo();
        imagecount = data.isPresentFlag();
        yukarin = data.isYukarin();
        Log.d("aaaaaa",String.valueOf(imagecount));
        if (imagecount == Integer.MIN_VALUE) {
            imagecount = 0;

        }

        //データを読みだして、その値でセットする画像を変える

//        if(imagecount==0){
//            image.setImageResource(R.drawable.ao);
//        }else{
//            image.setImageResource(R.drawable.ribon);
//        }


//画像読み込み
//        if(smallImage.equals("null.jpg")){
//            photoImageView.setImageResource(R.drawable.normal_shadow);
//        }else {
//            InputStream in;
//            try {
//                in = openFileInput(smallImage);//画像の名前からファイル開いて読み込み
//                bitmap = BitmapFactory.decodeStream(in);//読み込んだ画像をBitMap化
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            photoImageView.setImageBitmap(bitmap);
//        }

        //int birth = data.getBirthday();
        int birthyear = data.getYear();
        int birthmonth = data.getMonth();
        int birthday = data.getDay();

        a = birthmonth * 100 + birthday;  //誕生日を７月１４日を→７１４みたいな形に
        b = month * 100 + day; //現在の日付を６月１５日→６１５みたいな形に
        if (a > b) {  //もし誕生日の方の数値が大きかったら（まだ今年の誕生日がきてなかったら）
            age = year - birthyear - 1;   //今年ー誕生年から更に１才ひく
        } else {    //今年の誕生日がきていたら
            age = year - birthyear;  //そのまんま今年から誕生年をひく
        }

        //データのフォーマット
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        //date型の宣言
        Date dateTo = null;
        Date dateFrom = null;

        // 日付を作成します。
        try {
            //TODO ココらへんで今年の誕生日が過ぎていたら、来年の誕生日で計算させる
            dateFrom = sdf.parse(year + "/" + month + "/" + day); //現在の日付
            //指定フォーマットでデータを入力
            if (a < b) {  //誕生日より今日の日にちが大きかったら（もう誕生日がきていたら
                int num;
                num = year + 1; //＋１して来年の誕生日の数値を割り出す

                dateTo = sdf.parse(num + "/" + birthmonth + "/" + birthday);
            } else {  //まだ誕生日がきていなかったら

                dateTo = sdf.parse(year + "/" + birthmonth + "/" + birthday); //今年の誕生日を作る
            }

            //エラーが起きた時
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 日付をlong値に変換します。
        long dateTimeTo = dateTo.getTime();
        long dateTimeFrom = dateFrom.getTime();

        // 差分の日数を算出します。
        long dayDiff = (dateTimeTo - dateTimeFrom) / (1000 * 60 * 60 * 24);
        //int型に変換
        int remDay = (int) dayDiff;


        nameTV.setText(name);
        kanaTV.setText(kana);
        if(category.equals("<未選択>")){
            cateTV.setText("#"+category);
        }else{
            cateTV.setText("#<"+category+">");

        }
        birthTV.setText(String.valueOf(birthmonth) + "/" + String.valueOf(birthday));
        remaTV.setText("残り" + String.valueOf(remDay) + "日");
        memoTV.setText(memo);
        if(yukarin==0){
            ageTV.setText(String.valueOf(age) + "才");
        }else{
            ageTV.setText("XX才");
        }
    }

    public void memoclick(View view) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View dialog_view = inflater.inflate(R.layout.dialog_layout, null);
        //editTextを生成
        final EditText editText = new EditText(this);
        //EditTextの中身を指定
        editText.setText(memo);
        //レイアウトファイルからビューを取得

        //レイアウト、題名、OKボタンとキャンセルボタンをつけてダイアログ作成
        builder.setView(dialog_view)
                .setTitle("Memo")
                //ここで値をセットする
                .setView(editText)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                    /*OKのときの処理内容*/

                                String string = editText.getText().toString();
                                Data updateData =new Data();
                                updateData.setMemo(string);
                                //引数はid,data,場所
                                dbAssist.updateData(intentId,updateData,ReUserDetailActivity.this);

                                //memo画面の更新
                                Data data = dbAssist.idSelect(intentId, ReUserDetailActivity.this);
                                memo = data.getMemo(); //SQLiteからもってくる
                                memoTV.setText(memo);



                                //Log.d("sssss",string);
                            }
                        })
                .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    /*キャンセルされたときの処理*/
                    }
                });

        AlertDialog myDialog = builder.create();
        myDialog.setCanceledOnTouchOutside(false);
        //ダイアログ画面外をタッチされても消えないようにする。
        myDialog.show();
        //ダイアログ表示

    }

    //ここでmenuを作る
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.re_user_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //MenuItem(戻るボタン)の選択
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                Intent intent_setting = new Intent(ReUserDetailActivity.this, SettingActivity.class);
                startActivity(intent_setting);
                return true;

            case R.id.action_edits:
                Intent intent = new Intent(ReUserDetailActivity.this, UserRegisterActivity.class);
                intent.putExtra("IdNum",intentId);
                startActivity(intent);
                return true;
            case android.R.id.home:
                finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

}
