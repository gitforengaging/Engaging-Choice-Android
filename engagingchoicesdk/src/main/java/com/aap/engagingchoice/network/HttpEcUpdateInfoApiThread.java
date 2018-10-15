package com.aap.engagingchoice.network;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.aap.engagingchoice.pojo.EcUpdateInfoReq;
import com.aap.engagingchoice.utility.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * This class use to hit UpdateInfo Api in background
 */
public class HttpEcUpdateInfoApiThread extends Thread {

    private final EcUpdateInfoReq mUpdateReq;
    private Handler mHandler;

    public HttpEcUpdateInfoApiThread(Handler mHandler, EcUpdateInfoReq ecUpdateInfoReq) {
        this.mHandler = mHandler;
        this.mUpdateReq = ecUpdateInfoReq;
    }

    @Override
    public void run() {
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            String Baseurl = Constants.BASE_URL;
            url = new URL(Baseurl + Constants.UPDATE_INFO);
            String response = "";
            JSONObject postDataParams = new JSONObject();
            postDataParams.put(Constants.EMAIL, mUpdateReq.getEmail());
            postDataParams.put(Constants.OLD_EMAIL, mUpdateReq.getOld_email());
            postDataParams.put(Constants.MOBILE_NO, mUpdateReq.getMobile_no());
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
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.UPDATE_INFO_MESSAGE_SUCCESS, msgOfUpdateInfo);
                msg.setData(bundle);
                msg.sendToTarget();
            } else {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
                Log.e("response", response);
                msgOfUpdateInfo = new JSONObject(response).getString("detail");
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.UPDATE_INFO_MESSAGE_FAILIURE, msgOfUpdateInfo);
                msg.setData(bundle);
                msg.sendToTarget();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
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
