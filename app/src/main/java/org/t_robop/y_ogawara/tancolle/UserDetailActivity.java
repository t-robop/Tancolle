package org.t_robop.y_ogawara.tancolle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UserDetailActivity extends AppCompatActivity {

    int intentId;
    TextView nameTV;
    TextView kanaTV;
    TextView birthTV;
    TextView ageTV;
    TextView remaTV;
    TextView memoTV;
    Calendar calendar;
    int year,month,day ; //現在の日付
    int a,b,c;
    int age;//年齢の計算結果を入れる箱


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        Intent intent = getIntent();
        intentId = intent.getIntExtra("id",1);

        nameTV = (TextView) findViewById(R.id.Name);
        kanaTV = (TextView) findViewById(R.id.Kana);
        birthTV = (TextView) findViewById(R.id.Birthay);
        ageTV = (TextView) findViewById(R.id.age);
        remaTV = (TextView) findViewById(R.id.nokori);
        memoTV = (TextView) findViewById(R.id.chou);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1; //0から11までしかないから１個足す
        day = calendar.get(Calendar.DAY_OF_MONTH);



        Data data =  dbAssist.idSelect(intentId,this);
        String name = data.getName();
        String kana = data.getKana();
        int birth = data.getBirthday();
        int sqlDay;


        a=birth/10000;  //19970714→1997みたいね年度だけにする
        b=birth%10000;  //誕生日を７月１４日を→７１４みたいな形に
        c=month*100+day; //現在の日付を６月１５日→６１５みたいな形に
        sqlDay = b%100;
        if(b>c){  //もし誕生日の方の数値が大きかったら（まだ今年の誕生日がきてなかったら）
            age=year-a-1;   //今年ー誕生年から更に１才ひく
        }else{    //今年の誕生日がきていたら
            age=year-a;  //そのまんま今年から誕生年をひく
        }

        //データのフォーマット
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        //date型の宣言
        Date dateTo = null;
        Date dateFrom = null;

        // 日付を作成します。
        try {
            //指定フォーマットでデータを入力
            //TODO ココらへんで今年の誕生日が過ぎていたら、来年の誕生日で計算させる
            dateFrom = sdf.parse(year+"/"+month+"/"+day);
            dateTo = sdf.parse(year+"/"+b/100+"/"+sqlDay);
            //エラーが起きた時
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 日付をlong値に変換します。
        long dateTimeTo = dateTo.getTime();
        long dateTimeFrom = dateFrom.getTime();

        // 差分の日数を算出します。
        long dayDiff = ( dateTimeTo - dateTimeFrom  ) / (1000 * 60 * 60 * 24 );
        //int型に変換
        int remDay = (int) dayDiff;


        nameTV.setText(name);
        kanaTV.setText(kana);
        birthTV.setText(String.valueOf(b/100)+"/"+String.valueOf(b%100));
        ageTV.setText(String.valueOf(age)+"才");
        remaTV.setText(String.valueOf(remDay));





    }
}
