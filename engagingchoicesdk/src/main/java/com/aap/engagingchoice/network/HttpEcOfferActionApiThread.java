package com.aap.engagingchoice.network;


import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.aap.engagingchoice.pojo.EcOfferActionReq;
import com.aap.engagingchoice.utility.Constants;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * This class use to hit OfferAction Api in background
 */
public class HttpEcOfferActionApiThread extends Thread {
    private Handler mHandler;
    private EcOfferActionReq mRequestParam;

    public HttpEcOfferActionApiThread(Handler handler, EcOfferActionReq req) {
        this.mHandler = handler;
        this.mRequestParam = req;
    }

    @Override
    public void run() {
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            String Baseurl = Constants.BASE_URL;
            url = new URL(Baseurl + Constants.OFFER_ACTION);
            String response = "";
            JSONObject postDataParams = new JSONObject();
            postDataParams.put(Constants.EMAIL, mRequestParam.getEmail());
            postDataParams.put(Constants.OFFER_ID, mRequestParam.getOffer_id());
            postDataParams.put(Constants.ACTION, mRequestParam.getAction());
            postDataParams.put(Constants.CONTENT_ID, mRequestParam.getContent_id());

            urlConnection = (HttpURLConnection) url.openConnection();
            String publishSecretKey = EngagingChoiceKey.getInstance().getPublishSecretKey();
            if (!TextUtils.isEmpty(publishSecretKey)) {
                Log.e("Token", ":" + publishSecretKey);
            } else {
                Log.e("Token", ":" + "TokenNull");
                return;
            }
            urlConnection.setRequestProperty(Constants.TOKEN, EngagingChoiceKey.getInstance().getPublishSecretKey());
            urlConnection.setUseCaches(false);
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            /*write req param*/
            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            os.close();
            int responseCode = urlConnection.getResponseCode();
            Message msg = mHandler.obtainMessage();
            msg.what = responseCode;
            String msgOfUpdateInfo = "";
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
                msgOfUpdateInfo = new JSONObject(response).getString("detail");
            } else {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
                Log.e("responseerror", response);
            }
        } catch (Exception e) {

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}
