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
        return new Subscribe(null,
                "좋은생각",
                5000L,
                6000L,
                10,
                5L,
                false,
                true);
    }
}
