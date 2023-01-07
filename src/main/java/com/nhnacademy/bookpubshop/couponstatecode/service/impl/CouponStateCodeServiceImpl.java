package com.nhnacademy.bookpubshop.couponstatecode.service.impl;

import com.nhnacademy.bookpubshop.couponstatecode.dto.GetCouponStateCodeResponseDto;
import com.nhnacademy.bookpubshop.couponstatecode.exception.CouponStateCodeNotFoundException;
import com.nhnacademy.bookpubshop.couponstatecode.repository.CouponStateCodeRepository;
import com.nhnacademy.bookpubshop.couponstatecode.service.CouponStateCodeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 쿠폰상태코드 서비스 구현체.
 *
 * @author : 정유진
 * @since : 1.0
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CouponStateCodeServiceImpl implements CouponStateCodeService {

    private final CouponStateCodeRepository couponStateCodeRepository;

    /**
     * {@inheritDoc}
     *
     * @throws CouponStateCodeNotFoundException 쿠폰상태코드를 찾을 수 없음
     */
    @Override
    public GetCouponStateCodeResponseDto getCouponStateCode(Integer codeNo) {
        return couponStateCodeRepository.findByCodeNoAndCodeUsedTrue(codeNo)
                .orElseThrow(() -> new CouponStateCodeNotFoundException(codeNo));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetCouponStateCodeResponseDto> getCouponStateCodes() {
        return couponStateCodeRepository.findAllByCodeUsedTrue();
    }

}
