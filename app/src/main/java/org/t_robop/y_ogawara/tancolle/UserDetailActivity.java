package org.t_robop.y_ogawara.tancolle;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    int year, month, day; //現在の日付
    int a, b, c;
    int age;//年齢の計算結果を入れる箱
    ImageView image;
    int imagecount = 1;

    String memo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        Intent intent = getIntent();
        intentId = intent.getIntExtra("id", 1);

        nameTV = (TextView) findViewById(R.id.Name);
        kanaTV = (TextView) findViewById(R.id.Kana);
        birthTV = (TextView) findViewById(R.id.Birthay);
        ageTV = (TextView) findViewById(R.id.age);
        remaTV = (TextView) findViewById(R.id.nokori);
        memoTV = (TextView) findViewById(R.id.chou);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1; //0から11までしかないから１個足す
        day = calendar.get(Calendar.DAY_OF_MONTH);

        image = (ImageView) this.findViewById(R.id.present);
        //データを読みだして、その値でセットする画像を変える
        image.setImageResource(R.drawable.cat1);

        Data data = dbAssist.idSelect(intentId, this);
        String name = data.getName(); //SQliteからもってくる
        String kana = data.getKana();
        memo = data.getMemo();
        int birth = data.getBirthday();
        int sqlDay;


        a = birth / 10000;  //19970714→1997みたいね年度だけにする
        b = birth % 10000;  //誕生日を７月１４日を→７１４みたいな形に
        c = month * 100 + day; //現在の日付を６月１５日→６１５みたいな形に
        sqlDay = b % 100; //現在の日付を割り出す
        if (b > c) {  //もし誕生日の方の数値が大きかったら（まだ今年の誕生日がきてなかったら）
            age = year - a - 1;   //今年ー誕生年から更に１才ひく
        } else {    //今年の誕生日がきていたら
            age = year - a;  //そのまんま今年から誕生年をひく
        }

        //データのフォーマット
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        //date型の宣言
        Date dateTo = null;
        Date dateFrom = null;

        // 日付を作成します。
        try {
            //TODO ココらへんで今年の誕生日が過ぎていたら、来年の誕生日で計算させる
            dateFrom = sdf.parse(year + "/" + month + "/" + day);
            //指定フォーマットでデータを入力
            if (b < c) {  //誕生日より今日の日にちが大きかったら（もう誕生日がきていたら
                int num;
                num = year + 1; //＋１して来年の誕生日の数値を割り出す

                dateTo = sdf.parse(num + "/" + b / 100 + "/" + sqlDay);
            } else {  //まだ誕生日がきていなかったら

                dateTo = sdf.parse(year + "/" + b / 100 + "/" + sqlDay); //今年の誕生日を作る
            }

            //エラーが起きた時
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 日付をlong値に変換します。
        long dateTimeTo = dateTo.getTime();
        long dateTimeFrom = dateFrom.getTime();

        // 差分の日数を算出します。
        long dayDiff = (dateTimeTo - dateTimeFrom) / (1000 * 60 * 60 * 24);
        //int型に変換
        int remDay = (int) dayDiff;


        nameTV.setText(name);
        kanaTV.setText(kana);
        birthTV.setText(String.valueOf(b / 100) + "/" + String.valueOf(b % 100));
        ageTV.setText(String.valueOf(age) + "才");
        remaTV.setText("残り" + String.valueOf(remDay) + "日");
        memoTV.setText(memo);

    }

    public void memoclick(View view) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View dialog_view = inflater.inflate(R.layout.dialog_layout, null);
        //editTextを生成
        final EditText editText = new EditText(this);
        //EditTextの中身を指定
        editText.setText(memo);
        //レイアウトファイルからビューを取得

        //レイアウト、題名、OKボタンとキャンセルボタンをつけてダイアログ作成
        builder.setView(dialog_view)
                .setTitle("Memo")
                //ここで値をセットする
                .setView(editText)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                    /*OKのときの処理内容*/

                                String string = editText.getText().toString();
                                Data updateData =new Data();
                                updateData.setMemo(string);
                                //引数はid,data,場所
                                dbAssist.updateData(intentId,updateData,UserDetailActivity.this);

                                //memo画面の更新
                                Data data = dbAssist.idSelect(intentId, UserDetailActivity.this);
                                memo = data.getMemo(); //SQliteからもってくる
                                memoTV.setText(memo);



                                //Log.d("sssss",string);
                            }
                        })
                .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    /*キャンセルされたときの処理*/
                    }
                });

        AlertDialog myDialog = builder.create();
        myDialog.setCanceledOnTouchOutside(false);
        //ダイアログ画面外をタッチされても消えないようにする。
        myDialog.show();
        //ダイアログ表示

    }


    public void presentClick(View view) { //プレゼントボタンをおした時
        Data presentdate =new Data();
        presentdate.setPresentFlag(imagecount);
        dbAssist.updateData(intentId,presentdate,this);

        if(imagecount == 2){
            imagecount = 1;
            image.setImageResource(R.drawable.cat1);
        }else{
            imagecount = 2;
            image.setImageResource(R.drawable.cat2);

        }






    }
}
