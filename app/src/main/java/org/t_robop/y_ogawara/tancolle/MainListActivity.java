package org.t_robop.y_ogawara.tancolle;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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
                }
            });
        }

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
    }

    //リストをクリックした時のイベント
    public void listClick(View view) {
        Log.d("hello", String.valueOf(view.getTag()));


        //listViewのitemを取得してadapterからItemをもらってくる

        int numData = new Integer((Integer) view.getTag());

        //Intentで飛ばす＆idをキーにする
        Intent intent = new Intent(MainListActivity.this, UserDetailActivity.class);
        intent.putExtra("id", numData);
        startActivity(intent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_search:
                // User chose the "Search" action, mark the current item
                // as a favorite...
                Intent intent = new Intent(this,SearchActivity.class);
                startActivity(intent);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
