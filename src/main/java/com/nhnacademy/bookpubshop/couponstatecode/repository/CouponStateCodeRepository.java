package com.nhnacademy.bookpubshop.couponstatecode.repository;

import com.nhnacademy.bookpubshop.couponstatecode.entity.CouponStateCode;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 쿠폰상태코드를 데이터베이스에서 다루기위한 Repo 클래스입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public interface CouponStateCodeRepository extends JpaRepository<CouponStateCode, Integer>,
        CouponStateCodeRepositoryCustom {

}
