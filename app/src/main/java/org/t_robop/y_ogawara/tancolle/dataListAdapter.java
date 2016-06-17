package org.t_robop.y_ogawara.tancolle;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

//TODO クラスの中にクラスは作れません
//独自Adapterつくる
public class dataListAdapter extends ArrayAdapter<ListItem> {

    int itemId;
    private List<ListItem> items;
    LayoutInflater inflater;

    public dataListAdapter(Context context, int itemId, List<ListItem> items) {
        super(context, itemId,items);

        this.itemId = itemId;
        this.items = items;
         this.inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    //1行1行表示させる
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //convertViewを使うことでViewを再利用
        View view;
        if (convertView != null) {
            view = convertView;
        }
        else {
            view = this.inflater.inflate(this.itemId,null);
        }

        ListItem item = this.items.get(position);

        TextView tv = (TextView) view.findViewById(R.id.item_text);
        tv.setText(item.getName());

        return view;
    }

}
