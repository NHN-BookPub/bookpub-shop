package com.nhnacademy.bookpubshop.delivery.repository;

import com.nhnacademy.bookpubshop.delivery.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 배송을 데이터베이스에서 다루기위한 Repo 클래스입니다.
 *
 * @author : 김서현
 * @since : 1.0
 **/
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

}
