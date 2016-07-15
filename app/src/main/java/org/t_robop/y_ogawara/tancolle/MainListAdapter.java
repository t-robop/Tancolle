package org.t_robop.y_ogawara.tancolle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

        MainAdapterData mainListData;
        if (convertView == null) {
            convertView = layoutInflater.inflate(
                    R.layout.mainlistitem,
                    parent,
                    false
            );

        }

        //データを各idへセット
        mainListData = getItem(position);






        //linerにtagをつけてクリックしたときにわかるように
        LinearLayout liner1 = (LinearLayout) convertView.findViewById(R.id.liner1);
        liner1.setTag(mainListData.getId(0));
        LinearLayout liner2 = (LinearLayout) convertView.findViewById(R.id.liner2);
        liner2.setTag(mainListData.getId(1));
        LinearLayout liner3 = (LinearLayout) convertView.findViewById(R.id.liner3);
        liner3.setTag(mainListData.getId(2));



        TextView nameText1 = ((TextView) convertView.findViewById(R.id.name1));
        nameText1.setText(mainListData.getName(0));
        ((TextView) convertView.findViewById(R.id.birth1)).setText(mainListData.getBirthMonth(0)+"/"+mainListData.getBirthDay(0));
        TextView nameText2 = ((TextView) convertView.findViewById(R.id.name2));

        TextView birthText2 = ((TextView) convertView.findViewById(R.id.birth2));
        TextView nameText3 = ((TextView) convertView.findViewById(R.id.name3));

        TextView birthText3 = ((TextView) convertView.findViewById(R.id.birth3));


        //if (mainListData.getName(1)==null){
            nameText2.setText(mainListData.getName(1));
            if (mainListData.getName(1)!=null){
                birthText2 .setText(mainListData.getBirthMonth(1)+"/"+mainListData.getBirthDay(1));
            }

            // }
        //  if (mainListData.getName(2)==null){
            nameText3.setText(mainListData.getName(2));
        if (mainListData.getName(2)!=null){
            birthText3.setText(mainListData.getBirthMonth(2)+"/"+mainListData.getBirthDay(2));
        }

            //}

        //if (mainListData.getName(1)==null){
            //liner2.removeAllViews();
            //}
       // if (mainListData.getName(2)==null){
            //liner3.removeAllViews();
        //}


        //viewを返す
        return convertView;

    }

}
