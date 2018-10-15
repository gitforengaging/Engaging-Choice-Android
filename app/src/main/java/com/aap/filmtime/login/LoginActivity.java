package com.aap.filmtime.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.aap.engagingchoice.network.EngagingChoiceKey;
import com.aap.filmtime.R;
import com.aap.filmtime.base.BaseActivity;
import com.aap.filmtime.databinding.ActivityLoginBinding;
import com.aap.filmtime.home.HomeActivity;
import com.aap.filmtime.utils.Constants;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private ActivityLoginBinding mLoginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mLoginBinding.loginTvSignin.setOnClickListener(this);
        EngagingChoiceKey.getInstance().setPublishSecretKey(Constants.TOKEN_QA_VALUE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_tv_signin:
                goToHomeScreen();
                String emailId = mLoginBinding.loginEtEmail.getText().toString();
                if (!TextUtils.isEmpty(emailId)) {
                    EngagingChoiceKey.getInstance().setEmailId(emailId);
                }
                break;
        }
    }

    private void goToHomeScreen() {
        launchActivity(HomeActivity.class);
        finish();
    }
}
