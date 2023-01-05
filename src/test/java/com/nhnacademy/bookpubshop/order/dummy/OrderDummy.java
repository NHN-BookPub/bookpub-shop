package com.nhnacademy.bookpubshop.order.dummy;

import com.nhnacademy.bookpubshop.address.entity.Address;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.orderstatecode.entity.OrderStateCode;
import com.nhnacademy.bookpubshop.pricepolicy.entity.PricePolicy;
import java.time.LocalDateTime;

/**
 * 주문 더미클래스입니다.
 *
 * @author : 김서현
 * @since : 1.0
 **/
public class OrderDummy {
    public static BookpubOrder dummy(Member member, PricePolicy pricePolicy, PricePolicy packagePricePolicy, Address address, OrderStateCode orderStateCode){
        return new BookpubOrder(
                null,
                member,
                pricePolicy,
                packagePricePolicy,
                address,
                orderStateCode,
                LocalDateTime.now(),
                "test_recipient",
                "test_recipient_phone",
                "test_buyer",
                "test_buyer_phone",
                LocalDateTime.now(),
                null,
                10000L,
                100L,
                true,
                null,
                1000L
        );

    }

}
