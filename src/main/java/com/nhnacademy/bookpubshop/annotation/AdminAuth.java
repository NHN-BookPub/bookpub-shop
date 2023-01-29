package com.nhnacademy.bookpubshop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 관리자만 접근가능하도록 하는 어노테이션입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminAuth {
}
