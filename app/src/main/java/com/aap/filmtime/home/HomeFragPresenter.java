package com.aap.filmtime.home;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.aap.engagingchoice.Api.EcContentApi;
import com.aap.engagingchoice.Api.ListenerOfEcContentApi;
import com.aap.engagingchoice.pojo.EcContentResponse;
import com.aap.filmtime.R;
import com.aap.filmtime.home.detail.DetailActivity;
import com.aap.filmtime.model.DummyContentResp;
import com.aap.filmtime.utils.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomeFragPresenter {
    private final Context mContext;
    private List<DummyContentResp> mListOfDummyModel = new ArrayList<>();
    private HomeFragMvpView mMvpListener;

    public HomeFragPresenter(Context context) {
        this.mContext = context;
    }

    public List<String> addAllUrls() {
        List<String> urlList = new ArrayList<>();

//        urlList.add("https://i.pinimg.com/236x/e2/ce/03/e2ce03889615b5d5f6dd06f0cb3345f8.jpg");
        urlList.add("https://momsall.com/wp-content/uploads/2018/04/The-Boss-Baby.jpg");
        urlList.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQHD-CZiPZt-bd95akXKDMqICVaxGVYr0GAbN49spvbN0EpznJ2ZQ");
        urlList.add("https://lumiere-a.akamaihd.net/v1/images/open-uri20160107-21163-1uluvkw_9a643c10.jpeg");
        urlList.add("https://lumiere-a.akamaihd.net/v1/images/r_thegooddinosaur_header_bcfd18b3.jpeg?region=0,0,2048,808");
        urlList.add("https://d13ezvd6yrslxm.cloudfront.net/wp/wp-content/images/megamind-trailer-3.jpg");
//        urlList.add("https://i.pinimg.com/236x/fe/7a/d4/fe7ad4215e8b11f760b3c1c85af09a9e--mountain-illustration-mountain-landscape.jpg");
//        urlList.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTaGx8CeLPJ7jekirBvPuN1sumFNerdoTx9pM3SpB6dmhYADAmY");

        return urlList;
    }

    public List<String> names() {
        List<String> movieNameList = new ArrayList<>();
        movieNameList.add("Boss Babby");
        movieNameList.add("How to Train Your Dragon");
        movieNameList.add("Rapunzel");
        movieNameList.add("The Good Dinosaur");
        movieNameList.add("Megamind");
        return movieNameList;
    }

    public List<String> sponsorBy() {
        List<String> sponsorByList = new ArrayList<>();
        sponsorByList.add("by DreamWorksTV");
        sponsorByList.add("by DreamWorks Animation");
        sponsorByList.add("by Disney");
        sponsorByList.add("by Walt Disney Pictures");
        sponsorByList.add("by DreamWorks Animation");
        return sponsorByList;
    }

    public void setDummyContentResp() {
        List<String> list = addAllUrls();
        for (int i = 0; i < list.size(); i++) {
            DummyContentResp dummyContentResp = new DummyContentResp();
            dummyContentResp.setUrl(list.get(i));
            dummyContentResp.setName(names().get(i));
            dummyContentResp.setTitle(names().get(i));
            dummyContentResp.setSponsoredBy(sponsorBy().get(i));
            if (i % 2 == 0) {
                dummyContentResp.setPoweredBy(true);
            } else {
                dummyContentResp.setPoweredBy(false);
            }

            mListOfDummyModel.add(dummyContentResp);
        }

        mMvpListener.getDummyDataResp(mListOfDummyModel);
    }

    public void setListener(HomeFragMvpView fragMvpView) {
        this.mMvpListener = fragMvpView;
    }

    public void callContentListApi(ListenerOfEcContentApi listener, Context context) {
        EcContentApi ecContentApi = new EcContentApi(context);
        ecContentApi.callEcContentApi();
        ecContentApi.setListenerOfResponse(listener);
    }


    public void goToDetailActivity(FragmentActivity activity, boolean isEngagingChoice, List<EcContentResponse.DataBean> mEngagingChoiceList, List<DummyContentResp> mDummyList, int pos) {
        Intent intent = new Intent(activity, DetailActivity.class);
        if (isEngagingChoice) {
            DummyContentResp dummyContentResp1 = new DummyContentResp();
            dummyContentResp1.setTitle(mEngagingChoiceList.get(pos).getContent_title());
            if (mDummyList.size() > mEngagingChoiceList.size()) {
                dummyContentResp1.setSponsoredBy(mDummyList.get(pos).getSponsoredBy());
            } else {
                dummyContentResp1.setSponsoredBy(mContext.getString(R.string.by_dreamsworks_tv));
            }

            dummyContentResp1.setVideoUrl(mEngagingChoiceList.get(pos).getFile_name());
            dummyContentResp1.setDescription(mEngagingChoiceList.get(pos).getContent_description());
            dummyContentResp1.setName(mEngagingChoiceList.get(pos).getContent_title());
            dummyContentResp1.setUrl(mEngagingChoiceList.get(pos).getCover_image());
            dummyContentResp1.setPoweredBy(true);
            intent.putExtra(Constants.CONTENT_DATA, (Serializable) dummyContentResp1);
        } else {
            intent.putExtra(Constants.CONTENT_DATA, (Serializable) mDummyList.get(pos));
        }
        mContext.startActivity(intent);

    }
}
