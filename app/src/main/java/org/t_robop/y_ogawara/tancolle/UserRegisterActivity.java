package org.t_robop.y_ogawara.tancolle;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.Calendar;
import java.util.StringTokenizer;

public class UserRegisterActivity extends AppCompatActivity {
    private static final int REQUEST_GALLERY = 0;//ギャラリー選択で必要な初期化

    EditText edit_name;
    EditText edit_pho;
    EditText edit_twitter;
    EditText edit_days_ago;

    ImageView user_view;

    TextView user_birthday;

    InputMethodManager inputMethodManager;


    DatePickerDialog datePickerDialog;
//    int rema;
//    int nowM;
//    int nowD;
    Calendar calendar;
    Calendar nowCale;
    Calendar birthCale;

    int y;
    int m;
    int d;

    int birthday;



    //sppinerTest
    private Spinner spinnerCategory;

    private Spinner repetition;
    private String spinnerRepetitionItems[] = {"繰り返し通知無し", "毎日", "一週間毎","一ヶ月毎"};


    private AlertDialog Adddialog;

    //EditText用変数群
    View viewV;
    LayoutInflater inflater;
    EditText editText;

    ArrayAdapter<String> adapter;




    /////////////////////////Override/////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        //数多（予定）の宣言
        repetition=(Spinner)findViewById(R.id.spinner2);

        //Spinner設定
        sppinerCategory();
        sppinerSet(repetition,spinnerRepetitionItems);

        edit_name=(EditText)findViewById(R.id.EditName);
        // EditText が空のときに表示させるヒントを設定
        edit_name.setHint("Name");

        edit_pho=(EditText)findViewById(R.id.EditPho);
        // EditText が空のときに表示させるヒントを設定
        edit_pho.setHint("Phonetic");

        edit_twitter=(EditText)findViewById(R.id.twitter);
        // EditText が空のときに表示させるヒントを設定
        edit_twitter.setHint("@");

        edit_days_ago=(EditText)findViewById(R.id.ago);
        // EditText が空のときに表示させるヒントを設定
        edit_days_ago.setHint("Days");


