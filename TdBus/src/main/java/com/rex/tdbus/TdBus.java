package com.rex.tdbus;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TdBus {
    private Map<Class,List<SubscribeMethod>> METHOD_CACHE=new HashMap<>();
    private Map<String,List<Subscripion>> SUBSCRIBES=new HashMap<>();
    private Map<String,List<String>> REGISTER=new HashMap<>();
    private static volatile TdBus instance;
    private TdBus(){}
    public static TdBus getInstance(){
        if (instance==null) {
             synchronized (TdBus.class){
                 if (instance==null) {
                     instance=new TdBus();
                 }
             }
        }
        return instance;
    }
    public void register(Object object){
        Class<?> subscribeClass = object.getClass();
        List<SubscribeMethod> subscribes = findSubScribe(subscribeClass);
        for (SubscribeMethod subscribe : subscribes) {
            String lable = subscribe.getLable();
            List<Subscripion> subscription = SUBSCRIBES.get(lable);
            if (subscription==null) {
                subscription=new ArrayList<>();
                SUBSCRIBES.put(lable,subscription);
            }
            subscription.add(new Subscripion(subscribe,object));
        }


    }

    private List<SubscribeMethod> findSubScribe(Class<?> subscribeClass) {
        List<SubscribeMethod> subscribeMethod = METHOD_CACHE.get(subscribeClass);
        if (subscribeMethod==null) {
            subscribeMethod=new ArrayList<>();
            Method[] methods = subscribeClass.getMethods();
            if (methods!=null) {
                for (Method method : methods) {
                    Subscribe subscribe = method.getAnnotation(Subscribe.class);
                    if(subscribe!=null){
                        String[] valus = subscribe.value();
                        Class<?>[] paramsType = method.getParameterTypes();
                        for (String valu : valus) {
                            method.setAccessible(true);
                            subscribeMethod.add(new SubscribeMethod(method,paramsType,valu));
                        }
                    }

                }

            }
            METHOD_CACHE.put(subscribeClass,subscribeMethod);
        }

        return subscribeMethod;
    }
    public void post(String lable,Object... params){
        List<Subscripion> subscripions = SUBSCRIBES.get(lable);
        if (subscripions.isEmpty()) {
            return;
        }
        for (Subscripion subscripion : subscripions) {
            SubscribeMethod subscribeMethod = subscripion.getSubscribeMethed();
            Class[] paramsType = subscribeMethod.getParamterClass();
            Object[] realParams = new Object[paramsType.length];
            if(params!=null){
                for (int i = 0; i < paramsType.length; i++) {
                    if(i<params.length&&paramsType[i].isInstance(params[i])){
                        realParams[i]=params[i];
                    }else{
                        realParams[i]=null;
                    }
                }
            }
            try {
                subscribeMethod.getMethod().invoke(subscripion.getSubscriber(),realParams);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

    }
    public void unregister(Object object){
        Class<?> clz = object.getClass();
        List<String> lables = REGISTER.remove(clz);
        if (!lables.isEmpty()) {
            for (String lable : lables) {
                List<Subscripion> subscribetions = SUBSCRIBES.get(lable);
                if (!subscribetions.isEmpty()) {
                    Iterator<Subscripion> iterator = subscribetions.iterator();
                    while (iterator.hasNext()){
                        Subscripion subscribs = iterator.next();
                        if(object==subscribs.getSubscriber()){
                             iterator.remove();
                        }

                    }


                }
            }
        }

    }
}
