package com.nhnacademy.bookpubshop.coupontemplate.repository;

import com.nhnacademy.bookpubshop.coupontemplate.dto.response.GetCouponTemplateResponseDto;
import com.nhnacademy.bookpubshop.coupontemplate.dto.response.GetDetailCouponTemplateResponseDto;
import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * CouponTemplateRepository custom을 위한 레포입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@NoRepositoryBean
public interface CouponTemplateRepositoryCustom {

    /**
     * 쿠폰템플릿 번호를 통해 쿠폰템플릿을 상세 조회하는 메서드.
     *
     * @param templateNo 쿠폰템플릿 조회를 위한 쿠폰템플릿 번호
     * @return 쿠폰템플릿 상세 조회 Dto
     */
    Optional<GetDetailCouponTemplateResponseDto> findDetailByTemplateNo(Long templateNo);

    /**
     * 모든 쿠폰템플릿을 상세 조회하는 메서드.
     *
     * @param pageable 쿠폰템플릿 조회 시 페이지 정보
     * @return 쿠폰템플릿 상세 조회 Dto 페이지
     */
    Page<GetDetailCouponTemplateResponseDto> findDetailAllBy(Pageable pageable);

    /**
     * 모든 쿠폰템플릿을 조회하는 메서드.
     *
     * @param pageable 쿠폰템플릿 조회 시 페이지 정보
     * @return 쿠폰템플릿 조회 Dto 페이지
     */
    Page<GetCouponTemplateResponseDto> findAllBy(Pageable pageable);

    /**
     * 템플릿 이름으로 템플릿을 찾아오는 메소드.
     *
     * @param templateName 쿠폰템플릿이름.
     * @return 쿠폰템플릿.
     */
    Optional<CouponTemplate> findDetailByTemplateName(String templateName);
}
