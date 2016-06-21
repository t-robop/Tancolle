package org.t_robop.y_ogawara.tancolle;

import android.content.ClipData;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


//TODO クラスの中にクラスは作れません
//独自Adapterつくる
public class dataListAdapter extends ArrayAdapter<ListItem> implements Filterable {

    int itemId;
    private List<ListItem> items;
    LayoutInflater inflater;

    public dataListAdapter(Context context, int itemId, List<ListItem> items) {
        super(context, itemId, items);

        this.itemId = itemId;
        this.items = items;
        this.inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    //1行1行表示させる
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //convertViewを使うことでViewを再利用
        View view;
        if (convertView != null) {
            view = convertView;
        } else {
            view = this.inflater.inflate(this.itemId, null);
        }

        ListItem item = this.items.get(position);

        TextView tv = (TextView) view.findViewById(R.id.item_text);
        tv.setText(item.getName());
        TextView tv1 = (TextView) view.findViewById(R.id.kana_text);
        tv1.setText(item.getKana());

        return view;
    }

    private class ListFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults result = new FilterResults();
//            List<ListItem> allItem = getallItem();
            if (constraint == null || constraint.length() == 0) {

//                result.values = allItem;
//                result.count = allItem.size();
            } else {
                ArrayList<ListItem> filteredList = new ArrayList<ListItem>();
//                for(ListItem j: allItem){
//                    if(j.source.title.contains(constraint))
//                        filteredList.add(j);
//                }
                result.values = filteredList;
                result.count = filteredList.size();
            }

            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count == 0) {
                notifyDataSetInvalidated();
            } else {
                items = (ArrayList<ListItem>) results.values;
                notifyDataSetChanged();
            }
        }

    }
}
