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
public class MainListAdapter extends ArrayAdapter<MainListData> {


    private LayoutInflater layoutInflater;

    public MainListAdapter(Context c, int id, ArrayList<MainListData> MainListArray) {
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
        MainListData mainListData = getItem(position);
        ((TextView) convertView.findViewById(R.id.name1)).setText(mainListData.getName1());
        ((TextView) convertView.findViewById(R.id.birth1)).setText(mainListData.getBirth1());
        ((TextView) convertView.findViewById(R.id.name2)).setText(mainListData.getName2());
        ((TextView) convertView.findViewById(R.id.birth2)).setText(mainListData.getBirth2());
        ((TextView) convertView.findViewById(R.id.name3)).setText(mainListData.getName3());
        ((TextView) convertView.findViewById(R.id.birth3)).setText(mainListData.getBirth3());

        //linerにtagをつけてクリックしたときにわかるように
        LinearLayout liner1 = (LinearLayout) convertView.findViewById(R.id.liner1);
        liner1.setTag(position);
        LinearLayout liner2 = (LinearLayout) convertView.findViewById(R.id.liner2);
        liner2.setTag(position);
        LinearLayout liner3 = (LinearLayout) convertView.findViewById(R.id.liner3);
        liner3.setTag(position);

        //viewを返す
        return convertView;

    }

}
