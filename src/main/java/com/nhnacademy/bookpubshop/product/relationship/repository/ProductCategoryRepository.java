package com.nhnacademy.bookpubshop.product.relationship.repository;

import com.nhnacademy.bookpubshop.product.relationship.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 상품카테고리관계(product_and_category) 테이블 레포지토리.
 *
 * @author : 박경서
 * @since : 1.0
 **/
public interface ProductCategoryRepository
        extends JpaRepository<ProductCategory, ProductCategory.Pk> {
}