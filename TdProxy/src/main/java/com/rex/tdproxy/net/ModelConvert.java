package com.rex.tdproxy.net;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class ModelConvert<T> implements ICallback{
    @Override
    public void onSuccess(String resulte) {
        Class<? extends T> clz = getGeneticClass(this);
        T t=new Gson().fromJson(resulte,clz);
        onSuccess(t);

    }

    private Class<? extends T> getGeneticClass(Object object) {
        Type getGenericeClass = object.getClass().getGenericSuperclass();
        return (Class<? extends T>) ((ParameterizedType)getGenericeClass).getActualTypeArguments()[0];

    }

    public abstract void onSuccess(T t);
}
