package com.wyu.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;


import com.wyu.constant.MyState;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

public class CustomHttp {
    private Handler handler;

    public CustomHttp(Handler handler) {
        this.handler = handler;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    protected void success(Call call, Response response) throws IOException {
    }

    protected void fail() {
        Log.i(MyState.TAG, "onFailure()");
        Message msg = new Message();
        msg.what = MyState.CONNECTION_ERROR;
        handler.sendMessage(msg);
    }

    public void get(String url) {
        Log.i(MyState.TAG, "进入myConnect()");
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                fail();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                success(call, response);
            }
        });
    }

    public void get(String url, StringBuilder cookies) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("cookie", cookies.toString()).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                fail();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                success(call, response);
            }
        });

    }

    public void post(String url, Map<String, String> header, JSONObject body) {
        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), body.toString());
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder().url(url).post(requestBodyJson);
        for (Map.Entry<String, String> entry : header.entrySet()) {
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }
        Request request = requestBuilder.build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                fail();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                success(call, response);
            }
        });

    }

    public void get(String url, Map<String, String> header, RequestBody body) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder().url(url).addHeader("Content-Type", "application/json").post(body);
        for (Map.Entry<String, String> entry : header.entrySet()) {
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }
        Request request = requestBuilder.build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                fail();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                success(call, response);
            }
        });

    }
}
