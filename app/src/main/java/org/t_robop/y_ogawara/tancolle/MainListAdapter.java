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



        TextView nameText1 = ((TextView) convertView.findViewById(R.id.name1));
        nameText1.setText(mainListData.getName(0));
        ((TextView) convertView.findViewById(R.id.birth1)).setText(mainListData.getBirthMonth(0)+"/"+mainListData.getBirthDay(0));

        TextView nameText2 = ((TextView) convertView.findViewById(R.id.name2));
        TextView birthText2 = ((TextView) convertView.findViewById(R.id.birth2));

        TextView nameText3 = ((TextView) convertView.findViewById(R.id.name3));
        TextView birthText3 = ((TextView) convertView.findViewById(R.id.birth3));


        nameText2.setText(mainListData.getName(1));
//        if (mainListData.getName(1)!=null){
//            birthText2 .setText(mainListData.getBirthMonth(1)+"/"+mainListData.getBirthDay(1));
//        }
        birthText2.setText(mainListData.getBirthMonth(1)+"/"+mainListData.getBirthDay(1));
        nameText3.setText(mainListData.getName(2));
        birthText3.setText(mainListData.getBirthMonth(2)+"/"+mainListData.getBirthDay(2));
        //listの行数が、最終行だったとき
        //if (position!=0){
        if(mainListData.getAllSize()==1||mainListData.getAllSize()==2) {
          //  if (position == mainListData.getAllSize() / 3) {
                switch (mainListData.getSize()) {
                    case 0:
                        liner2.setVisibility(View.INVISIBLE);
                    case 1:
                        liner3.setVisibility(View.INVISIBLE);
                        break;
                }
          //  }
        }
//        else{
//           // if (position == mainListData.getAllSize() / 3) {
//            switch (mainListData.getSize()) {
//                case 0:
//                    liner2.setVisibility(View.INVISIBLE);
//                case 1:
//                    liner3.setVisibility(View.INVISIBLE);
//                    break;
//            }
          //  }
       // }
        //}
//        else{
//                switch (mainListData.getSize()){
//                    case 1:
//                        liner2.setVisibility(View.INVISIBLE);
//                    case 2:
//                        liner3.setVisibility(View.INVISIBLE);
//                        break;
//            }
//        }

//        if (mainListData.getName(2)!=null){
//            birthText3.setText(mainListData.getBirthMonth(2)+"/"+mainListData.getBirthDay(2));
//        }

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
