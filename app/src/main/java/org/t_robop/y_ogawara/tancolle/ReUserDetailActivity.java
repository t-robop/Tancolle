package org.t_robop.y_ogawara.tancolle;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

    TextView textNotifMonth;
    TextView textNotifWeek;
    TextView textNotifYesterday;
    TextView textNotifToday;
    TextView textCus[]=new TextView[3];
    DatePickerDialog PickerDialog;
    //チェックフラグ
    int flagNotifMonth;
    int flagNotifWeek;
    int flagNotifYesterday;
    int flagNotifToday;
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

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //FloatingActionButtonの宣言
        floatingBoth1 = (FloatingActionButton) findViewById(R.id.floating_both1);

        presentClick();

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

        floatingBoth1 = (FloatingActionButton) findViewById(R.id.floating_both1);

        //データを読みだして、その値でセットする画像を変える
        if(imagecount==0){
            floatingBoth1.setImageResource(R.drawable.gift_48);
        }else{
            floatingBoth1.setImageResource(R.drawable.gift_checked);
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
        birthTV = (TextView) findViewById(R.id.Birthay);
        ageTV = (TextView) findViewById(R.id.age);
        remaTV = (TextView) findViewById(R.id.nokori);
        memoTV = (TextView) findViewById(R.id.chou);
        cateTV = (TextView) findViewById(R.id.category);
        //TODO チェックボックスのXMLの結びつけ
        textNotifMonth = (TextView) findViewById(R.id.m_notif);
        textNotifWeek = (TextView) findViewById(R.id.w_notif);
        textNotifYesterday = (TextView) findViewById(R.id.y_notif);
        textNotifToday = (TextView) findViewById(R.id.t_notif);
        //カスタム通知の日時表示用のテキストの関連付け
        textCus[0]=(TextView)findViewById(R.id.cusText1);
        textCus[1]=(TextView)findViewById(R.id.cusText2);
        textCus[2]=(TextView)findViewById(R.id.cusText3);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1; //0から11までしかないから１個足す
        day = calendar.get(Calendar.DAY_OF_MONTH);

        photoImageView = (ImageView)findViewById(R.id.imageView);

//        //textの初期設定
//        textNotifMonth.setTextColor(Color.GRAY);
//        textNotifWeek.setTextColor(Color.GRAY);
//        textNotifYesterday.setTextColor(Color.GRAY);
//        textNotifToday.setTextColor(Color.GRAY);
//        for(int i=0;i<3;i++){
//            textCus[i].setTextColor(Color.GRAY);
//            textCus[i].setText("指定通知可能");
//        }

        textNotifMonth.setVisibility(View.INVISIBLE);
        textNotifWeek.setVisibility(View.INVISIBLE);
        textNotifYesterday.setVisibility(View.INVISIBLE);
        textNotifToday.setVisibility(View.INVISIBLE);
        for(int i=0;i<3;i++){
            textCus[i].setVisibility(View.INVISIBLE);
        }

        Data data = dbAssist.idSelect(intentId, this);
        String name = data.getName(); //SQliteからもってくる
        String smallImage = data.getImage(); //TODO
        String category = data.getCategory();
        TwitterID = data.getTwitterID();
        memo = data.getMemo();
        imagecount = data.isPresentFlag();
        yukarin = data.isYukarin();
        //TODO 通知のカスタム

//        //フラグついてたら黒色に
//        flagNotifMonth =data.getNotif_month();
//        if(flagNotifMonth==1){
//            textNotifMonth.setTextColor(Color.BLACK);
//        }
//        flagNotifWeek =data.getNotif_week();
//        if(flagNotifWeek==1){
//            textNotifWeek.setTextColor(Color.BLACK);
//        }
//        flagNotifYesterday = data.isNotif_yest();
//        if(flagNotifYesterday==1){
//            textNotifYesterday.setTextColor(Color.BLACK);
//        }
//        flagNotifToday = data.isNotif_today();
//        if(flagNotifToday==1){
//            textNotifMonth.setTextColor(Color.BLACK);
//        }
//        userNotifCus[0]=data.getNotif_cus1();
//        userNotifCus[1]=data.getNotif_cus2();
//        userNotifCus[2]=data.getNotif_cus3();
//        for(int i=0;i<3;i++){
//            if(userNotifCus[i]!=0){
//                textCus[i].setText(
//                                        UserRegisterActivity.OutCale(userNotifCus[i],"year") + "/" +
//                                        UserRegisterActivity.OutCale(userNotifCus[i],"month")+"/" +
//                                        UserRegisterActivity.OutCale(userNotifCus[i],"day"));
//                textCus[i].setTextColor(Color.BLACK);
//            }
//        }

        //フラグついてたら黒色に
        flagNotifMonth =data.getNotif_month();
        if(flagNotifMonth==1){
            textNotifMonth.setTextColor(Color.BLACK);
            textNotifMonth.setVisibility(View.VISIBLE);
        }
        flagNotifWeek =data.getNotif_week();
        if(flagNotifWeek==1){
            textNotifWeek.setTextColor(Color.BLACK);
            textNotifWeek.setVisibility(View.VISIBLE);
        }
        flagNotifYesterday = data.isNotif_yest();
        if(flagNotifYesterday==1){
            textNotifYesterday.setTextColor(Color.BLACK);
            textNotifYesterday.setVisibility(View.VISIBLE);
        }
        flagNotifToday = data.isNotif_today();
        if(flagNotifToday==1){
            textNotifMonth.setTextColor(Color.BLACK);
            textNotifMonth.setVisibility(View.VISIBLE);
        }
        userNotifCus[0]=data.getNotif_cus1();
        userNotifCus[1]=data.getNotif_cus2();
        userNotifCus[2]=data.getNotif_cus3();
        for(int i=0;i<3;i++){
            if(userNotifCus[i]!=0){
                textCus[i].setText(
                        UserRegisterActivity.OutCale(userNotifCus[i],"year") + "/" +
                                UserRegisterActivity.OutCale(userNotifCus[i],"month")+"/" +
                                UserRegisterActivity.OutCale(userNotifCus[i],"day"));
                textCus[i].setTextColor(Color.BLACK);
                textCus[i].setVisibility(View.VISIBLE);
            }
        }


        if (imagecount == Integer.MIN_VALUE) {
            imagecount = 0;
        }

        floatingBoth1 = (FloatingActionButton) findViewById(R.id.floating_both1);

        //データを読みだして、その値でセットする画像を変える
        if(imagecount==0){
            floatingBoth1.setImageResource(R.drawable.gift_48);
        }else{
            floatingBoth1.setImageResource(R.drawable.gift_checked);
        }

        //タイトル指定
        setTitle(name);

//画像読み込み
        if(smallImage.equals("null.jpg")){
            //photoImageView.setImageResource(R.drawable.noimagedetail);
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

        if(category.equals("<未選択>")){
            cateTV.setText("未選択");
        }else{
            cateTV.setText(category);
        }
        birthTV.setText(String.valueOf(birthmonth) + "/" + String.valueOf(birthday));
        if(remDay!=0) {
            remaTV.setText("残り" + String.valueOf(remDay) + "日");
        }
        else{
            remaTV.setText("今日");
        }
        memoTV.setText(memo);
        if(yukarin==0&&age>=0){
            ageTV.setText(String.valueOf(age) + "才");
        }
        else{
            ageTV.setText("不明");
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
                .setTitle("メモ")
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

    //checkboxの中身を判断してtruefalse変更///
    public void CheckBoxChange(CheckBox Cb,int check) {
        if(check==0) {
            Cb.setChecked(false);
        }
        else {
            Cb.setChecked(true);
        }
    }
    public void tes(View v){
        Log.d("Android","楽しい");

        Data updateData = new Data();
        if (imagecount == 0) {
            imagecount = 1;
            updateData.setPresentFlag(imagecount);
            dbAssist.updateData(intentId, updateData, this);
            floatingBoth1.setImageResource(R.drawable.gift_checked);
        } else {
            imagecount = 0;
            updateData.setPresentFlag(imagecount);
            dbAssist.updateData(intentId, updateData, this);
            floatingBoth1.setImageResource(R.drawable.gift_48);
        }

    }

    public void presentClick() {

        if(floatingBoth1!=null) {
            floatingBoth1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Data updateData = new Data();
                    if (imagecount == 0) {
                        imagecount = 1;
                        updateData.setPresentFlag(imagecount);
                        dbAssist.updateData(intentId, updateData, getApplicationContext());
                        floatingBoth1.setImageResource(R.drawable.ic_delete_white_48dp);

                        //トースト展開
                        Toast toast = Toast.makeText(ReUserDetailActivity.this, "ごみ", Toast.LENGTH_LONG);
                        toast.show();
                    } else {
                        imagecount = 0;
                        updateData.setPresentFlag(imagecount);
                        dbAssist.updateData(intentId, updateData, getApplicationContext());
                        floatingBoth1.setImageResource(R.drawable.ao);

                        //トースト展開
                        Toast toast = Toast.makeText(ReUserDetailActivity.this, "あお", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            });
        }
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
