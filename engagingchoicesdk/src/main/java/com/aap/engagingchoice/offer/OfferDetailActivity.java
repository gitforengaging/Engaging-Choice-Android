package com.aap.engagingchoice.offer;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.aap.engagingchoice.R;
import com.aap.engagingchoice.databinding.ActivityOfferDetailBinding;
import com.aap.engagingchoice.network.CallBackListenerClass;
import com.aap.engagingchoice.pojo.EcOfferListResponse;
import com.aap.engagingchoice.utility.Constants;
import com.aap.engagingchoice.utility.Utils;
import com.bumptech.glide.Glide;

/**
 * This Activity shows offer detail
 */
public class OfferDetailActivity extends AppCompatActivity implements OfferDetailMvpView {

    private static final long DISPLAY_TIME = 100;
    private ActivityOfferDetailBinding mBinding;
    private int mVideoPos;
    private String mVideo, mThumbNail, mFileBaseUrl;
    private EcOfferListResponse.DataBean mOfferData;
    private boolean isVideoClicked = true, isPrepared = false;
    private Handler mHandler;
    private Runnable mRunnable, mRunnable1, mRunnable2;
    private OfferDetailPresenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_offer_detail);
        setUiAccordingtoOrientation();
        setClickListener();
        mBinding.activityDetailTvOfferDescrp.setMovementMethod(new ScrollingMovementMethod());
        initializePresenter();
    }

    /**
     * set all clicklistener
     */
    private void setClickListener() {
        mHandler = new Handler();
        mBinding.activityDetailOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPlayPauseFunction();
            }
        });

        mBinding.activityOfferFrCrossCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBinding.activityOfferFrCrossContLand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBinding.activityDetailTvGetOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callOfferActionApi(Constants.CONSUMED);
                mPresenter.openWebView(mOfferData);
            }
        });
        mBinding.activityDetailTvGetOfferLand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callOfferActionApi(Constants.CONSUMED);
                mPresenter.openWebView(mOfferData);
            }
        });
        mBinding.activityDetailTvLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callOfferActionApi(Constants.LATER);
            }
        });
        mBinding.activityDetailTvLaterLand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callOfferActionApi(Constants.LATER);
            }
        });
        mBinding.activityDetailTvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callOfferActionApi(Constants.SKIP);
            }
        });
        mBinding.activityDetailTvSkipLand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callOfferActionApi(Constants.SKIP);
            }
        });
    }

    /**
     * Set play and pause video
     */
    private void setPlayPauseFunction() {
        if (isPrepared) {
            if (isVideoClicked) {

                // show pause button and hide visibiliy of button
                int length = mBinding.activityDetailVvOffer.getDuration();
                mVideoPos = mBinding.activityDetailVvOffer.getCurrentPosition();
                if (mVideoPos != length) {
                    mBinding.activityOfferIvPlayPause.setVisibility(View.VISIBLE);
                    mBinding.activityOfferIvPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.pause_icon));
                    mRunnable = new Runnable() {
                        @Override
                        public void run() {
                            isVideoClicked = false;
                            mBinding.activityOfferIvPlayPause.setVisibility(View.VISIBLE);
                            mBinding.activityOfferIvPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.ec_play_icon));
                            mVideoPos = mBinding.activityDetailVvOffer.getCurrentPosition();
                            mBinding.activityDetailVvOffer.pause();
                        }
                    };
                    if (mHandler != null)
                        mHandler.postDelayed(mRunnable, DISPLAY_TIME);
                }
            } else {
                // show play button and hide visibiliy of button
                int length = mBinding.activityDetailVvOffer.getDuration();
                if (mVideoPos != length) {
                    mBinding.activityOfferIvPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.pause_icon));
                    mRunnable1 = new Runnable() {
                        @Override
                        public void run() {
                            isVideoClicked = true;
                            mBinding.activityOfferIvPlayPause.setVisibility(View.GONE);
                            mBinding.activityDetailVvOffer.seekTo(mVideoPos);
                            mBinding.activityDetailVvOffer.start();
                        }
                    };
                    if (mHandler != null)
                        mHandler.postDelayed(mRunnable1, DISPLAY_TIME);
                }

            }
        }
    }

    /**
     * UI will change according to orientation
     */
    private void setUiAccordingtoOrientation() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setVisibilityOfLandscape();
        } else {
//            checkDeviceType();
            setVisibilityOfPortrait();
        }
    }

    /**
     * check device type - Tablet 7 or 10 or normal device
     */
    private void checkDeviceType() {
        float smallestWidth = Utils.getDeviceWidth(OfferDetailActivity.this);
        if (smallestWidth > 720) {
            setVisibilityOfLandscape();
        } else if (smallestWidth > 600) {
            setVisibilityOfLandscape();
        } else {
            setVisibilityOfPortrait();
        }
    }

    /**
     * Remove handler when activity is destroyed
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    /**
     * Call offer action api
     *
     * @param typeOfCount contains type of action - later , skip ,getoffer
     */
    private void callOfferActionApi(int typeOfCount) {
        mPresenter.callActionApi(typeOfCount, mOfferData);
        finish();
    }

    /**
     * This method calls when orientation done
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setVisibilityOfLandscape();
        } else {
//            checkDeviceType();
            setVisibilityOfPortrait();
        }
    }

    @Override
    public void onBackPressed() {
        if (CallBackListenerClass.getInstance().getmListener() != null) {
            CallBackListenerClass.getInstance().getmListener().callBackOfOffer();
        }
        finish();
    }

    /**
     * Set portrait mode ui
     */
    private void setVisibilityOfPortrait() {
        mBinding.activityOfferFrCrossCont.setVisibility(View.VISIBLE);
        mBinding.activityDetailLlPortraitCont.setVisibility(View.VISIBLE);
        mBinding.activityOfferRrLandsButtonCont.setVisibility(View.GONE);
        mBinding.activityOfferFrCrossContLand.setVisibility(View.GONE);
        if (mOfferData != null) {
            if (mOfferData.getDiscount_type() == 1) {
                mBinding.activityDetailTvOfferOff.setVisibility(View.VISIBLE);
                mBinding.activityDetailLlContOfferOffPortrait.setVisibility(View.GONE);
            } else {
                mBinding.activityDetailLlContOfferOffPortrait.setVisibility(View.VISIBLE);
                mBinding.activityDetailTvOfferOff.setVisibility(View.GONE);
            }
        }
        mBinding.activityDetailTvOfferOffLand.setVisibility(View.GONE);
        mBinding.activityDetailLlContOfferOffLand.setVisibility(View.GONE);


        mBinding.activityOfferFrVideoCont.getLayoutParams().height = (int) getResources().getDimension(R.dimen.dp_196);
        mBinding.activityOfferFrVideoCont.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
    }

    /**
     * Set landscape mode
     */
    private void setVisibilityOfLandscape() {
        mBinding.activityOfferFrCrossCont.setVisibility(View.GONE);
        mBinding.activityDetailLlPortraitCont.setVisibility(View.GONE);
        mBinding.activityOfferRrLandsButtonCont.setVisibility(View.VISIBLE);
        mBinding.activityOfferFrCrossContLand.setVisibility(View.VISIBLE);
        if (mOfferData != null) {
            if (mOfferData.getDiscount_type() == 1) {
                mBinding.activityDetailTvOfferOffLand.setVisibility(View.VISIBLE);
                mBinding.activityDetailLlContOfferOffLand.setVisibility(View.GONE);
            } else {
                mBinding.activityDetailTvOfferOffLand.setVisibility(View.GONE);
                mBinding.activityDetailLlContOfferOffLand.setVisibility(View.VISIBLE);
            }
        }


        mBinding.activityDetailLlContOfferOffPortrait.setVisibility(View.GONE);
        mBinding.activityDetailTvOfferOff.setVisibility(View.GONE);

        mBinding.activityOfferFrVideoCont.getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;
        mBinding.activityOfferFrVideoCont.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
        mBinding.activityOfferFrVideoCont.requestLayout();
    }

    /**
     * Initialize presenter
     */
    private void initializePresenter() {
        mPresenter = new OfferDetailPresenter(OfferDetailActivity.this);
        mPresenter.setListener(this);
        mPresenter.getDataFromIntent();
    }

    /**
     * This method is called from OfferDetailPresenter class
     *
     * @param data contains offer data
     */
    @Override
    public void setData(EcOfferListResponse.DataBean data) {
        mOfferData = data;
        mBinding.activityDetailTvOfferName.setText(data.getOffer_title());
        mBinding.activityDetailTvOfferNameLand.setText(data.getOffer_title());
        mBinding.activityDetailTvOfferDescrp.setText(data.getOffer_description());
        mBinding.activityDetailTvOfferDescrpLand.setText(data.getOffer_description());
        int discountType = data.getDiscount_type();
        switch (discountType) {
            case Constants.DISCOUNT_BUY1_GET1:
                mBinding.activityDetailTvOfferOff.setText(getString(R.string.buy1_get1));
                mBinding.activityDetailTvOfferOffLand.setText(getString(R.string.buy1_get1));
                mBinding.activityDetailLlContOfferOffLand.setVisibility(View.GONE);
                mBinding.activityDetailLlContOfferOffPortrait.setVisibility(View.GONE);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    mBinding.activityDetailTvOfferOff.setVisibility(View.GONE);
                    mBinding.activityDetailTvOfferOffLand.setVisibility(View.VISIBLE);
                } else {
                    mBinding.activityDetailTvOfferOff.setVisibility(View.VISIBLE);
                    mBinding.activityDetailTvOfferOffLand.setVisibility(View.GONE);
                }
                break;
            default:
                mBinding.activityDetailTvNumeric.setText(mOfferData.getDiscount());
                mBinding.activityDetailTvNumericLand.setText(mOfferData.getDiscount());

                mBinding.activityDetailTvOfferOff.setVisibility(View.GONE);
                mBinding.activityDetailTvOfferOffLand.setVisibility(View.GONE);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    mBinding.activityDetailLlContOfferOffPortrait.setVisibility(View.GONE);
                    mBinding.activityDetailLlContOfferOffLand.setVisibility(View.VISIBLE);
                } else {
                    mBinding.activityDetailLlContOfferOffPortrait.setVisibility(View.VISIBLE);
                    mBinding.activityDetailLlContOfferOffLand.setVisibility(View.GONE);
                }
                break;
        }
        int fileType = data.getFile_type();
        switch (fileType) {
            case Constants.FILE_IMAGE:
                mThumbNail = mFileBaseUrl + data.getFile_name();
                Glide.with(this).load(mThumbNail).into(mBinding.activityDetailIvOffer);
                mBinding.activityDetailVvOffer.setVisibility(View.GONE);
                mBinding.activityOfferProgress.setVisibility(View.GONE);
                break;
            case Constants.FILE_VIDEO:
                mBinding.activityOfferProgress.setVisibility(View.VISIBLE);
                mVideo = mFileBaseUrl + data.getFile_name();
                mBinding.activityDetailVvOffer.setVideoURI(Uri.parse(mVideo));
                mBinding.activityDetailIvOffer.setVisibility(View.GONE);
                mBinding.activityDetailVvOffer.requestFocus();
                mBinding.activityDetailVvOffer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        isPrepared = true;
                        mBinding.activityOfferProgress.setVisibility(View.GONE);
                        mVideoPos = mp.getCurrentPosition();
                        mBinding.activityDetailVvOffer.start();
                        mBinding.activityOfferIvPlayPause.setVisibility(View.VISIBLE);
                        mBinding.activityOfferIvPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.ec_play_icon));
                        mRunnable2 = new Runnable() {
                            @Override
                            public void run() {
                                mBinding.activityOfferIvPlayPause.setVisibility(View.GONE);
                            }
                        };
                        if (mHandler != null)
                            mHandler.postDelayed(mRunnable2, DISPLAY_TIME);
                    }
                });
                break;
        }
    }

    /**
     * This method is called from OfferDetailPresenter class
     *
     * @param data contains offer data
     */
    @Override
    public void setPaginationData(EcOfferListResponse.PaginationBean data) {
        mFileBaseUrl = data.getFile_url();
    }
}
