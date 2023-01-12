package com.nhnacademy.bookpubshop.order.relationship.dummy;

import com.nhnacademy.bookpubshop.subscribe.relationship.entity.OrderSubscribeStateCode;

/**
 * Some description here.
 *
 * @author : 박경서
 * @since : 1.0
 **/
public class OrderSubscribeStateCodeDummy {

    public static OrderSubscribeStateCode dummy() {
        return new OrderSubscribeStateCode(
                null,
                "codeName",
                false,
                "info"
        );
    }
}
