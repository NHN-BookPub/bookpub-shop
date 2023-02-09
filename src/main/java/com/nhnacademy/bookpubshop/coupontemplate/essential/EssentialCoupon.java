package com.nhnacademy.bookpubshop.coupontemplate.essential;

import lombok.Getter;

/**
 * db에 항상 존재해야하는 쿠폰을 관리하는 enum 입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Getter
public enum EssentialCoupon {
    SIGN_UP("회원가입 축하 쿠폰");

    private final String couponName;

    EssentialCoupon(String couponName) {
        this.couponName = couponName;
    }
}
