package com.aap.filmtime;

import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Bundle;

import com.aap.filmtime.base.BaseActivity;
import com.aap.filmtime.login.LoginActivity;

/**
 * Splash screen of Sample
 */
public class SplashActivity extends BaseActivity {
    private static final int SPLASH_SCREEN_VISIBLE_TIME = 5000;//in milisec
    private Handler mHandler;
    private Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_splash);
        goToLoginActivity();
    }

    private void goToLoginActivity() {
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                launchActivity(LoginActivity.class);
                finish();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mHandler != null)
            mHandler.postDelayed(mRunnable, SPLASH_SCREEN_VISIBLE_TIME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mHandler != null && mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }
}
