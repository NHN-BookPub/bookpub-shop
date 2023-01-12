package com.nhnacademy.bookpubshop.product.relationship.dummy;

import static com.nhnacademy.bookpubshop.state.ProductTypeState.NEW;
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
                NEW.getName(),
                NEW.isUsed(),
                "info"
        );
    }
}
