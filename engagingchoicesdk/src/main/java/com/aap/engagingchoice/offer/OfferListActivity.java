package com.aap.engagingchoice.offer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.aap.engagingchoice.Api.EcOfferListApi;
import com.aap.engagingchoice.Api.EcViewCountApi;
import com.aap.engagingchoice.Api.ListenerOfEcOfferListApi;
import com.aap.engagingchoice.ProfileActivity;
import com.aap.engagingchoice.R;
import com.aap.engagingchoice.databinding.ActivityOfferListBinding;
import com.aap.engagingchoice.network.CallBackListenerClass;
import com.aap.engagingchoice.pojo.EcOfferListResponse;
import com.aap.engagingchoice.utility.CallBackClass;
import com.aap.engagingchoice.utility.CallbacklistenerOfOfferList;
import com.aap.engagingchoice.utility.Constants;
import com.aap.engagingchoice.utility.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OfferListActivity extends AppCompatActivity implements ListenerOfEcOfferListApi, OnItemClickListener, CallbacklistenerOfOfferList, LocationListener {


    private ActivityOfferListBinding mBinding;
    private OfferListAdapter mAdapter;
    private List<EcOfferListResponse.DataBean> mOfferList = new ArrayList<>();
    private EcOfferListResponse mEcOfferResponse;
    private int mScreenH, mWidth;
    private Bundle mSavedInstance;
    private LocationManager locationManager;
    private double mLat, mLng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_offer_list);
        setHWOfRow();
        CallBackClass.getInstance().setmListener(this);
        setRecylerViewLayouts();
        mSavedInstance = savedInstanceState;
        if (savedInstanceState == null) {
            if (Utils.isNetworkAvailable(this, true)) {
                callOfferListApi();
            }
        } else {
            mEcOfferResponse = (EcOfferListResponse) savedInstanceState.getSerializable(Constants.OFFER_LIST_DATA);
            successOfferData(mEcOfferResponse);
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mBinding.activityOfferIvLoadingLayout.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            mBinding.activityOfferIvLoadingLayout.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        checkPermissionLoc();
    }

    /**
     * Check location permission
     */
    private void checkPermissionLoc() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        } else {
            getLocation();
        }
    }

    /**
     * Request permission result of location
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                // call offerlist api
            }
        }
    }

    /**
     * This method is used to get location
     */
    public void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            assert locationManager != null;
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to save data while orientation
     *
     * @param outState - bundle
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Constants.OFFER_LIST_DATA, mEcOfferResponse);
    }

    /**
     * This method is used to set height and width for offerlist
     */
    private void setHWOfRow() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        mWidth = displayMetrics.widthPixels;
        int toolbarH = (int) (getResources().getDimension(R.dimen.dp_74));
        mScreenH = height - toolbarH;

    }

    /**
     * This method is used to call offer list api
     */
    private void callOfferListApi() {
        mBinding.activityOfferIvLoadingLayout.setVisibility(View.VISIBLE);
        EcOfferListApi ecOfferListApi = new EcOfferListApi();
        ecOfferListApi.callEcOfferListApi(mLat, mLng);
        ecOfferListApi.setOfferListListener(this);
    }

    /**
     * set layouts of recylerview
     */
    private void setRecylerViewLayouts() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mBinding.activityRecylerOffer.setLayoutManager(linearLayoutManager);
        mAdapter = new OfferListAdapter(this, mOfferList);
        mAdapter.setHeightWidth(mScreenH, mWidth);
        mAdapter.setListener(this);
        mBinding.activityRecylerOffer.setAdapter(mAdapter);
        mBinding.activityOfferIvCrossIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * This method is fired from Ecofferlist api class
     * @param ecOfferListResponse contains data of offer list
     */
    @Override
    public void successOfferData(EcOfferListResponse ecOfferListResponse) {
        if (!this.isFinishing() || !this.isDestroyed()) {
            mEcOfferResponse = ecOfferListResponse;
            mAdapter.setOfferResponse(mEcOfferResponse);
            List<EcOfferListResponse.DataBean> ecOfferListResponseList = new ArrayList<>();
            ecOfferListResponseList.addAll(ecOfferListResponse.getData());
            mBinding.activityOfferIvLoadingLayout.setVisibility(View.GONE);
            if (ecOfferListResponseList.size() > 0) {
                mOfferList.addAll(ecOfferListResponseList);
                mAdapter.notifyDataSetChanged();
                if (ecOfferListResponse.getPagination().getIs_popup() == Constants.DIALOG_OPEN) {
                    if (mSavedInstance == null) {
                        openProfileActivity();
                    }
                }
            } else {
                mBinding.activityRecylerOffer.setVisibility(View.GONE);
                mBinding.activityTvNoOffers.setVisibility(View.VISIBLE);
                CallBackListenerClass.getInstance().getmListener().callBackOfOffer();
                finish();
            }

        }
    }

    /**
     * This method open profile screen
     */
    private void openProfileActivity() {
        if (!isFinishing()) {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra(Constants.USER_TYPE, mEcOfferResponse.getPagination().getIs_other_user());
            startActivity(intent);
        }
    }

    /**
     * This method fires from Ecofferlist api class which triger when failiure come while hitting api
     * @param msg - failiure msg
     */
    @Override
    public void failiure(String msg) {
        mBinding.activityOfferIvLoadingLayout.setVisibility(View.GONE);
        CallBackListenerClass.getInstance().getmListener().callBackOfOffer();
        Log.e("OfferListActivity", msg);
        finish();
    }

    /**
     * This method fires when activity is closed from backpressed of device
     */
    @Override
    public void onBackPressed() {
        CallBackListenerClass.getInstance().getmListener().callBackOfOffer();
        finish();
    }

    /**
     * This method calls from adapter
     * @param view view
     * @param pos position of list
     */
    @Override
    public void onItemClick(View view, int pos) {
        callViewCountApi(pos);
        goToDetailActivity(pos);
    }

    /**
     * This method call view count api
     * @param pos
     */
    private void callViewCountApi(int pos) {
        EcViewCountApi ecViewCountApi = new EcViewCountApi(mOfferList.get(pos).getId());
        ecViewCountApi.callViewCountApi();
    }

    /**
     * This method open offerdetail screen
     * @param pos
     */
    private void goToDetailActivity(int pos) {
        Intent intent = new Intent(this, OfferDetailActivity.class);
        intent.putExtra(Constants.OFFER_INFO, (Serializable) mOfferList.get(pos));
        intent.putExtra(Constants.OFFER_PAGINATION_DATA, (mEcOfferResponse.getPagination()));
        startActivity(intent);
    }

    /**
     * This method calls when offer actions performed from offerdetail screen and webview screen
     */
    @Override
    public void callBackListenerToOfferList() {
        finish();
    }

    /**
     * This method will get current location
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            mLat = location.getLatitude();
            mLng = location.getLongitude();
            Log.e("location", String.valueOf(mLat + " " + mLng));
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
