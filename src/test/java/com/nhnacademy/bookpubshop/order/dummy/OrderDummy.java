package com.nhnacademy.bookpubshop.order.dummy;

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
    public static BookpubOrder dummy(Member member, PricePolicy pricePolicy, PricePolicy packagePricePolicy, OrderStateCode orderStateCode) {
        return new BookpubOrder(
                null,
                member,
                pricePolicy,
                packagePricePolicy,
                orderStateCode,
                "test_recipient",
                "test_recipient_phone",
                "test_buyer",
                "test_buyer_phone",
                LocalDateTime.now(),
                null,
                10000L,
                100L,
                1500L,
                true,
                null,
                1000L,
                "IT",
                "광주 동구 조선대길",
                "dasdljzxlkj",
                "asdasdasd"
        );

    }

    public static BookpubOrder dummy2(Member member, PricePolicy pricePolicy, PricePolicy packagePricePolicy, OrderStateCode orderStateCode) {
        return new BookpubOrder(
                1L,
                member,
                pricePolicy,
                packagePricePolicy,
                orderStateCode,
                "test_recipient",
                "test_recipient_phone",
                "test_buyer",
                "test_buyer_phone",
                LocalDateTime.now(),
                null,
                10000L,
                100L,
                1500L,
                true,
                null,
                1000L,
                "IT",
                "광주 동구 조선대길",
                "dalskdjzx",
                "orderName"
        );
    }

    public static BookpubOrder dummy3(Member member, PricePolicy pricePolicy, PricePolicy packagePricePolicy, OrderStateCode orderStateCode) {
        return new BookpubOrder(
                null,
                member,
                pricePolicy,
                packagePricePolicy,
                orderStateCode,
                "test_recipient",
                "test_recipient_phone",
                "test_buyer",
                "test_buyer_phone",
                LocalDateTime.of(2023,2,18,4,0),
                null,
                10000L,
                100L,
                1500L,
                true,
                null,
                1000L,
                "IT",
                "광주 동구 조선대길",
                "dasdljzxlkj",
                "asdasdasd"
        );

    }

}
