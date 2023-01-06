package com.nhnacademy.bookpubshop.subscribe.relationship.repository;

import com.nhnacademy.bookpubshop.subscribe.relationship.entity.OrderSubscribeStateCode;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 구독상태코드 레포지토리.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public interface OrderSubscribeStateCodeRepository
        extends JpaRepository<OrderSubscribeStateCode, Integer> {

}