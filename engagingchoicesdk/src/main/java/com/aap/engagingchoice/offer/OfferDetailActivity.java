package com.aap.engagingchoice.offer;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
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
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * This Activity shows offer detail
 */
public class OfferDetailActivity extends AppCompatActivity implements OfferDetailMvpView {

    private ActivityOfferDetailBinding mBinding;
    private String mVideo, mThumbNail, mFileBaseUrl;
    private EcOfferListResponse.DataBean mOfferData;
    private Handler mHandler;
    private OfferDetailPresenter mPresenter;
    private SimpleExoPlayer player;
    private long playbackPosition;
    private int currentWindow;
    private int mFileType;
    private int count = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
     * UI will change according to orientation
     */
    private void setUiAccordingtoOrientation() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
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
        releasePlayer();
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
        mFileType = data.getFile_type();
        switch (mFileType) {
            case Constants.FILE_IMAGE:
                mThumbNail = mFileBaseUrl + data.getFile_name();
                Glide.with(this).load(mThumbNail).into(mBinding.activityDetailIvOffer);
                mBinding.activityDetailExoplayer.setVisibility(View.GONE);
                mBinding.activityOfferProgress.setVisibility(View.GONE);
                break;
            case Constants.FILE_VIDEO:
                hideSystemUi();
                mBinding.activityOfferProgress.setVisibility(View.VISIBLE);
                mBinding.activityDetailOverlay.setVisibility(View.GONE);
                mVideo = mFileBaseUrl + data.getFile_name();
                mBinding.activityDetailIvOffer.setVisibility(View.GONE);
                initializePlayer();
                break;
        }
    }

    private void hideSystemUi() {
        mBinding.activityDetailExoplayer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
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

    private void initializePlayer() {

        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());

        mBinding.activityDetailExoplayer.setPlayer(player);

        Uri uri = Uri.parse(mVideo);
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, true, false);
        player.setPlayWhenReady(true);
        player.seekTo(currentWindow, playbackPosition);


        if (Utils.isNetworkAvailable(this, false)) {
            mBinding.activityOfferProgress.setVisibility(View.VISIBLE);
        } else {
            if (count == 1) {
                Utils.showMessageS(getString(R.string.no_network), this);
            }
            count++;
            mBinding.activityOfferProgress.setVisibility(View.GONE);
        }



        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                if (Utils.isNetworkAvailable(OfferDetailActivity.this, false)) {
                    if (isLoading) {
                        mBinding.activityOfferProgress.setVisibility(View.VISIBLE);
                    }else {
                        count =1;
                        mBinding.activityOfferProgress.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                releasePlayer();
                initializePlayer();
            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
        player.addVideoListener(new SimpleExoPlayer.VideoListener() {
            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {

            }

            @Override
            public void onRenderedFirstFrame() {
                mBinding.activityOfferProgress.setVisibility(View.GONE);
            }
        });


    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            if (mFileType == Constants.FILE_VIDEO) {
                hideSystemUi();
                initializePlayer();
            }
        }
    }
}
