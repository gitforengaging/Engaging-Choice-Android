package com.aap.engagingchoice.Api;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.aap.engagingchoice.network.HttpEcContentApiThread;
import com.aap.engagingchoice.pojo.EcContentResponse;

import java.net.HttpURLConnection;
import java.util.List;

/**
 * This Class is used to call HttpEcContentApiThread
 */
public class EcContentApi implements Handler.Callback {
    private ListenerOfEcContentApi mListener;
    private Handler mHandler;

    public EcContentApi(Context context) {
    }

    /**
     * This method call Content Api via Thread
     */
    public void callEcContentApi() {
        mHandler = new Handler(Looper.getMainLooper(), this);
        HttpEcContentApiThread thread = new HttpEcContentApiThread(mHandler);
        thread.start();
    }

    /**
     * This method sets the listener ListenerOfEcContentApi to get callback of success and failiure
     * @param listenerOfResponse
     */
    public void setListenerOfResponse(ListenerOfEcContentApi listenerOfResponse) {
        mListener = listenerOfResponse;
    }

    /**
     * This method calls from HttpEcContentApiThread class
     * @param message
     * @return
     */
    @Override
    public boolean handleMessage(Message message) {
        if (message.what == HttpURLConnection.HTTP_OK) {
            List<EcContentResponse.DataBean> content = (List<EcContentResponse.DataBean>) message.getData().getSerializable("content_list");
            mListener.successData(content);
        } else {
            String failiureMsg = message.getData().getString("failiure_info");
            mListener.failiure(failiureMsg);
        }
        mHandler.removeCallbacksAndMessages(null);
        return false;

    }

}
