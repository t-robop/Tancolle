package org.t_robop.y_ogawara.tancolle;

import java.util.ArrayList;

/**
 * Created by taiga on 2016/07/05.
 */
public class MainAdapterData {

//データをsqlから3人分(１行分)毎に全員検索かけて必要なデータのみ"格納"させておく。読み込ませたりとかの処理はいらない。
//必要なデータはid,名前,誕生月,誕生日,プレゼントフラグ

    //id
    private int[] id=new int[3];

    //名前
    private String[] name=new String[3];

    //誕生月
    private int[] birthMonth=new int[3];

    //誕生日
    private int[] birthDay=new int[3];

    //誕生日フラグ(0or1)
    private int[] presentFlag=new int[3];

    //変数初期化
    public void startMad()
    {
        for(int i=0;i<3;i++)
        {
            id[i]=0;
            name[i]="";
            birthMonth[i]=0;
            birthDay[i]=0;
            presentFlag[i]=0;
        }
    }

    //保存
    //num(配列番号)
    public void setId(int num,int id)
    {
        this.id[num]=id;
    }

    public void setName(int num,String name)
    {
        this.name[num]=name;
    }

    public void setBirthMonth(int num,int birthMonth)
    {
        this.birthMonth[num]=birthMonth;
    }

    public void setBirthDay(int num,int birthDay)
    {
        this.birthDay[num]=birthDay;
    }

    public void setPresentFlag(int num,int presentFlag)
    {
        this.presentFlag[num]=presentFlag;
    }


    //取り出し
    //num(配列番号)
    public int getId(int num)
    {
        return id[num];
    }

    public String getName(int num)
    {
        return name[num];
    }

    public int getBirthMonth(int num)
    {
        return birthMonth[num];
    }

    public int getBirthDay(int num)
    {
        return birthDay[num];
    }

    public int getPresentFlag(int num)
    {
        return presentFlag[num];
    }


}