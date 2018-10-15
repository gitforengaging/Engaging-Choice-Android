package com.aap.engagingchoice.network;

/**
 * This is interface of UpdateInfo Api
 */
public interface EcUpdateInfoListener {

    void updateSuccessFully(String msg);

    void failiure(String msg);
}
