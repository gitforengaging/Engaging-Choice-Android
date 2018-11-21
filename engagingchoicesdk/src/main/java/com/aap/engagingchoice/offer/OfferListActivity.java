package com.aap.engagingchoice.offer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OfferListActivity extends AppCompatActivity implements ListenerOfEcOfferListApi, OnItemClickListener, CallbacklistenerOfOfferList, com.google.android.gms.location.LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    protected static final int REQUEST_CHECK_SETTINGS = 1000;
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    private ActivityOfferListBinding mBinding;
    private OfferListAdapter mAdapter;
    private List<EcOfferListResponse.DataBean> mOfferList = new ArrayList<>();
    private EcOfferListResponse mEcOfferResponse;
    private int mScreenH, mWidth;
    private Bundle mSavedInstance;
    private LocationManager locationManager;
    private double mLat, mLng;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private boolean isGpsOn;
    private boolean isGpsCancelled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_offer_list);
        mSavedInstance = savedInstanceState;
        setHWOfRow();
        CallBackClass.getInstance().setmListener(this);
        setRecylerViewLayouts();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mBinding.activityOfferIvLoadingLayout.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            mBinding.activityOfferIvLoadingLayout.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        createLocationRequest();
        if (mSavedInstance == null) {
            checkPermissionLoc();
        } else {
            String gpsOn = mSavedInstance.getString(Constants.isGPSON);
            String gpsCancelled = mSavedInstance.getString(Constants.isGPSCANCEELD);
            if (!TextUtils.isEmpty(gpsOn)) {
                if (gpsOn.equalsIgnoreCase("1")) {
                    isGpsOn = true;
                } else {
                    isGpsOn = false;
                }
            }
            if (!TextUtils.isEmpty(gpsCancelled)) {
                if (gpsCancelled.equalsIgnoreCase("1")) {
                    isGpsCancelled = true;
                } else {
                    isGpsCancelled = false;
                }
            }
            if (isGpsOn) {
                getData();
            } else if (isGpsCancelled) {
                getData();
            }
        }
    }

    private void getData() {
        mEcOfferResponse = (EcOfferListResponse) mSavedInstance.getSerializable(Constants.OFFER_LIST_DATA);
        if (mEcOfferResponse != null) {
            successOfferData(mEcOfferResponse);
        } else {
            if (Utils.isNetworkAvailable(this, true))
                callOfferListApi();
        }
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, OfferListActivity.this);
        Log.d("OfferlistActivity", "Location update started ..............: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private GoogleApiClient getAPIClientInstance(Context context) {
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).
                        addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        return mGoogleApiClient;
    }

    private void requestGPSSettings() {
        LocationSettingsRequest.Builder settingsBuilder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        settingsBuilder.setAlwaysShow(true);
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(this)
                .checkLocationSettings(settingsBuilder.build());
        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response =
                            task.getResult(ApiException.class);
                    assert response != null;
                    if (response.getLocationSettingsStates().isGpsUsable()) {
                        isGpsOn = true;
                        callGoogleApiClient();
                    }
                } catch (ApiException ex) {
                    switch (ex.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvableApiException =
                                        (ResolvableApiException) ex;
                                resolvableApiException
                                        .startResolutionForResult(OfferListActivity.this,
                                                REQUEST_CHECK_SETTINGS);
                                isGpsOn = false;
                            } catch (IntentSender.SendIntentException e) {

                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                            break;
                        case LocationSettingsStatusCodes.SUCCESS:
                            break;
                    }
                }
            }
        });
    }

    private void callGoogleApiClient() {
        mGoogleApiClient = getAPIClientInstance(OfferListActivity.this);
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity
                        .RESULT_OK:
                    isGpsCancelled = false;
                    isGpsOn = true;
                    callGoogleApiClient();
                    break;
                case Activity.RESULT_CANCELED:
                    isGpsCancelled = true;
                    if (mSavedInstance == null) {
                        callApi();
                    } else {
                        callOfferListApi();
                    }
                    break;
            }
        }
    }

    /**
     * Check location permission
     */
    private void checkPermissionLoc() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        } else {
            requestGPSSettings();
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
                requestGPSSettings();
            } else {
                isGpsOn = false;
                isGpsCancelled = true;
                // call offerlist api
                if (mSavedInstance == null) {
                    callApi();
                } else {
                    callOfferListApi();
                }
            }
        }
    }

    private void callApi() {
        if (mSavedInstance == null) {
            if (Utils.isNetworkAvailable(this, true)) {
                callOfferListApi();
            }
        } else {
            mEcOfferResponse = (EcOfferListResponse) mSavedInstance.getSerializable(Constants.OFFER_LIST_DATA);
            if (mEcOfferResponse != null) {
                successOfferData(mEcOfferResponse);
            } else {
                callOfferListApi();
            }
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
        Bundle bundle = new Bundle();
        if (isGpsOn) {
            outState.putString(Constants.isGPSON, "1");
        } else {
            outState.putString(Constants.isGPSON, "0");
        }
        if (isGpsCancelled) {
            outState.putString(Constants.isGPSCANCEELD, "1");
        } else {
            outState.putString(Constants.isGPSCANCEELD, "0");
        }

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
        mBinding.activityOfferProgress.setVisibility(View.VISIBLE);
        EcOfferListApi ecOfferListApi = EcOfferListApi.getInstance();
        ecOfferListApi.removeAllCallbackAndMessages();
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
     *
     * @param ecOfferListResponse contains data of offer list
     */
    @Override
    public void successOfferData(EcOfferListResponse ecOfferListResponse) {
        if (!this.isFinishing() || !this.isDestroyed() && ecOfferListResponse != null && ecOfferListResponse.getData() != null) {
            mEcOfferResponse = ecOfferListResponse;
            Log.e("datacame", "true");
            mAdapter.setOfferResponse(mEcOfferResponse);
            List<EcOfferListResponse.DataBean> ecOfferListResponseList = new ArrayList<>();
            ecOfferListResponseList.addAll(ecOfferListResponse.getData());
            mBinding.activityOfferIvLoadingLayout.setVisibility(View.GONE);
            mBinding.activityOfferProgress.setVisibility(View.GONE);
            if (ecOfferListResponseList.size() > 0) {
                mOfferList.clear();
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
                if (CallBackListenerClass.getInstance().getmListener() != null) {
                    CallBackListenerClass.getInstance().getmListener().callBackOfOffer();
                }
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
     *
     * @param msg - failiure msg
     */
    @Override
    public void failiure(String msg) {
        if (!this.isFinishing() || !this.isDestroyed() && !(TextUtils.isEmpty(msg))) {
            mBinding.activityOfferIvLoadingLayout.setVisibility(View.GONE);
            mBinding.activityOfferProgress.setVisibility(View.GONE);
            if (CallBackListenerClass.getInstance().getmListener() != null) {
                CallBackListenerClass.getInstance().getmListener().callBackOfOffer();
            }
            Log.e("OfferListActivity", msg);
            finish();
        }
    }

    /**
     * This method fires when activity is closed from backpressed of device
     */
    @Override
    public void onBackPressed() {
        if (CallBackListenerClass.getInstance().getmListener() != null) {
            CallBackListenerClass.getInstance().getmListener().callBackOfOffer();
        }
        finish();
    }

    /**
     * This method calls from adapter
     *
     * @param view view
     * @param pos  position of list
     */
    @Override
    public void onItemClick(View view, int pos) {
        callViewCountApi(pos);
        goToDetailActivity(pos);
    }

    /**
     * This method call view count api
     *
     * @param pos
     */
    private void callViewCountApi(int pos) {
        EcViewCountApi ecViewCountApi = new EcViewCountApi(mOfferList.get(pos).getId());
        ecViewCountApi.callViewCountApi();
    }

    /**
     * This method open offerdetail screen
     *
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
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            mLat = location.getLatitude();
            mLng = location.getLongitude();
            Log.e("location", String.valueOf(mLat + " " + mLng));
            if (mEcOfferResponse == null) {
                if (mSavedInstance == null) {
                    callApi();
                } else {
                    callOfferListApi();
                }
            } else {
                mBinding.activityOfferProgress.setVisibility(View.GONE);
                mBinding.activityOfferIvLoadingLayout.setVisibility(View.GONE);
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
            Log.d("OfferlistActivity", "Location update stopped .......................");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}