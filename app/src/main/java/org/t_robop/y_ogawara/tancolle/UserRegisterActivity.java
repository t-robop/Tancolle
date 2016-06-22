package org.t_robop.y_ogawara.tancolle;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class UserRegisterActivity extends AppCompatActivity {

    EditText edit_name;
    EditText edit_pho;
    EditText edit_twitter;
    EditText edit_days_ago;

    InputMethodManager inputMethodManager;



    //sppinerTest
    private Spinner tamura;
    private String spinnerTamuraItems[] = {"田村", "ゆかりん", "40歳"};
    private TextView TamuraView;

    private Spinner repetition;
    private String spinnerRepetitionItems[] = {"繰り返し通知無し", "毎日", "一週間毎","一ヶ月毎"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        //数多（予定）の宣言
        TamuraView = (TextView)findViewById(R.id.tamura);
        tamura = (Spinner)findViewById(R.id.spinner1);
        repetition=(Spinner)findViewById(R.id.spinner2);

        //Spinner設定
        sppinerDraw(tamura,TamuraView,spinnerTamuraItems);
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


    }

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


    //spinnerとTextView使おうぜ！！（セットするspinner,セットするTextView,セットするString）
    public void sppinerDraw(Spinner nSpinner, final TextView textView, final String spinnerItems[])
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

                if (item.equals(spinnerItems[1])) {
                    textView.setText(spinnerItems[1]);
                } else if (item.equals(spinnerItems[2])) {
                    textView.setText(spinnerItems[2]);
                } else {
                    textView.setText(spinnerItems[0]);
                }
            }

            //　アイテムが選択されなかった
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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

}
