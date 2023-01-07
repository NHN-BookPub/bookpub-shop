package com.nhnacademy.bookpubshop.couponstatecode.repository;

import com.nhnacademy.bookpubshop.couponstatecode.dto.GetCouponStateCodeResponseDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 쿠폰상태코드 Custom Repo 클래스입니다.
 *
 * @author : 정유진
 * @since : 1.0
 */
@NoRepositoryBean
public interface CouponStateCodeRepositoryCustom {

    /**
     * 쿠폰상태코드 단건 조회를 위한 메소드입니다.
     *
     * @param codeNo 쿠폰상태코드 번호
     * @return GetCouponStateCodeResponseDto 적용타겟 이름이 반환됩니다.
     */
    Optional<GetCouponStateCodeResponseDto> findByCodeNoAndCodeUsedTrue(Integer codeNo);

    /**
     * 쿠폰상태코드 리스트 조회를 위한 메소드입니다.
     *
     * @return 전체 적용타겟 이름이 반환됩니다.
     */
    List<GetCouponStateCodeResponseDto> findAllByCodeUsedTrue();
}
