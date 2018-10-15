package com.aap.engagingchoice.offer;

import com.aap.engagingchoice.pojo.EcOfferListResponse;

/**
 * This is the interface of MVP view used in offer Detail screen
 */
public interface OfferDetailMvpView {
    void setData(EcOfferListResponse.DataBean data);

    void setPaginationData(EcOfferListResponse.PaginationBean data);
}
