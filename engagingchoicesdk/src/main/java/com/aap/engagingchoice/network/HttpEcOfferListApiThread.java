package com.aap.engagingchoice.network;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.aap.engagingchoice.pojo.EcOfferListResponse;
import com.aap.engagingchoice.utility.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * This class use to hit OfferList Api in background
 */
public class HttpEcOfferListApiThread extends Thread {
    private final double mLng, mLat;
    private Handler mHandler;

    public HttpEcOfferListApiThread(Handler mHandler, double lat, double lng) {
        this.mHandler = mHandler;
        this.mLat = lat;
        this.mLng = lng;
    }

    @Override
    public void run() {
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            String email = EngagingChoiceKey.getInstance().getEmailId();
            if (!TextUtils.isEmpty(email)) {
                Log.e("email", email);
            }

            url = new URL(Constants.BASE_URL + Constants.OFFER_LIST_API + "/" + email);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(Constants.REQUEST_TYPE_GET);
            String publishSecretKey = EngagingChoiceKey.getInstance().getPublishSecretKey();
            if (!TextUtils.isEmpty(publishSecretKey)) {
                Log.e("Token", ":" + publishSecretKey);
            } else {
                Log.e("Token", ":" + "TokenNull");
                return;
            }
            urlConnection.setRequestProperty(Constants.TOKEN, EngagingChoiceKey.getInstance().getPublishSecretKey());
            InputStream in = urlConnection.getInputStream();
            int responseCode = urlConnection.getResponseCode();
            Message msg = mHandler.obtainMessage();
            msg.what = responseCode;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String resp = readStream(in);
                EcOfferListResponse bean = new EcOfferListResponse();
                EcOfferListResponse.PaginationBean eCDataBean = new EcOfferListResponse.PaginationBean();
                JSONObject jsonObjectPagnation = new JSONObject(resp).getJSONObject("pagination");
                eCDataBean.setNum_pages(jsonObjectPagnation.getInt("num_pages"));
                eCDataBean.setJoin_days(jsonObjectPagnation.getInt("join_days"));
                eCDataBean.setIs_popup(jsonObjectPagnation.getInt("is_popup"));
                eCDataBean.setIs_other_user(jsonObjectPagnation.getInt("is_other_user"));
                eCDataBean.setPer_page(jsonObjectPagnation.getInt("per_page"));
                eCDataBean.setFile_url(jsonObjectPagnation.getString("file_url"));
                eCDataBean.setCount(jsonObjectPagnation.getInt("count"));
                JSONArray jsonArray = new JSONObject(resp).getJSONArray("data");
                List<EcOfferListResponse.DataBean> list = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    EcOfferListResponse.DataBean eCcontent = new EcOfferListResponse.DataBean();
                    eCcontent.setId(jsonArray.getJSONObject(i).optInt("id"));
                    eCcontent.setTracking_number(jsonArray.getJSONObject(i).optString("tracking_number"));
                    eCcontent.setOffer_title(jsonArray.getJSONObject(i).optString("offer_title"));
                    eCcontent.setOffer_description(jsonArray.getJSONObject(i).optString("offer_description"));
                    eCcontent.setOffer_start_date(jsonArray.getJSONObject(i).optString("offer_start_date"));
                    eCcontent.setOffer_end_date(jsonArray.getJSONObject(i).optString("offer_end_date"));
                    eCcontent.setOffer_url(jsonArray.getJSONObject(i).optString("offer_url"));
                    eCcontent.setOffer_budget(jsonArray.getJSONObject(i).optString("offer_budget"));
                    eCcontent.setPrice_engagement(jsonArray.getJSONObject(i).optString("price_engagement"));
                    eCcontent.setCoupon_code(jsonArray.getJSONObject(i).optString("coupon_code"));
                    eCcontent.setNumber_of_uses(jsonArray.getJSONObject(i).optInt("number_of_uses"));
                    eCcontent.setOffer_uses_time(jsonArray.getJSONObject(i).optInt("offer_uses_time"));
                    eCcontent.setStatus(jsonArray.getJSONObject(i).optInt("status"));
                    eCcontent.setFile_name(jsonArray.getJSONObject(i).optString("file_name"));
                    eCcontent.setFile_type(jsonArray.getJSONObject(i).optInt("file_type"));
                    eCcontent.setDiscount(jsonArray.getJSONObject(i).optString("discount"));
                    eCcontent.setDiscount_type(jsonArray.getJSONObject(i).optInt("discount_type"));
                    eCcontent.setCover_image(jsonArray.getJSONObject(i).optString("cover_image"));
                    list.add(eCcontent);
                }
                bean.setData(list);
                bean.setPagination(eCDataBean);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.OFFER_LIST, (Serializable) bean);
                msg.setData(bundle);
                msg.sendToTarget();
            } else {
                InputStream errorInputStream = urlConnection.getErrorStream();
                // failiure in api
                Message msg1 = mHandler.obtainMessage();
                msg1.what = -1;
                Bundle bundle = new Bundle();
                String resp = readStream(errorInputStream);
                bundle.putString(Constants.FAILIURE_INFO, resp);
                msg1.setData(bundle);
                msg1.sendToTarget();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Message msg = mHandler.obtainMessage();
            msg.what = -1;
            Bundle bundle = new Bundle();
            bundle.putString(Constants.FAILIURE_INFO, e.getMessage());
            msg.setData(bundle);
            msg.sendToTarget();
            e.printStackTrace();
        } catch (IOException e) {
            Message msg = mHandler.obtainMessage();
            msg.what = -1;
            Bundle bundle = new Bundle();
            bundle.putString(Constants.FAILIURE_INFO, e.getMessage());
            msg.sendToTarget();
            msg.setData(bundle);
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}
