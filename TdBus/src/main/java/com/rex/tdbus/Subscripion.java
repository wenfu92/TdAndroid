package com.rex.tdbus;


public class Subscripion {
    SubscribeMethod subscribeMethod;
    Object subScriber;

    public Subscripion(SubscribeMethod subscribeMethod, Object object) {
        this.subscribeMethod = subscribeMethod;
        this.subScriber = object;
    }

    public SubscribeMethod getSubscribeMethed() {
        return subscribeMethod;
    }

    public Object getSubscriber() {
        return subScriber;
    }
}
