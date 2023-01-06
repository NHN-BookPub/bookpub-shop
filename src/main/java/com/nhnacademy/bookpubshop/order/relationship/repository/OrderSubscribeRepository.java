package com.nhnacademy.bookpubshop.order.relationship.repository;

import com.nhnacademy.bookpubshop.order.relationship.entity.OrderSubscribe;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 구독주문상품 레포지토리.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public interface OrderSubscribeRepository extends JpaRepository<OrderSubscribe, Long> {
}