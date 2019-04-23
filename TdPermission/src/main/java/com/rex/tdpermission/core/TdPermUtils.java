package com.rex.tdpermission.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.SimpleArrayMap;

import com.rex.tdpermission.mobile.Default;
import com.rex.tdpermission.mobile.IMenu;
import com.rex.tdpermission.mobile.Oppo;
import com.rex.tdpermission.mobile.Vivo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 操作权限的工具类
 */

public class TdPermUtils {
    private static SimpleArrayMap<String,Integer> SDK_PERMISSTION=new SimpleArrayMap<>(8);
    private static SimpleArrayMap<String,Class<? extends IMenu>> permMenu=new SimpleArrayMap<>();
    public static final int DEFAULT_REQUEST_CODE = 0xABC1994;
    static {
        SDK_PERMISSTION.put("com.android.voicemail.permission.ADD_VOICEMAIL", 14);
        SDK_PERMISSTION.put("android.permission.BODY_SENSORS", 20);
        SDK_PERMISSTION.put("android.permission.READ_CALL_LOG", 16);
        SDK_PERMISSTION.put("android.permission.READ_EXTERNAL_STORAGE", 16);
        SDK_PERMISSTION.put("android.permission.USE_SIP", 9);
        SDK_PERMISSTION.put("android.permission.WRITE_CALL_LOG", 16);
        SDK_PERMISSTION.put("android.permission.SYSTEM_ALERT_WINDOW", 23);
        SDK_PERMISSTION.put("android.permission.WRITE_SETTINGS", 23);
    }
    public static final String MOBILE_HUAWEI = "huawei";//华为
    public static final String MOBILE_MEIZU = "meizu";//魅族
    public static final String MOBILE_XIAOMI = "xiaomi";//小米
    public static final String MOBILE_SONY = "sony";//索尼
    public static final String MOBILE_OPPO = "oppo";
    public static final String MOBILE_LG = "lg";
    public static final String MMOBILE_VIVO = "vivo";
    public static final String MOBILE_SAMSUNG = "samsung";//三星
    public static final String MOBILE_LETV = "letv";//乐视
    public static final String MOBILE_ZTE = "zte";//中兴
    public static final String MOBILE_YULONG = "yulong";//酷派
    public static final String MOBILE_LENOVO = "lenovo";//联想
    private static final String MOBILE_DEFAULT = "Default";//默认
    static {
        permMenu.put(MOBILE_DEFAULT, Default.class);
        permMenu.put(MOBILE_OPPO, Oppo.class);
        permMenu.put(MMOBILE_VIVO, Vivo.class);
    }
    /**
     * 检查是否需要请求权限
     * @param context
     * @param permissions
     * @return
     *      false --- 需要  true ---不需要
     */
    public static boolean hasPermission(Context context, String ...permissions) {

        for (String permission : permissions) {
            if (permissionExists(permission) && !hasSelfPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @Description 检测某个权限是否已经授权；如果已授权则返回true，如果未授权则返回false
     */
    private static boolean hasSelfPermission(Context context, String permission) {
        try {
            // ContextCompat.checkSelfPermission，主要用于检测某个权限是否已经被授予。
            // 方法返回值为PackageManager.PERMISSION_DENIED或者PackageManager.PERMISSION_GRANTED
            // 当返回DENIED就需要进行申请授权了。
            return ContextCompat.checkSelfPermission(context, permission)
                    == PackageManager.PERMISSION_GRANTED;
        } catch (RuntimeException t) {
            return false;
        }
    }
    /**
     * @Description 如果在这个SDK版本存在的权限，则返回true
     */
    private static boolean permissionExists(String permission) {
        Integer minVersion = SDK_PERMISSTION.get(permission);
        return minVersion == null || Build.VERSION.SDK_INT >= minVersion;
    }
    /**
     * @Description 检查需要给予的权限是否需要显示理由
     */
    public static boolean shouldShowRequestPermissionRationale(Activity activity, String... permissions) {
        for (String permission : permissions) {
            // 这个API主要用于给用户一个申请权限的解释，该方法只有在用户在上一次已经拒绝过你的这个权限申请。
            // 也就是说，用户已经拒绝一次了，你又弹个授权框，你需要给用户一个解释，为什么要授权，则使用该方法。
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true;
            }
        }
        return false;
    }

    public static boolean verifyPermission(Context context, int ... gantedResults) {

        if (gantedResults == null || gantedResults.length == 0 ) {
            return false;
        }

        for (int ganted : gantedResults) {
            if (ganted != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    public static void invokeAnnotion(Object obj,Class annotionClass){
        //获取切面的clz类
        Class<?> clz = obj.getClass();
        //获取类中的方法
        Method[] methods = clz.getDeclaredMethods();
        if (methods==null) {
            return;
        }
        for (Method method : methods) {
            boolean isHasAnnotion = method.isAnnotationPresent(annotionClass);
            if (isHasAnnotion) {
                method.setAccessible(true);
                try {
                    method.invoke(obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    public static void getMenu(Context context){
        Class clazz = permMenu.get(Build.MANUFACTURER.toLowerCase());
        if (clazz == null) {
            clazz = permMenu.get(MOBILE_DEFAULT);
        }

        try {
            IMenu iMenu = (IMenu) clazz.newInstance();

            Intent menuIntent = iMenu.getMenuIntet(context);
            if (menuIntent == null) return;
            context.startActivity(menuIntent);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
