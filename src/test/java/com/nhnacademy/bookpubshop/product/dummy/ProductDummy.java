package com.nhnacademy.bookpubshop.product.dummy;

import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 상품에 대한 더미 클래스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
public class ProductDummy {

    public static Product dummy(
            ProductPolicy productPolicy,
            ProductTypeStateCode productTypeStateCode,
            ProductSaleStateCode productSaleStateCode) {
        return new Product(
                null,
                productPolicy,
                productTypeStateCode,
                productSaleStateCode,
                List.of(new Product(
                        null,
                        productPolicy,
                        productTypeStateCode,
                        productSaleStateCode,
                        Collections.EMPTY_LIST,
                        "isbn",
                        "title",
                        "publisher",
                        100,
                        "description",
                        "thumbnail",
                        "path",
                        8000L,
                        10000L,
                        20,
                        0L,
                        -3,
                        false,
                        1,
                        LocalDateTime.now(),
                        true)),
                "isbn",
                "title",
                "publisher",
                100,
                "description",
                "thumbnail",
                "path",
                9000L,
                10000L,
                10,
                0L,
                0,
                false,
                1,
                LocalDateTime.now(),
                true);
    }
}
