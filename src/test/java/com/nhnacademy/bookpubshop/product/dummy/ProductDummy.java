package com.nhnacademy.bookpubshop.product.dummy;

import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

        List<Product> relation = new ArrayList<>();
        relation.add(new Product(
                null,
                productPolicy,
                productTypeStateCode,
                productSaleStateCode,
                null,
                "1111111111",
                "title",
                "publisher",
                100,
                "description",
                8000L,
                10000L,
                20,
                0L,
                3,
                false,
                1,
                LocalDateTime.now(),
                true));


        return new Product(
                null,
                productPolicy,
                productTypeStateCode,
                productSaleStateCode,
                null,
                "1111111111",
                "title",
                "publisher",
                100,
                "description",
                9000L,
                10000L,
                10,
                0L,
                5,
                false,
                1,
                LocalDateTime.now(),
                true);
    }

    public static Product dummy(
            ProductPolicy productPolicy,
            ProductTypeStateCode productTypeStateCode,
            ProductSaleStateCode productSaleStateCode,
            int productStock) {

        List<Product> relation = new ArrayList<>();
        relation.add(new Product(
                null,
                productPolicy,
                productTypeStateCode,
                productSaleStateCode,
                null,
                "1111111111",
                "title",
                "publisher",
                100,
                "description",
                8000L,
                10000L,
                20,
                0L,
                3,
                false,
                productStock,
                LocalDateTime.now(),
                true));


        return new Product(
                null,
                productPolicy,
                productTypeStateCode,
                productSaleStateCode,
                null,
                "1111111111",
                "title",
                "publisher",
                100,
                "description",
                9000L,
                10000L,
                10,
                0L,
                5,
                false,
                productStock,
                LocalDateTime.now(),
                true);
    }

    public static Product dummy(
            ProductPolicy productPolicy,
            ProductTypeStateCode productTypeStateCode,
            ProductSaleStateCode productSaleStateCode,
            Long id) {

        List<Product> relation = new ArrayList<>();
        relation.add(new Product(
                id + 1,
                productPolicy,
                productTypeStateCode,
                productSaleStateCode,
                null,
                "1111111111",
                "title",
                "publisher",
                100,
                "description",
                8000L,
                10000L,
                20,
                0L,
                3,
                false,
                1,
                LocalDateTime.now(),
                true));


        return new Product(
                id,
                productPolicy,
                productTypeStateCode,
                productSaleStateCode,
                null,
                "1111111111",
                "title",
                "publisher",
                100,
                "description",
                9000L,
                10000L,
                10,
                0L,
                5,
                false,
                1,
                LocalDateTime.now(),
                true);
    }

    public static Product dummy(
            ProductPolicy productPolicy,
            ProductTypeStateCode productTypeStateCode,
            ProductSaleStateCode productSaleStateCode,
            Long id,
            List<Product> relations) {
        return new Product(
                id,
                productPolicy,
                productTypeStateCode,
                productSaleStateCode,
                null,
                "1111111111",
                "title",
                "publisher",
                100,
                "description",
                9000L,
                10000L,
                10,
                0L,
                5,
                false,
                1,
                LocalDateTime.now(),
                true);
    }
}
