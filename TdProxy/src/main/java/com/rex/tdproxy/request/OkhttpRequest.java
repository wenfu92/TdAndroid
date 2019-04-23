package com.rex.tdproxy.request;

import com.rex.tdproxy.net.HttpRequest;
import com.rex.tdproxy.net.ICallback;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkhttpRequest implements HttpRequest {
    @Override
    public void get(String url, Map<String, String> params, final ICallback callback) {
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(404,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
               callback.onSuccess(response.body().toString());
            }
        });

    }

    @Override
    public void post(String url, Map<String, String> params, ICallback callback) {

    }
}
