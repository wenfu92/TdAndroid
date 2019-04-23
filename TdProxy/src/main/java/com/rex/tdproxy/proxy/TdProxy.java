package com.rex.tdproxy.proxy;

import com.rex.tdproxy.net.HttpRequest;
import com.rex.tdproxy.net.ICallback;

import java.util.Map;

public class TdProxy implements HttpRequest {
    private HttpRequest httpRequst;
    private static volatile TdProxy instance;

    private TdProxy(HttpRequest httpRequest) {
        this.httpRequst = httpRequest;
    }

    public static void init(HttpRequest httpRequest) {
        if (instance == null) {
            synchronized (TdProxy.class) {
                if (instance == null) {
                    instance = new TdProxy(httpRequest);
                }
            }
        }

    }

    public static HttpRequest getInstance() {
        return instance;
    }

    @Override
    public void get(String url, Map<String, String> params, ICallback callback) {
        httpRequst.get(url, params, callback);
    }

    @Override
    public void post(String url, Map<String, String> params, ICallback callback) {
        httpRequst.post(url, params, callback);

    }
}
