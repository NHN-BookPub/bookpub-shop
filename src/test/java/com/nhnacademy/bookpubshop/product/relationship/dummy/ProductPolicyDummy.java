package com.nhnacademy.bookpubshop.product.relationship.dummy;

import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;

/**
 * Some description here.
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
}
