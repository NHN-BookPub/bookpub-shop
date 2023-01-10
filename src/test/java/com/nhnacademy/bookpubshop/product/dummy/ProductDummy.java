package com.nhnacademy.bookpubshop.product.dummy;

import static com.nhnacademy.bookpubshop.state.ProductTypeState.BEST_SELLER;
import static com.nhnacademy.bookpubshop.state.ProductTypeState.NEW;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import java.time.LocalDateTime;
import java.util.Collections;

/**
 * 상품에 대한 더미 클래스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
public class ProductDummy {

    public static Product dummy() {
        return new Product(
                null,
                new ProductPolicy(null,"method",true,1),
                new ProductTypeStateCode(null,BEST_SELLER.getName(),BEST_SELLER.isUsed(),"info"),
                new ProductSaleStateCode(null, NEW.getName(),NEW.isUsed(),"info"),
                Collections.EMPTY_LIST,
                "isbn",
                "title",
                100,
                "description",
                "thumbnail",
                "path",
                100L,
                10L,
                10,
                1L,
                10,
                false,
                1,
                LocalDateTime.now(),
                LocalDateTime.now(),
                true);
    }
}
