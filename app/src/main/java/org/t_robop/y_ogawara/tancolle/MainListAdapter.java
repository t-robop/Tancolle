package org.t_robop.y_ogawara.tancolle;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by iris on 2016/07/01.
 */
public class MainListAdapter extends ArrayAdapter<MainAdapterData> {

    private LayoutInflater layoutInflater;
    Context context;

    public MainListAdapter(Context c, int id, ArrayList<MainAdapterData> MainListArray) {
        super(c, id, MainListArray);
        context=c;
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
        ((TextView) convertView.findViewById(R.id.birth1)).setText(remainingDay(mainListData.getBirthMonth(0),mainListData.getBirthDay(0)));
        if (mainListData.getPresentFlag(0)==1) {
            liner1.setBackgroundResource(R.drawable.ribbon_tra);
        }
        //ListView用のTextView2(中央)の宣言
        TextView nameText2 = ((TextView) convertView.findViewById(R.id.name2));
        TextView birthText2 = ((TextView) convertView.findViewById(R.id.birth2));

        //ListView用のTextView3(右端)の宣言
        TextView nameText3 = ((TextView) convertView.findViewById(R.id.name3));
        TextView birthText3 = ((TextView) convertView.findViewById(R.id.birth3));


        //listの行数が最終行だったとき
          if (position == (mainListData.getAllSize()-1) / 3) {
              //いくつ余ってるかの分岐
                switch (mainListData.getAllSize()%3) {
                    case 2://一つ無いとき
                        //中央のTextだけ表示させます
                        nameText2.setText(mainListData.getName(1));
                        //birthText2.setText(mainListData.getBirthMonth(1)+"/"+mainListData.getBirthDay(1));
                        birthText2.setText(remainingDay(mainListData.getBirthMonth(1),mainListData.getBirthDay(1)));

                        if (mainListData.getPresentFlag(1)==1) {
                            liner2.setBackgroundResource(R.drawable.ribbon_tra);
                        }
                        break;
                    case 1://二つ無いとき
                        //if (GestureDecActivity.flag == true) {
                            liner2.setVisibility(View.INVISIBLE);
                            liner3.setVisibility(View.INVISIBLE);
                        //}
                        //何も表示させません
                        break;
                    case 0:
                        liner2.setVisibility(View.VISIBLE);
                        liner3.setVisibility(View.VISIBLE);
                        nameText2.setText(mainListData.getName(1));
                        birthText2.setText(remainingDay(mainListData.getBirthMonth(1),mainListData.getBirthDay(1)));
                        if (mainListData.getPresentFlag(1)==1) {
                            liner2.setBackgroundResource(R.drawable.ribbon_tra);
                        }

                        nameText3.setText(mainListData.getName(2));
                        birthText3.setText(remainingDay(mainListData.getBirthMonth(2),mainListData.getBirthDay(2)));
                        if (mainListData.getPresentFlag(2)==1) {
                            liner3.setBackgroundResource(R.drawable.ribbon_tra);
                        }
                        break;
                }
            }
        else{
              //最終行ではないので全て表示させます
              liner2.setVisibility(View.VISIBLE);
              liner3.setVisibility(View.VISIBLE);
              nameText2.setText(mainListData.getName(1));
              birthText2.setText(remainingDay(mainListData.getBirthMonth(1),mainListData.getBirthDay(1)));
              if (mainListData.getPresentFlag(1)==1) {
                  liner2.setBackgroundResource(R.drawable.ribbon_tra);
              }

              nameText3.setText(mainListData.getName(2));
              birthText3.setText(remainingDay(mainListData.getBirthMonth(2),mainListData.getBirthDay(2)));
              if (mainListData.getPresentFlag(2)==1) {
                  liner3.setBackgroundResource(R.drawable.ribbon_tra);
              }

          }


//        else if (mainListData.getPresentFlag(2)==1){
//            liner2.setBackgroundResource(R.drawable.ribbon_tra);
//        }
//        else if (mainListData.getPresentFlag(3)==1) {
//            liner3.setBackgroundResource(R.drawable.ribbon_tra);
//        }
//        else {
//        }
        //viewを返す
        return convertView;

    }
    public String remainingDay(int sqlMonth,int sqlDay){
        SharedPreferences pref = context.getSharedPreferences("Setting", Context.MODE_PRIVATE);
        boolean drawType = pref.getBoolean("drawType", false);

        if (drawType == true){
            //残日計算
            int MONTH =  Calendar.getInstance().get(Calendar.MONTH);
            int day = Calendar.getInstance().get(Calendar.DATE);
            //今の日付
            int currentMonthDay =  Integer.parseInt(String.valueOf(MONTH)+String.valueOf(day));
            int sqlMinthDay = Integer.parseInt(String.valueOf(sqlMonth)+String.valueOf(sqlDay));

            //現在の日付よりもsqlの日付が後の場合 2016年
            if (currentMonthDay<sqlMinthDay) {
                return String.valueOf(dayTo(sqlMonth,sqlDay,false)+"日");

            }else {
                return String.valueOf(dayTo(sqlMonth,sqlDay,true))+"日";
            }
            //sqliteデータの日付
        }else{
            return sqlMonth+"/"+sqlDay;


        }
    }
    //boolean testは現在の年数かどうかの判定 flaseなら2016 trueなら2017
    int dayTo(int sqlMonth,int sqlDay,boolean test){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date dateTo = null;
        Date dateFrom = null;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month =  Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DATE);
        // 日付を作成します。
        try {
            dateFrom = sdf.parse(year+"/"+(month+1)+"/"+day);
            if (test == true) {
                dateTo = sdf.parse(year+1+"/"+sqlMonth+"/"+sqlDay);
            }
            else {
                dateTo = sdf.parse(year+"/"+sqlMonth+"/"+sqlDay);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 日付をlong値に変換します。
        long dateTimeTo = dateTo.getTime();
        long dateTimeFrom = dateFrom.getTime();

        // 差分の日数を算出します。
        int dayDiff = (int) (( dateTimeTo - dateTimeFrom  ) / (1000 * 60 * 60 * 24 ));
        return dayDiff;
    }

}
