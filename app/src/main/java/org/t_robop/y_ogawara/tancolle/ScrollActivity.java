package org.t_robop.y_ogawara.tancolle;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
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


public class ScrollActivity extends Activity implements GestureDetector.OnGestureListener {
    private HorizontalScrollView horizontalScrollView; //横スクロールの宣言
    private GestureDetector gestureDetector;//ジェスチャイベントの宣言

    private int page = 0; // ページ数
    private int displayWidth; // 画面サイズ：X 横
    private int displayHeight; // 画面サイズ：Y 縦
    private int pageCount = 0; // 画面数

    private boolean scrollFlg = false; // スクロールチェックのフラグ
    private static final int SCROLL_NONE = 0; // スライド距離が一定量を超えない
    private static final int SCROLL_LEFT = 2; //
    private static final int SCROLL_RIGHT = 1; //
    private int slideLimitFlg = SCROLL_NONE; // スライドの状態判定

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll); //xmlの関連付け
        setViewSize();

        gestureDetector = new GestureDetector(getApplicationContext(), this); // GestureDetectorの生成

        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.hsv_main); //xmlとの関連付け
        horizontalScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) { //触った時の動き
                // GestureDetectorにイベントを委譲する
                boolean result = gestureDetector.onTouchEvent(event);

                // スクロールが発生した後に画面から指を離した時
                if ((event.getAction() == MotionEvent.ACTION_UP) && scrollFlg) {
                    switch (slideLimitFlg) {
                        case SCROLL_NONE: //動かさなかったら（？）
                            break;
                        case SCROLL_LEFT: //左にスクロールしたら
                            setPage(true); //右の画面を表示する（次のページ
                            break;
                        case SCROLL_RIGHT: //右にスクロールしたら
                            setPage(false); //左の画面を表示する（前のページ
                            break;
                    }
                    //指定ページへスクロールする 1枚の画面でつながっているから始点ページから何ピクセル×移動したいページ数で飛ぶ
                    horizontalScrollView.scrollTo(page * displayWidth,
                            displayHeight);
                }
                return result;
            }
        });
    }

    //ページ設定用 true;次のページ false:前のページ
    private void setPage(boolean check) {
        if (check) {
            if (page < pageCount) { //もしページ数より画面数（１２）のほうが大きかったら
                page++; //ページ数を増やす
            }else{ //ページ数が画面数の１２をこしたら
                page=0; //ページを０にする
            }
        } else {
            if (page > 0) { //ページ数が0よりも画面数の12より大きかったら
                page--; //ページ数を減らす（画面数より下回るところまで）
            }else{ //ページ数が０になったら
                page=pageCount; //画面数の最高数に（最後のページ数に）ページ数を合わせる
            }

        }
    }

    // 各ImageViewを画面サイズと同じサイズに設定
    private void setViewSize() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        displayWidth = display.getWidth(); //横の幅を取得
        displayHeight = display.getHeight(); //縦の幅を取得

        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(
                displayWidth, displayHeight); //
        ViewGroup layout = (ViewGroup) findViewById(R.id.ll_main);

        // ページ数の設定
        pageCount = layout.getChildCount() - 1; //xmlから画面数を取得 １２個あるのを０〜１１にするために −１

        for (int i = 0; i <= pageCount; i++) {   //0から11まで
            layout.getChildAt(i).setLayoutParams(layoutParam);//xmlから取得したページを表示
        }
    }

    // フリック入力の検出
    @Override
    public boolean onFling(MotionEvent envent1, MotionEvent envent2,
                           float velocityX, float velocityY) {
        Log.d("onFling", "onFling");
//        // スクロールが一定距離を超えていない時のフリック操作は有効とする
//        if (slideLimitFlg == SCROLL_NONE) {
//            if (velocityX < 0) { //X座標が０より小さければ次のページヘ（右から左への動き
//                setPage(true);
//            } else if (velocityX > 0) { //X座標が０より大きければ前のページヘ（左から右への動き
//                // 右フリック
//                setPage(false);
//            }
//            horizontalScrollView.scrollTo(page * displayWidth, displayHeight);
//        }
        return true;
    }

    // スライド入力検出
    @Override
    public boolean onScroll(MotionEvent envent1, MotionEvent envent2,
                            float distanceX, float distanceY) {
        Log.d("onScroll", "onScroll");
        scrollFlg = true;

        // スライド距離の計算
        int rangeX = (int) (envent1.getRawX() - envent2.getRawX());//指をおいたX座標の場所ー指を離したX座標の場所

        if (rangeX < -displayWidth * 0.2) { //動かした数値よりー（画面の横幅×０．６）の長さの数値のほうが大きかったら
            // 右に一定距離のスライド
            slideLimitFlg = SCROLL_RIGHT;
        } else if (rangeX > displayWidth * 0.2) { //動かした数値より（画面の横幅×０．６）の長さの数値のほうが大きかったら
            // 左に一定距離のスライド
            slideLimitFlg = SCROLL_LEFT; //TODO わからない
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
