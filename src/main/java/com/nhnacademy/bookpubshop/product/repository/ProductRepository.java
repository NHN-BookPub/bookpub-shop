package com.nhnacademy.bookpubshop.product.repository;

import com.nhnacademy.bookpubshop.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 상품 레포지토리.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
}