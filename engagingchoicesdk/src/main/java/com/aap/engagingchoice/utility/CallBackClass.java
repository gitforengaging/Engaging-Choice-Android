package com.aap.engagingchoice.utility;

public class CallBackClass {

    private static CallBackClass mInstance;
    private CallbacklistenerOfOfferList mListener;

    public static CallBackClass getInstance() {
        if (mInstance == null) {
            mInstance = new CallBackClass();
        }
        return mInstance;
    }

    public CallbacklistenerOfOfferList getmListener() {
        return mListener;
    }

    public void setmListener(CallbacklistenerOfOfferList mListener) {
        this.mListener = mListener;
    }

}
