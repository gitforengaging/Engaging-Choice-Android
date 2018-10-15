package com.aap.engagingchoice.network;

import com.aap.engagingchoice.offer.EcCallBackOfOfferListener;

/**
 * This is CallbackListenerclass which set listener to get callbacks after from SDK when offer is viewed by user
 */
public class CallBackListenerClass {

    private static CallBackListenerClass callBackListenerClass;
    private EcCallBackOfOfferListener mListener;

    public static CallBackListenerClass getInstance() {
        if (callBackListenerClass == null) {
            callBackListenerClass = new CallBackListenerClass();
        }
        return callBackListenerClass;
    }

    public EcCallBackOfOfferListener getmListener() {
        return mListener;
    }

    public void setmListener(EcCallBackOfOfferListener mListener) {
        this.mListener = mListener;
    }


}
