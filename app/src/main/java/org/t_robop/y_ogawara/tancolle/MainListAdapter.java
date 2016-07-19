package org.t_robop.y_ogawara.tancolle;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by iris on 2016/07/01.
 */
public class MainListAdapter extends ArrayAdapter<MainAdapterData> {
    
    private LayoutInflater layoutInflater;

    public MainListAdapter(Context c, int id, ArrayList<MainAdapterData> MainListArray) {
        super(c, id, MainListArray);
        this.layoutInflater = (LayoutInflater) c.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
        );
    }

    //viewを使い回すのにinflaterを使う(お決まりのやつ)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(
                    R.layout.mainlistitem,
                    parent,
                    false
            );
        }

        //データを各idへセット
        MainAdapterData mainListData = getItem(position);

        //linerにtagをつけてクリックしたときにわかるように
        LinearLayout liner1 = (LinearLayout) convertView.findViewById(R.id.liner1);
        liner1.setTag(mainListData.getId(0));
        LinearLayout liner2 = (LinearLayout) convertView.findViewById(R.id.liner2);
        liner2.setTag(mainListData.getId(1));
        LinearLayout liner3 = (LinearLayout) convertView.findViewById(R.id.liner3);
        liner3.setTag(mainListData.getId(2));

        //ListView用のTextView1(左端)の宣言
        //一つ目（左端）は桁が表示されれば絶対表示されるので固定
        TextView nameText1 = ((TextView) convertView.findViewById(R.id.name1));
        nameText1.setText(mainListData.getName(0));
        ((TextView) convertView.findViewById(R.id.birth1)).setText(mainListData.getBirthMonth(0)+"/"+mainListData.getBirthDay(0));

        //ListView用のTextView2(中央)の宣言
        TextView nameText2 = ((TextView) convertView.findViewById(R.id.name2));
        TextView birthText2 = ((TextView) convertView.findViewById(R.id.birth2));

        //ListView用のTextView3(右端)の宣言
        TextView nameText3 = ((TextView) convertView.findViewById(R.id.name3));
        TextView birthText3 = ((TextView) convertView.findViewById(R.id.birth3));


        //listの行数が最終行だったとき
          if (position == mainListData.getAllSize() / 3) {
              //いくつ余ってるかの分岐
                switch (mainListData.getAllSize()%3) {
                    case 2://一つ無いとき
                        //中央のTextだけ表示させます
                        nameText2.setText(mainListData.getName(1));
                        birthText2.setText(mainListData.getBirthMonth(1)+"/"+mainListData.getBirthDay(1));
                        break;
                    case 1://二つ無いとき
                        //何も表示させません
                        break;
                }
            }
        else{
              //最終行ではないので全て表示させます

              nameText2.setText(mainListData.getName(1));
              birthText2.setText(mainListData.getBirthMonth(1)+"/"+mainListData.getBirthDay(1));

              nameText3.setText(mainListData.getName(2));
              birthText3.setText(mainListData.getBirthMonth(2)+"/"+mainListData.getBirthDay(2));
          }

        //viewを返す
        return convertView;

    }

}
