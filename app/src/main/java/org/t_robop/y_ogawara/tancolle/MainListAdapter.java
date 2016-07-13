package org.t_robop.y_ogawara.tancolle;

import android.content.Context;
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





        ((TextView) convertView.findViewById(R.id.name1)).setText(mainListData.getName(0));
        ((TextView) convertView.findViewById(R.id.birth1)).setText(mainListData.getBirthMonth(0)+"/"+mainListData.getBirthDay(0));
        ((TextView) convertView.findViewById(R.id.name2)).setText(mainListData.getName(1));
        ((TextView) convertView.findViewById(R.id.birth2)).setText(mainListData.getBirthMonth(1)+"/"+mainListData.getBirthDay(1));
        ((TextView) convertView.findViewById(R.id.name3)).setText(mainListData.getName(2));
        ((TextView) convertView.findViewById(R.id.birth3)).setText(mainListData.getBirthMonth(2)+"/"+mainListData.getBirthDay(2));



        //linerにtagをつけてクリックしたときにわかるように
        LinearLayout liner1 = (LinearLayout) convertView.findViewById(R.id.liner1);
        liner1.setTag(mainListData.getId(0));
        LinearLayout liner2 = (LinearLayout) convertView.findViewById(R.id.liner2);
        liner2.setTag(mainListData.getId(1));
        LinearLayout liner3 = (LinearLayout) convertView.findViewById(R.id.liner3);
        liner3.setTag(mainListData.getId(2));

        //viewを返す
        return convertView;

    }

}
