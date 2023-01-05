package com.nhnacademy.bookpubshop.deliverlocation.repository;

import com.nhnacademy.bookpubshop.deliverlocation.entity.DeliveryLocation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 배송위치를 데이터베이스에서 다루기위한 Repo 클래스입니다.
 *
 * @author : 김서현
 * @since : 1.0
 **/
public interface DeliveryLocationRepository extends JpaRepository<DeliveryLocation, Long> {

}
