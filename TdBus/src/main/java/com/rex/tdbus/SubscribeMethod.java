package com.rex.tdbus;

import java.lang.reflect.Method;

public class SubscribeMethod {
    private Method method;
    private Class[] paramterClass;
    private String lable;

    public Method getMethod() {
        return method;
    }

    public Class[] getParamterClass() {
        return paramterClass;
    }

    public String getLable() {
        return lable;
    }

    public SubscribeMethod(Method method, Class[] paramterClass, String lable) {
        this.method = method;
        this.paramterClass = paramterClass;
        this.lable = lable;
    }
}
