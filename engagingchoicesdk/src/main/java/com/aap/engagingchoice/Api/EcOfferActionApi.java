package com.aap.engagingchoice.Api;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.aap.engagingchoice.network.HttpEcOfferActionApiThread;
import com.aap.engagingchoice.pojo.EcOfferActionReq;

/**
 * This Class is used to call HttpEcOfferActionApiThread
 */
public class EcOfferActionApi implements Handler.Callback {

    private Handler mHandler;
    private EcOfferActionReq mOfferActionReq;

    public EcOfferActionApi(EcOfferActionReq req) {
        this.mOfferActionReq = req;
    }

    public void callEcOfferActionApi() {
        mHandler = new Handler(Looper.getMainLooper(), this);
        HttpEcOfferActionApiThread thread = new HttpEcOfferActionApiThread(mHandler, mOfferActionReq);
        thread.start();
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
