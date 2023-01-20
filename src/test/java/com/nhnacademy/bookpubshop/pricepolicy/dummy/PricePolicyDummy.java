package com.nhnacademy.bookpubshop.pricepolicy.dummy;

import com.nhnacademy.bookpubshop.pricepolicy.entity.PricePolicy;

/**
 * 가격정책 더미 클래스입니다.
 *
 * @author : 김서현
 * @since : 1.0
 **/
public class PricePolicyDummy {

    public static PricePolicy dummy() {
        return new PricePolicy(
                null,
                "test_policy_name",
                3000L
        );
    }

    public static PricePolicy dummy(Integer no){
        return new PricePolicy(
                no,
                "test_policy_name",
                3000L
        );
    }


}
