package kr.co.royworld.smartcart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

/**
 * Created by Jin on 2016-01-06.
 */
public class IntroActivity extends Activity {
    private Handler mHandler;    // 핸들러 설정

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);    // Full Screen으로 변경
        setContentView(R.layout.intro);

        mHandler = new Handler();            // 딜레이를 주기 위한 핸들러
        mHandler.postDelayed(mRun, 2000);    //
    }

    /**
     * 인트로 -> 메인으로 이동
     */
    Runnable mRun = new Runnable(){
        @Override
        public void run(){
            Intent intent = new Intent(IntroActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    };

    /**
     * 인트로에서 뒤로가기 막기
     */
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        mHandler.removeCallbacks(mRun);
    }
}