        //キーボード表示を制御するためのオブジェクト
        inputMethodManager =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);


        //EditTextの内容設定
        EditTextSet(edit_name);
        EditTextSet(edit_pho);
        EditTextSet(edit_twitter);
        EditTextSet(edit_days_ago);

        //ImageViewの初期設定
        user_view= (ImageView)findViewById(R.id.userview);
        user_view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        user_view.setImageResource(R.drawable.kaede);

        user_birthday=(TextView)findViewById(R.id.userbirthday);


        //誕生日初期設定
        BirthTodaySet();

        //誕生日描画
        BirthDayDraw(birthday);



        inflater = LayoutInflater.from(UserRegisterActivity.this);
        viewV = inflater.inflate(R.layout.dialog_user_register, null);
        editText = (EditText)viewV.findViewById(R.id.editText1);

    }

    //画像をドキュメントから選択からのImageViewセット
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //TODO Auto-generated method stub
        if(requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
            try {
                InputStream in = getContentResolver().openInputStream(data.getData());
                Bitmap pct = BitmapFactory.decodeStream(in);

                // 作られたサムネイルから横幅とそれに応じた高さを指定
                int pctWidth = pct.getWidth();
                int pctHeight = pct.getHeight();

                //1000以上であれば5で割らせる
                if(pctWidth>1000||pctHeight>1000) {
                    pctWidth = pctWidth / 3;
                    pctHeight = pctHeight / 3;
                }
                else//1000以下であれば2で許す
                {
                    pctWidth = pctWidth / 2;
                    pctHeight = pctHeight / 2;
                }

                // bitmapの画像を元の比率から最適化された比率で作成する
                Bitmap img = Bitmap.createScaledBitmap(pct, pctWidth, pctHeight, false);
                in.close();

                // 選択した画像を表示
                user_view.setImageBitmap(img);
                } catch (Exception e) {

                }
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
        // ギャラリー呼び出し
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    //誕生日クリック時
    public void BirthDay(View v)
    {
        DatePickerSet();
        // 日付設定ダイアログの表示
        datePickerDialog.show();
    }

    //////////////////////////////////////////////////




    ////////////////////自作関数群////////////////////

    public void EditDaysAgo()
    {
        String text;
        // エディットテキストのテキストを全選択します
        edit_days_ago.selectAll();
        // エディットテキストのテキストを取得します
        text = edit_days_ago.getText().toString();
        //editTextの初期化
        edit_days_ago.getEditableText().clear();

        //textの長さが0(何も入っていない)の場合
        if(text.length()==0)
        {

        }
        else
        {
            //textから数値のみ取り出す
            int ret = Integer.parseInt(text.replaceAll("[^0-9]",""));
            //「日前」を加えて表示
            edit_days_ago.setText(String.valueOf(ret)+"日前");
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

                    EditDaysAgo();//edit_days_ago用処理

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
        // アイテムを追加します
        adapter.add("<選択>");
        adapter.add("+");
        spinnerCategory = (Spinner) findViewById(R.id.spinner1);
        // アダプターを設定します
        spinnerCategory.setAdapter(adapter);
        // スピナーのアイテムが選択された時に呼び出されるコールバックリスナーを登録します
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //処理

                if(position==1)
                {
                    SpinnerCategoryAdd();
                    Adddialog.show();
                }
                else
                {
                    //ここでカテゴリの名前保存しよう
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


//        // ArrayAdapter
//        ArrayAdapter<String> adapter
//                = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerItems);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        // spinner に adapter をセット
//        nSpinner.setAdapter(adapter);
//
//        // リスナーを登録
//        nSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            //　アイテムが選択された時
//            public void onItemSelected(AdapterView<?> parent, View viw, int arg2, long arg3) {
//                Spinner spinner = (Spinner) parent;
//                String item = (String) spinner.getSelectedItem();
//
//                if (item.equals(spinnerItems[1])) {
//
//                } else if (item.equals(spinnerItems[2])) {
//
//                } else {
//
//                }
//            }
//
//            //　アイテムが選択されなかった
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
    }

    //spinner使おうぜ！！（セットするspinner,セットするTextView,セットするString）
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
                }
                else if (item.equals(spinnerItems[2]))
                {

                }
                else if (item.equals(spinnerItems[3]))
                {

                }
                else //初期値
                {

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

        y = calendar.get(Calendar.YEAR); // 年
        m = calendar.get(Calendar.MONTH); // 月
        d = calendar.get(Calendar.DAY_OF_MONTH); // 日

        nowCale=calendar;

        birthday=BirthDayGet(y,m,d);

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
                y = year;
                m = monthOfYear;
                d = dayOfMonth;

                //birthCale.set(y,m,d);

                birthday=BirthDayGet(y,m,d);

                //描画
                BirthDayDraw(birthday);
            }
        };
        // 日付設定ダイアログの作成・リスナの登録
        datePickerDialog = new DatePickerDialog(this, DateSetListener, y, m, d);
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

        y=caly;
        m=calm;
        d=cald;

        cal=m+1;

        user_birthday.setText(y+"/"+cal+"/"+d);
    }

    //spinnerCategory追加処理
    public void SpinnerCategoryAdd()
    {
        if(Adddialog == null)//Adddialogが作成されていない時
        {


            Adddialog = new AlertDialog.Builder(UserRegisterActivity.this)
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

                            //adapter更新
                            adapter.notifyDataSetChanged();

                            editText.getEditableText().clear();//editTextの初期化

                            spinnerCategory.setSelection(adapter.getPosition(addcategory));

                        }
                    })
                    .create();//初回AddDiaLog制作
        }
    }

    //////////////////////////////////////////////////

}
