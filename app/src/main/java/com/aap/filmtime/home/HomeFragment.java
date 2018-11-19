package com.aap.filmtime.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aap.engagingchoice.Api.ListenerOfEcContentApi;
import com.aap.engagingchoice.network.EngagingChoiceKey;
import com.aap.engagingchoice.pojo.EcContentResponse;
import com.aap.engagingchoice.utility.Utils;
import com.aap.filmtime.R;
import com.aap.filmtime.databinding.FragmentHomeBinding;
import com.aap.filmtime.home.adapters.EngagingChoiceAdapter;
import com.aap.filmtime.home.adapters.MostPopularAdapter;
import com.aap.filmtime.home.adapters.RecommendedAdapter;
import com.aap.filmtime.model.DummyContentResp;

import java.util.ArrayList;
import java.util.List;

/**
 * This is Fragment which shows the Engaging Choice SDK Data called Content list
 * ListenerOfEcContentApi - This is the interface which is implemented to get data from SDK
 */
public class HomeFragment extends Fragment implements OnItemClickListener, HomeFragMvpView, ListenerOfEcContentApi {
    private FragmentHomeBinding mBinding;
    private List<DummyContentResp> mDummyList = new ArrayList<>();
    private EngagingChoiceAdapter mAdapter;
    private MostPopularAdapter mMostPopularAdapter;
    private RecommendedAdapter mAdapterR;
    private HomeFragPresenter mHomeFragPresenter;
    private List<EcContentResponse.DataBean> mEngagingChoiceList = new ArrayList<>();

    public HomeFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new EngagingChoiceAdapter(getActivity(), mEngagingChoiceList);
        mMostPopularAdapter = new MostPopularAdapter(getActivity(), mDummyList);
        mAdapterR = new RecommendedAdapter(getActivity(), mDummyList);
        mAdapter.setListener(this);
        mMostPopularAdapter.setListener(this);
        mAdapterR.setListener(this);
        mHomeFragPresenter = new HomeFragPresenter(getActivity());
        mHomeFragPresenter.setListener(this);
        mHomeFragPresenter.setDummyContentResp();
        mBinding.fragmentHomeRecylerView.setAdapter(mAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManagerP = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManagerR = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManagerN = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mBinding.fragmentHomeNewReleaseDummyCont.setVisibility(View.VISIBLE);
        mBinding.fragmentHomeEngagingChoiceCont.setVisibility(View.GONE);
        mBinding.fragmentHomeRecylerViewNewRelease.setLayoutManager(linearLayoutManagerN);
        mBinding.fragmentHomeRecylerView.setLayoutManager(linearLayoutManager);
        mBinding.fragmentHomeRecylerViewMostP.setLayoutManager(linearLayoutManagerP);
        mBinding.fragmentHomeRecylerViewNewRelease.setAdapter(mMostPopularAdapter);
        mBinding.fragmentHomeRecylerViewMostP.setAdapter(mMostPopularAdapter);
        mBinding.fragmentHomeRecylerViewRecommended.setLayoutManager(linearLayoutManagerR);
        mBinding.fragmentHomeRecylerViewRecommended.setAdapter(mAdapterR);
        mBinding.fragmentHomeRecylerView.setNestedScrollingEnabled(false);
        mBinding.fragmentHomeRecylerViewMostP.setNestedScrollingEnabled(false);
        mBinding.fragmentHomeRecylerViewRecommended.setNestedScrollingEnabled(false);
        callContentlistApi();

        // pull to refresh
        mBinding.frgamentHomeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callContentlistApi();
            }
        });
    }

    /**
     * call content list api of Engaging Choice content - SDK
     */
    private void callContentlistApi() {
        if (Utils.isNetworkAvailable(getActivity(), true)) {
            mHomeFragPresenter.callContentListApi(this);
        }
    }

    /**
     * This method is called from Dummy Data Adapter
     *
     * @param view
     * @param pos
     */
    @Override
    public void onClick(View view, int pos) {
        switch (view.getId()) {
            case R.id.row_of_engaging_ec_view:
                EngagingChoiceKey.getInstance().setContentId(-1);
                mHomeFragPresenter.goToDetailActivity(getActivity(), false, mEngagingChoiceList, mDummyList, pos);
                break;
        }
    }

    /**
     * This method is called from Adapter
     * After click on Engaging choice SDK Content view then user will navigate to Detail screen
     *
     * @param view
     * @param pos
     */
    @Override
    public void onEngagingChoiceClick(View view, int pos) {
        switch (view.getId()) {
            case R.id.row_of_engaging_ec_view:
                EngagingChoiceKey.getInstance().setContentId(mEngagingChoiceList.get(pos).getId());
                mHomeFragPresenter.goToDetailActivity(getActivity(), true, mEngagingChoiceList, mDummyList, pos);
                break;
        }

    }


    @Override
    public void getDummyDataResp(List<DummyContentResp> dummyDataResp) {
        mDummyList.addAll(dummyDataResp);
        mAdapter.notifyDataSetChanged();
        mAdapterR.notifyDataSetChanged();
        mMostPopularAdapter.notifyDataSetChanged();
    }

    /**
     * This method is called from Engaging Choice SDK
     *
     * @param list - contains the Content list of Engaging Choice SDK
     */
    @Override
    public void successData(List<EcContentResponse.DataBean> list) {
        mBinding.frgamentHomeRefreshLayout.setRefreshing(false);
        if (list.size() > 0) {
            mBinding.fragmentHomeNewReleaseDummyCont.setVisibility(View.GONE);
            mBinding.fragmentHomeEngagingChoiceCont.setVisibility(View.VISIBLE);
            mEngagingChoiceList.clear();
            mEngagingChoiceList.addAll(list);
            mAdapter.notifyDataSetChanged();
        } else {
            mBinding.fragmentHomeTvEngagingChoice.setVisibility(View.GONE);
            mBinding.fragmentHomeNewReleaseDummyCont.setVisibility(View.VISIBLE);
        }

    }

    /**
     * This method is called from Enagaging Choice SDK
     *
     * @param s contains msg if Content list api fails
     */
    @Override
    public void failiure(String s) {
        mEngagingChoiceList.clear();
        mBinding.frgamentHomeRefreshLayout.setRefreshing(false);
        mBinding.fragmentHomeNewReleaseDummyCont.setVisibility(View.VISIBLE);
        mBinding.fragmentHomeEngagingChoiceCont.setVisibility(View.GONE);
        mBinding.fragmentHomeTvEngagingChoice.setVisibility(View.GONE);
//        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }
}
