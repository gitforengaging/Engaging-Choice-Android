package com.aap.engagingchoice.Api;

import com.aap.engagingchoice.pojo.EcContentResponse;

import java.util.List;

/**
 * This is interface of ContentApi Data
 */
public interface ListenerOfEcContentApi {

    void successData(List<EcContentResponse.DataBean> ecContentResp);

    void failiure(String msg);

}
