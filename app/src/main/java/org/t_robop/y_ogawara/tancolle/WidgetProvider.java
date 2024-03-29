package org.t_robop.y_ogawara.tancolle;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.util.TypedValue;
import android.widget.RemoteViews;
import java.util.ArrayList;

/**
 * Created by taiga on 2016/08/23.
 */
public class WidgetProvider extends AppWidgetProvider {
    //update時(今回は一日一回)
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        upDateWidget(context);
    }

    //Widget登場時(画面に一つ目のWidgetが登場する度に呼ばれる)
    @Override
    public void onEnabled(Context context) {
        upDateWidget(context);
    }

    // アップデート
    public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
        ComponentName myWidget = new ComponentName(context, WidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget, remoteViews);
    }

    //widget更新
    static void upDateWidget(Context context){
        // ウィジェットレイアウトの初期化
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        /*****変数群の宣言*****/
        //無登録特定用カウント変数
        int cnt=0;
        //登録されてる最も近い日を取得する変数
        int num = 366;
        //読み込む月
        int month=0;
        //名前取得用変数
        String name;
        //sql読み込み用リスト
        ArrayList<Data> monthData;
        /********************/
        //12ヶ月分ループ
        for(int i=0;i<12;i++){
            //ArrayListに月検索したデータをぶち込む
            monthData = dbAssist.birthdaySelect(i+1, context);
            //何か登録されてる時
            if(monthData.size()!=0){
                //今より前の残日を取得
                int n=MainListAdapter.dayTo(monthData.get(0).getMonth(), monthData.get(0).getDay(), false);
                //マイナス値なら
                if(n<0){
                    //今より先の残日を取得
                    n=MainListAdapter.dayTo(monthData.get(0).getMonth(), monthData.get(0).getDay(), true);
                }
                //今まで読み込んだ月より小さい場合
                if(num>n){
                    //入れ替え
                    num=n;
                    //これが最小かもしれないので月データを代入
                    month=i;
                }
            }
            //何も登録されてない時
            else{
                //カウントダウン
                cnt++;
            }
        }
        //ArrayListに確定した月データで検索したデータをぶち込む
        monthData = dbAssist.birthdaySelect(month+1, context);
        //カウントダウンがMAXでない(何かしら登録されてた)時
        if(cnt<12) {
            //名前取得
            name = monthData.get(0).getName();
            /*****取得した残日の桁数によってTextSizeを変更*****/
            if(num/10==0){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    remoteViews.setTextViewTextSize(R.id.widget_remain, TypedValue.COMPLEX_UNIT_DIP,40);
                }
            }
            else if(num/100==0){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    remoteViews.setTextViewTextSize(R.id.widget_remain, TypedValue.COMPLEX_UNIT_DIP,30);
                }
            }
            /********************/
            if(num==0){
                //テキストフィールドに名前を表示
                remoteViews.setTextViewText(R.id.widget_name, name + "さんの誕生日は");
                //テキストフィールドに残日を表示
                remoteViews.setTextViewText(R.id.widget_remain, "今日です");
            }
            else {
                //テキストフィールドに名前を表示
                remoteViews.setTextViewText(R.id.widget_name, name + "さんの誕生日まで");
                //テキストフィールドに残日を表示
                remoteViews.setTextViewText(R.id.widget_remain, "あと" + num + "日");
            }
        }
        //カウントダウンMAX!(一つも登録されてない時)
        else {
            //テキストフィールドに名前を表示
            remoteViews.setTextViewText(R.id.widget_name, "登録してください");
            //テキストフィールドに残日を表示
            remoteViews.setTextViewText(R.id.widget_remain, "");
    }
        // アップデートメソッド呼び出し
        pushWidgetUpdate(context, remoteViews);
    }
}
