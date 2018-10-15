package com.aap.engagingchoice.network;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.aap.engagingchoice.pojo.EcContentResponse;
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
 * This class use to hit Content Api in background
 */
public class HttpEcContentApiThread extends Thread {
    private Handler mHandler;

    public HttpEcContentApiThread(Handler mHandler) {
        this.mHandler = mHandler;
    }


    @Override
    public void run() {
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            String Baseurl = Constants.BASE_URL;
            url = new URL(Baseurl + Constants.CONTENT_LIST_API);
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
            String resp = "";
            if (responseCode == HttpURLConnection.HTTP_OK) {
                resp = readStream(in);
                JSONArray jsonArray = new JSONObject(resp).getJSONArray("data");
                List<EcContentResponse.DataBean> list = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    EcContentResponse.DataBean eCcontent = new EcContentResponse.DataBean();
                    eCcontent.setContent_description(jsonArray.getJSONObject(i).optString("content_description"));
                    eCcontent.setContent_end_date(jsonArray.getJSONObject(i).optString("content_end_date"));
                    eCcontent.setContent_start_date(jsonArray.getJSONObject(i).optString("content_start_date"));
                    eCcontent.setContent_title(jsonArray.getJSONObject(i).optString("content_title"));
                    eCcontent.setCover_image(jsonArray.getJSONObject(i).optString("cover_image"));
                    eCcontent.setEntire_us(jsonArray.getJSONObject(i).optInt("entire_us"));
                    eCcontent.setFile_name(jsonArray.getJSONObject(i).optString("file_name"));
                    eCcontent.setId(jsonArray.getJSONObject(i).optInt("id"));
                    eCcontent.setFile_type(jsonArray.getJSONObject(i).optInt("file_type"));
                    eCcontent.setStatus(jsonArray.getJSONObject(i).optInt("status"));
                    eCcontent.setUser_id(jsonArray.getJSONObject(i).optInt("user_id"));
                    eCcontent.setRange(jsonArray.getJSONObject(i).optInt("range"));
                    list.add(eCcontent);
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("content_list", (Serializable) list);
                msg.setData(bundle);
                msg.sendToTarget();
                Log.e("response", resp);
            } else {
                // failiure in api
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
                while ((line = br.readLine()) != null) {
                    resp += line;
                }
                Log.e("response", resp);
                Message msg1 = mHandler.obtainMessage();
                msg1.what = -1;
                Bundle bundle = new Bundle();
//                resp = readStream(in);
                bundle.putString("failiure_info", resp);
                msg1.setData(bundle);
                msg1.sendToTarget();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Message msg = mHandler.obtainMessage();
            msg.what = -1;
            Bundle bundle = new Bundle();
            bundle.putString("failiure_info", e.getMessage());
            msg.setData(bundle);
            msg.sendToTarget();
            e.printStackTrace();
        } catch (IOException e) {
            Message msg = mHandler.obtainMessage();
            msg.what = -1;
            Bundle bundle = new Bundle();
            bundle.putString("failiure_info", e.getMessage());
            msg.sendToTarget();
            msg.setData(bundle);
            e.printStackTrace();
        } catch (JSONException e) {
            Message msg = mHandler.obtainMessage();
            msg.what = -1;
            Bundle bundle = new Bundle();
            bundle.putString("failiure_info", e.getMessage());
            msg.setData(bundle);
            msg.sendToTarget();
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
