package com.aap.engagingchoice.Api;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.aap.engagingchoice.network.HttpEcUpdateInfoApiThread;
import com.aap.engagingchoice.network.HttpEcViewCountApiThread;
import com.aap.engagingchoice.utility.Constants;

/**
 * This Class is used to call HttpEcViewCountApiThread
 */
public class EcViewCountApi implements Handler.Callback {
    private int mContentId;
    private Handler mHandler;

    public EcViewCountApi(int id) {
        this.mContentId = id;
    }

    /**
     * This method calls view count api via thread
     */
    public void callViewCountApi() {
        mHandler = new Handler(Looper.getMainLooper(), this);
        HttpEcViewCountApiThread thread = new HttpEcViewCountApiThread(mHandler, mContentId);
        thread.start();
    }

    @Override
    public boolean handleMessage(Message message) {
        if (message.getData().containsKey(Constants.VIEW_COUNT_MSG_SUCCESS)) {

        } else {

        }
        return false;
    }
}
