package org.t_robop.y_ogawara.tancolle;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.ViewFlipper;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    //タッチイベントを取得する変数
    private GestureDetector gestureDetector;
    private ViewFlipper flipper;
    private Animation slideInFromLeft;
    private Animation slideInFromRight;

    private Animation slideOutToLeft;
    private Animation slideOutToRight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ListView listView = (ListView) findViewById(R.id.listView);
        flipper = (ViewFlipper)findViewById(R.id.flipper);
        slideInFromLeft =
                AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left);
        slideInFromRight =
                AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right);
        slideOutToLeft =
                AnimationUtils.loadAnimation(this, R.anim.slide_out_to_left);
        slideOutToRight =
                AnimationUtils.loadAnimation(this, R.anim.slide_out_to_right);
        gestureDetector = new GestureDetector(this,this);
        // データを準備
        ArrayList<ListCellData> cellDataArray = new ArrayList<>();


        for (int i =1;i<9;i=i+3){
            ListCellData cellData = new ListCellData();
            cellData.setBirthday1("6/10");
            cellData.setBirthday2("6/12");
            cellData.setBirthday3("6/20");
            cellData.setName1("西村");
            cellData.setName2("西");
            cellData.setName3("村");

            cellData.setBitmap1(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
            cellData.setBitmap2(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
            cellData.setBitmap3(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));

            cellData.setId1(i);
            cellData.setId2(i+1);
            cellData.setId3(i+2);
            cellDataArray.add(cellData);
        }

        // Adapter - ArrayAdapter - UserAdapter
        ListCellAdapter adapter = new ListCellAdapter(this, 0, cellDataArray);


        // ListViewに表示
        listView.setEmptyView(findViewById(R.id.listView));
        listView.setAdapter(adapter);
    }
    public void cellClick(View view) {
        Log.v("yorosiku!",  String.valueOf(view.getTag()));

    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event)
                || super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public final boolean onFling(
            final MotionEvent e1,
            final MotionEvent e2,
            final float velocityX,
            final float velocityY) {
        float dx = Math.abs(e1.getX() - e2.getX());
        float dy = Math.abs(e1.getY() - e2.getY());
        if (dx > dy) {
            if (velocityX > 0) {
                flipper.setInAnimation(slideInFromLeft);
                //flipper.setOutAnimation(slideOutToRight);
                flipper.showPrevious();

            } else {
                flipper.setInAnimation(slideInFromRight);
                //TODO: ここで一度画面外にとどめておいて、更新して、コールバックでアニメーションを使い、戻す
                //TODO: アニメーションは止まった、後は更新した結果をコールバックして戻すだけ
                //flipper.setOutAnimation(slideOutToLeft);
                flipper.showNext();
            }
            return true;
        }
        return false;
    }
}
