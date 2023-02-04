package com.nhnacademy.bookpubshop.tier.relationship.repository;

import com.nhnacademy.bookpubshop.tier.relationship.dto.response.GetTierCouponResponseDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * TierCoupon Querydsl 사용하기 위한 인터페이스.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@NoRepositoryBean
public interface TierCouponRepositoryCustom {

    /**
     * 등급 쿠폰 리스트 페이지를 조회하는 메서드.
     *
     * @param pageable 페이지.
     * @return 등급 쿠폰 DTO 페이지.
     */
    Page<GetTierCouponResponseDto> findAllBy(Pageable pageable);

    /**
     * 등급번호로 쿠폰 템플릿 번호를 조회하는 메서드.
     *
     * @param tierNo 등급 번호
     * @return 템플릿 번호 리스트
     */
    List<Long> findAllByTierNo(Integer tierNo);
}
