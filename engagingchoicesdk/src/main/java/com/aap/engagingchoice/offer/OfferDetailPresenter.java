package com.aap.engagingchoice.offer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.aap.engagingchoice.Api.EcOfferActionApi;
import com.aap.engagingchoice.network.CallBackListenerClass;
import com.aap.engagingchoice.network.EngagingChoiceKey;
import com.aap.engagingchoice.pojo.EcOfferActionReq;
import com.aap.engagingchoice.pojo.EcOfferListResponse;
import com.aap.engagingchoice.utility.CallBackClass;
import com.aap.engagingchoice.utility.Constants;

/**
 * This class is presenter of OfferDetail screen which contains all logic and give callback to offerdetail
 */
public class OfferDetailPresenter {

    private Activity mActivity;
    private OfferDetailMvpView mMvpListener;

    public OfferDetailPresenter(Activity activity) {
        this.mActivity = activity;
    }

    /**
     * Set listener of MVP Pattern
     * @param mvpView
     */
    public void setListener(OfferDetailMvpView mvpView) {
        this.mMvpListener = mvpView;
    }

    /**
     * This method get data from intent from offerlist screen
     */
    public void getDataFromIntent() {
        if (mActivity.getIntent().getExtras().containsKey(Constants.OFFER_INFO)) {
            EcOfferListResponse.DataBean data = (EcOfferListResponse.DataBean) mActivity.getIntent().getSerializableExtra(Constants.OFFER_INFO);
            EcOfferListResponse.PaginationBean paginationBean = (EcOfferListResponse.PaginationBean) mActivity.getIntent().getSerializableExtra(Constants.OFFER_PAGINATION_DATA);
            mMvpListener.setPaginationData(paginationBean);
            mMvpListener.setData(data);
        }
    }

    /**
     * Call Offer Action Api
     * @param typeOfCount
     * @param mOfferData
     */
    public void callActionApi(int typeOfCount, EcOfferListResponse.DataBean mOfferData) {
        EcOfferActionReq ecOfferActionReq = new EcOfferActionReq();
        ecOfferActionReq.setAction(typeOfCount);
        ecOfferActionReq.setEmail(EngagingChoiceKey.getInstance().getEmailId());
        ecOfferActionReq.setOffer_id(mOfferData.getId());
        ecOfferActionReq.setContent_id(EngagingChoiceKey.getInstance().getContentId());
        EcOfferActionApi ecOfferActionApi = new EcOfferActionApi(ecOfferActionReq);
        ecOfferActionApi.callEcOfferActionApi();
        CallBackClass.getInstance().getmListener().callBackListenerToOfferList();
        if (typeOfCount != Constants.CONSUMED) {
            if (CallBackListenerClass.getInstance().getmListener() != null) {
                CallBackListenerClass.getInstance().getmListener().callBackOfOffer();
            }
        }
    }

    /**
     * This method open Offer url in webview
     * @param mOfferData - contains offer data
     */
    public void openWebView(EcOfferListResponse.DataBean mOfferData) {
        Intent intent = new Intent(mActivity, OfferWebViewActivity.class);
        intent.putExtra(Constants.OFFER_URL, mOfferData.getOffer_url());
        mActivity.startActivity(intent);
    }
}
