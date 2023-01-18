package com.nhnacademy.bookpubshop.product.relationship.repository;

import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 상품유형코드(product_type_state_code) 레포지토리.
 *
 * @author : 박경서
 * @since : 1.0
 **/
public interface ProductTypeStateCodeRepository
        extends JpaRepository<ProductTypeStateCode, Integer>, ProductTypeStateCodeRepositoryCustom {
}