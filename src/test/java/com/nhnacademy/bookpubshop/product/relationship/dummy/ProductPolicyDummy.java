package com.nhnacademy.bookpubshop.product.relationship.dummy;

import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;

/**
 * 상품정책 더미입니다.
 *
 * @author : 박경서
 * @since : 1.0
 **/
public class ProductPolicyDummy {

    public static ProductPolicy dummy() {
        return new ProductPolicy(
                null,
                "method",
                true,
                1);
    }

    public static ProductPolicy dummy(Integer id) {
        return new ProductPolicy(
                id,
                "method",
                true,
                1);
    }
}
