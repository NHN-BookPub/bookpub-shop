package com.nhnacademy.bookpubshop.delivery.dummy;

import com.nhnacademy.bookpubshop.delivery.entity.Delivery;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import java.time.LocalDateTime;

/**
 * 배송 더미클래스입니다.
 *
 * @author : 김서현
 * @since : 1.0
 **/
public class DeliveryDummy {

    public static Delivery dummy(BookpubOrder order) {
        return new Delivery(
                null,
                order,
                "123-123",
                "test_company",
                "test_delivery_state",
                null,
                "test_delivery_recipient",
                "test_phone",
                LocalDateTime.now()
        );
    }

}
