package com.rex.tdproxy.net;

public interface ICallback {
    //成功回调
    void onSuccess(String resulte);
    //失败回调
    void onFailure(int code,Throwable t);
}
