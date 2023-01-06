package com.nhnacademy.bookpubshop.product.relationship.repository;

import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 상품태그(product_and_tag) 레포지토리.
 *
 * @author : 박경서
 * @since : 1.0
 **/
public interface ProductTagRepository extends JpaRepository<ProductTag, ProductTag.Pk> {
}