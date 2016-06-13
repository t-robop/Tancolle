package org.t_robop.y_ogawara.tancolle;

import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class UserRegisterActivity extends AppCompatActivity {

    TextView NameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        NameView=(TextView)findViewById(R.id.NameView);

        NameView.setPaintFlags(NameView.getPaintFlags()
                | Paint.UNDERLINE_TEXT_FLAG);
    }
}
