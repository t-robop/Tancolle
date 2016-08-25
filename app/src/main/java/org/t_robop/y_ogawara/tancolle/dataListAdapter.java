package org.t_robop.y_ogawara.tancolle;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


//独自Adapterつくる
public class dataListAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private ArrayList<ListItem> items;
    private ArrayList<ListItem> mStringFilterList;
    private ValueFilter valueFilter;

    dataListAdapter(Context context, ArrayList<ListItem> items) {
        this.context = context;
        this.items = items;
        mStringFilterList = items;
    }


    //BOX?に収める
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

        //Inflaterを使うことでviewを動的に生成(item.xmlで作ったviewを使いまわせる)
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        convertView = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_search__item, null);

            TextView name_tv = (TextView) convertView.findViewById(R.id.name_text);
            TextView kana_tv = (TextView) convertView.findViewById(R.id.kana_text);
            ImageView item_image = (ImageView) convertView.findViewById(R.id.item_image);

            ListItem listItem = items.get(position);

            name_tv.setText(listItem.getName());
            kana_tv.setText(listItem.getKana());

            //画像読み込み
            InputStream in;
            Bitmap img = null;
            try {
                in = context.openFileInput(listItem.getSmallImage());
                img = BitmapFactory.decodeStream(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //画像セット
            item_image.setImageBitmap(img);
        }
        //Viewを返す
        return convertView;
    }


    //extendしたアダプタで使うfilter
    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    //標準のfilterをextendする
    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<ListItem> filterList = new ArrayList<ListItem>();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if ((mStringFilterList.get(i).getKana().toUpperCase() + mStringFilterList.get(i).getName().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {

                        ListItem listItem = mStringFilterList.get(i);

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
