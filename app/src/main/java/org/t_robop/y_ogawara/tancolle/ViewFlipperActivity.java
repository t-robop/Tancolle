package org.t_robop.y_ogawara.tancolle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

public class ViewFlipperActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private GestureDetector gestureDetector;
    private ViewFlipper flipper;
    private Animation slideInFromLeft; //アニメーションの宣言
    private Animation slideInFromRight; //アニメーションの宣言
    private Animation slideOutToLeft;  //アニメーションの宣言
    private Animation slideOutToRight; //アニメーションの宣言

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_flipper); //xmlの関連付け
        flipper = (ViewFlipper)findViewById(R.id.flipper); //viewflipperの関連付け
        slideInFromLeft =
                AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left); //animの関連付け
        slideInFromRight =
                AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right); //animの関連付け
        slideOutToLeft =
                AnimationUtils.loadAnimation(this, R.anim.slide_out_to_left); //animの関連付け
        slideOutToRight =
                AnimationUtils.loadAnimation(this, R.anim.slide_out_to_right); //animの関連付け
        gestureDetector = new GestureDetector(this, this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event)
                || super.dispatchTouchEvent(event);
    }

    @Override
    public final boolean onFling( //ディスプレイをタッチした時のイベント作成
            final MotionEvent e1, //中にXとY 定数 はじめの値（指をおいた位置）
            final MotionEvent e2, //中にXとY 定数 おわりの値（指を離した位置）
            final float velocityX, //X座標が動いているか どこに動いているかの値
            final float velocityY) //Y座標が動いているか どこに動いているかの値
    {
        float dx = Math.abs(e1.getX() - e2.getX()); //X座標の始まりー終わり（どれくらい動いたか）※絶対値
        float dy = Math.abs(e1.getY() - e2.getY()); //Y座標の始まりー終わり（どれくらい動いたか）※絶対値
        if (dx > dy) { //もしもXのほうがでかかったら（縦よりも横の動きのほうが大きく横よりの動きだったら
            if (velocityX > 0) { //更にX座標がプラス方向に動いていたら（右に向かって動いていたら）
                flipper.setInAnimation(slideInFromLeft); //左からビューをどーん
                flipper.setOutAnimation(slideOutToRight); //右へビューをぽいっ
                flipper.showPrevious();
            } else { //逆にX座標がマイナス方向に動いていたら（左に向かって動いていたら）
                flipper.setInAnimation(slideInFromRight); //右からビューをどーん
                flipper.setOutAnimation(slideOutToLeft); //左へビューをぽいっ
                flipper.showNext();
            }  //盾のほうが動きが大きかったら縦寄りの動きなのでビューを動かさない
            return true;
        }
        return false;
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

    // ...
}