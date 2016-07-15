package org.t_robop.y_ogawara.tancolle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

public class ViewFlipperTestActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    //タッチイベントを取得する変数
    private GestureDetector gestureDetector;
    //ViewFlipper型の宣言
    private ViewFlipper flipper;
    //アニメーションを読み込む変数
    private Animation slideInFromLeft;
    private Animation slideInFromRight;
    private Animation slideOutToLeft;
    private Animation slideOutToRight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_flipper_test);
        //ViewFlipperの関連付け
        flipper = (ViewFlipper)findViewById(R.id.flipper);
        //flipper.addView();
        //アニメーション用の変数にデータをセット
        slideInFromLeft =
                AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left);
        slideInFromRight =
                AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right);
        slideOutToLeft =
                AnimationUtils.loadAnimation(this, R.anim.slide_out_to_left);
        slideOutToRight =
                AnimationUtils.loadAnimation(this, R.anim.slide_out_to_right);
        //gestureDetectorクラスのインスタンスを生成(実態を生成しているみたいな、classがあるだけだと、設計図があるだけなので)
        gestureDetector = new GestureDetector(this,this);
    }

    //画面をタッチした時に呼ばれるメソッド
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

    //フリックした時に呼ばれる
    @Override
    public final boolean onFling(final MotionEvent e1, final MotionEvent e2, final float velocityX, final float velocityY) {
        //画面をタッチした時の値と、画面から指を離した時の値を引いて、絶対値を入れる
        float dx = Math.abs(e1.getX() - e2.getX());
        float dy = Math.abs(e1.getY() - e2.getY());
        //もし、縦の移動よりも横の移動のほうが多い場合
        if (dx > dy) {
            //フリックの速度 -なら左 +なら右にフリック
            if (velocityX > 0) {
                //flipper.setInAnimation(slideInFromLeft);
                //flipper.setOutAnimation(slideOutToRight);
                //アニメーションを実行する
                //flipper.showPrevious();
                flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left));
                flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_to_right));
                flipper.showPrevious();


            } else {
                //flipper.setInAnimation(slideInFromRight);
                //flipper.setOutAnimation(slideOutToLeft);
                //アニメーションを実行する
                //flipper.showNext();


                flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right));
                flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_to_left));
                flipper.showNext();
            }
            return true;
        }
        return false;
    }

    // ...
}