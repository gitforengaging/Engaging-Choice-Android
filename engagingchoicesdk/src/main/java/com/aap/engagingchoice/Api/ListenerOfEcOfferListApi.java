package com.aap.engagingchoice.Api;

import com.aap.engagingchoice.pojo.EcOfferListResponse;

import java.util.List;

/**
 * This is interface of Offerlist Data
 */
public interface ListenerOfEcOfferListApi {
    void successOfferData(EcOfferListResponse ecOfferListResponseList);

    void failiure(String msg);
}
