package org.t_robop.y_ogawara.tancolle;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    CheckBox checkNotifMonth;
    CheckBox checkNotifWeek;
    CheckBox checkNotifYesterday;
    CheckBox checkToday;
    CheckBox checkCus[]=new CheckBox[3];
    TextView textCus[]=new TextView[3];
    DatePickerDialog PickerDialog;
    //チェックフラグ
    int flagNotifMonth;
    int flagNotifWeek;
    int flagNotifYesterday;
    int flagNotifToday;
    int flagNotifCus[]=new int[3];
    /////
    //カスタム用の通知月日の保存
    int userNotifCus[]=new int[3];

    DatePickerDialog.OnDateSetListener DateSetListener;


    //FloatingActionButtonの宣言
    FloatingActionButton floatingBoth1;


    //前のpage番号
    int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        //Toolbar関連
        //setTitle("新しいタイトル");
        //setSupportActionBar(toolbar);
        //Drawable d = toolbar.getBackground();
        //d.setAlpha(0);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //TextView a =  (TextView) toolbar.getChildAt(0);
        //a.setText("ワロタ");
        //setSupportActionBar(toolbar);

        //actionBar.setTitle("title");

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
        PM=new PreferenceMethod();
        //配列を読み込み (保存のkey,場所)
        String[] categoryItem = PM.getArray("StringItem",this);
        int count = 0;
        if(categoryItem!=null){
            for(int i = 0; i<categoryItem.length; i++) { //0からカテゴリリストの最大値まで繰り返す
                if (!(categoryItem[i].equals(category))) { //もしもカテゴリリストのi個目と今読み込んだカテゴリの名前が一致しなかったら
                    count++; //カウントを足していく
                }
                if(count==categoryItem.length) { //もし一致しなかった数＝カテゴリの最大値だったら（一個も一致しない 存在しなかったら）
                    Data updateData = new Data(); //そのカテゴリは存在しないのでSQLに未選択で書き換える
                    updateData.setCategory("<未選択>");
                    dbAssist.updateData(intentId, updateData, this);
                }
            }


        }else{
            if(!category.equals("<未選択>")){
                Data updateData = new Data(); //そのカテゴリは存在しないのでSQLに未選択で書き換える
                updateData.setCategory("<未選択>");
                dbAssist.updateData(intentId, updateData, this);
            }
        }

        presentClick();

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
        //TODO チェックボックスのXMLの結びつけ
        checkNotifMonth = (CheckBox) findViewById(R.id.MonthCheck);
        checkNotifWeek = (CheckBox) findViewById(R.id.WeekCheck);
        checkNotifYesterday = (CheckBox) findViewById(R.id.YesterdayCheck);
        checkToday = (CheckBox) findViewById(R.id.TodayCheck);
        //カスタム通知で通知するかどうかのチェックボックスの関連付け
        checkCus[0]=(CheckBox)findViewById(R.id.CutomCheck1);
        checkCus[1]=(CheckBox)findViewById(R.id.CutomCheck2);
        checkCus[2]=(CheckBox)findViewById(R.id.CutomCheck3);
        //カスタム通知の日時表示用のテキストの関連付け
        textCus[0]=(TextView)findViewById(R.id.cusText1);
        textCus[1]=(TextView)findViewById(R.id.cusText2);
        textCus[2]=(TextView)findViewById(R.id.cusText3);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1; //0から11までしかないから１個足す
        day = calendar.get(Calendar.DAY_OF_MONTH);

        photoImageView = (ImageView)findViewById(R.id.imageView);
        image = (ImageView) this.findViewById(R.id.present);



        Data data = dbAssist.idSelect(intentId, this);
        String name = data.getName(); //SQliteからもってくる
        String kana = data.getKana();
        String smallImage = data.getImage(); //TODO
        String category = data.getCategory();
        TwitterID = data.getTwitterID();
        memo = data.getMemo();
        imagecount = data.isPresentFlag();
        yukarin = data.isYukarin();
        //TODO 通知のカスタム
        flagNotifMonth =data.getNotif_month();
        flagNotifWeek =data.getNotif_week();
        flagNotifYesterday = data.isNotif_yest();
        flagNotifToday = data.isNotif_today();

        if (imagecount == Integer.MIN_VALUE) {
            imagecount = 0;
        }

        //データを読みだして、その値でセットする画像を変える

        if(imagecount==0){
            image.setImageResource(R.drawable.ao);
        }else{
            image.setImageResource(R.drawable.ribon);
        }


