package com.nhnacademy.bookpubshop.product.relationship.dummy;

import static com.nhnacademy.bookpubshop.state.ProductSaleState.SALE;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;

/**
 * Some description here.
 *
 * @author : 박경서
 * @since : 1.0
 **/
public class ProductSaleStateCodeDummy {

    public static ProductSaleStateCode dummy() {
        return new ProductSaleStateCode(
                null,
                SALE.getName(),
                SALE.isUsed(),
                "info"
        );
    }

    public static ProductSaleStateCode dummy(Integer id) {
        return new ProductSaleStateCode(
                id,
                SALE.getName(),
                SALE.isUsed(),
                "info"
        );
    }
}
