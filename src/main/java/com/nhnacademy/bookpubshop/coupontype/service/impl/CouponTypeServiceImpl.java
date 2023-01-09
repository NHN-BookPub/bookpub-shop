package com.nhnacademy.bookpubshop.coupontype.service.impl;

import com.nhnacademy.bookpubshop.coupontype.dto.response.GetCouponTypeResponseDto;
import com.nhnacademy.bookpubshop.coupontype.exception.CouponTypeNotFoundException;
import com.nhnacademy.bookpubshop.coupontype.repository.CouponTypeRepository;
import com.nhnacademy.bookpubshop.coupontype.service.CouponTypeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 쿠폰유형 서비스 구현체.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CouponTypeServiceImpl implements CouponTypeService {
    private final CouponTypeRepository couponTypeRepository;

    /**
     * {@inheritDoc}
     *
     * @throws CouponTypeNotFoundException 해당 쿠폰유형을 찾을 수 없음
     */
    @Override
    public GetCouponTypeResponseDto getCouponType(Long codeNo) {
        return couponTypeRepository.findByTypeNo(codeNo).orElseThrow(
                () -> new CouponTypeNotFoundException(codeNo));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetCouponTypeResponseDto> getCouponTypes() {
        return couponTypeRepository.findAllBy();
    }

}
