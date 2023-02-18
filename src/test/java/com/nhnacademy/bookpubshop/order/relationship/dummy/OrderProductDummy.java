package com.nhnacademy.bookpubshop.order.relationship.dummy;

import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProduct;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProductStateCode;
import com.nhnacademy.bookpubshop.product.entity.Product;

/**
 * 주문상품 더미입니다.
 * 테스트 시 사용됩니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class OrderProductDummy {
    public static OrderProduct dummy(Product product, BookpubOrder order,
                                     OrderProductStateCode orderProductStateCode) {
        return new OrderProduct(
                null,
                product,
                order,
                orderProductStateCode,
                5,
                100L,
                10000L,
                "reason",
                100L,
                "변심"
        );
    }
}
