package com.nhnacademy.bookpubshop.product.relationship.dummy;

import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.state.ProductSaleState;

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
                ProductSaleState.SALE.getName(),
                ProductSaleState.SALE.isUsed(),
                "info"
        );
    }

    public static ProductSaleStateCode dummy(Integer id) {
        return new ProductSaleStateCode(
                id,
                ProductSaleState.SALE.name(),
                ProductSaleState.SALE.isUsed(),
                "info"
        );
    }
}
