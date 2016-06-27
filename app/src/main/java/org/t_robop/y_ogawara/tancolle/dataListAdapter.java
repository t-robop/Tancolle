package org.t_robop.y_ogawara.tancolle;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


//TODO クラスの中にクラスは作れません
//独自Adapterつくる
public class dataListAdapter extends BaseAdapter implements Filterable {

    Context context;
    ArrayList<ListItem> items;
    ArrayList<ListItem> mStringFilterList;
    ValueFilter valueFilter;


    dataListAdapter(Context context, ArrayList<ListItem> items) {
        this.context = context;
        this.items = items;
        mStringFilterList = items;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }


    @Override
    public long getItemId(int position) {
        return items.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        convertView = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_search__item, null);

            TextView name_tv = (TextView) convertView.findViewById(R.id.name_text);
            TextView kana_tv = (TextView) convertView.findViewById(R.id.kana_text);

            ListItem listItem = items.get(position);

            name_tv.setText(listItem.getName());
            kana_tv.setText(listItem.getKana());
        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<ListItem> filterList = new ArrayList<ListItem>();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if ((mStringFilterList.get(i).getName().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {

                        ListItem listItem = new ListItem(mStringFilterList.get(i)
                                .getName(), mStringFilterList.get(i)
                                .getKana());

                        filterList.add(listItem);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mStringFilterList.size();
                results.values = mStringFilterList;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            items = (ArrayList<ListItem>) results.values;
            notifyDataSetChanged();
        }

    }
}
