package com.nhnacademy.bookpubshop.product.relationship.repository;

import com.nhnacademy.bookpubshop.product.relationship.entity.ProductAuthor;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 상품저자 레포지토리.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public interface ProductAuthorRepository extends JpaRepository<ProductAuthor, ProductAuthor.Pk> {
}