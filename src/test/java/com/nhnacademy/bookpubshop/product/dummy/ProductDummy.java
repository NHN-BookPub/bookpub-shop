package com.nhnacademy.bookpubshop.product.dummy;

import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import java.time.LocalDateTime;
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
                    null,
                        "isbn",
                        "title",
                        100,
                        "description",
                        "thumbnail",
                        "path",
                        100L,
                        1000L,
                        null,
                        null,
                        null,
                        false,
                        1,
                        LocalDateTime.now(),
                        true)),
                "isbn",
                "title",
                100,
                "description",
                "thumbnail",
                "path",
                100L,
                1000L,
                null,
                null,
                null,
                false,
                1,
                LocalDateTime.now(),
                true);
    }
}
