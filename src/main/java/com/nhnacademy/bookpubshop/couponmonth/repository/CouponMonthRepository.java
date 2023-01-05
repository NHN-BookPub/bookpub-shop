package com.nhnacademy.bookpubshop.couponmonth.repository;

import com.nhnacademy.bookpubshop.couponmonth.entity.CouponMonth;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 이달의쿠폰을 데이터베이스에서 다루기위한 Repo 클래스입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public interface CouponMonthRepository extends JpaRepository<CouponMonth, Long> {
}
