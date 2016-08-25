package org.t_robop.y_ogawara.tancolle;

import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class UserRegisterActivity extends AppCompatActivity  {
    private static final int REQUEST_GALLERY = 0;//ギャラリー選択で必要な初期化
    EditText editName;
    EditText editPho;
    EditText editTwitter;
    ImageView imageUser;
    TextView textBirthday;
    TextView textYearsOld;
    CheckBox checkTamura;
    CheckBox checkNotifMonth;
    CheckBox checkNotifWeek;
    CheckBox checkNotifYesterday;
    CheckBox checkToday;
    CheckBox checkCus[]=new CheckBox[3];
    TextView textCus[]=new TextView[3];
    EditText editMemo;
    DatePickerDialog PickerDialog;
    Calendar calendar;
    TextView textOldUnknown;
    TextView textMnotif;
    TextView textWnotif;
    TextView textYnotif;
    TextView textTnotif;
    //キーボード制御
    InputMethodManager inputMethodManager;

    //EditText用変数群
    View viewDialog;
    //DiaLog用xmlから来るやつ
    LayoutInflater inflaterDialog;
    //DiaLog用xmlのEditText
    EditText editDialog;

    //カテゴリ用arraylist&arrayadapter
    ArrayAdapter<String> categoryAdapter;
    //カテゴリのList
    ArrayList<String> categorylist;
    //選択されたカテゴリ保管（後でsqlに飛ばすよ）
    String userCategory;
    //MostImportantDate（sqlからデータ読み込む時に使う大本用のデータ型変数）
    Data idDate;
    //img初期設定用
    String imgStartName;
    //カテゴリ追加用DiaLog
    private AlertDialog addCategoryDialog;
    //表示するBitmapデータ
        Bitmap img;
        Bitmap small_img;
    /////
    //選択されたBitmapの名前（後でsqlに飛ばすよ）
        String userImage;
        String userImageSmall;
    /////
    //sppinerTest
        //カテゴリスピナー
        private Spinner spinnerCategory;
        //一時的な年月日の保存
        int temporary_year;
        int temporary_month;
        int temporary_day;
    /////
    //誕生年月日
        int userBirthYear;
        int userBirthMonth;
        int userBirthDay;
    /////
    //チェックフラグ
        int flagTamura;
        int flagNotifMonth;
        int flagNotifWeek;
        int flagNotifYesterday;
        int flagNotifToday;
        int flagNotifCus[]=new int[3];
    /////
    //カスタム用の通知月日の保存
    int userNotifCus[]=new int[3];
    //idチェック
    int id;
    //TwitterId用変数
    String userTwitterId;
    //画像の縦横の大きさ
        int pctWidth;
        int pctHeight;
    /////
    //画像回転用変数群
        int orientation;
        ExifInterface exifInterface;
        Matrix mat;
    /////
    int monthSetting;//Todo 初期"月"設定テスト(修復時：消せ)
    //キーボードの有無確認
    boolean keyBoad=false;
    //ダイアログでok押された時のリスナー
    DatePickerDialog.OnDateSetListener DateSetListener;
    //preference用クラス
    PreferenceMethod PM;

    //編集したかどうかの確認(true：編集済)
    boolean registJudge;

    //指定されてるspinnerのposition保存
    int positionSpinner=0;

    //alarmを鳴らすようのIdを格納
    int alarmId;


    /////////////////////////Override/////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register_re);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //関連付け
        Association();

        filterEnglish(editTwitter);

        //編集済かどうかの初期設定
        registJudge=false;

        //preferenceクラス宣言
        PM=new PreferenceMethod();

        //初期のカスタムテキストの色の設定
            textCus[0].setTextColor(Color.GRAY);
            textCus[1].setTextColor(Color.GRAY);
            textCus[2].setTextColor(Color.GRAY);
        /////
        //画面切り替わり時のid取得
            Intent intent = getIntent();
            id = intent.getIntExtra("IdNum", 0);//何も無かったら（新規作成でidデータが送られてない場合）0を入れる
        /////
        monthSetting=intent.getIntExtra("month",0);//Todo 初期"月"設定テスト(選択された月データを取得します。修復時：消せ)
        //カテゴリー用のリストを新規作成
        categorylist = new ArrayList<>();
        //Spinner設定
            sppinerCategorySet();//カテゴリスピナー設定
        /////
        //EditTextの内容設定
            EditTextSet(editPho);
            EditTextSet(editName);
            EditTextSet(editTwitter);
        /////
        //振り仮名自動入力のためのリスナー（謎）
        //editName.addTextChangedListener(this);
        //ImageViewの初期設定
        imageUser.setScaleType(ImageView.ScaleType.CENTER_CROP);//CENTER_CROPでViewに合わせて拡大し、画像のはみ出した部分は切り取って、中心にフォーカスする
        //全CheckBoxのリスナーを登録
            for(int i=0;i<3;i++){
                CheckJudge(checkCus[i],i);
            }
            CheckJudge(checkTamura,3);
            CheckJudge(checkNotifMonth,4);
            CheckJudge(checkNotifWeek,5);
            CheckJudge(checkNotifYesterday,6);
            CheckJudge(checkToday,7);
        /////
        // プリファレンスからカテゴリー一覧を取得
        String[] categoryItem = PM.getArray("StringItem",this);
        //カテゴリー追加処理
            //まず[カテゴリ無し]を追加します
            categoryAdapter.add("[カテゴリ無し]");
            //何かカテゴリが保存されてる時
                if(categoryItem!=null) {
                    //保存されてるカテゴリ数だけループさせます
                    for (int n = 0; n < categoryItem.length; n++) {
                        //読み込んだカテゴリを追加
                            categoryAdapter.add(categoryItem[n]);
                            categorylist.add(categoryItem[n]);
                        /////
                    }
                    /////
                }
            /////
        /////
        //データがある場合（編集として呼ばれた場合）は読み込み
            if (id != 0) {
                //toolbarの表示変更
                toolbar.setTitle("編集");
                //sqlからid毎で取得
                idDate = dbAssist.idSelect(id, this);
                //取得したデータの読み込み
                    editName.setText(idDate.getName());
                    editPho.setText(idDate.getKana());
                    editTwitter.setText(idDate.getTwitterID());
                    editMemo.setText(idDate.getMemo());
                    imgStartName = idDate.getImage();//ここで取得した名前から新しく画像生成する
                    flagTamura = idDate.isYukarin();
                    flagNotifMonth =idDate.getNotif_month();
                    flagNotifWeek =idDate.getNotif_week();
                    flagNotifYesterday = idDate.isNotif_yest();
                    flagNotifToday = idDate.isNotif_today();
                    userNotifCus[0]=idDate.getNotif_cus1();
                    userNotifCus[1]=idDate.getNotif_cus2();
                    userNotifCus[2]=idDate.getNotif_cus3();
                    userCategory=idDate.getCategory();
                    //spinnerに指定した文字列があればセット
                    setSelection(spinnerCategory,userCategory);
                    //カスタム通知（CheckBox&TextView）の配列にデータを反映するため三回回す
                        for(int i=0;i<3;i++){
                            //最初は0(チェック入ってない物を避けるため)
                            flagNotifCus[i]=0;
                            //カスタムに何か入ってる時
                                if(userNotifCus[i]!=0){
                                    //フラグ立てる
                                    flagNotifCus[i]=1;
                                    //それの色を黒に
                                    textCus[i].setTextColor(Color.BLACK);
                                    //何か入ってる時は入ってる日付も出力する
                                    textCus[i].setText(
                                                    String.valueOf(OutCale(userNotifCus[i],"year"))+"/"+
                                                    String.valueOf(OutCale(userNotifCus[i],"month"))+"/"+
                                                    String.valueOf(OutCale(userNotifCus[i],"day")));
                                }
                            /////
                        }
                    /////
                    //画像の名前を読み込んでActivityで使える形にする
                        //画像の名前を読み込みます
                            userImage =imgStartName;
                            userImageSmall =idDate.getSmallImage();
                        /////
                        //名前の後ろの「.jpg」のみ取り除きます
                            userImage =CutString(userImage,4);
                            userImageSmall =CutString(userImageSmall,4);
                        /////
                    /////
                    //誕生年月日の初期値を設定年月日へ
                        //sqlから誕生年を取得
                        userBirthYear =idDate.getYear();
                        //sqlから誕生月を取得
                        userBirthMonth =idDate.getMonth();
                        //月取得時のズレを亡くす
                        userBirthMonth--;
                        //sqlから誕生日を取得
                        userBirthDay =idDate.getDay();
                    /////
                    //ダイアログ用変数に代入
                        temporary_year= userBirthYear;
                        temporary_month= userBirthMonth;
                        temporary_day= userBirthDay;
                    /////
                    //読み込んだ段階でデータから全フラグをチェックボックスに適用
                        CheckBoxChange(checkTamura,textOldUnknown, flagTamura);
                        CheckBoxChange(checkNotifMonth,textMnotif, flagNotifMonth);
                        CheckBoxChange(checkNotifWeek,textWnotif, flagNotifWeek);
                        CheckBoxChange(checkNotifYesterday,textYnotif, flagNotifYesterday);
                        CheckBoxChange(checkToday,textTnotif, flagNotifToday);
                        for(int i=0;i<3;i++){
                            CheckBoxChange(checkCus[i],null, flagNotifCus[i]);
                        }
                    /////
                /////
            }
        /////
        //新規作成として呼ばれた場合
            else{
                //toolbarの表示変更
                toolbar.setTitle("登録");
                //現在の日時を誕生日欄にセット
                    temporary_year=getToday("year");
                    temporary_month=getToday("month");
                    temporary_day=getToday("day");
                    temporary_month=monthSetting-1;//Todo 初期"月"設定テスト(修復時：消せ)
                /////
                //それぞれ代入
                    userBirthYear =temporary_year;
                    userBirthMonth =temporary_month;
                    userBirthDay =temporary_day;
                /////
                //新規作成の場合でも画像の名前を設定しておく
                imgStartName = "null.jpg";
                //編集済フラグ
                registJudge=true;
                textOldUnknown.setTextColor(Color.GRAY);
                textMnotif.setTextColor(Color.GRAY);
                textWnotif.setTextColor(Color.GRAY);
                textYnotif.setTextColor(Color.GRAY);
                textTnotif.setTextColor(Color.GRAY);
            }
        /////
        //誕生日と年齢表示
            //月のズレを解消
            userBirthMonth++;
            //描画処理
            drawBirthAndOld();
        /////
        //画像読み込み
            InputStream in;
            try {
                //読み込んだデータが「null.jpg」(つまり新規作成)の時
                    if(imgStartName.equals("null.jpg")){
                        //端末内からデータを取得するためのリソースの宣言
                        Resources r = getResources();
                        //端末内の指定データをbitmap化
                        img = BitmapFactory.decodeResource(r, R.drawable.noimageregister);
                    }
                /////
                //読み込んだデータが「null.jpg」(つまり新規作成)でない時
                    else {
                        //画像の名前からファイル開いて読み込み
                        in = openFileInput(imgStartName);
                        //読み込んだ画像をBitMap化
                        img = BitmapFactory.decodeStream(in);
                        in.close();
                    }
                /////
            } catch (IOException e) {
                e.printStackTrace();
            }
        /////
        //BitMapから画像をImageViewにセット
        imageUser.setImageBitmap(img);

        //toolbarをセット
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    //画像をドキュメントから選択からのImageViewセット
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //TODO Auto-generated method stub

        if (resultCode != RESULT_OK) return;

        if (requestCode == 0) {

                String[] columns = {MediaStore.MediaColumns.DATA};
            Cursor cursor = getContentResolver().query(data.getData(), columns, null, null, null);

            if (cursor.moveToFirst()) {
                try {
                    exifInterface = new ExifInterface(cursor.getString(0));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == 1) {

            Uri uri = data.getData();
            //ギャラリーの時
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {

                String id = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    id = DocumentsContract.getDocumentId(data.getData());
                }
                String selection = "_id=?";
                String[] selectionArgs = new String[]{id.split(":")[1]};

                Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.MediaColumns.DATA}, selection, selectionArgs, null);

                if (cursor.moveToFirst()) {
                    // fileから写真を読み込む
                    try {
                        exifInterface = new ExifInterface(cursor.getString(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                cursor.close();
            }
            //ダウンロードからの時
            else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                String id = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    id = DocumentsContract.getDocumentId(data.getData());
                }
                Uri docUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                Cursor cursor = getContentResolver().query(docUri, new String[]{MediaStore.MediaColumns.DATA}, null, null, null);

                if (cursor.moveToFirst()) {
                    // fileから写真を読み込む
                    try {
                        exifInterface = new ExifInterface(cursor.getString(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                cursor.close();
            }

            }

        try {
            //画像取得
                //選択した画像を取得
                InputStream in = getContentResolver().openInputStream(data.getData());
                //取得した画像をBitMap化
                Bitmap pct = BitmapFactory.decodeStream(in);
                //InputStreamを閉じる
                if (in != null) {
                    in.close();
                }
            /////

            //作られたBitMapから横幅を取得
            pctWidth = pct.getWidth();
            //作られたBitMapから高さを取得
            pctHeight = pct.getHeight();

            //Todo 画像回転の何か（謎）
            orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            //画像回転メソッド
            ViewRotate();
            //画像設定
            img = Bitmap.createBitmap(pct,0,0, pctWidth, pctHeight,mat, true);
            //小さい画像の作成
            small_img= Bitmap.createScaledBitmap(img,pctWidth/2,pctHeight/2,false);
            //BitMapを表示
            imageUser.setImageBitmap(img);
        }
        catch (Exception ignored) {}
        //画像保存時の名前用の現在日時取得
            calendar = Calendar.getInstance();//現在日時を取得
            int imgye, imgmo, imgda, imgho, imgmi, imgse;
            imgye = calendar.get(Calendar.YEAR); // 年
            imgmo = calendar.get(Calendar.MONTH); // 月
            imgmo++;//monthに+１して整合性を保つ
            imgda = calendar.get(Calendar.DAY_OF_MONTH); // 日
            imgho = calendar.get(Calendar.HOUR_OF_DAY);//時
            imgmi = calendar.get(Calendar.MINUTE);//分
            imgse = calendar.get(Calendar.SECOND);//秒
        /////
        //保存する画像の名前の決定
            //通常の画像の名前
            userImage
                    = String.valueOf(imgye)
                    + String.valueOf(imgmo)
                    + String.valueOf(imgda)
                    + String.valueOf(imgho)
                    + String.valueOf(imgmi)
                    + String.valueOf(imgse);
            //小さい画像の名前
            userImageSmall ="small_"+ userImage;
        /////

        //ローカルファイルへ保存
        FileOutputStream out;
        try {
            //作成するデータの名前と設定
            out = this.openFileOutput(userImage + ".jpg", Context.MODE_PRIVATE);//.jpgつけてちょ
            //画像の保存（フォーマット設定,品質,設定データ）
            img.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            //作成するデータの名前と設定
            out = this.openFileOutput(userImageSmall + ".jpg", Context.MODE_PRIVATE);//.jpgつけてちょ
            //画像の保存（フォーマット設定,品質,設定データ）
            small_img.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    //TextWatcher/////未完成/////
//    @Override
//    public void beforeTextChanged(final CharSequence s, int start, int count, int after) {
//        //操作前のEtidTextの状態を取得する
//        editPho.setText(s.toString());
//
//        //EditTextにリスナーをセット
//        editName.setOnKeyListener(new View.OnKeyListener() {
//
//            //コールバックとしてonKey()メソッドを定義
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                //イベントを取得するタイミングには、ボタンが押されてなおかつエンターキーだったときを指定
//                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
//
//                    //キーボードを閉じる
//                    inputMethodManager.hideSoftInputFromWindow(editName.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
//
//                    //フォーカスを外す
//                    editName.setFocusable(false);
//                    editName.setFocusableInTouchMode(false);
//                    editName.requestFocus();
//
//                    keyBoad=false;
//
//                    // エディットテキストのテキストを全選択します
//                    editName.selectAll();
//
//                    return true;
//                }
//                return false;
//            }
//        });
//    }
//
//    @Override
//    public void onTextChanged(CharSequence s, int start, int before, int count) {
//        //操作中のEtidTextの状態を取得する
//    }
//
//    @Override
//    public void afterTextChanged(Editable s) {
//        //操作後のEtidTextの状態を取得する
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // アクションバー内に使用するメニューアイテムを注入します。
        if(id!=0) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.user_register_menu_cus, menu);
            return super.onCreateOptionsMenu(menu);
        }
        else{
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.user_register_menu_new, menu);
            return super.onCreateOptionsMenu(menu);
        }
    }

    //アクションバー処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // アクションバーアイテム上の押下を処理します。
        switch (item.getItemId()) {

            case R.id.action_save:
                //名前が入力されてない時
                if(editName.getText().toString().equals("")) {
                    //警告ダイアログ表示
                    cautionDialog();
                    break;
                }
                else{
                    //保存
                    AllRegist();
                    return true;
                }

            case R.id.action_del:
                //削除
                Delete();
                return true;

            //toolbarの戻るボタン
            case android.R.id.home:
                //編集されてた時
                if(registJudge==true) {
                    //保存最終確認ダイアログ表示
                    finishRegstDialog();
                }
                //未編集時
                else {
                    //Activity終了
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);

    }


    //端末のバックボタンクリック時
    @Override
    public void onBackPressed() {
        //編集されてた時
        if(registJudge==true) {
            //保存最終確認ダイアログ表示
            finishRegstDialog();
        }
        //未編集時
        else {
            //Activity終了
            finish();
        }
    }

    ////////////////////////////////////////////////////////////


    ////////////////////クリック処理群/////////////////////////

    public void EditName(View v) {
        //フォーカスon
        EditTextClick(editName);
        //キーボード表示
        inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);
        keyBoad=true;
        //編集済フラグ
        registJudge=true;
    }

    public void EditPho(View v) {
        //フォーカスon
        EditTextClick(editPho);
        //キーボード表示
        inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);
        keyBoad=true;
        //編集済フラグ
        registJudge=true;
    }

    public void EditTwitter(View v) {
        //フォーカスon
        EditTextClick(editTwitter);
        //キーボード表示
        inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);
        keyBoad=true;
        //編集済フラグ
        registJudge=true;
    }

    //画像クリック時
    public void UserView(View v) {
        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            //どんな拡張子でも読み込めるように
            intent.setType("image/*");
            //startActivityForResult(Intent.createChooser(intent, "選べよ"), 0);
            //startActivity(intent);
            startActivityForResult(intent, 0);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            //どんな拡張子でも読み込めるように
            intent.setType("image/*");
            //startActivityForResult(Intent.createChooser(intent, "選べよ"), 1);
            //startActivity(intent);
            startActivityForResult(intent, 1);
        }
        //編集済フラグ
        registJudge=true;
    }

    //誕生日クリック時
    public void BirthDay(View v) {
        DatePickerSet(3);
        // 日付設定ダイアログの表示
        PickerDialog = new DatePickerDialog(this, DateSetListener, temporary_year, temporary_month, temporary_day);
        PickerDialog.show();
    }

    //カテゴリ追加
    public void CategoryPlus(View v) {
        SpinnerCategoryAdd();
        addCategoryDialog.show();
    }

    //カスタム用テキストクリック時
    public void cusText(View v) {
        String string = String.valueOf(v.getTag());
        int num = Integer.parseInt(string);
        if (flagNotifCus[num] == 1) {
            //ok時の処理の設定
            DatePickerSet(num);
            //ダイアログ生成
            PickerDialog = new DatePickerDialog(this, DateSetListener,
                    OutCale(userNotifCus[num], "year"),
                    OutCale(userNotifCus[num], "month")-1,//カレンダーの月がズレるので合わせます
                    OutCale(userNotifCus[num], "day"));
            PickerDialog.show();
        }
    }
    //////////////////////////////////////////////////


    ////////////////////自作関数群////////////////////

    //関連付けまとめ
    public void Association() {
        //繰り返し通知スピナーの関連付け
        //spinnerRepetition = (Spinner) findViewById(R.id.spinner2);
        //カテゴリスピナーの関連付け
        spinnerCategory = (Spinner) findViewById(R.id.spinner1);
        //年齢表示の関連付け
        textYearsOld = (TextView) findViewById(R.id.YearsOld);
        //誕生日の関連付け
        textBirthday = (TextView) findViewById(R.id.userbirthday);
        //名前入力欄の関連付け
        editName = (EditText) findViewById(R.id.EditName);
        //振り仮名入力欄の関連付け
        editPho = (EditText) findViewById(R.id.EditPho);
        //twitterid入力欄の関連付け
        editTwitter = (EditText) findViewById(R.id.twitter);
        //通知は何日前かの入力欄の関連付け
        //edit_days_ago = (EditText) findViewById(R.id.ago);
        //メモ入力欄の関連付け
        editMemo = (EditText) findViewById(R.id.Memo);
        //キーボード表示を制御（出したり消したり）するためのオブジェクトの関連付け
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //画像表示場所の関連付け
        imageUser = (ImageView) findViewById(R.id.userview);
        //田村チェックボックスの関連付け
        checkTamura = (CheckBox) findViewById(R.id.TamuraCheck);
        //一ヶ月前に通知するかどうかのチェックボックスの関連付け
        checkNotifMonth = (CheckBox) findViewById(R.id.MonthCheck);
        //一週間前に通知するかどうかのチェックボックスの関連付け
        checkNotifWeek = (CheckBox) findViewById(R.id.WeekCheck);
        //前日に通知するかどうかのチェックボックスの関連付け
        checkNotifYesterday = (CheckBox) findViewById(R.id.YesterdayCheck);
        //当日に通知するかどうかのチェックボックスの関連付け
        checkToday = (CheckBox) findViewById(R.id.TodayCheck);
        //カスタム通知で通知するかどうかのチェックボックスの関連付け
        checkCus[0]=(CheckBox)findViewById(R.id.CutomCheck1);
        checkCus[1]=(CheckBox)findViewById(R.id.CutomCheck2);
        checkCus[2]=(CheckBox)findViewById(R.id.CutomCheck3);
        //カスタム通知の日時表示用のテキストの関連付け
        textCus[0]=(TextView)findViewById(R.id.cusText1);
        textCus[1]=(TextView)findViewById(R.id.cusText2);
        textCus[2]=(TextView)findViewById(R.id.cusText3);
        //DiaLog用のxmlとの連携の関連付け
        inflaterDialog = LayoutInflater.from(UserRegisterActivity.this);
        viewDialog = inflaterDialog.inflate(R.layout.dialog_user_register, null);
        editDialog = (EditText) viewDialog.findViewById(R.id.editText1);
        //色変えText群
        textOldUnknown=(TextView)findViewById(R.id.tamura);
        textMnotif=(TextView)findViewById(R.id.m_notif);
        textWnotif=(TextView)findViewById(R.id.w_notif);
        textYnotif=(TextView)findViewById(R.id.y_notif);
        textTnotif=(TextView)findViewById(R.id.t_notif);
    }

    //EditTextに指定した文字列を加えて表示させるメソッド
    public void EditSetString(EditText edit,String string) {
        //String型の宣言
        String text;
        //数値の宣言
        int num;
        // エディットテキストのテキストを全選択します
        edit.selectAll();
        // エディットテキストのテキストを取得します
        text = edit.getText().toString();
        //editTextの初期化
        edit.getEditableText().clear();
        //textの長さが0でない(何か入ってる)場合
        if(text.length()!=0) {
            //textから数値のみ取り出す
            num = Integer.parseInt(text.replaceAll("[^0-9]",""));
            //「日前」を加えて表示
            edit.setText(String.valueOf(num)+string);
        }
    }

    //Stringの指定文字数から後ろ全てを消すメソッド（("ABCD",2)➝AB）
    public String CutString(String string,int num){
        //指定されたString型・・・①
        //String型の文字数を取得
        int size= (int) string.length();
        //結果用のString型を宣言
        String sumString;
        //文字数から指定された分の文字数を引く・・・②
        size=size-num;
        //①のString型から前から数えて②の数の分だけ取得
        sumString=string.substring(0,size);
        //結果を返す
        return sumString;
    }

    //EditTextのキーボード関連の処理のメソッド
    public void EditTextSet(final EditText edit) {
        //EditTextにリスナーをセット
        edit.setOnKeyListener(new View.OnKeyListener() {

            //コールバックとしてonKey()メソッドを定義
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //イベントを取得するタイミングには、ボタンが押されてなおかつエンターキーだったときを指定
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){

                    //キーボードを閉じる
                    inputMethodManager.hideSoftInputFromWindow(edit.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

                    //フォーカスを外す
                    edit.setFocusable(false);
                    edit.setFocusableInTouchMode(false);
                    edit.requestFocus();

                    // エディットテキストのテキストを全選択します
                    edit.selectAll();

                    keyBoad=false;//キーボードが閉じてる状態のフラグ

                    return true;
                }
                return false;
            }
        });
    }

    //指定されたEditTextがクリックされた時にフォーカスを移す処理
    public void EditTextClick(EditText edit) {
        //指定されたEditTextのフォーカスをonへ
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
        edit.requestFocus();
    }

    //カテゴリー追加用spinner設定
    public void sppinerCategorySet() {
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // アダプターを設定します
        spinnerCategory.setAdapter(categoryAdapter);

        // スピナーのアイテムが選択された時に呼び出されるコールバックリスナーを登録します
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //処理
                //セットした要素が変わった時
                if(position!=positionSpinner){
                    //編集済フラグ
                    registJudge=true;
                }
                //String型変数user_categoryに選択されたアイテムを代入
                userCategory =(String) spinnerCategory.getItemAtPosition(position);
                //現在のpositionを取得
                positionSpinner=position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    //現在の日付の取得("year""month""day"で現在の年月日のどれかを取得)
    public int getToday(String wantType) {
        ////////// 日付情報の初期設定 //////////
        int year,month,day;
        //現在の年月日の取得と代入
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR); // 年
        month = calendar.get(Calendar.MONTH); // 月
        day = calendar.get(Calendar.DAY_OF_MONTH); // 日

        month++;//ズレ防止

        //年を返す
        if(wantType.equals("year")){
            return year;
        }
        //月を返す
        else if(wantType.equals("month")){
            return month;
        }
        //日を返す
        else if(wantType.equals("day")){
            return day;
        }
        //例外処理
        else{
            return 0;
        }
        ////////////////////////////////////////
    }

    //DatePicker(日付設定)のリスナー登録
    public void DatePickerSet(final int setType) {
        //ok押した時に処理するリスナーの登録
            DateSetListener = new DatePickerDialog.OnDateSetListener() {
                //okボタンを押した時（その時点で選択されてる年月日を取得できる）
                    public void onDateSet(DatePicker datePicker, int year,
                                          int monthOfYear, int dayOfMonth) {
                        //誕生日セットの時の処理
                            if(setType==3) {
                                //押されてる日時をカレンダー生成用の変数へ
                                    temporary_year = year;
                                    temporary_month = monthOfYear;
                                    temporary_day = dayOfMonth;
                                /////
                                //カレンダーのズレを直す
                                int moNth=temporary_month+1;
                                //誕生日変数に代入
                                    userBirthYear = temporary_year;
                                    userBirthMonth = moNth;
                                    userBirthDay = temporary_day;
                                /////
                                //描画
                                drawBirthAndOld();
                            }
                        /////
                        //カスタム通知日セットの時の処理
                            else{
                                //押されたカスタム通知のフラグを立てる
                                flagNotifCus[setType] =1;
                                //カレンダーのズレを直した変数の作成
                                int moNth=monthOfYear+1;
                                //カスタム通知日として描画
                                    textCus[setType].setText(
                                                    String.valueOf(year)+ "/"+
                                                    String.valueOf(moNth)+"/"+
                                                    String.valueOf(dayOfMonth));
                                /////
                                //カスタム通知日を8桁で代入
                                userNotifCus[setType]=OutNum(year,moNth,dayOfMonth);
                                //okが押された時なのでチェックボックスをonに
                                checkCus[setType].setChecked(true);
                                //okが押された時なのでTextの色を黒に
                                textCus[setType].setTextColor(Color.BLACK);
                            }
                        /////
                        //編集済フラグ
                        registJudge=true;
                    }
                /////
            };
        /////
    }
    //誕生日と年齢表示
    public void drawBirthAndOld(){
        if(flagTamura ==0) {
            textBirthday.setText(
                            String.valueOf(userBirthYear) + "/" +
                            String.valueOf(userBirthMonth) + "/" +
                            String.valueOf(userBirthDay));
            //年齢表示
            if (YearsOldSet(userBirthYear, userBirthMonth, userBirthDay) > 1000 || YearsOldSet(userBirthYear, userBirthMonth, userBirthDay) < 0) {
                textYearsOld.setText("");
            } else {
                textYearsOld.setText(String.valueOf(YearsOldSet(userBirthYear, userBirthMonth, userBirthDay)) + "歳");
            }
        }
        else{
            textBirthday.setText(userBirthMonth + "/" + userBirthDay);
            //年齢表示
            textYearsOld.setText("");
        }
    }

    //spinnerCategory追加処理
    public void SpinnerCategoryAdd() {
        if(addCategoryDialog == null)//Adddialogが作成されていない時
        {
            addCategoryDialog = new AlertDialog.Builder(UserRegisterActivity.this)
                    .setTitle("カテゴリー入力")//DiaLogタイトル
                    .setView(viewDialog)//View指定
                    //DiaLog内の決定を押した時の処理
                    .setPositiveButton("決定", new DialogInterface.OnClickListener() //ボタン作成
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //クリック時の処理

                            //入力値取得用変数
                            String addcategory;

                            // エディットテキストのテキストを全選択します
                            editDialog.selectAll();

                            //editTextに何も入力されてない時
                            if (editDialog.getText().toString().equals("")) {
                                addcategory = null;
                            } else {
                                // エディットテキストのテキストを取得します
                                addcategory = editDialog.getText().toString();
                            }

                            //取得したEditTextのTextがnullでない時
                            if(addcategory!=null) {
                                //追加可能(引数true)の時
                                if (checkSameCategoryItem(addcategory, UserRegisterActivity.this)) {
                                    //要素追加
                                    //リストとadaptorに入力値を入れる
                                    categoryAdapter.add(addcategory);
                                    categorylist.add(addcategory);
                                }
                                //追加不可であった(引数false)の時
                                else{
                                    //トースト展開
                                    Toast toast = Toast.makeText(UserRegisterActivity.this, "そのカテゴリは存在しています", Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            }
                            //adapter更新
                            categoryAdapter.notifyDataSetChanged();
                            //カテゴリの保存
                            PM.saveArray(categorylist, "StringItem",UserRegisterActivity.this);
                            //editTextの初期化
                            editDialog.getEditableText().clear();
                            //追加したカテゴリをスピナーで選択させる
                            spinnerCategory.setSelection(categoryAdapter.getPosition(addcategory));
                            //編集済フラグ
                            registJudge=true;
                        }
                    })
                    .setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
                        //DiaLog内の決定をクリックした時
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .create();//初回AddDiaLog制作
        }
    }

    //年齢計算
    //誕生年月日を入れてください
    public int YearsOldSet(int birthyear,int birthmonth,int birthday) {
        //現在の年月日との比較用の変数と引数用変数の宣言
        int nowyear,nowmonth,nowday,yearsold;

        //現在の年月日の取得
        calendar = Calendar.getInstance();
        //取得した年月日の代入
        nowyear = calendar.get(Calendar.YEAR); // 年
        nowmonth = calendar.get(Calendar.MONTH); // 月
        nowday = calendar.get(Calendar.DAY_OF_MONTH); // 日

        nowmonth=nowmonth+1;

        //単純な年齢取得
        yearsold=nowyear-birthyear;

        //性格な年齢を表示させるための比較
        if(birthmonth>nowmonth) {//月が違う場合の処理（例：現年月日2016/1/1で誕生日2015/12/31でも「1歳」と表示されてしまうので）
            yearsold=yearsold-1;//年齢を一歳下げます
        }
        else if((birthmonth==nowmonth)&&(birthday>nowday)) {//日が違う場合の処理（例：現年月日2016/1/1で誕生日2015/1/10でも「1歳」と表示されてしまうので）
            yearsold=yearsold-1;//年齢を一歳下げます
        }
        //年齢を返す
        return yearsold;
    }

    //CheckBoxリスナー登録（cus:0~2,tamura:3,month:4,week:5,yesterday:6,today:7）
    public void CheckJudge(final CheckBox check, final int flag) {
        // チェックボックスがクリックされた時に呼び出されるコールバックリスナーを登録します
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            // チェックボックスがクリックされた時
            public void onClick(View v) {
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
                                        //キャンセル対策のため一旦チェックを外す
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
                                        //switch抜ける
                                        break;
                                /////
                                //年齢不詳チェック処理
                                    case 3:
                                        //フラグ立てる
                                        flagTamura = 1;
                                        //誕生日表記から年を取り除く
                                        textBirthday.setText(userBirthMonth + "/" + userBirthDay);
                                        //年齢を不詳へ
                                        textYearsOld.setText("");
                                        textOldUnknown.setTextColor(Color.BLACK);
                                        //switch抜ける
                                        break;
                                /////
                                //毎月通知チェック処理
                                    case 4:
                                        //フラグ立てる
                                        flagNotifMonth =1;
                                        textMnotif.setTextColor(Color.BLACK);
                                        //switch抜ける
                                        break;
                                /////
                                //毎週通知チェック処理
                                    case 5:
                                        //フラグ立てる
                                        flagNotifWeek =1;
                                        textWnotif.setTextColor(Color.BLACK);
                                        //switch抜ける
                                        break;
                                /////
                                //昨日通知チェック処理
                                    case 6:
                                        //フラグ立てる
                                        flagNotifYesterday =1;
                                        textYnotif.setTextColor(Color.BLACK);
                                        //switch抜ける
                                        break;
                                /////
                                //当日通知チェック処理
                                    case 7:
                                        //フラグ立てる
                                        flagNotifToday =1;
                                        textTnotif.setTextColor(Color.BLACK);
                                        //switch抜ける
                                        break;
                                /////
                            }
                            /////
                    }
                /////
                //チェックがfalse(押された事でfalseになった)時
                else {
                    //指定されたフラグによる処理群
                    switch (flag){
                        //0~1のどれか(カスタム通知用処理)だった時
                        case 0:
                        case 1:
                        case 2:
                            //フラグをしまう
                            flagNotifCus[flag] =0;
                            //テキストの色を灰色に
                            textCus[flag].setTextColor(Color.GRAY);
                            //カスタム通知日を無登録扱いの0に
                            userNotifCus[flag]=0;
                            //テキストを初期状態に戻す
                            textCus[flag].setText("通知日を追加");
                            //switch抜けます
                            break;
                        /////
                        case 3:
                            //フラグをしまう
                            flagTamura = 0;
                            //誕生日を通常の表記（年も含めた表記）にする
                            textBirthday.setText(userBirthYear +"/"+ userBirthMonth + "/" + userBirthDay);
                            //年齢表示
                                //バグとかで通常はありえない数値(1000歳以上とか年齢が負の数とか)の時
                                    if (YearsOldSet(userBirthYear, userBirthMonth, userBirthDay) > 1000 || YearsOldSet(userBirthYear, userBirthMonth, userBirthDay) < 0) {
                                        //空白をセット
                                        textYearsOld.setText("");
                                    }
                                /////
                                //正常な年齢の時
                                    else {
                                        //年齢を算出して「歳」を付けて表示
                                        textYearsOld.setText(String.valueOf(YearsOldSet(userBirthYear, userBirthMonth, userBirthDay)) + "歳");
                                    }
                                /////
                            /////
                            textOldUnknown.setTextColor(Color.GRAY);
                            //switch抜けます
                            break;
                        case 4:
                            //フラグをしまう
                            flagNotifMonth =0;
                            textMnotif.setTextColor(Color.GRAY);
                            //switch抜けます
                            break;
                        case 5:
                            //フラグをしまう
                            flagNotifWeek =0;
                            textWnotif.setTextColor(Color.GRAY);
                            //switch抜けます
                            break;
                        case 6:
                            //フラグをしまう
                            flagNotifYesterday =0;
                            textYnotif.setTextColor(Color.GRAY);
                            //switch抜けます
                            break;
                        case 7:
                            //フラグをしまう
                            flagNotifToday =0;
                            textTnotif.setTextColor(Color.GRAY);
                            //switch抜けます
                            break;
                    }
                }
                /////
                //編集済フラグ
                registJudge=true;
            }
            /////
        });
        /////
    }

    //checkboxの中身を判断してtruefalse変更
    public void CheckBoxChange(CheckBox Cb,TextView tV,int check) {
        if(check==0) {
            Cb.setChecked(false);
            if(tV!=null) {
                tV.setTextColor(Color.GRAY);
            }
        }
        else {
            Cb.setChecked(true);
            if(tV!=null) {
                tV.setTextColor(Color.BLACK);
            }
        }
    }

    //数値を並べて出す(例：(10,20,30)➝102030)
    public int OutNum(int year,int month,int day){
        int cal1,cal2;
        cal1=year*10000;
        cal2=month*100;
        return cal1+cal2+day;
    }

    //数値を年月日毎に出す
    static int OutCale(int numDate,String wantType){
        int year;
        int month;
        int day;
        int cal1,cal2;

        year=numDate/10000;
        cal1=year*10000;
        cal2=numDate-cal1;
        month=cal2/100;
        cal1=month*100;
        day=cal2-cal1;

        if(wantType.equals("year")){
            return year;
        }
        else if(wantType.equals("month")){
            return month;
        }
        else if(wantType.equals("day")){
            return day;
        }
        else{
            return 0;
        }
    }

    //画像回転用メソッド
    public void ViewRotate() {
        float width;
        // ウィンドウマネージャのインスタンス取得
        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        // ディスプレイのインスタンス生成
        Display disp = wm.getDefaultDisplay();
        width = disp.getWidth();

        float factor;
        mat = new Matrix();
        mat.reset();
        switch(orientation) {
            case 1://only scaling
                factor = (float)width/(float)pctWidth;
                mat.preScale(factor, factor);
                break;
            case 2://flip vertical
                factor = (float)width/(float)pctWidth;
                mat.postScale(factor, -factor);
                mat.postTranslate(0, pctHeight*factor);
                break;
            case 3://rotate 180
                mat.postRotate(180, pctWidth/2f, pctHeight/2f);
                factor = (float)width/(float)pctWidth;
                mat.postScale(factor, factor);
                break;
            case 4://flip horizontal
                factor = (float)width/(float)pctWidth;
                mat.postScale(-factor, factor);
                mat.postTranslate(pctWidth*factor, 0);
                break;
            case 5://flip vertical rotate270
                mat.postRotate(270, 0, 0);
                factor = (float)width/(float)pctHeight;
                mat.postScale(factor, -factor);
                break;
            case 6://rotate 90
                mat.postRotate(90, 0, 0);
                factor = (float)width/(float)pctHeight;
                mat.postScale(factor, factor);
                mat.postTranslate(pctHeight*factor, 0);
                break;
            case 7://flip vertical, rotate 90
                mat.postRotate(90, 0, 0);
                factor = (float)width/(float)pctHeight;
                mat.postScale(factor, -factor);
                mat.postTranslate(pctHeight*factor, pctWidth*factor);
                break;
            case 8://rotate 270
                mat.postRotate(270, 0, 0);
                factor = (float)width/(float)pctHeight;
                mat.postScale(factor, factor);
                mat.postTranslate(0, pctWidth*factor);
                break;
        }
    }

    //editText英数字制限
    public void filterEnglish(EditText editText){
        editText.setFilters(new InputFilter[]{
                (source, sourceStart, sourceEnd, destination, destinationStart, destinationEnd) -> {
                    if (source.toString().matches("^[a-zA-Z0-9_]+$")) {
                        return source;
                    } else {
                        return "";
                    }
                }
        });
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

    //同じ名前のカテゴリがないか確認し、なければtrue、あればfalseを飛ばすメソッド
    static  boolean checkSameCategoryItem(String addcategory,Context context){
        /*****同じ名前の要素が存在するかどうか*****/
        //preference用クラス
        PreferenceMethod PM=new PreferenceMethod();
        //カテゴリを追加するかどうかのフラグ(追加可能ならtrue)
        Boolean flagCanAddCategory = true;
        //保存されたカテゴリ一覧を取得
        String categoryItems[] = PM.getArray("StringItem", context);
        if(categoryItems!=null) {
            //カテゴリ数だけループ
            for (int i = 0; i < categoryItems.length; i++) {
                //同じ名前の要素が確認された時
                if (addcategory.equals(categoryItems[i])) {
                    //追加不可フラグ
                    flagCanAddCategory = false;
                }
            }
        }
        return flagCanAddCategory;
        /********************/
    }

    //編集するかしないかの最終確認ダイアログ
    public void finishRegstDialog(){
        //このアクティビティに表示する削除確認ダイアログの宣言
        AlertDialog.Builder aldialogDeleCategory=new AlertDialog.Builder(UserRegisterActivity.this);
        //ダイアログタイトルの決定
        aldialogDeleCategory.setTitle("編集内容を保存しますか");
        //positiveボタン(今回はok)のリスナー登録
        aldialogDeleCategory.setPositiveButton("はい", new DialogInterface.OnClickListener() {
            //保存用ダイアログ内のokボタン押した時
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //名前が入力されてない時
                if(editName.getText().toString().equals("")) {
                    //警告ダイアログ表示
                    cautionDialog();
                }
                else {
                    //保存
                    AllRegist();
                    //Activity終了
                    finish();
                }
            }
        });
        //negativeボタン(今回はキャンセル)のリスナー登録
        aldialogDeleCategory.setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /***キーボードが表示されてるかどうか判定***/
                //キーボードが表示されてる時
                if (keyBoad == true) {
                    //キーボード絶対堕とすマン
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                /*****************************************/
                //Activity終了
                finish();
            }
        });
        //設定したダイアログの表示
        aldialogDeleCategory.show();
    }
    public void cautionDialog(){
        //このアクティビティに表示する削除確認ダイアログの宣言
        AlertDialog.Builder caution=new AlertDialog.Builder(UserRegisterActivity.this);
        //ダイアログタイトルの決定
        caution.setTitle("名前が登録されてません");
        //positiveボタン(今回はok)のリスナー登録
        caution.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            //削除用ダイアログ内のokボタン押した時
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        //設定したダイアログの表示
        caution.show();
    }

    //削除処理
    public void Delete(){
        new AlertDialog.Builder(UserRegisterActivity.this)
                .setMessage("本当に削除してもいいですか？")
                .setCancelable(false)
                .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int idlog) {
                        //idから削除
                        dbAssist.deleteData(id,getApplicationContext());
                        //widget更新
                        WidgetProvider.upDateWidget(UserRegisterActivity.this);
                        //トースト展開
                        Toast toast = Toast.makeText(UserRegisterActivity.this, "データを消去しました", Toast.LENGTH_LONG);
                        toast.show();
                        //削除したら帰る場所が無くなるから自然(ホーム画面)に還す
                        Intent intent = new Intent(UserRegisterActivity.this, GestureDecActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    public void AllRegist() {
        /***キーボードが表示されてるかどうか判定***/
        //キーボードが表示されてる時
        if (keyBoad == true) {
            //キーボード絶対堕とすマン
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        /*****************************************/
        //sqlに保存
            //Data型の宣言
            Data allData = new Data();
            //Data型にデータをセット
                //現在の名前に表示されてる文字列をセット
                allData.setName(editName.getText().toString());
                //現在の振り仮名に表示されてる文字列をセット
                allData.setKana(editPho.getText().toString());
                //誕生年をセット
                allData.setYear(userBirthYear);
                //誕生月をセット
                allData.setMonth(userBirthMonth);
                //誕生日をセット
                allData.setDay(userBirthDay);
                //選択されたカテゴリをセット
                allData.setCategory(userCategory);
                //現在のedit_twitterに表示されてる文字列を取得
                userTwitterId = editTwitter.getText().toString();
                //TwitterIdの文字数が０である（何も入力されてない）時
                    if (userTwitterId.length() != 0) {
                        //一文字目に@が付いてる時
                            if (userTwitterId.charAt(0) == '@') {
                                //一文字目のみ消して取得
                                userTwitterId = userTwitterId.substring(1);
                            }
                        /////
                    }
                /////
                //TwitterIdをセット
                allData.setTwitterID(userTwitterId);
                //現在表示されてるメモをセット
                allData.setMemo(editMemo.getText().toString());
                //保存した画像の名前に形式を付属した物をセット
                allData.setImage(userImage + ".jpg");
                //保存した小さい画像に形式を付属した物をセット
                allData.setSmallImage(userImageSmall + ".jpg");
                //年齢不詳チェックのフラグをセット
                allData.setYukarin(flagTamura);
                //毎月通知チェックのフラグをセット
                allData.setNotif_month(flagNotifMonth);
                //毎週通知チェックのフラグをセット
                allData.setNotif_week(flagNotifWeek);
                //前日通知チェックのフラグをセット
                allData.setNotif_yest(flagNotifYesterday);
                //当日通知チェックのフラグをセット
                allData.setNotif_today(flagNotifToday);
                //カスタム通知の通知日をセット
                    allData.setNotif_cus1(userNotifCus[0]);
                    allData.setNotif_cus2(userNotifCus[1]);
                    allData.setNotif_cus3(userNotifCus[2]);
                /////
            /////
            //dbに書き込み
            //新規作成の時
                if(id==0) {
                    //SQLの追加
                    dbAssist.insertData(allData, this);
                }
            /////
            //編集の時
                else{
                    //SQLの変更
                    dbAssist.updateData(id,allData,this);
                }
            /////
        /////

        /*****通知セット*****/
        //何も無かったら「名前がありません」として保存
        String name=MainListAdapter.setNullName(editName.getText().toString());

        ArrayList<Data> datas = new ArrayList<>();
        datas = dbAssist.allSelect(this);
        Data data = new Data();
        //データベースが存在しない場合
        if (datas.get(0)==null){
            alarmId = 1;
        }

         else if (id ==0){
            //データベースの最後のid
            data = datas.get(datas.size()-1);
            alarmId =data.getId();
            //編集で飛んできた時
        }else {
            alarmId = id;
        }

        //設定値から通知をセット
        Notifier.alarm(
                alarmId,
                name,
                getToday("year"),
                getToday("month"),
                getToday("day"),
                userBirthMonth,
                userBirthDay,
                flagNotifMonth,
                flagNotifWeek,
                flagNotifYesterday,
                flagNotifToday,
                userNotifCus[0],
                userNotifCus[1],
                userNotifCus[2],
                this);
        /********************/

        //widget更新
        WidgetProvider.upDateWidget(this);

        //Activity消す
        finish();

        ALLLOG();
        }

    //Log
    public void ALLLOG() {
        Log.d("ALLLOG",String.valueOf(id));
        Log.d("ALLLOG", editName.getText().toString());
        Log.d("ALLLOG", editPho.getText().toString());
        Log.d("ALLLOG",String.valueOf(userBirthYear)+String.valueOf(userBirthMonth)+String.valueOf(userBirthDay));
        Log.d("ALLLOG", userCategory);
        Log.d("ALLLOG", editTwitter.getText().toString());
        Log.d("ALLLOG", editMemo.getText().toString());
        Log.d("ALLLOG", userImage +".jpg");
        Log.d("ALLLOG", userImage +".jpg");
        Log.d("ALLLOG",String.valueOf(flagTamura));
        Log.d("ALLLOG",String.valueOf(flagNotifMonth));
        Log.d("ALLLOG",String.valueOf(flagNotifWeek));
        Log.d("ALLLOG",String.valueOf(flagNotifYesterday));
        Log.d("ALLLOG",String.valueOf(flagNotifToday));
        Log.d("ALLLOG",String.valueOf(userNotifCus[0]));
        Log.d("ALLLOG",String.valueOf(userNotifCus[1]));
        Log.d("ALLLOG",String.valueOf(userNotifCus[2]));
    }
}