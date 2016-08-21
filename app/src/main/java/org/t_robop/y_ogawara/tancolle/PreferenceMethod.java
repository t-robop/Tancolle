package org.t_robop.y_ogawara.tancolle;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Created by taiga on 2016/08/21.
 */
public class PreferenceMethod extends AppCompatActivity {

    public PreferenceMethod(){

    }

    // プリファレンス保存
    // aaa,bbb,ccc... の文字列で保存
    public void saveArray(ArrayList<String> array, String PrefKey ,Context context){
        String str = new String("");
        for (int i =0;i<array.size();i++){
            str = str + array.get(i);
            if (i !=array.size()-1){
                str = str + ",";
            }
        }
        SharedPreferences prefs1 = context.getSharedPreferences("Array", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs1.edit();
        editor.putString(PrefKey, str).commit();
    }

    // プリファレンス取得
    // aaa,bbb,ccc...としたものをsplitして返す
    public String[] getArray(String PrefKey,Context context){
        SharedPreferences prefs2 = context.getSharedPreferences("Array", Context.MODE_PRIVATE);
        String stringItem = prefs2.getString(PrefKey,"");
        if(stringItem != null && stringItem.length() != 0){
            return stringItem.split(",");
        }else{
            return null;
        }
    }
}
