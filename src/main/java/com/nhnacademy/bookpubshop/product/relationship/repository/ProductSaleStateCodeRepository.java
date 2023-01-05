package com.nhnacademy.bookpubshop.product.relationship.repository;

import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 상품판매여부코드(product_sale_state_code) 레포지토리.
 *
 * @author : 박경서
 * @since : 1.0
 **/
public interface ProductSaleStateCodeRepository
        extends JpaRepository<ProductSaleStateCode, Integer> {
}
