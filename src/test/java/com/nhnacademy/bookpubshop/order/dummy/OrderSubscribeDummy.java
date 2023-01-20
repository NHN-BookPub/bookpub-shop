package com.nhnacademy.bookpubshop.order.dummy;

import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderSubscribe;
import com.nhnacademy.bookpubshop.orderstatecode.dummy.OrderStateCodeDummy;
import com.nhnacademy.bookpubshop.pricepolicy.dummy.PricePolicyDummy;
import com.nhnacademy.bookpubshop.subscribe.dummy.SubscribeDummy;
import com.nhnacademy.bookpubshop.subscribe.relationship.entity.OrderSubscribeStateCode;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import java.time.LocalDateTime;

/**
 * 주문구독 더미 생성.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public class OrderSubscribeDummy {
    public static OrderSubscribe dummy() {
        return new OrderSubscribe(null,
                SubscribeDummy.dummy(),
                OrderDummy.dummy(MemberDummy.dummy(TierDummy.dummy()), PricePolicyDummy.dummy(), PricePolicyDummy.dummy(), OrderStateCodeDummy.dummy()),
                        new OrderSubscribeStateCode(null, "테스트", true, "테스트"),
                        1000L,
                        8000L,
                LocalDateTime.of(2000, 12, 12, 14, 30));
    }
}
