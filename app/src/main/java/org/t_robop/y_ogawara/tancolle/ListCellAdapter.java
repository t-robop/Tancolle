package org.t_robop.y_ogawara.tancolle;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yuusuke on 16/06/27.
 */
public class ListCellAdapter extends ArrayAdapter<ListCellData> {

    private LayoutInflater layoutInflater;
    public ListCellAdapter(Context c, int id, ArrayList<ListCellData> ListCellArray) {
        super(c, id, ListCellArray);
        this.layoutInflater = (LayoutInflater) c.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
        );
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.cell, parent, false);
        }

        ListCellData cellData = (ListCellData) getItem(position);
        ////((LinearLayout) convertView.findViewById(R.id.icon)).setBackgroundResource(cellData.getBitmap1());
        ((TextView) convertView.findViewById(R.id.name1)).setText(cellData.getName1());
        ((TextView) convertView.findViewById(R.id.birthday1)).setText(cellData.getBirthday1());
        ((TextView) convertView.findViewById(R.id.name2)).setText(cellData.getName2());
        ((TextView) convertView.findViewById(R.id.birthday2)).setText(cellData.getBirthday2());
        ((TextView) convertView.findViewById(R.id.name3)).setText(cellData.getName3());
        ((TextView) convertView.findViewById(R.id.birthday3)).setText(cellData.getBirthday3());

        //Buttonをおした時と、画像の表示処理
        LinearLayout linearLayout1 = (LinearLayout) convertView .findViewById(R.id.liner1);
        BitmapDrawable background1 = new BitmapDrawable(cellData.getBitmap1());
        linearLayout1.setBackgroundDrawable(background1);

        //Buttonをおした時と、画像の表示処理
        LinearLayout linearLayout2 = (LinearLayout) convertView .findViewById(R.id.liner2);
        BitmapDrawable background2 = new BitmapDrawable(cellData.getBitmap2());
        linearLayout2.setBackgroundDrawable(background2);

        //Buttonをおした時と、画像の表示処理
        LinearLayout linearLayout3 = (LinearLayout) convertView .findViewById(R.id.liner3);
        BitmapDrawable background3 = new BitmapDrawable(cellData.getBitmap3());
        linearLayout3.setBackgroundDrawable(background3);


        //TODO ここで、固有IDをセットして上げて、それをそのままintentで送れば・・・？
        //そうすると、ListCellData拡張してIDも入るように
        linearLayout1.setTag(cellData.getId1());
        linearLayout2.setTag(cellData.getId2());
        linearLayout3.setTag(cellData.getId3());

        return convertView;
    }

}