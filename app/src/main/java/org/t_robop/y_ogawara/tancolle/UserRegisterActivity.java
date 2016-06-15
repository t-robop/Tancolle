package org.t_robop.y_ogawara.tancolle;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class UserRegisterActivity extends AppCompatActivity {

    TextView NameView;
    EditText edit_name;
    EditText edit_pho;

    InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        edit_name=(EditText)findViewById(R.id.EditName);
        // EditText が空のときに表示させるヒントを設定
        edit_name.setHint("Name");

        edit_pho=(EditText)findViewById(R.id.EditPho);
        // EditText が空のときに表示させるヒントを設定
        edit_pho.setHint("Phonetic");


        //キーボード表示を制御するためのオブジェクト
        inputMethodManager =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        EditTextSet(edit_name);

        EditTextSet(edit_pho);


    }

    public void EditName(View v)
    {
        EditTextClick(edit_name);
        inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }

    public void EditPho(View v)
    {
        EditTextClick(edit_pho);
        inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);
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

    public void EditTextClick(EditText edit)
    {
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
        edit.requestFocus();
        edit.getEditableText().clear();//editTextの初期化
    }

}
