package org.t_robop.y_ogawara.tancolle;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    EditText edit_days_ago;
    ImageView user_view;
    TextView user_birthday;
    TextView user_yearsold;
    CheckBox tamura_check;
    CheckBox yesterday_check;
    CheckBox today_check;
    EditText edit_memo;
    DatePickerDialog datePickerDialog;
    Calendar calendar;
    Calendar nowCale;

    //キーボード制御
    InputMethodManager inputMethodManager;

    //EditText用変数群
    View viewV;
    LayoutInflater inflater;//DiaLog用xmlから来るやつ
    EditText editText;//DiaLog用xmlのEditText

    //カテゴリ用arraylist&arrayadapter
    ArrayAdapter<String> adapter;
    ArrayList<String> arraylist;//カテゴリのList

    String user_category;//選択されたカテゴリ保管（後でsqlに飛ばすよ）

    //MostImportantDate（sqlからデータ読み込む時に使うよ）
    Data idDate;

    //img初期設定用
    String imgSetting;

    //カテゴリ追加用DiaLog
    private AlertDialog addCategory;

    Bitmap img;//表示するBitmapデータ
    String img_name;//選択されたBitmapの名前（後でsqlに飛ばすよ）

    //sppinerTest
    private Spinner spinnerCategory;//カテゴリスピナー
    private Spinner spinnerRepetition;//繰り返し通知スピナー

    //繰り返し通知スピナーに並べる文字列群
    private String spinnerRepetitionItems[] = {"繰り返し通知無し", "毎日", "一週間毎", "一ヶ月毎"};

    //一時的な年月日の保存
    int temporary_year;
    int temporary_month;
    int temporary_day;

    //誕生年月日（八桁）
    int birthday;

    int reptition_loop;//繰り返しフラグ
    int days_ago;//何日前ですかー？

    //チェックフラグ
    int tamura_flag;
    int yesterday_flag;
    int today_flag;

    //idチェック
    int id;

    int pctWidth;
    int pctHeight;


    int orientation;
    ExifInterface exifInterface;
    Matrix mat;

    /////////////////////////Override/////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        //繰り返し通知スピナーの関連付け
        spinnerRepetition = (Spinner) findViewById(R.id.spinner2);
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
        edit_days_ago = (EditText) findViewById(R.id.ago);
        //メモ入力欄の関連付け
        edit_memo = (EditText) findViewById(R.id.Memo);
        //キーボード表示を制御（出したり消したり）するためのオブジェクトの関連付け
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //画像表示場所の関連付け
        user_view = (ImageView) findViewById(R.id.userview);
        //田村チェックボックスの関連付け
        tamura_check = (CheckBox) findViewById(R.id.TamuraCheck);
        //前日に通知するかどうかのチェックボックスの関連付け
        yesterday_check = (CheckBox) findViewById(R.id.YesterdayCheck);
        //当日に通知するかどうかのチェックボックスの関連付け
        today_check = (CheckBox) findViewById(R.id.TodayCheck);
        //DiaLog用のxmlとの連携の関連付け
        inflater = LayoutInflater.from(UserRegisterActivity.this);
        viewV = inflater.inflate(R.layout.dialog_user_register, null);
        editText = (EditText) viewV.findViewById(R.id.editText1);

        // EditText が空のときに表示させるヒントを設定
        edit_name.setHint("Name");
        edit_pho.setHint("Phonetic");
        edit_twitter.setHint("@twitter");
        edit_days_ago.setHint("Days");
        edit_memo.setHint("何でも自由に書いてね！");

        //画面切り替わり時のid取得
        Intent intent = getIntent();
        id = intent.getIntExtra("IdNum", 0);//何も無かったら（新規作成でidデータが送られてない場合）0を入れる

        arraylist = new ArrayList<>();//新規ArrayList使う

        //Spinner設定
        sppinerCategory();//カテゴリスピナー設定
        sppinerSet(spinnerRepetition, spinnerRepetitionItems);//繰り返し通知スピナー設定

        //EditTextの内容設定
        EditTextSet(edit_name);
        EditTextSet(edit_pho);
        EditTextSet(edit_twitter);
        EditTextSet(edit_days_ago);

        //ImageViewの初期設定
        user_view.setScaleType(ImageView.ScaleType.CENTER_CROP);//CENTER_CROPでViewに合わせて拡大し、画像のはみ出した部分は切り取って、中心にフォーカスする

        //CheckBoxの値を取得
        //TODO　クソ
        TamuraJudge(tamura_check);
        YesterdayJudge(yesterday_check);
        TodayJudge(today_check);

        //年齢表示
        if (YearsOldSet(birthday) > 1000 || YearsOldSet(birthday) < 0) {//バグとか、通常はありえない数値の場合は空白をセット
            user_yearsold.setText("");
        } else {
            user_yearsold.setText(String.valueOf(YearsOldSet(birthday)) + "歳");//年齢を算出して「歳」を付けて表示
        }

        //データがある場合（編集として呼ばれた場合）は読み込み
        if (id != 0) {
            idDate = dbAssist.idSelect(id, this);

            edit_name.setText(idDate.getName());
            edit_pho.setText(idDate.getKana());
            edit_twitter.setText(idDate.getTwitterID());
            edit_days_ago.setText(idDate.getNotif_day());
            edit_memo.setText(idDate.getMemo());
            tamura_flag = idDate.isYukarin();
            yesterday_flag = idDate.isNotif_yest();
            today_flag = idDate.isNotif_today();
            spinnerRepetition.setSelection(idDate.getNotif_recy());
            imgSetting = idDate.getImage();

            //年月日で読み込んでから8桁に変換
            birthday = BirthDayGet(idDate.getYear(), idDate.getMonth(), idDate.getDay());
            //読み込んだ段階でデータからフラグを適用
            CheckBoxChange(tamura_check, tamura_flag);
            CheckBoxChange(yesterday_check, yesterday_flag);
            CheckBoxChange(today_check, today_flag);
        } else//新規作成として呼ばれた場合
        {
            BirthTodaySet();//新規作成の場合は現在の日時を誕生日欄にセット
            imgSetting = "null.jpg";//新規作成の場合でも画像の名前を設定しておく
        }

        //誕生日描画
        BirthDayDraw(birthday);

        //TextWacher
        //TODO　ヤバイ
        edit_name.addTextChangedListener(this);

        //画像読み込み
        InputStream in;
        try {
            in = openFileInput(imgSetting);//画像の名前からファイル開いて読み込み
            img = BitmapFactory.decodeStream(in);//読み込んだ画像をBitMap化
        } catch (IOException e) {
            e.printStackTrace();
        }
        //BitMapから画像セット
        user_view.setImageBitmap(img);
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
//        if(requestCode == REQUEST_GALLERY && resultCode == RESULT_OK)//安心設計（ギャラリー開くリクエストしてギャラリーが開かれる＆画像選択に成功した場合のみの処理）
//        {

        try {
            //画像取得
            InputStream in = getContentResolver().openInputStream(data.getData());//選択した画像を取得
            Bitmap pct = BitmapFactory.decodeStream(in);//取得した画像をBitMap化
            in.close();//TODO　わっかんねえ
            //InputStreamを閉じてる

            // 作られたBitMapから横幅と高さを取得
            pctWidth = pct.getWidth();
            pctHeight = pct.getHeight();

            //Cursor c;
            //String[] columns= {MediaStore.Images.Media.DATA };

            //TODO dataからfilepathへの変換
            /*
            *
            //c = getContentResolver().query(data.getData(), columns, null, null, null);
            //c.moveToFirst();
            //exifInterface = new ExifInterface(c.getString(0));
            // TODO 向きを取得
            //orientation = Integer.parseInt(exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION));
            */
            orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);


            ViewRotate();
                    /*
                    //BitMapの解像度が1000以上であれば5で割らせる
                    if (pctWidth > 1000 || pctHeight > 1000) {
                        pctWidth = pctWidth / 3;
                        pctHeight = pctHeight / 3;
                    } else//BitMapの解像度が1000以下であれば2で許す
                    {
                        pctWidth = pctWidth / 2;
                        pctHeight = pctHeight / 2;
                    }
                    */

            //設定した解像度をBitMapに反映
            //img = Bitmap.createScaledBitmap(pct, pctWidth, pctHeight, false);

            //TODO　闇
            //img = Bitmap.createBitmap(pct,0,0,pctWidth,pctHeight,mat,false);
            img = Bitmap.createBitmap(pct,0,0, pctWidth, pctHeight,mat, true);

            //BitMapを表示
            user_view.setImageBitmap(img);
        } catch (Exception e) {

        }

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
        img_name = String.valueOf(imgye) + String.valueOf(imgmo) + String.valueOf(imgda) + String.valueOf(imgho) + String.valueOf(imgmi) + String.valueOf(imgse);


        //TODO　画像回転させようとしたら保存出来なくなった（BitMap取得できてないからかと思われる）ので詰んでます
        //ローカルファイルへ保存
        try {
            final FileOutputStream out = openFileOutput(img_name + ".jpg", Context.MODE_WORLD_READABLE);//.jpgつけてちょ
            img.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//}


    //TODO　やめろ！見るな！
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
                if((event.getAction() != KeyEvent.ACTION_DOWN)){

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
//////////TODO　ここまで見るな！




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

    public void EditName(View v)
    {
        //フォーカスon
        EditTextClick(edit_name);
        //キーボード表示
        inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }

    public void EditPho(View v)
    {
        //フォーカスon
        EditTextClick(edit_pho);
        //キーボード表示
        inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }

    public void EditTwitter(View v)
    {
        //フォーカスon
        EditTextClick(edit_twitter);
        //キーボード表示
        inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }

    public void EditAgo(View v)
    {
        //フォーカスon
        EditTextClick(edit_days_ago);
        //キーボード表示
        inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }

    //画像クリック時
    public void UserView(View v)
    {
//        // ギャラリー呼び出し
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, REQUEST_GALLERY);

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
    public void BirthDay(View v)
    {
        DatePickerSet();
        // 日付設定ダイアログの表示
        datePickerDialog.show();
    }

    //カテゴリ追加
    public void CategoryPlus(View v)
    {
        SpinnerCategoryAdd();
        addCategory.show();
    }


    //////////////////////////////////////////////////




    ////////////////////自作関数群////////////////////

    public void EditSetString(EditText edit,String string)
    {
        String text;
        // エディットテキストのテキストを全選択します
        edit.selectAll();
        // エディットテキストのテキストを取得します
        text = edit.getText().toString();
        //editTextの初期化
        edit.getEditableText().clear();

        //textの長さが0(何も入っていない)の場合
        if(text.length()==0)
        {

        }
        else
        {
            //textから数値のみ取り出す
            days_ago = Integer.parseInt(text.replaceAll("[^0-9]",""));
            //「日前」を加えて表示
            edit.setText(String.valueOf(days_ago)+string);
        }
    }


    public void EditTextSet(final EditText edit)
    {
        //EditTextにリスナーをセット
        edit.setOnKeyListener(new View.OnKeyListener() {

            //コールバックとしてonKey()メソッドを定義
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //イベントを取得するタイミングには、ボタンが押されてなおかつエンターキーだったときを指定
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){

                    //キーボードを閉じる
                    inputMethodManager.hideSoftInputFromWindow(edit.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

                    EditSetString(edit_days_ago,"日前");//edit_days_ago用処理

                    //フォーカスを外す
                    edit.setFocusable(false);
                    edit.setFocusableInTouchMode(false);
                    edit.requestFocus();

                    // エディットテキストのテキストを全選択します
                    edit.selectAll();

                    return true;
                }

                return false;
            }
        });
    }

    //指定されたEditTextがクリックされた時にフォーカスを移す処理
    public void EditTextClick(EditText edit)
    {
        //指定されたEditTextのフォーカスをonへ
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
        edit.requestFocus();
        //edit.getEditableText().clear();//editTextの初期化
    }


    //カテゴリー追加用spinner
    public void sppinerCategory()
    {

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // プリファレンスから取得
        String[] arrayItem2 = getArray("StringItem");

        //Log.d("aaaa",String.valueOf(arrayItem2.length));

        // アイテムを追加します
        adapter.add("<未選択>");
        if(arrayItem2==null) {
        }
        else {
            for (int n = 1; n < arrayItem2.length; n++) {
                adapter.add(arrayItem2[n]);
            }
        }
        spinnerCategory = (Spinner) findViewById(R.id.spinner1);
        // アダプターを設定します
        spinnerCategory.setAdapter(adapter);
        // スピナーのアイテムが選択された時に呼び出されるコールバックリスナーを登録します
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //処理
                user_category=(String) spinnerCategory.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    //spinner使おうぜ！！（セットするspinner,セットするString）
    public void sppinerSet(Spinner nSpinner, final String spinnerItems[])
    {
        // ArrayAdapter
        ArrayAdapter<String> adapter
                = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // spinner に adapter をセット
        nSpinner.setAdapter(adapter);

        // リスナーを登録
        nSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //　アイテムが選択された時
            public void onItemSelected(AdapterView<?> parent, View viw, int arg2, long arg3) {
                Spinner spinner = (Spinner) parent;
                String item = (String) spinner.getSelectedItem();

                if (item.equals(spinnerItems[1]))
                {
                    reptition_loop=1;
                }
                else if (item.equals(spinnerItems[2]))
                {
                    reptition_loop=2;
                }
                else if (item.equals(spinnerItems[3]))
                {
                    reptition_loop=3;
                }
                else //初期値
                {
                    reptition_loop=0;
                }
            }

            //　アイテムが選択されなかった
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    //現在の日付の取得
    public void BirthTodaySet()
    {
        ////////// 日付情報の初期設定 //////////
        calendar = Calendar.getInstance();

        temporary_year = calendar.get(Calendar.YEAR); // 年
        temporary_month = calendar.get(Calendar.MONTH); // 月
        temporary_day = calendar.get(Calendar.DAY_OF_MONTH); // 日

        nowCale=calendar;

        birthday=BirthDayGet(temporary_year, temporary_month,temporary_day);

        ////////////////////////////////////////
    }

    //DatePickerの設定
    public void DatePickerSet()
    {
        // 日付設定時のリスナ作成
        //ok押した時の処理
        DatePickerDialog.OnDateSetListener DateSetListener = new DatePickerDialog.OnDateSetListener() {
            //okボタンを押したときの年月日を取得できる
            public void onDateSet(DatePicker datePicker, int year,
                                  int monthOfYear, int dayOfMonth) {
                //押されてる日時を変数へ
                temporary_year = year;
                temporary_month = monthOfYear;
                temporary_day = dayOfMonth;

                birthday=BirthDayGet(temporary_year, temporary_month,temporary_day);

                //描画
                BirthDayDraw(birthday);

                //年齢表示
                if(YearsOldSet(birthday)>1000) {
                    user_yearsold.setText("");
                }
                else {
                    user_yearsold.setText(String.valueOf(YearsOldSet(birthday))+"歳");
                }
            }
        };
        // 日付設定ダイアログの作成・リスナの登録
        datePickerDialog = new DatePickerDialog(this, DateSetListener, temporary_year, temporary_month, temporary_day);
    }


    //カレンダーデータを八桁の数値へ
    public int BirthDayGet(int Year,int Month,int Day)
    {
        int caly,calm;

        caly=Year*10000;
        calm=Month*100;

        return caly+calm+Day;
    }

    //八桁の数値から算出してTextViewに描画
    public void BirthDayDraw(int Birthday)
    {
        int caly,calm,cald,cal;

        caly=Birthday/10000;

        calm=Birthday/100;
        cal=caly*100;
        calm=calm-cal;

        cal=caly*10000;
        cald=Birthday-cal;
        cal=calm*100;
        cald=cald-cal;

        temporary_year=caly;
        temporary_month =calm;
        temporary_day=cald;

        cal= temporary_month +1;

        user_birthday.setText(temporary_year+"/"+cal+"/"+temporary_day);
    }

    //spinnerCategory追加処理
    public void SpinnerCategoryAdd()
    {
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
                                addcategory = "";//0が入る
                            } else {
                                // エディットテキストのテキストを取得します
                                addcategory = editText.getText().toString();
                            }

                            //要素追加
                            //リストとadaptorに入力値を入れる
                            adapter.add(addcategory);

                            arraylist.add(addcategory);

                            //adapter更新
                            adapter.notifyDataSetChanged();

                            editText.getEditableText().clear();//editTextの初期化

                            spinnerCategory.setSelection(adapter.getPosition(addcategory));

                        }
                    })
                    .create();//初回AddDiaLog制作
        }
    }

    //年齢計算
    public int YearsOldSet(int birth)
    {
        int birthyear,nowyear,nowmonth,nowday,yearsold;
        int caly,calm,cald,cal;

        caly=birth/10000;

        calm=birth/100;
        cal=caly*100;
        calm=calm-cal;

        cal=caly*10000;
        cald=birth-cal;
        cal=calm*100;
        cald=cald-cal;


        calendar = Calendar.getInstance();

        nowyear = calendar.get(Calendar.YEAR); // 年
        nowmonth = calendar.get(Calendar.MONTH); // 月
        nowday = calendar.get(Calendar.DAY_OF_MONTH); // 日

        birthyear=birth/10000;

        yearsold=nowyear-birthyear;

        if(calm>nowmonth)
        {
            yearsold=yearsold-1;
        }
        else if(calm==nowmonth&&cald>nowday)
        {
            yearsold=yearsold-1;
        }

        return yearsold;
    }


    //TamuraCheck判定処理
    public void TamuraJudge(final CheckBox check)
    {
        // チェックボックスがクリックされた時に呼び出されるコールバックリスナーを登録します
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            // チェックボックスがクリックされた時に呼び出されます
            public void onClick(View v) {
                // チェックボックスのチェック状態を取得します
                boolean checked = check.isChecked();

                if(checked==true)
                {
                    tamura_flag =1;
                }
                else
                {
                    tamura_flag =0;
                }
            }
        });
    }
    //YesterdayCheck判定処理
    public void YesterdayJudge(final CheckBox check)
    {
        // チェックボックスがクリックされた時に呼び出されるコールバックリスナーを登録します
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            // チェックボックスがクリックされた時に呼び出されます
            public void onClick(View v) {
                // チェックボックスのチェック状態を取得します
                boolean checked = check.isChecked();

                if(checked==true)
                {
                    yesterday_flag =1;
                }
                else
                {
                    yesterday_flag =0;
                }
            }
        });
    }
    //TodayCheck判定処理
    public void TodayJudge(final CheckBox check)
    {
        // チェックボックスがクリックされた時に呼び出されるコールバックリスナーを登録します
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            // チェックボックスがクリックされた時に呼び出されます
            public void onClick(View v) {
                // チェックボックスのチェック状態を取得します
                boolean checked = check.isChecked();

                if(checked==true)
                {
                    today_flag =1;
                }
                else
                {
                    today_flag =0;
                }
            }
        });
    }

    //checkboxの中身を判断してtruefalse変更
    public void CheckBoxChange(CheckBox Cb,int check)
    {
        if(check==0)
        {
            Cb.setChecked(false);
        }
        else
        {
            Cb.setChecked(true);
        }
    }


    // プリファレンス保存
