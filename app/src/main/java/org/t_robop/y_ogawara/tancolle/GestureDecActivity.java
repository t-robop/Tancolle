package org.t_robop.y_ogawara.tancolle;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class GestureDecActivity extends Activity implements GestureDetector.OnGestureListener {
    //横スクロールの宣言
    private HorizontalScrollView horizontalScrollView;
    //
    private GestureDetector gestureDetector;

    private int page = 0; // ページ数
    private int displayWidth; // 画面サイズ：X
    private int displayHeight; // 画面サイズ：Y
    private int pageCount = 0; // 画面数 (要するに最後の画面)

    private boolean scrollFlg = false; // スクロールチェック
    private static final int SCROLL_NONE = 0; // スライド距離が一定量を超えない
    private static final int SCROLL_LEFT = 2; //
    private static final int SCROLL_RIGHT = 1; //
    private int slideLimitFlg = SCROLL_NONE; // スライドの状態判定

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_dec);
        setViewSize();

        // GestureDetectorの生成
        gestureDetector = new GestureDetector(getApplicationContext(), this);

        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.hsv_main);
        horizontalScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // GestureDetectorにイベントを委譲する
                boolean result = gestureDetector.onTouchEvent(event);

                // スクロールが発生した後に画面から指を離した時
                if ((event.getAction() == MotionEvent.ACTION_UP) && scrollFlg) {
                    switch (slideLimitFlg) {
                        case SCROLL_NONE:
                            break;
                        case SCROLL_LEFT:
                            setPage(true);
                            break;
                        case SCROLL_RIGHT:
                            setPage(false);
                            break;
                    }
                    // 指定ページへスクロールする
                    horizontalScrollView.scrollTo(page * displayWidth,
                            displayHeight);
                }
                return result;
            }
        });
    }

    // ページ設定用 true;次のページ false:前のページ
    private void setPage(boolean check) {
        if (check) {
            if (page < pageCount) {
                page++;
            }
            //もし一番右にいた時に、右側に行こうとした時
            else{
                page = 0;
            }
        } else {
            if (page > 0) {
                page--;
            }
            //もし一番左側にいた時に、左に行こうとした時
            else {
                page = pageCount;
            }
        }
    }

    // 各ImageViewを画面サイズと同じサイズに設定
    private void setViewSize() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        displayWidth = display.getWidth();
        displayHeight = display.getHeight();

        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(
                displayWidth, displayHeight);
        ViewGroup layout = (ViewGroup) findViewById(R.id.ll_main);

        // ページ数の設定
        pageCount = layout.getChildCount() - 1;

        for (int i = 0; i <= pageCount; i++) {
            layout.getChildAt(i).setLayoutParams(layoutParam);
        }
    }

    // フリック入力の検出
    @Override
    public boolean onFling(MotionEvent envent1, MotionEvent envent2,
                           float velocityX, float velocityY) {
        Log.d("onFling", "onFling");
        // スクロールが一定距離を超えていない時のフリック操作は有効とする
        if (slideLimitFlg == SCROLL_NONE) {
            if (velocityX < 0) {
                // 左フリック
                setPage(true);
            } else if (velocityX > 0) {
                // 右フリック
                setPage(false);
            }
            horizontalScrollView.scrollTo(page * displayWidth, displayHeight);
        }
        return true;
    }

    // スライド入力検出
    @Override
    public boolean onScroll(MotionEvent envent1, MotionEvent envent2,
                            float distanceX, float distanceY) {
        Log.d("onScroll", "onScroll");
        scrollFlg = true;

        // スライド距離の計算
        int rangeX = (int) (envent1.getRawX() - envent2.getRawX());

        if (rangeX < -displayWidth * 0.6) {
            // 右に一定距離のスライド
            slideLimitFlg = SCROLL_RIGHT;
        } else if (rangeX > displayWidth * 0.6) {
            // 左に一定距離のスライド
            slideLimitFlg = SCROLL_LEFT;
        } else {
            slideLimitFlg = SCROLL_NONE;
        }
        return false;
    }

    /*** 今回未使用のOnGestureListener関連イベント *********************/
    @Override
    public boolean onDown(MotionEvent envent) {
        Log.d("onDown", "onDown");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent envent) {
        Log.d("onShowPress", "onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent envent) {
        Log.d("onSingleTapUp", "onSingleTapUp");
        return false;
    }

    @Override
    public void onLongPress(MotionEvent envent) {
        Log.d("onLongPress", "onLongPress");
    }
    /*******************************************************************/
}