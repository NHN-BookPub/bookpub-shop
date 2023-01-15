package com.nhnacademy.bookpubshop.coupon.repository;

import com.nhnacademy.bookpubshop.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 쿠폰 레포지토리.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public interface CouponRepository extends JpaRepository<Coupon, Long>, CouponRepositoryCustom {

}
