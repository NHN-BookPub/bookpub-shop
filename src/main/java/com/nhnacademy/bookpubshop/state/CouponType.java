package com.nhnacademy.bookpubshop.state;

import lombok.Getter;

/**
 * Some description here.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
public enum CouponType {
    COMMON("일반"),
    DUPLICATE("중복"),
    POINT("포인트");
    private final String name;

    CouponType(String name) {
        this.name = name;
    }
}
