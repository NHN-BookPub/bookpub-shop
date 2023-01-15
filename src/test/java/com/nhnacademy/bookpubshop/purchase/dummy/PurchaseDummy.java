package com.nhnacademy.bookpubshop.purchase.dummy;

import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.purchase.entity.Purchase;

/**
 * Some description here.
 *
 * @author : 박경서
 * @since : 1.0
 **/
public class PurchaseDummy {
    public static Purchase dummy(Product product) {
        return new Purchase(
                null,
                product,
                1000L,
                10
        );
    }

    public static Purchase dummy(Product product, Long id) {
        return new Purchase(
                id,
                product,
                1000L,
                10
        );
    }
}
