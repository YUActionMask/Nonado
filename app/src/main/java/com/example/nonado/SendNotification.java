package com.example.nonado;

import android.os.AsyncTask;
import android.util.Log;


import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendNotification {


    //; charset=utf-8
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static void sendNotification(String regToken, String title, String messsage, String activity){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... parms) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json = new JSONObject();

                    JSONObject noti = new JSONObject();
                    noti.put("body", messsage);
                    noti.put("title", title );


                    JSONObject data = new JSONObject();
                    data.put("body", messsage);
                    data.put("title", title );
                    data.put("click_action", activity);

                    json.put("notification", noti);
                    json.put("data", data);
                    json.put("to", regToken);

                    RequestBody body = RequestBody.create(JSON, json.toString());
                    Request request = new Request.Builder()
                            .header("Authorization", "key=" + "AAAA3wOK1Ss:APA91bGsq7k52Csp78vQQswe8eib4bDFhaKKBSf9VxlC_P9igOxR5G-VKYRPo1BCNz0vBG-MgDDbfUH3_64AsH-xPbvIrKhhDZLNNXWt_sQyr7gTIweAnD_e-n4NaqLatW9L9i646A88")
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String finalResponse = response.body().string();
                }catch (Exception e){
                    Log.d("error", e+"");
                }
                return null;
            }
        }.execute();
    }


}

