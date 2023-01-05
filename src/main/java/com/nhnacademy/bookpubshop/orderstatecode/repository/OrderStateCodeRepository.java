package com.nhnacademy.bookpubshop.orderstatecode.repository;

import com.nhnacademy.bookpubshop.orderstatecode.entity.OrderStateCode;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 주문상태코드를 데이터베이스에서 다루기위한 Repo 클래스입니다.
 *
 * @author : 김서현
 * @since : 1.0
 **/
public interface OrderStateCodeRepository extends JpaRepository<OrderStateCode, Integer> {

}