// aaa,bbb,ccc... の文字列で保存
    private void saveArray(ArrayList<String> array, String PrefKey){
        String str = "";
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
//    private void saveArray(ArrayList<String> array, String PrefKey){
//        String str = null;
//        for (int i =0;i<array.size();i++){
//            str = array.get(i);
//            if (i !=array.size()-1){
//                str = ",";
//            }
//        }
//        SharedPreferences prefs1 = getSharedPreferences("Array", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs1.edit();
//        editor.putString(PrefKey, str).commit();
//
//        StringBuffer buffer = new StringBuffer();
//        String stringItem = null;
//        for(String item : array){
//            buffer.append(item+",");
//        };
//        if(buffer != null){
//            String buf = buffer.toString();
//            stringItem = buf.substring(0, buf.length() - 1);
//
//            SharedPreferences prefs1 = getSharedPreferences("Array", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = prefs1.edit();
//            editor.putString(PrefKey, stringItem).commit();
//        }
//    }

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


    public void ViewRotate()
    {
        float width;
        // ウィンドウマネージャのインスタンス取得
        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        // ディスプレイのインスタンス生成
        Display disp = wm.getDefaultDisplay();
        width = disp.getWidth();

        float factor;
        mat = new Matrix();
        mat.reset();
        switch(orientation)
        {
            case 1://only scaling
                factor = (float)width/(float)pctWidth;
                mat.preScale(factor, factor);
//                lp.width = (int)(wOrg*factor);
//                lp.height = (int)(hOrg*factor);
                break;
            case 2://flip vertical
                factor = (float)width/(float)pctWidth;
                mat.postScale(factor, -factor);
                mat.postTranslate(0, pctHeight*factor);
//                lp.width = (int)(wOrg*factor);
//                lp.height = (int)(hOrg*factor);
                break;
            case 3://rotate 180
                mat.postRotate(180, pctWidth/2f, pctHeight/2f);
                factor = (float)width/(float)pctWidth;
                mat.postScale(factor, factor);
//                lp.width = (int)(wOrg*factor);
//                lp.height = (int)(hOrg*factor);
                break;
            case 4://flip horizontal
                factor = (float)width/(float)pctWidth;
                mat.postScale(-factor, factor);
                mat.postTranslate(pctWidth*factor, 0);
//                lp.width = (int)(wOrg*factor);
//                lp.height = (int)(hOrg*factor);
                break;
            case 5://flip vertical rotate270
                mat.postRotate(270, 0, 0);
                factor = (float)width/(float)pctHeight;
                mat.postScale(factor, -factor);
//                lp.width = (int)(hOrg*factor);
//                lp.height = (int)(wOrg*factor);
                break;
            case 6://rotate 90
                mat.postRotate(90, 0, 0);
                factor = (float)width/(float)pctHeight;
                mat.postScale(factor, factor);
                mat.postTranslate(pctHeight*factor, 0);
//                lp.width = (int)(hOrg*factor);
//                lp.height = (int)(wOrg*factor);
                break;
            case 7://flip vertical, rotate 90
                mat.postRotate(90, 0, 0);
                factor = (float)width/(float)pctHeight;
                mat.postScale(factor, -factor);
                mat.postTranslate(pctHeight*factor, pctWidth*factor);
//                lp.width = (int)(hOrg*factor);
//                lp.height = (int)(wOrg*factor);
                break;
            case 8://rotate 270
                mat.postRotate(270, 0, 0);
                factor = (float)width/(float)pctHeight;
                mat.postScale(factor, factor);
                mat.postTranslate(0, pctWidth*factor);
//                lp.width = (int)(hOrg*factor);
//                lp.height = (int)(wOrg*factor);
                break;
        }
    }

    //年のみ算出
    public int YearLoad(int Birthday)
    {
        int caly;

        caly=Birthday/10000;

        return caly;
    }

    //月のみ算出
    public int MonthLoad(int Birthday)
    {
        int caly,calm,cal;

        caly=Birthday/10000;

        calm=Birthday/100;
        cal=caly*100;
        calm=calm-cal;

        return calm+1;
    }

    //日のみ算出
    public int DayLoad(int Birthday)
    {
        int caly,calm,cald,cal;

        caly=Birthday/10000;

        calm=Birthday/100;
        cal=caly*100;
        calm=calm-cal;

        cal=caly*10000;
        cald=Birthday-cal;
        cal=calm*100;
        cald=cald-cal;

        return cald;
    }


    public void AllRegist()
    {
        //Data型の宣言
        Data allData =new Data();
        //Data型にデータをセット
        allData.setName(edit_name.getText().toString());
        allData.setKana(edit_pho.getText().toString());

        allData.setYear(YearLoad(birthday));
        allData.setMonth(MonthLoad(birthday));
        allData.setDay(DayLoad(birthday));

        allData.setCategory(user_category);
        allData.setTwitterID(edit_twitter.getText().toString());
        allData.setMemo(edit_memo.getText().toString());
        allData.setImage(img_name+".jpg");
        allData.setSmallImage(img_name+".jpg");
        allData.setYukarin(tamura_flag);
        allData.setNotif_yest(yesterday_flag);
        allData.setNotif_today(today_flag);
        allData.setNotif_day(days_ago);
        allData.setNotif_recy(reptition_loop);
        //dbに書き込み
        dbAssist.insertData(allData,this);

        // プレファレンスに保存
        //saveArray(arraylist,"StringItem");

        //新規作成か編集かによって画面切り替え場所の変更
        if(id==0) {
            //MainへGo!
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else
        {
            //DetailへGo!
            Intent intent = new Intent(this, UserDetailActivity.class);
            startActivity(intent);
        }

        ALLLOG();
    }

    //Log
    public void ALLLOG()
    {
        Log.d("ALLLOG",edit_name.getText().toString());
        Log.d("ALLLOG",edit_pho.getText().toString());
        Log.d("ALLLOG",String.valueOf(birthday+100));
        Log.d("ALLLOG",user_category);
        Log.d("ALLLOG",edit_twitter.getText().toString());
        Log.d("ALLLOG",edit_memo.getText().toString());
        Log.d("ALLLOG",img_name+".jpg");
        Log.d("ALLLOG",img_name+".jpg");
        Log.d("ALLLOG",String.valueOf(tamura_flag));
        Log.d("ALLLOG",String.valueOf(yesterday_flag));
        Log.d("ALLLOG",String.valueOf(today_flag));
        Log.d("ALLLOG",String.valueOf(days_ago));
        Log.d("ALLLOG",String.valueOf(reptition_loop));
    }

    //////////////////////////////////////////////////

}