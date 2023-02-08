package com.nhnacademy.bookpubshop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 회원과 관리자가 둘다사용가능한 annotation 입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MemberAndAuth {
}
