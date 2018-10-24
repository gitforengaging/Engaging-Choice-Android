package com.aap.engagingchoice.Api;


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
public class EcMediaContent implements Handler.Callback {
    private static EcMediaContent mEcContentApi;
    private ListenerOfEcContentApi mListener;
    private Handler mHandler;
    private int limit, offset;

    public static EcMediaContent getInstance() {
        if (mEcContentApi == null) {
            mEcContentApi = new EcMediaContent();
        }
        return mEcContentApi;
    }

    /**
     * This method call Content Api via Thread
     */
    public void callEcContentApi() {
        mHandler = new Handler(Looper.getMainLooper(), this);
        HttpEcContentApiThread thread = new HttpEcContentApiThread(mHandler, limit, offset);
        thread.start();
    }

    /**
     * This method sets the listener ListenerOfEcContentApi to get callback of success and failiure
     *
     * @param listenerOfResponse
     */
    public void setListenerOfResponse(ListenerOfEcContentApi listenerOfResponse) {
        mListener = listenerOfResponse;
    }

    /**
     * This method calls from HttpEcContentApiThread class
     *
     * @param message
     * @return
     */
    @Override
    public boolean handleMessage(Message message) {
        if (message.what == HttpURLConnection.HTTP_OK) {
            List<EcContentResponse.DataBean> content = (List<EcContentResponse.DataBean>) message.getData().getSerializable("content_list");
            if (mListener != null)
                mListener.successData(content);
        } else {
            String failiureMsg = message.getData().getString("failiure_info");
            if (mListener != null)
                mListener.failiure(failiureMsg);
        }
        mHandler.removeCallbacksAndMessages(null);
        return false;

    }

    public void setPageLimit(int offset, int limit) {
        this.limit = limit;
        this.offset = offset;
    }

}
