package com.nhnacademy.bookpubshop.product.relationship.repository;

import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 상품정책(product_policy) 테이블 레포지토리.
 *
 * @author : 박경서
 * @since : 1.0
 **/
public interface ProductPolicyRepository
        extends JpaRepository<ProductPolicy, Integer>, ProductPolicyRepositoryCustom {
}