package com.nhnacademy.bookpubshop.order.relationship.dummy;

import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderSubscribe;
import com.nhnacademy.bookpubshop.subscribe.entity.Subscribe;
import com.nhnacademy.bookpubshop.subscribe.relationship.entity.OrderSubscribeStateCode;
import java.time.LocalDateTime;

/**
 * Some description here.
 *
 * @author : 박경서
 * @since : 1.0
 **/
public class OrderSubscribeDummy {

    public static OrderSubscribe dummy(Subscribe subscribe, BookpubOrder order,
                                       OrderSubscribeStateCode orderSubscribeStateCode) {
        return new OrderSubscribe(
                null,
                subscribe,
                order,
                orderSubscribeStateCode,
                1000L,
                1000L,
                LocalDateTime.now()
        );
    }
}
