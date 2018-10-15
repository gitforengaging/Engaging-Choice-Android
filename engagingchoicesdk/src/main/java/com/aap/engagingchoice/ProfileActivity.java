package com.aap.engagingchoice;

import android.app.Activity;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.aap.engagingchoice.Api.EcUpdateInfoApi;
import com.aap.engagingchoice.databinding.ActivityProfileBinding;
import com.aap.engagingchoice.network.EcUpdateInfoListener;
import com.aap.engagingchoice.network.EngagingChoiceKey;
import com.aap.engagingchoice.pojo.EcUpdateInfoReq;
import com.aap.engagingchoice.utility.Constants;
import com.aap.engagingchoice.utility.Utils;

/**
 * This is Profile Screen of SDK
 */
public class ProfileActivity extends Activity implements EcUpdateInfoListener {

    private ActivityProfileBinding mBinding;
    private int mUserType;
    private int mWidthPixels;
    private int mHeightPixels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            checkDeviceType(true);
        } else {
            checkDeviceType(false);
        }
        if (getIntent() != null && getIntent().getExtras().containsKey(Constants.USER_TYPE)) {
            mUserType = (int) getIntent().getExtras().get(Constants.USER_TYPE);
            switch (mUserType) {
                case Constants.NEW_USER:
                    mBinding.activityProfileTvDescrp.setText(getString(R.string.new_user_msg));
                    break;
                case Constants.GUEST_USER:
                    mBinding.activityProfileTvDescrp.setText(getString(R.string.new_user_msg));
                    break;
            }
        }
        mBinding.activityProfileTvProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidData()) {
                    if (Utils.isNetworkAvailable(ProfileActivity.this, true)) {
                        callRegistrationApi();
                    }
                }
            }
        });
        mBinding.activityProfileTvProceedLand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidData()) {
                    callRegistrationApi();
                }
            }
        });
        mBinding.activityProfileLlCancelLand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBinding.activityProfileLlCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * This method is used to check device type
     *
     * @param isLand
     */
    private void checkDeviceType(boolean isLand) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mWidthPixels = metrics.widthPixels;
        mHeightPixels = metrics.heightPixels;
        float scaleFactor = metrics.density;
        float widthDp = mWidthPixels / scaleFactor;
        float heightDp = mHeightPixels / scaleFactor;
        float smallestWidth = Math.min(widthDp, heightDp);

        if (smallestWidth > 720) {
            //Device is a 10" tablet
            setLandscapeModeOfTablet(true);
        } else if (smallestWidth > 600) {
            //Device is a 7" tablet
            setLandscapeModeOfTablet(false);
        } else {
            if (isLand) {
                setLandscapeMode();
            } else {
                setPortraitMode();
            }
        }
    }

    /**
     * Set landscape mode UI for tablet
     *
     * @param is10Inch
     */
    private void setLandscapeModeOfTablet(boolean is10Inch) {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(this.getWindow().getAttributes());
        if (is10Inch) {
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        } else {
            int eightyPerW = (mWidthPixels / 100) * 80;
            lp.width = eightyPerW;
        }

        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);
        mBinding.activityProfileTvProceed.setVisibility(View.GONE);
        mBinding.activityProfileLlCancel.setVisibility(View.GONE);
        mBinding.activityProfileContProceedCancelLand.setVisibility(View.VISIBLE);
    }

    /**
     * Set landscape mode ui
     */
    private void setLandscapeMode() {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(this.getWindow().getAttributes());
        // 80 percentage of screen width
        int eightyPerW = (mWidthPixels / 100) * 80;
        lp.width = eightyPerW;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(lp);
        mBinding.activityProfileTvProceed.setVisibility(View.GONE);
        mBinding.activityProfileLlCancel.setVisibility(View.GONE);
        mBinding.activityProfileContProceedCancelLand.setVisibility(View.VISIBLE);
    }

    /**
     * Set Portrait Mode UI
     */
    private void setPortraitMode() {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(this.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);
        mBinding.activityProfileTvProceed.setVisibility(View.VISIBLE);
        mBinding.activityProfileLlCancel.setVisibility(View.VISIBLE);
        mBinding.activityProfileContProceedCancelLand.setVisibility(View.GONE);
    }

    /**
     * This method check valid data
     *
     * @return
     */
    private boolean checkValidData() {
        if (TextUtils.isEmpty(mBinding.activityProfileEtEmail.getText().toString()) || (TextUtils.isEmpty(mBinding.activityProfileEtNumber.getText().toString()))) {
            Utils.showDialog(ProfileActivity.this, getResources().getString(R.string.required_field), false);
            return false;
        } else {
            return true;
        }

    }

    /**
     * This method calls registration api
     */
    private void callRegistrationApi() {
        EcUpdateInfoReq ecUpdateInfoReq = new EcUpdateInfoReq();
        ecUpdateInfoReq.setEmail(mBinding.activityProfileEtEmail.getText().toString());
        ecUpdateInfoReq.setOld_email(EngagingChoiceKey.getInstance().getEmailId());
        ecUpdateInfoReq.setMobile_no(mBinding.activityProfileEtNumber.getText().toString());
        EcUpdateInfoApi ecUpdateInfoApi = new EcUpdateInfoApi(ecUpdateInfoReq, this);
        ecUpdateInfoApi.callUpdateInfoApi();
    }

    /**
     * This method is called from EcUpdateInfoApi class
     *
     * @param msg - contains msg when api response is success
     */
    @Override
    public void updateSuccessFully(String msg) {
        Utils.showDialog(ProfileActivity.this, msg, true);
    }

    /**
     * This method is called from EcUpdateInfoApi class
     *
     * @param msg - contains msg when api response is failiure
     */
    @Override
    public void failiure(String msg) {
        Utils.showDialog(ProfileActivity.this, msg, false);
    }
}
