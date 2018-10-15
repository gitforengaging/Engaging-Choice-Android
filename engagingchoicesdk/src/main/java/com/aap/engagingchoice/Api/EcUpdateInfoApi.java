package com.aap.engagingchoice.Api;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.aap.engagingchoice.network.EcUpdateInfoListener;
import com.aap.engagingchoice.network.HttpEcUpdateInfoApiThread;
import com.aap.engagingchoice.pojo.EcUpdateInfoReq;
import com.aap.engagingchoice.utility.Constants;

/**
 * This Class is used to call HttpEcUpdateInfoApiThread
 */
public class EcUpdateInfoApi implements Handler.Callback {
    private final EcUpdateInfoReq mUpdateReq;
    private final EcUpdateInfoListener mListener;
    private Handler mHandler;

    public EcUpdateInfoApi(EcUpdateInfoReq ecUpdateInfoReq, EcUpdateInfoListener listener) {
        this.mUpdateReq = ecUpdateInfoReq;
        this.mListener = listener;
    }

    /**
     * This method calls Update info api via thread
     */
    public void callUpdateInfoApi() {
        mHandler = new Handler(Looper.getMainLooper(), this);
        HttpEcUpdateInfoApiThread thread = new HttpEcUpdateInfoApiThread(mHandler, mUpdateReq);
        thread.start();
    }

    /**
     * This method called from HttpEcUpdateInfoApiThread class
     * @param message
     * @return
     */
    @Override
    public boolean handleMessage(Message message) {
        if (message.getData().containsKey(Constants.UPDATE_INFO_MESSAGE_SUCCESS)) {
            String data = message.getData().getString(Constants.UPDATE_INFO_MESSAGE_SUCCESS);
            mListener.updateSuccessFully(data);
        } else {
            String data = message.getData().getString(Constants.UPDATE_INFO_MESSAGE_FAILIURE);
            mListener.failiure(data);
        }
        return false;
    }
}
