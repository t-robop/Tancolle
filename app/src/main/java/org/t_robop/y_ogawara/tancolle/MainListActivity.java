package org.t_robop.y_ogawara.tancolle;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by iris on 2016/07/06.
 */
public class MainListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);
        ListView listView2 = (ListView) findViewById(R.id.listView1);

        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.add);
        if (add != null) {
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainListActivity.this, UserRegisterActivity.class);
                    startActivity(intent);
//                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
                }
            });
        }


//        ArrayList<MainListData> MainListArray = new ArrayList<>();
//
//b
        // データを突っ込む
//        for (int i =1;i<3;i=i+3){
//            MainListData MainList = new MainListData();
//            MainList.setBirth1("6/10");
//            MainList.setBirth2("1/2");
//            MainList.setBirth3("12/25");
//            MainList.setName1("西村");
//            MainList.setName2("いか");
//            MainList.setName3("キリスト");
//
//
//            MainListArray.add(MainList);
//
//        }
//
        Data testData = new Data();

        //Data型にデータをセット
        testData.setName("西村1111");
        testData.setKana("にしむら");
        testData.setBirthday(19970616);
        testData.setYear(1997);
        testData.setMonth(7);
        testData.setDay(16);
        testData.setCategory("友達");
        testData.setTwitterID("Taiga_Natto");
        testData.setMemo("教科書を見て実装して欲しい");
        testData.setImage("Imageデータ");
        testData.setSmallImage("Imageデータ");
        testData.setPresentFlag(0);
        testData.setYukarin(1);
        testData.setNotif_yest(1);
        testData.setNotif_today(1);
        testData.setNotif_day(3);
        testData.setNotif_recy(3);
        //dbに書き込み
        dbAssist.insertData(testData, this);

        //Data型にデータをセット
        testData.setName("西村22222");
        testData.setKana("にしむら");
        testData.setBirthday(19970616);
        testData.setYear(1997);
        testData.setMonth(7);
        testData.setDay(16);
        testData.setCategory("友達");
        testData.setTwitterID("Taiga_Natto");
        testData.setMemo("教科書を見て実装して欲しい");
        testData.setImage("Imageデータ");
        testData.setSmallImage("Imageデータ");
        testData.setPresentFlag(0);
        testData.setYukarin(1);
        testData.setNotif_yest(1);
        testData.setNotif_today(1);
        testData.setNotif_day(3);
        testData.setNotif_recy(3);
        //dbに書き込み
        dbAssist.insertData(testData, this);


        //Data型にデータをセット
        testData.setName("西村33333");
        testData.setKana("にしむら");
        testData.setBirthday(19970616);
        testData.setYear(1997);
        testData.setMonth(7);
        testData.setDay(16);
        testData.setCategory("友達");
        testData.setTwitterID("Taiga_Natto");
        testData.setMemo("教科書を見て実装して欲しい");
        testData.setImage("Imageデータ");
        testData.setSmallImage("Imageデータ");
        testData.setPresentFlag(0);
        testData.setYukarin(1);
        testData.setNotif_yest(1);
        testData.setNotif_today(1);
        testData.setNotif_day(3);
        testData.setNotif_recy(3);
        //dbに書き込み
        dbAssist.insertData(testData, this);


        //Data型にデータをセット
        testData.setName("西村44444");
        testData.setKana("にしむら");
        testData.setBirthday(19970616);
        testData.setYear(1997);
        testData.setMonth(7);
        testData.setDay(16);
        testData.setCategory("友達");
        testData.setTwitterID("Taiga_Natto");
        testData.setMemo("教科書を見て実装して欲しい");
        testData.setImage("Imageデータ");
        testData.setSmallImage("Imageデータ");
        testData.setPresentFlag(0);
        testData.setYukarin(1);
        testData.setNotif_yest(1);
        testData.setNotif_today(1);
        testData.setNotif_day(3);
        testData.setNotif_recy(3);
        //dbに書き込み
        dbAssist.insertData(testData, this);
        //adapterDataセット
        int month = 7;//とりあえず7月でプレイ(ここらへんで月の指定お願いします)

        ArrayList<Data> monthTurnData;//ArrayListの宣言

        monthTurnData = dbAssist.birthdaySelect(month, this);//ArrayListに月検索したデータをぶち込む

        MainAdapterData Mad;//自分で作成したclassの宣言

        ArrayList<MainAdapterData> adapterData = new ArrayList<>();//classのArrayListの作成


        int num = monthTurnData.size();//int型変数numにmonthTurnDataの配列数を入れる

        //読み込んだ月のデータの数だけ回す。（3分の1でいいのと、後述のListデータの取得に使うため+3）
        for (int j = 0; j < monthTurnData.size(); j = j + 3) {
            //三人分だけ保存するため3回回す。
           // Mad.startMad();//クラスの変数の初期化
            Mad = new MainAdapterData();//自分で作成したclassの宣言

            for (int i = 0; i < 3; i++) {
                Data getData;//monthTurnData取得用のデータ型

                Log.d("aaaa", String.valueOf(i + j));

                if (i <= num)//iとnumを比較してiの方が低い時だけ（データ無いのに取得しようとして落ちるやつの修正）
                {
                    getData = monthTurnData.get(i + j);//読み込んだListの要素を取得

                    Mad.setId(i, getData.getId());//idのセット
                    Mad.setName(i, getData.getName());//名前のセット
                    Mad.setBirthMonth(i, getData.getMonth());//誕生月のセット
                    Mad.setBirthDay(i, getData.getDay());//誕生日のセット
                    Mad.setPresentFlag(i, getData.isPresentFlag());//プレゼントフラグのセット
                }
                num = num - 1;//ここでnumから今セットした三人分だけ引く
            }

            //この辺に書き込み処理書いてくらさい。

            adapterData.add(Mad);//三人のデータの追加

        }

        //Adapterをセット
        MainListAdapter adapter = new MainListAdapter(this, 0, adapterData);
        //listView.setEmptyView(findViewById(R.id.listView));
        listView2.setAdapter(adapter);


//        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void listClick(AdapterView parent, View view, int position, long id) {
//                //ここに書く
//                //listViewのitemを取得してadapterからItemをもらってくる
//                ListView listView = (ListView) parent;
//                listView.getItemAtPosition(position);
//
//                ListItem listItem = (ListItem) listView.getAdapter().getItem(position);
//
//                //Intentで飛ばす＆idをキーにする
//                Intent intent = new Intent(MainListActivity.this, UserDetailActivity.class);
//                //intent.putExtra("id", MainAdapterData.getId());
//                startActivity(intent);
//
//                //トースト
//                //Toast.makeText(SearchActivity.this, "Click: " + item, Toast.LENGTH_LONG).show();
//
////            }
//        });
    }

    //リストをクリックした時のイベント
    public void listClick(View view) {
        Log.d("hello", String.valueOf(view.getTag()));


//        //listViewのitemを取得してadapterからItemをもらってくる
//        ListView listView = (ListView) parent;
//        listView.getItemAtPosition(position);

        int numData = new Integer((Integer) view.getTag());

        if (numData != 0){
            //Intentで飛ばす＆idをキーにする
            Intent intent = new Intent(MainListActivity.this, UserDetailActivity.class);
            intent.putExtra("id", numData);
            startActivity(intent);
        }



    }
}
