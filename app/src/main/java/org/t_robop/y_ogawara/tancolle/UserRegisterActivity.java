package org.t_robop.y_ogawara.tancolle;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
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
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class UserRegisterActivity extends AppCompatActivity implements TextWatcher {
    private static final int REQUEST_GALLERY = 0;//ギャラリー選択で必要な初期化

    EditText edit_name;
    EditText edit_pho;
    EditText edit_twitter;
    //EditText edit_days_ago;
    ImageView user_view;
    TextView user_birthday;
    TextView user_yearsold;
    CheckBox tamura_check;
    CheckBox month_check;
    CheckBox week_check;
    CheckBox yesterday_check;
    CheckBox today_check;

    CheckBox cus_check[]=new CheckBox[3];

    TextView cus_text[]=new TextView[3];

    EditText edit_memo;
    DatePickerDialog PickerDialog;
    DatePickerDialog cusPickerDialog;
    Calendar calendar;
    Calendar nowCale;

    //キーボード制御
    InputMethodManager inputMethodManager;

    //EditText用変数群
    View viewV;
    LayoutInflater inflater;//DiaLog用xmlから来るやつ
    EditText editText;//DiaLog用xmlのEditText

    //カテゴリ用arraylist&arrayadapter
    ArrayAdapter<String> CategoryAdapter;
    ArrayList<String> arraylist;//カテゴリのList

    String user_category;//選択されたカテゴリ保管（後でsqlに飛ばすよ）

    //MostImportantDate（sqlからデータ読み込む時に使うよ）
    Data idDate;

    //img初期設定用
    String imgSetting;

    //カテゴリ追加用DiaLog
    private AlertDialog addCategory;

    //表示するBitmapデータ
    Bitmap img;
    Bitmap small_img;
    //選択されたBitmapの名前（後でsqlに飛ばすよ）
    String img_name;
    String small_img_name;

    //sppinerTest
    private Spinner spinnerCategory;//カテゴリスピナー
    //private Spinner spinnerRepetition;//繰り返し通知スピナー

    //繰り返し通知スピナーに並べる文字列群
    //private String spinnerRepetitionItems[] = {"繰り返し通知無し", "毎日", "一週間毎", "一ヶ月毎"};

    //一時的な年月日の保存
    int temporary_year;
    int temporary_month;
    int temporary_day;

    //誕生年月日
    int birthYear;
    int birthMonth;
    int birthDay;

    //int reptition_loop;//繰り返しフラグ
    //int days_ago;//何日前ですかー？

    //チェックフラグ
    int tamura_flag;
    int notifMonth_flag;
    int notifWeek_flag;
    int notifYesterday_flag;
    int notifToday_flag;
    int notifCus_flag[]=new int[3];

    //カスタム用の通知月日の保存
    int cus_date[]=new int[3];

    //idチェック
    int id;

    //TwitterId用変数
    String twitter_id;

    //画像の縦横の大きさ
    int pctWidth;
    int pctHeight;

    int orientation;
    ExifInterface exifInterface;
    Matrix mat;

    int monthSetting;//Todo 初期"月"設定テスト(修復時：消せ)

    //キーボードの有無確認
    boolean keyBoad=false;

    //ダイアログでok押された時のリスナー
    DatePickerDialog.OnDateSetListener DateSetListener;

    /////////////////////////Override/////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        //関連付け
        Association();

        cus_text[0].setTextColor(Color.GRAY);
        cus_text[1].setTextColor(Color.GRAY);
        cus_text[2].setTextColor(Color.GRAY);

        // EditText が空のときに表示させるヒントを設定
        edit_name.setHint("Name");
        edit_pho.setHint("Phonetic");
        edit_twitter.setHint("@twitter");
        //edit_days_ago.setHint("Days");
        edit_memo.setHint("何でも自由に書いてね！");

        //画面切り替わり時のid取得
        Intent intent = getIntent();
        id = intent.getIntExtra("IdNum", 0);//何も無かったら（新規作成でidデータが送られてない場合）0を入れる

        monthSetting=intent.getIntExtra("month",0);//Todo 初期"月"設定テスト(選択された月データを取得します。修復時：消せ)

        arraylist = new ArrayList<>();//新規ArrayList使う

        //Spinner設定
        sppinerCategorySet();//カテゴリスピナー設定

        //EditTextの内容設定
        EditTextSet(edit_pho);
        EditTextSet(edit_twitter);

        //振り仮名自動入力のためのリスナー（謎）
        edit_name.addTextChangedListener(this);

        //ImageViewの初期設定
        user_view.setScaleType(ImageView.ScaleType.CENTER_CROP);//CENTER_CROPでViewに合わせて拡大し、画像のはみ出した部分は切り取って、中心にフォーカスする

        //全CheckBoxの値を取得
        for(int i=0;i<3;i++){
            CheckJudge(cus_check[i],i);
        }
        CheckJudge(tamura_check,3);
        CheckJudge(month_check,4);
        CheckJudge(week_check,5);
        CheckJudge(yesterday_check,6);
        CheckJudge(today_check,7);

        //データがある場合（編集として呼ばれた場合）は読み込み
        if (id != 0) {
            idDate = dbAssist.idSelect(id, this);

            edit_name.setText(idDate.getName());
            edit_pho.setText(idDate.getKana());
            edit_twitter.setText(idDate.getTwitterID());
            edit_memo.setText(idDate.getMemo());
            imgSetting = idDate.getImage();
            tamura_flag = idDate.isYukarin();
            notifMonth_flag=idDate.getNotif_month();
            notifWeek_flag=idDate.getNotif_week();
            notifYesterday_flag = idDate.isNotif_yest();
            notifToday_flag = idDate.isNotif_today();
            cus_date[0]=idDate.getNotif_cus1();
            cus_date[1]=idDate.getNotif_cus2();
            cus_date[2]=idDate.getNotif_cus3();

            //配列に入力するため三回回す
            for(int i=0;i<3;i++){
                //最初は0(チェック入ってない物を避けるため)
                notifCus_flag[i]=0;
                if(cus_date[i]!=0){
                    //カスタムに何か入ってる時はフラグ立てる
                    notifCus_flag[i]=1;
                    //それの色を黒に
                    cus_text[i].setTextColor(Color.BLACK);
                    //何か入ってる時は入ってる日付も出力する
                    cus_text[i].setText(
                                    String.valueOf(OutCale(cus_date[i],"year"))+"/"+
                                    String.valueOf(OutCale(cus_date[i],"month"))+"/"+
                                    String.valueOf(OutCale(cus_date[i],"day")));
                }
            }
            //画像の名前を読み込みます
            img_name=idDate.getImage();
            small_img_name=idDate.getSmallImage();
            //名前の後ろの「.jpg」のみ取り除きます
            img_name=CutString(img_name,4);
            small_img_name=CutString(small_img_name,4);

            //誕生年月日の初期値を設定年月日へ
            birthYear=idDate.getYear();
            birthMonth=idDate.getMonth();
            birthMonth--;
            birthDay=idDate.getDay();
            //ダイアログ用変数に代入
            temporary_year=birthYear;
            temporary_month=birthMonth;
            temporary_day=birthDay;

            //読み込んだ段階でデータから全フラグをチェックボックスに適用
            CheckBoxChange(tamura_check, tamura_flag);
            CheckBoxChange(month_check,notifMonth_flag);
            CheckBoxChange(week_check,notifWeek_flag);
            CheckBoxChange(yesterday_check, notifYesterday_flag);
            CheckBoxChange(today_check, notifToday_flag);
            for(int i=0;i<3;i++){
                CheckBoxChange(cus_check[i],notifCus_flag[i]);
            }
        } else{//新規作成として呼ばれた場合
            //新規作成の場合は現在の日時を誕生日欄にセット
            temporary_year=getToday("year");
            temporary_month=getToday("month");
            temporary_day=getToday("day");
            temporary_month=monthSetting-1;//Todo 初期"月"設定テスト(修復時：消せ)
            //それぞれ代入
            birthYear=temporary_year;
            birthMonth=temporary_month;
            birthDay=temporary_day;
            //新規作成の場合でも画像の名前を設定しておく
            imgSetting = "null.jpg";
        }
        //誕生日と年齢表示
        birthMonth++;
        drawBirthAndOld();

        //画像読み込み
        InputStream in;
        try {
            if(imgSetting.equals("null.jpg")){
                Resources r = getResources();
                img = BitmapFactory.decodeResource(r, R.drawable.normal_shadow);
            }
            else {
                in = openFileInput(imgSetting);//画像の名前からファイル開いて読み込み
                img = BitmapFactory.decodeStream(in);//読み込んだ画像をBitMap化
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //BitMapから画像セット
        user_view.setImageBitmap(img);

        // プリファレンスから取得
        String[] arrayItem2 = getArray("StringItem");

        // アイテムを追加します
        CategoryAdapter.add("<未選択>");
        if(arrayItem2==null) {
        } else {
            for (int n = 0; n < arrayItem2.length; n++) {
                CategoryAdapter.add(arrayItem2[n]);
                arraylist.add(arrayItem2[n]);
            }
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

        try {
            //画像取得
            InputStream in = getContentResolver().openInputStream(data.getData());//選択した画像を取得
            Bitmap pct = BitmapFactory.decodeStream(in);//取得した画像をBitMap化
            in.close();//InputStreamを閉じてる

            // 作られたBitMapから横幅と高さを取得
            pctWidth = pct.getWidth();
            pctHeight = pct.getHeight();

            orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            //画像回転メソッド
            ViewRotate();

            //画像設定
            img = Bitmap.createBitmap(pct,0,0, pctWidth, pctHeight,mat, true);
            //小さい画像の作成
            small_img= Bitmap.createScaledBitmap(img,pctWidth/4,pctHeight/4,false);

            //BitMapを表示
            user_view.setImageBitmap(img);
        } catch (Exception e) {}

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

        //保存する画像の名前の決定
        img_name
                = String.valueOf(imgye)
                + String.valueOf(imgmo)
                + String.valueOf(imgda)
                + String.valueOf(imgho)
                + String.valueOf(imgmi)
                + String.valueOf(imgse);
        small_img_name ="small_"+ img_name;

        //ローカルファイルへ保存
        FileOutputStream out;
        try {
            //作成するデータの名前と設定
            out = this.openFileOutput(img_name + ".jpg", Context.MODE_PRIVATE);//.jpgつけてちょ
            //画像の保存（フォーマット設定,品質,設定データ）
            img.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            //作成するデータの名前と設定
            out = this.openFileOutput(small_img_name + ".jpg", Context.MODE_PRIVATE);//.jpgつけてちょ
            //画像の保存（フォーマット設定,品質,設定データ）
            small_img.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TextWatcher/////未完成/////
    @Override
    public void beforeTextChanged(final CharSequence s, int start, int count, int after) {
        //操作前のEtidTextの状態を取得する
        edit_pho.setText(s.toString());

        //EditTextにリスナーをセット
        edit_name.setOnKeyListener(new View.OnKeyListener() {

            //コールバックとしてonKey()メソッドを定義
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //イベントを取得するタイミングには、ボタンが押されてなおかつエンターキーだったときを指定
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){

                    //キーボードを閉じる
                    inputMethodManager.hideSoftInputFromWindow(edit_name.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

                    //フォーカスを外す
                    edit_name.setFocusable(false);
                    edit_name.setFocusableInTouchMode(false);
                    edit_name.requestFocus();

                    keyBoad=false;

                    // エディットテキストのテキストを全選択します
                    edit_name.selectAll();

                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //操作中のEtidTextの状態を取得する
    }

    @Override
    public void afterTextChanged(Editable s) {
        //操作後のEtidTextの状態を取得する
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // アクションバー内に使用するメニューアイテムを注入します。
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_register_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //アクションバー処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // アクションバーアイテム上の押下を処理します。
        switch (item.getItemId()) {
            case R.id.action_button:
                AllRegist();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    ////////////////////////////////////////////////////////////


    ////////////////////クリック処理群/////////////////////////

    public void EditName(View v) {
        //フォーカスon
        EditTextClick(edit_name);
        //キーボード表示
        inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);
        keyBoad=true;
    }

    public void EditPho(View v) {
        //フォーカスon
        EditTextClick(edit_pho);
        //キーボード表示
        inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);
        keyBoad=true;
    }

    public void EditTwitter(View v) {
        //フォーカスon
        EditTextClick(edit_twitter);
        //キーボード表示
        inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);
        keyBoad=true;
    }

//    public void EditAgo(View v) {
//        //フォーカスon
//        EditTextClick(edit_days_ago);
//        //キーボード表示
//        inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);
//        keyBoad=true;
//    }

    //画像クリック時
    public void UserView(View v) {
        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/jpeg");
            startActivityForResult(Intent.createChooser(intent, "Pick a source"), 0);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/jpeg");
            startActivityForResult(Intent.createChooser(intent, "Pick a source"), 1);
        }
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
        addCategory.show();
    }

    //カスタム用テキストクリック時
    public void cusText(View v) {
        String string = String.valueOf(v.getTag());
        int num = Integer.parseInt(string);
        if (notifCus_flag[num] == 1) {
            //ok時の処理の設定
            DatePickerSet(num);
            //ダイアログ生成
            PickerDialog = new DatePickerDialog(this, DateSetListener,
                    OutCale(cus_date[num], "year"),
                    OutCale(cus_date[num], "month"),//カレンダーの月がズレるので合わせます
                    OutCale(cus_date[num], "day"));
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
        user_yearsold = (TextView) findViewById(R.id.YearsOld);
        //誕生日の関連付け
        user_birthday = (TextView) findViewById(R.id.userbirthday);
        //名前入力欄の関連付け
        edit_name = (EditText) findViewById(R.id.EditName);
        //振り仮名入力欄の関連付け
        edit_pho = (EditText) findViewById(R.id.EditPho);
        //twitterid入力欄の関連付け
        edit_twitter = (EditText) findViewById(R.id.twitter);
        //通知は何日前かの入力欄の関連付け
        //edit_days_ago = (EditText) findViewById(R.id.ago);
        //メモ入力欄の関連付け
        edit_memo = (EditText) findViewById(R.id.Memo);
        //キーボード表示を制御（出したり消したり）するためのオブジェクトの関連付け
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //画像表示場所の関連付け
        user_view = (ImageView) findViewById(R.id.userview);
        //田村チェックボックスの関連付け
        tamura_check = (CheckBox) findViewById(R.id.TamuraCheck);
        //一ヶ月前に通知するかどうかのチェックボックスの関連付け
        month_check = (CheckBox) findViewById(R.id.MonthCheck);
        //一週間前に通知するかどうかのチェックボックスの関連付け
        week_check = (CheckBox) findViewById(R.id.WeekCheck);
        //前日に通知するかどうかのチェックボックスの関連付け
        yesterday_check = (CheckBox) findViewById(R.id.YesterdayCheck);
        //当日に通知するかどうかのチェックボックスの関連付け
        today_check = (CheckBox) findViewById(R.id.TodayCheck);
        //カスタム通知で通知するかどうかのチェックボックスの関連付け
        cus_check[0]=(CheckBox)findViewById(R.id.CutomCheck1);
        cus_check[1]=(CheckBox)findViewById(R.id.CutomCheck2);
        cus_check[2]=(CheckBox)findViewById(R.id.CutomCheck3);
        //カスタム通知の日時表示用のテキストの関連付け
        cus_text[0]=(TextView)findViewById(R.id.cusText1);
        cus_text[1]=(TextView)findViewById(R.id.cusText2);
        cus_text[2]=(TextView)findViewById(R.id.cusText3);
        //DiaLog用のxmlとの連携の関連付け
        inflater = LayoutInflater.from(UserRegisterActivity.this);
        viewV = inflater.inflate(R.layout.dialog_user_register, null);
        editText = (EditText) viewV.findViewById(R.id.editText1);
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
        CategoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        CategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // アダプターを設定します
        spinnerCategory.setAdapter(CategoryAdapter);

        // スピナーのアイテムが選択された時に呼び出されるコールバックリスナーを登録します
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //処理
                //String型変数user_categoryに選択されたアイテムを代入
                user_category=(String) spinnerCategory.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    //繰り返し通知の選択用spinner設定
//    public void spinnerRepetitionSet() {
//        // ArrayAdapterの宣言
//        ArrayAdapter<String> adapter
//                = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerRepetitionItems);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        // spinner に adapter をセット
//        spinnerRepetition.setAdapter(adapter);
//        // リスナーを登録
//        spinnerRepetition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            //　アイテムが選択された時
//            public void onItemSelected(AdapterView<?> parent, View viw, int arg2, long arg3) {
//                Spinner spinner = (Spinner) parent;
//                String item = (String) spinner.getSelectedItem();
//
//                //それぞれの選択肢が選択された時の処理
//                if (item.equals(spinnerRepetitionItems[1])) {
//                    reptition_loop=1;
//                }
//                else if (item.equals(spinnerRepetitionItems[2])) {
//                    reptition_loop=2;
//                }
//                else if (item.equals(spinnerRepetitionItems[3])) {
//                    reptition_loop=3;
//                }
//                else {//初期値
//                    reptition_loop=0;
//                }
//            }
//            //　アイテムが選択されなかった
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//    }

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

    //DatePickerの設定
    public void DatePickerSet(final int setType) {
        // 日付設定時のリスナ作成
        //ok押した時の処理
        DateSetListener = new DatePickerDialog.OnDateSetListener() {
            //okボタンを押したときの年月日を取得できる
            public void onDateSet(DatePicker datePicker, int year,
                                  int monthOfYear, int dayOfMonth) {

                if(setType==3) {
                    //押されてる日時を変数へ
                    temporary_year = year;
                    temporary_month = monthOfYear;
                    temporary_day = dayOfMonth;

                    int moNth=temporary_month+1;//カレンダーのズレを直す

                    //それぞれ代入
                    birthYear = temporary_year;
                    birthMonth = moNth;
                    birthDay = temporary_day;
                    //描画
                    drawBirthAndOld();
                }
                else{
                    //押されてる日時を変数へ
                    int moNth=monthOfYear+1;//カレンダーのズレを直す
                    //カスタム通知日として表示
                    cus_text[setType].setText(
                                    String.valueOf(year)+ "/"+
                                    String.valueOf(moNth)+"/"+
                                    String.valueOf(dayOfMonth));
                    //カスタム通知日を8桁で代入
                    cus_date[setType]=OutNum(year,monthOfYear,dayOfMonth);
                }
            }
        };
    }

    //誕生日と年齢表示
    public void drawBirthAndOld(){
        if(tamura_flag==0) {
            user_birthday.setText(
                            String.valueOf(birthYear) + "/" +
                            String.valueOf(birthMonth) + "/" +
                            String.valueOf(birthDay));
            //年齢表示
            if (YearsOldSet(birthYear, birthMonth, birthDay) > 1000) {
                user_yearsold.setText("");
            } else {
                user_yearsold.setText(String.valueOf(YearsOldSet(birthYear, birthMonth, birthDay)) + "歳");
            }
        }
        else{
            user_birthday.setText(birthMonth + "/" + birthDay);
            //年齢表示
            user_yearsold.setText("不明");
        }
    }

    //spinnerCategory追加処理
    public void SpinnerCategoryAdd() {
        if(addCategory == null)//Adddialogが作成されていない時
        {
            addCategory = new AlertDialog.Builder(UserRegisterActivity.this)
                    .setTitle("カテゴリー入力")//DiaLogタイトル
                    .setView(viewV)//View指定
                    //DiaLog内の決定を押した時の処理
                    .setPositiveButton("決定", new DialogInterface.OnClickListener() //ボタン作成
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //クリック時の処理

                            //入力値取得用変数
                            String addcategory;

                            // エディットテキストのテキストを全選択します
                            editText.selectAll();

                            //editTextに何も入力されてない時
                            if (editText.getText().toString().equals("")) {
                                addcategory = null;
                            } else {
                                // エディットテキストのテキストを取得します
                                addcategory = editText.getText().toString();
                            }
                            //要素追加
                            //リストとadaptorに入力値を入れる
                            if(addcategory!=null) {
                                CategoryAdapter.add(addcategory);

                                arraylist.add(addcategory);
                            }
                            //adapter更新
                            CategoryAdapter.notifyDataSetChanged();

                            editText.getEditableText().clear();//editTextの初期化

                            spinnerCategory.setSelection(CategoryAdapter.getPosition(addcategory));
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

    //CheckBox判定処理（tamura:0,yesterday:1,today:2）
    public void CheckJudge(final CheckBox check, final int flag) {
        // チェックボックスがクリックされた時に呼び出されるコールバックリスナーを登録します
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            // チェックボックスがクリックされた時に呼び出されます
            public void onClick(View v) {
                // チェックボックスのチェック状態を取得します
                boolean checked = check.isChecked();

                if(checked==true) {
                    switch (flag){
                        case 0:
                        case 1:
                        case 2:
                            notifCus_flag[flag] =1;
                            cus_text[flag].setTextColor(Color.BLACK);
                            DatePickerSet(flag);
                            if(cus_date[flag]!=0) {
                                PickerDialog = new DatePickerDialog(UserRegisterActivity.this, DateSetListener,
                                        OutCale(cus_date[flag], "year"),
                                        OutCale(cus_date[flag], "month"),
                                        OutCale(cus_date[flag], "day"));
                            }
                            else{
                                PickerDialog = new DatePickerDialog(UserRegisterActivity.this, DateSetListener,getToday("year"),getToday("month")-1,getToday("day"));
                            }
                            PickerDialog.show();
                            break;
                        case 3:
                            //年齢不詳
                            tamura_flag = 1;
                            user_birthday.setText(birthMonth + "/" + birthDay);
                            user_yearsold.setText("不明");
                            break;
                        case 4:
                            notifMonth_flag =1;
                            break;
                        case 5:
                            notifWeek_flag =1;
                            break;
                        case 6:
                            notifYesterday_flag =1;
                            break;
                        case 7:
                            notifToday_flag =1;
                            break;
                    }
                }
                else {
                    switch (flag){
                        case 0:
                        case 1:
                        case 2:
                            notifCus_flag[flag] =0;
                            cus_text[flag].setTextColor(Color.GRAY);
                            break;
                        case 3:
                            tamura_flag = 0;
                            user_birthday.setText(birthYear+"/"+birthMonth + "/" + birthDay);
                            //年齢表示
                            if (YearsOldSet(birthYear, birthMonth, birthDay) > 1000 || YearsOldSet(birthYear, birthMonth, birthDay) < 0) {//バグとか、通常はありえない数値の場合は空白をセット
                                user_yearsold.setText("");
                            } else {
                                user_yearsold.setText(String.valueOf(YearsOldSet(birthYear, birthMonth, birthDay)) + "歳");//年齢を算出して「歳」を付けて表示
                            }
                            break;
                        case 4:
                            notifMonth_flag =0;
                            break;
                        case 5:
                            notifWeek_flag =0;
                            break;
                        case 6:
                            notifYesterday_flag =0;
                            break;
                        case 7:
                            notifToday_flag =0;
                            break;
                    }
                }
            }
        });
    }

    //checkboxの中身を判断してtruefalse変更
    public void CheckBoxChange(CheckBox Cb,int check) {
        if(check==0) {
            Cb.setChecked(false);
        }
        else {
            Cb.setChecked(true);
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
    public int OutCale(int numDate,String wantType){
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

    public void AllRegist() {
        //キーボードが表示されてるかどうか判定
        if (keyBoad == true) {
            //キーボード絶対堕とすマン
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        //sqlに保存
        //Data型の宣言
        Data allData = new Data();
        //Data型にデータをセット
        allData.setName(edit_name.getText().toString());
        allData.setKana(edit_pho.getText().toString());
        allData.setYear(birthYear);
        allData.setMonth(birthMonth);
        allData.setDay(birthDay);
        allData.setCategory(user_category);

        twitter_id = edit_twitter.getText().toString();//現在のedit_twitterに表示されてる文字列を取得
        if (twitter_id.length() != 0) {
            if (twitter_id.charAt(0) == '@') {//一文字目を取得して@が付いてたらそれだけ消して取得
                twitter_id = twitter_id.substring(1);
            }
        }
        allData.setTwitterID(twitter_id);
        allData.setMemo(edit_memo.getText().toString());
        allData.setImage(img_name + ".jpg");
        allData.setSmallImage(small_img_name + ".jpg");
        allData.setYukarin(tamura_flag);
        allData.setNotif_month(notifMonth_flag);
        allData.setNotif_week(notifWeek_flag);
        allData.setNotif_yest(notifYesterday_flag);
        allData.setNotif_today(notifToday_flag);
        allData.setNotif_cus1(cus_date[0]);
        allData.setNotif_cus2(cus_date[1]);
        allData.setNotif_cus3(cus_date[2]);

        //dbに書き込み
        if(id==0) {
            //新規作成の場合はSQLの追加
            dbAssist.insertData(allData, this);
        }
        else{
            //編集の場合はSQLの変更
            dbAssist.updateData(id,allData,this);
        }

        // プレファレンスに保存
        saveArray(arraylist, "StringItem");

        //新規作成か編集かによって画面切り替え場所の変更
        if (id == 0) {
            //MainへGo!
            Intent intent = new Intent(this, GestureDecActivity.class);
            startActivity(intent);
        } else {
            //DetailへGo!
            finish();
        }
        ALLLOG();
        }

    //Log
    public void ALLLOG() {
        Log.d("ALLLOG",String.valueOf(id));
        Log.d("ALLLOG",edit_name.getText().toString());
        Log.d("ALLLOG",edit_pho.getText().toString());
        Log.d("ALLLOG",String.valueOf(birthYear)+String.valueOf(birthMonth)+String.valueOf(birthDay));
        Log.d("ALLLOG",user_category);
        Log.d("ALLLOG",edit_twitter.getText().toString());
        Log.d("ALLLOG",edit_memo.getText().toString());
        Log.d("ALLLOG",img_name+".jpg");
        Log.d("ALLLOG",img_name+".jpg");
        Log.d("ALLLOG",String.valueOf(tamura_flag));
        Log.d("ALLLOG",String.valueOf(notifMonth_flag));
        Log.d("ALLLOG",String.valueOf(notifWeek_flag));
        Log.d("ALLLOG",String.valueOf(notifYesterday_flag));
        Log.d("ALLLOG",String.valueOf(notifToday_flag));
        Log.d("ALLLOG",String.valueOf(cus_date[0]));
        Log.d("ALLLOG",String.valueOf(cus_date[1]));
        Log.d("ALLLOG",String.valueOf(cus_date[2]));
    }
}