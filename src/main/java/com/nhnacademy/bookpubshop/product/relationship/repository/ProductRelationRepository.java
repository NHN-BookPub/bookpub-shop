package com.nhnacademy.bookpubshop.product.relationship.repository;

import com.nhnacademy.bookpubshop.product.relationship.entity.ProductRelation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 연관상품(product_relation) 레포지토리.
 *
 * @author : 박경서
 * @since : 1.0
 **/
public interface ProductRelationRepository extends JpaRepository<ProductRelation, Long> {
}
