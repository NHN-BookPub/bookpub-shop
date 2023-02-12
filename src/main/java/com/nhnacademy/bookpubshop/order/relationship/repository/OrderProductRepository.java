package com.nhnacademy.bookpubshop.order.relationship.repository;

import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 주문상품 레포지토리.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public interface OrderProductRepository
        extends JpaRepository<OrderProduct, Long>, OrderProductRepositoryCustom {

}