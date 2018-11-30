package com.aap.filmtime.home.detail;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.aap.engagingchoice.network.CallBackListenerClass;
import com.aap.engagingchoice.offer.EcCallBackOfOfferListener;
import com.aap.engagingchoice.offer.OfferListActivity;
import com.aap.filmtime.R;
import com.aap.filmtime.databinding.ActivityDetailBinding;
import com.aap.filmtime.home.VideoActivity;
import com.aap.filmtime.model.DummyContentResp;
import com.aap.filmtime.utils.Constants;
import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener, EcCallBackOfOfferListener {
    private ActivityDetailBinding mBinding;
    private DummyContentResp mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        mBinding.detailFrCrossCont.setOnClickListener(this);
        mBinding.detailPlayIcon.setOnClickListener(this);
        // This is called to set listener to get callback when offer work is done and perfrom your task
        CallBackListenerClass.getInstance().setmListener(this);
        if (getIntent() != null && getIntent().getExtras().containsKey(Constants.CONTENT_DATA)) {
            mData = (DummyContentResp) getIntent().getSerializableExtra(Constants.CONTENT_DATA);
            setData();
        }
    }

    /**
     * This method sets data which is recieved from Engaging choice SDK - Content of Engaging Choice
     */
    private void setData() {
        Glide.with(this).load(mData.getUrl()).into(mBinding.detailIvCover);
        mBinding.detailTvTitle.setText(mData.getName());
        if (mData.isPoweredBy()) {
            mBinding.detailTvPoweredBy.setVisibility(View.VISIBLE);
        } else {
            mBinding.detailTvPoweredBy.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail_fr_cross_cont:
                finish();
                break;
            case R.id.detail_play_icon:
                if (mData.isPoweredBy()) {
                    goToOfferListActivity();
                } else if (!TextUtils.isEmpty(mData.getVideoUrl())) {
                    playVideo();
                }
                break;
        }
    }

    /**
     * This method open VideoScreen which contains the video url which is provided by EngagingChoice SDK
     */
    private void playVideo() {
        Intent intent = new Intent(this, VideoActivity.class);
        intent.putExtra(Constants.MOVIE_URL, mData.getVideoUrl());
        startActivity(intent);
    }

    /**
     * This method open offerlist screen of SDK
     */
    private void goToOfferListActivity() {
        Intent intent = new Intent(this, OfferListActivity.class);
        startActivity(intent);
    }

    /**
     * This method is of SDK which gives callback after offer viewed by user
     */
    @Override
    public void callBackOfOffer() {
        if (mData.isPoweredBy() && !TextUtils.isEmpty(mData.getVideoUrl())) {
            playVideo();
        }

    }
}
