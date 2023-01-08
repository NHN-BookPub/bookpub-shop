package com.nhnacademy.bookpubshop.coupontype.repository;

import com.nhnacademy.bookpubshop.coupontype.dto.response.GetCouponTypeResponseDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 쿠폰유형 Custom Repo 클래스입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@NoRepositoryBean
public interface CouponTypeRepositoryCustom {

    /**
     * 쿠폰유형 단건 조회를 위한 메소드입니다.
     *
     * @param typeNo 쿠폰유형번호
     * @return GetCouponTypeResponseDto 쿠폰유형이름이 반환됩니다.
     */
    Optional<GetCouponTypeResponseDto> findByTypeNo(Long typeNo);

    /**
     * 쿠폰유형 리스트 조회를 위한 메소드입니다.
     *
     * @return GetCouponTypeResponseDto 쿠폰유형이름 리스트가 반환됩니다.
     */
    List<GetCouponTypeResponseDto> findAllBy();
}