//画像読み込み
        if(smallImage.equals("null.jpg")){
            photoImageView.setImageResource(R.drawable.noimagedetail);
        }else {
            InputStream in;
            try {
                in = openFileInput(smallImage);//画像の名前からファイル開いて読み込み
                bitmap = BitmapFactory.decodeStream(in);//読み込んだ画像をBitMap化
            } catch (IOException e) {
                e.printStackTrace();
            }
            photoImageView.setImageBitmap(bitmap);
        }

        //int birth = data.getBirthday();
        int birthyear = data.getYear();
        int birthmonth = data.getMonth();
        int birthday = data.getDay();

        //誕生日を７月１４日を→７１４みたいな形に
        //現在の日付を６月１５日→６１５みたいな形に
        if (birthmonth * 100 + birthday > month * 100 + day) {  //もし誕生日の方の数値が大きかったら（まだ今年の誕生日がきてなかったら）
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
            if (birthmonth * 100 + birthday < month * 100 + day) {  //誕生日より今日の日にちが大きかったら（もう誕生日がきていたら
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


        setTitle("");
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

        /*for(int i=0;i<3;i++){
            CheckJudge(checkCus[i],i);
        }*/
        CheckJudge(checkNotifMonth,4);
        CheckJudge(checkNotifWeek,5);
        CheckJudge(checkNotifYesterday,6);
        CheckJudge(checkToday,7);
        CheckBoxChange(checkNotifMonth, flagNotifMonth);
        CheckBoxChange(checkNotifWeek, flagNotifWeek);
        CheckBoxChange(checkNotifYesterday, flagNotifYesterday);
        CheckBoxChange(checkToday, flagNotifToday);
        /*for(int i=0;i<3;i++){
            CheckBoxChange(checkCus[i], flagNotifCus[i]);
        }*/



        //Log.d("bbbbbbb",String.valueOf(flagNotifMonth));


        //TODO カスタム通知の通知日をセット
        /*data.setNotif_cus1(userNotifCus[0]);
        data.setNotif_cus2(userNotifCus[1]);
        Data.setNotif_cus3(userNotifCus[2]);*/

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

    public void CheckJudge(final CheckBox check, final int flag) {
        // チェックボックスがクリックされた時に呼び出されるコールバックリスナーを登録します
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            // チェックボックスがクリックされた時
            public void onClick(View v) {
                Data updateData =new Data();
                // チェックボックスのチェック状態を取得します
                boolean checked = check.isChecked();
                //チェックがtrue(押された事でtrueになった)時
                if(checked==true) {
                    //指定されたフラグによる処理群
                    switch (flag){
                        //0~1のどれか(カスタム通知用処理)だった時
                        case 0:
                        case 1:
                        case 2:
                            /*キャンセル対策のため一旦チェックを外す
                            checkCus[flag].setChecked(false);
                            //ok押された時のリスナー登録
                            DatePickerSet(flag);
                            //現在の日時を初期値としたDatePickerDialogの設定
                            PickerDialog = new DatePickerDialog(UserRegisterActivity.this, DateSetListener,
                                    getToday("year"),
                                    getToday("month")-1,
                                    getToday("day"));
                            /////
                            //DatePickerDialogの表示
                            PickerDialog.show();
                            //switch抜ける*/
                            break;
                        /////
                        case 3:
                            break;

                        //毎月通知チェック処理
                        case 4:
                            flagNotifMonth =1; //毎月通知チェック処理
                            break;
                        case 5:
                            flagNotifWeek =1; //毎週通知チェック処理
                            break;
                        case 6:
                            flagNotifYesterday =1; //昨日通知チェック処理
                            break;
                        case 7:
                            flagNotifToday =1; //当日通知チェック処理
                            break;
                    }
                }else{ //チェックがfalse(押された事でfalseになった)時
                    //指定されたフラグによる処理群
                    switch (flag){
                        //0~1のどれか(カスタム通知用処理)だった時
                        case 0:
                        case 1:
                        case 2:
                            /*
                            //フラグをしまう
                            flagNotifCus[flag] =0;
                            //テキストの色を灰色に
                            textCus[flag].setTextColor(Color.GRAY);
                            //カスタム通知日を無登録扱いの0に
                            userNotifCus[flag]=0;
                            //テキストを初期状態に戻す
                            textCus[flag].setText("通知日を追加");
                            //switch抜けます*/
                            break;
                        case 3:
                            break;
                        case 4:
                            flagNotifMonth =0; //フラグをしまう
                            break;
                        case 5:
                            flagNotifWeek =0; //フラグをしまう
                            break;
                        case 6:
                            flagNotifYesterday =0; //フラグをしまう
                            break;
                        case 7:
                            flagNotifToday =0; //フラグをしまう
                            break;
                    }
                }
                updateData.setNotif_month(flagNotifMonth); //毎月通知チェックのフラグをセット
                updateData.setNotif_week(flagNotifWeek); //毎週通知チェックのフラグをセット
                updateData.setNotif_yest(flagNotifYesterday); //前日通知チェックのフラグをセット
                updateData.setNotif_today(flagNotifToday); //当日通知チェックのフラグをセット*/
                dbAssist.updateData(intentId,updateData,ReUserDetailActivity.this);
            }
        });
    }

    //checkboxの中身を判断してtruefalse変更///
    public void CheckBoxChange(CheckBox Cb,int check) {
        if(check==0) {
            Cb.setChecked(false);
        }
        else {
            Cb.setChecked(true);
        }
    }

    public void presentClick() { //プレゼントボタンをおした時



        //FloatingActionButtonの宣言
        floatingBoth1 = (FloatingActionButton) findViewById(R.id.floating_both1);
        floatingBoth1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Data updateData = new Data();
                if (imagecount == 0) {
                    imagecount = 1;
                    updateData.setPresentFlag(imagecount);
                    dbAssist.updateData(intentId, updateData, getApplicationContext());
                    floatingBoth1.setImageResource(R.drawable.ic_delete_white_48dp);
                } else {
                    imagecount = 0;
                    updateData.setPresentFlag(imagecount);
                    dbAssist.updateData(intentId, updateData, getApplicationContext());
                    floatingBoth1.setImageResource(R.drawable.ao);
                }
            }
        });
    }

    public void tweetClick(){ //ツイートボタンをおした時
        if(TwitterID.equals("")){
            Toast toast = Toast.makeText(ReUserDetailActivity.this, "TwitterIDが登録されていません", Toast.LENGTH_LONG);
            toast.show();
        }else{
            //TODO IntentがAndroid5.0以上だとこれだけでいけるっぽいので確認してください
            TwChromeIntent();
//            Intent intent = new Intent();
//            intent.setAction( Intent.ACTION_VIEW );
//            intent.setData( Uri.parse("twitter://user?screen_name="+TwitterID) ); // @skc1210 (アカウントを指定)
            try {
                //startActivity(intent);
            } // Twitterが端末にインストールされていない場合はTwitterインストール画面へ
            catch( ActivityNotFoundException e ) {
                try {
                    TwChromeIntent();
                    //startActivity( new Intent( Intent.ACTION_VIEW, Uri.parse("market://details?id=com.twitter.android") ) );
                } catch ( android.content.ActivityNotFoundException anfe ) {
                    startActivity( new Intent( Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.twitter.android") ) );
                }
            }

        }

    }

    //twitterをChromeで開くメソッド
    private void TwChromeIntent() {
        String base = "https://twitter.com/"+TwitterID; //URLの文字列
        Uri uri = Uri.parse(base);//URIにParse
        Intent i = new Intent(Intent.ACTION_VIEW,uri);//インテントの作成
        startActivity(i);//アクティビティの起動
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
            case R.id.action_tweet:
                tweetClick();
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
