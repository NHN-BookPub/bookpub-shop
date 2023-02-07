package com.nhnacademy.bookpubshop.couponmonth.repository;

import com.nhnacademy.bookpubshop.couponmonth.entity.CouponMonth;
import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 이달의쿠폰을 데이터베이스에서 다루기위한 Repo 클래스입니다.
 *
 * @author : 정유진, 김서현
 * @since : 1.0
 **/
public interface CouponMonthRepository extends JpaRepository<CouponMonth, Long>,
        CouponMonthRepositoryCustom {

    /**
     * 템플릿 번호로 이달의 쿠폰을 조회합니다.
     *
     * @param couponTemplate 쿠폰 템플릿 객체
     */
    Optional<CouponMonth> findByCouponTemplate(CouponTemplate couponTemplate);
}
