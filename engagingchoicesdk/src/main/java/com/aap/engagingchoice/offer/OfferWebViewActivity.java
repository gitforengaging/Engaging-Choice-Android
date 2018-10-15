package com.aap.engagingchoice.offer;

import android.annotation.TargetApi;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.aap.engagingchoice.R;
import com.aap.engagingchoice.databinding.ActivityOfferWebViewBinding;
import com.aap.engagingchoice.network.CallBackListenerClass;
import com.aap.engagingchoice.utility.Constants;

/**
 * This class is used to show offer url in Webivew
 */
public class OfferWebViewActivity extends AppCompatActivity {

    private String mOfferUrl;
    private ActivityOfferWebViewBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_offer_web_view);
        if (getIntent() != null && getIntent().getExtras().containsKey(Constants.OFFER_URL)) {
            mOfferUrl = getIntent().getStringExtra(Constants.OFFER_URL);
        }
        mBinding.activityOfferWebView.getSettings().setJavaScriptEnabled(true);
        mBinding.activityOfferWebView.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(OfferWebViewActivity.this, description, Toast.LENGTH_SHORT).show();
            }

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Toast.makeText(OfferWebViewActivity.this, error.getDescription(), Toast.LENGTH_SHORT).show();
            }
        });
        mBinding.activityOfferWebView.loadUrl(mOfferUrl);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CallBackListenerClass.getInstance().getmListener().callBackOfOffer();
        finish();
    }
}
