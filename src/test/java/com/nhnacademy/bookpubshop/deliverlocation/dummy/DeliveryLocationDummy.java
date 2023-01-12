package com.nhnacademy.bookpubshop.deliverlocation.dummy;

import com.nhnacademy.bookpubshop.deliverlocation.entity.DeliveryLocation;
import com.nhnacademy.bookpubshop.delivery.entity.Delivery;
import java.time.LocalDateTime;

/**
 * 배송위치 더미클래스입니다.
 *
 * @author : 김서현
 * @since : 1.0
 **/
public class DeliveryLocationDummy {

    public static DeliveryLocation dummy(Delivery delivery) {
        return new DeliveryLocation(
                null,
                delivery,
                "test_location_name"
        );
    }

}
