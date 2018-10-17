package com.aap.engagingchoice.Api;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.aap.engagingchoice.network.HttpEcOfferListApiThread;
import com.aap.engagingchoice.pojo.EcOfferListResponse;
import com.aap.engagingchoice.utility.Constants;

import java.net.HttpURLConnection;
import java.util.List;

/**
 * This Class is used to call HttpEcOfferListApiThread
 */
public class EcOfferListApi implements Handler.Callback {
    private Handler mHandler;
    private ListenerOfEcOfferListApi mOfferListDataListener;

    public EcOfferListApi() {
    }

    public void callEcOfferListApi(double lat, double lng) {
        mHandler = new Handler(Looper.getMainLooper(), this);
        HttpEcOfferListApiThread thread = new HttpEcOfferListApiThread(mHandler, lat, lng);
        thread.start();
    }

    /**
     * This method sets the listener ListenerOfEcOfferListApi to get callback of success and failiure
     *
     * @param listListener
     */
    public void setOfferListListener(ListenerOfEcOfferListApi listListener) {
        this.mOfferListDataListener = listListener;
    }

    @Override
    public boolean handleMessage(Message message) {
        if (message.what == HttpURLConnection.HTTP_OK) {
            EcOfferListResponse content = (EcOfferListResponse) message.getData().getSerializable(Constants.OFFER_LIST);
            mOfferListDataListener.successOfferData(content);
        } else {
            if (message.getData().containsKey(Constants.FAILIURE_INFO)) {
                String failiureMsg = message.getData().getString(Constants.FAILIURE_INFO);
                mOfferListDataListener.failiure(failiureMsg);
            } else if (message.getData().containsKey(Constants.FAILIURE_INFO_EXCEPTION)) {
                String failiureMsg = message.getData().getString(Constants.FAILIURE_INFO_EXCEPTION);
                if(!TextUtils.isEmpty(failiureMsg)){
                    mOfferListDataListener.failiure(failiureMsg);
                    Log.e("failiure", failiureMsg);
                }
            }
        }
        mHandler.removeCallbacksAndMessages(null);
        return false;
    }
}
