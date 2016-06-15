package org.t_robop.y_ogawara.tancolle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Calendar;

public class UserDetailActivity extends AppCompatActivity {

    int intentId;
    TextView Name;
    TextView Kana;
    TextView Birth;
    TextView age;
    TextView Rema;
    TextView memo;
    Calendar calendar;
    int year ;
    int month;
    int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        Intent intent = getIntent();
        intentId = intent.getIntExtra("id",0);

        Name = (TextView) findViewById(R.id.Name);
        Kana = (TextView) findViewById(R.id.Kana);
        Birth = (TextView) findViewById(R.id.Birthay);
        age = (TextView) findViewById(R.id.age);
        Rema = (TextView) findViewById(R.id.nokori);
        memo = (TextView) findViewById(R.id.chou);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        age.setText(String.valueOf(day));
    }
}
