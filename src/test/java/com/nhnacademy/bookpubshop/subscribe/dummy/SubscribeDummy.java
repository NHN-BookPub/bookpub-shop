package com.nhnacademy.bookpubshop.subscribe.dummy;

import com.nhnacademy.bookpubshop.subscribe.entity.Subscribe;

/**
 * 구독의 더미 클래스입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public class SubscribeDummy {
    public static Subscribe dummy() {
        return Subscribe.builder()
                .subscribePrice(1000L)
                .subscribeName("name")
                .salesPrice(100L)
                .salesRate(10)
                .renewed(false)
                .build();
    }
}
