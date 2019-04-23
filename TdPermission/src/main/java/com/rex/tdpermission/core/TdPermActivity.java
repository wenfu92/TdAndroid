package com.rex.tdpermission.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.rex.tdpermission.R;
import com.rex.tdpermission.mobile.IPermisstion;

public class TdPermActivity extends Activity {
    private static String TAG_PERMSION="permisstion";
    private static String TAG_CODE="requst_code";
    private static IPermisstion permissionListener;

    /**
     * 请求权限
     */
    public static void requestPermission(Context context, String[] permissions, int requestCode, IPermisstion iPermission) {
        permissionListener = iPermission;
        Intent intent = new Intent(context, TdPermActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle = new Bundle();
        bundle.putStringArray(TAG_PERMSION, permissions);
        bundle.putInt(TAG_CODE, requestCode);
        intent.putExtras(bundle);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(0, 0);
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trans_latout);
        Intent intent = getIntent();
        String[] permsions = intent.getStringArrayExtra(TAG_PERMSION);
        int permsionCode = intent.getIntExtra(TAG_CODE, -1);
        if(permsions==null||permsionCode<0||permissionListener==null){
         this.finish();
         return;
        }
        if(TdPermUtils.hasPermission(this,permsions)){
            permissionListener.granted();
            finish();
            return;

        }
        ActivityCompat.requestPermissions(this,permsions,permsionCode);
    }
    /**
     * grantResults对应于申请的结果，这里的数组对应于申请时的第二个权限字符串数组。
     * 如果你同时申请两个权限，那么grantResults的length就为2，分别记录你两个权限的申请结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //请求权限成功
        if (TdPermUtils.verifyPermission(this, grantResults)) {
            permissionListener.granted();
            finish();
            return;
        }

        //用户点击了不再显示
        if (!TdPermUtils.shouldShowRequestPermissionRationale(this, permissions)) {
            permissionListener.denied();
            finish();
            return;
        }

        //用户取消
        permissionListener.cancled();
        finish();
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,0);
    }
}