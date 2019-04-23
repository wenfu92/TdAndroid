package com.rex.tdpermission.core;

import android.content.Context;

import com.rex.tdpermission.mobile.IPermisstion;
import com.rex.tdpermission.register.Permission;
import com.rex.tdpermission.register.PermissionCanceled;
import com.rex.tdpermission.register.PermissionDenied;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class TdPermAspect {
    @Pointcut("execution(@com.rex.tdpermission.register.Permission * *(..)) && @annotation(permission)")
    public void requestPermission(Permission permission) {

    }
    @Around("requestPermission(permission)")
    public void aroundJointPoint(final ProceedingJoinPoint joinPoint, Permission permission) throws Throwable{

        //初始化context
        Context context = null;

        final Object object = joinPoint.getThis();
        if (joinPoint.getThis() instanceof Context) {
            context = (Context) object;
        } else if (joinPoint.getThis() instanceof android.support.v4.app.Fragment) {
            context = ((android.support.v4.app.Fragment) object).getActivity();
        } else if (joinPoint.getThis() instanceof android.app.Fragment) {
            context = ((android.app.Fragment) object).getActivity();
        } else {
        }

        if (context == null || permission == null) {
            return;
        }

        final Context finalContext = context;
        TdPermActivity.requestPermission(context, permission.value(), permission.requstCode(), new IPermisstion() {
            @Override
            public void granted() {
                try {
                    joinPoint.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override
            public void cancled() {
                TdPermUtils.invokeAnnotion(object, PermissionCanceled.class);
            }

            @Override
            public void denied() {
                TdPermUtils.invokeAnnotion(object, PermissionDenied.class);
                TdPermUtils.getMenu(finalContext);
            }
        });

    }


}
