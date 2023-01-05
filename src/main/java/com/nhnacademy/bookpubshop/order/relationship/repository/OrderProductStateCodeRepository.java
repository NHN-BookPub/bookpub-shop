package com.nhnacademy.bookpubshop.order.relationship.repository;

import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProductStateCode;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 주문상품상태코드 레포지토리.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public interface OrderProductStateCodeRepository
        extends JpaRepository<OrderProductStateCode, Integer> {
}