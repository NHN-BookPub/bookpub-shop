package com.nhnacademy.bookpubshop.product.relationship.dummy;

import static com.nhnacademy.bookpubshop.state.ProductTypeState.BEST_SELLER;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;

/**
 * Some description here.
 *
 * @author : 박경서
 * @since : 1.0
 **/
public class ProductTypeStateCodeDummy {

    public static ProductTypeStateCode dummy() {
        return new ProductTypeStateCode(
                null,
                BEST_SELLER.getName(),
                BEST_SELLER.isUsed(),
                "info"
        );
    }

    public static ProductTypeStateCode dummy(Integer id) {
        return new ProductTypeStateCode(
                id,
                BEST_SELLER.getName(),
                BEST_SELLER.isUsed(),
                "info"
        );
    }
}
