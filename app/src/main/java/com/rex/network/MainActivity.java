package com.rex.network;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.rex.tdbus.Subscribe;
import com.rex.tdbus.TdBus;
import com.rex.tdpermission.register.Permission;
import com.rex.tdpermission.register.PermissionCanceled;
import com.rex.tdpermission.register.PermissionDenied;
import com.rex.tdproxy.net.ICallback;
import com.rex.tdproxy.net.ModelConvert;
import com.rex.tdproxy.proxy.TdProxy;
import com.rex.tdproxy.request.AsyncHttpRequest;
import com.rex.tdproxy.request.OkhttpRequest;

import java.net.HttpCookie;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TdProxy.init(new AsyncHttpRequest());
        getHttp();
        TdBus.getInstance().register(this);
        findViewById(R.id.tv_jump).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPerssion();
            }


        });
    }
    @Permission(value = {Manifest.permission.ACCESS_FINE_LOCATION},requstCode = 200)
    private void showPerssion() {

    }
    @Subscribe({"1","1"})
    public void test(String msg1,String msg2){
        Log.e("TDBUS", "msg1: "+msg1+"msg2:"+msg2 );
    }

    private void getHttp() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsIsOpen = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (gpsIsOpen) {
        }else{

        }
        Map<String,String> params = new HashMap<>();
        params.put("city","长沙");
        params.put("key","13cb58f5884f9749287abbead9c658f2");
        TdProxy.getInstance().get("http://restapi.amap.com/v3/weather/weatherInfo", params, new ModelConvert<WeatherInfo>() {
            @Override
            public void onSuccess(WeatherInfo weatherInfo) {
                Log.e("北京天气：", "onSuccess: "+weatherInfo.toString());
            }

            @Override
            public void onFailure(int code, Throwable t) {

            }

        });
    }
 
    @Subscribe("s")
    public void event(){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TdBus.getInstance().unregister(this);
    }
    @PermissionCanceled()
    private void cancel() {
        Log.i("", "writeCancel: " );
        Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied()
    private void deny() {
        Log.i("", "writeDeny:");
        Toast.makeText(this, "deny", Toast.LENGTH_SHORT).show();
    }

}
