package com.rex.tdpermission.register;

import com.rex.tdpermission.core.TdPermUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Permission {
    String[] value();
    int requstCode()default TdPermUtils.DEFAULT_REQUEST_CODE;
}
