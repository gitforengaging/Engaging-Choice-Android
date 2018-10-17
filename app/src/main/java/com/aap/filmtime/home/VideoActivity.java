package com.aap.filmtime.home;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aap.filmtime.R;
import com.aap.filmtime.databinding.ActivityVideoBinding;
import com.aap.filmtime.utils.Constants;

public class VideoActivity extends AppCompatActivity {
    private ActivityVideoBinding mBinding;
    private String mUrl;
    private MediaController mc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_video);

        if (getIntent() != null && getIntent().getExtras().containsKey(Constants.MOVIE_URL)) {
            mUrl = getIntent().getExtras().getString(Constants.MOVIE_URL);
        }
        if (!TextUtils.isEmpty(mUrl)) {
            mBinding.activityVideoView.setVideoURI(Uri.parse(mUrl));
            mc = new MediaController(VideoActivity.this);
            mBinding.activityVideoView.setMediaController(mc);
            mc.setAnchorView(mBinding.activityVideoView);
            mBinding.activityVideoView.requestFocus();
            mBinding.activityVideoProgress.setVisibility(View.VISIBLE);
            mBinding.activityVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mBinding.activityVideoProgress.setVisibility(View.GONE);
                    mBinding.activityVideoView.start();
                }
            });
        }

        mBinding.activityVideoFrCrossCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mBinding.activityVideoFrCrossContLand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mBinding.activityVideoFrCrossCont.setVisibility(View.GONE);
            mBinding.activityVideoFrCrossContLand.setVisibility(View.VISIBLE);
        } else {
            mBinding.activityVideoFrCrossCont.setVisibility(View.VISIBLE);
            mBinding.activityVideoFrCrossContLand.setVisibility(View.GONE);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mBinding.activityVideoFrCrossCont.setVisibility(View.GONE);
            mBinding.activityVideoFrCrossContLand.setVisibility(View.VISIBLE);
        } else {
            mBinding.activityVideoFrCrossCont.setVisibility(View.VISIBLE);
            mBinding.activityVideoFrCrossContLand.setVisibility(View.GONE);
        }
    }

}
