package org.t_robop.y_ogawara.tancolle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class ReUserRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register_re);

         //名前入力欄の関連付け
        EditText editName = (EditText) findViewById(R.id.EditName);
        //振り仮名入力欄の関連付け
        EditText editPho = (EditText) findViewById(R.id.EditPho);
    }
}
