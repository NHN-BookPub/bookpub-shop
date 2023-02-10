package com.nhnacademy.bookpubshop.couponmonth.service.impl;

import com.nhnacademy.bookpubshop.couponmonth.dto.request.CreateCouponMonthRequestDto;
import com.nhnacademy.bookpubshop.couponmonth.dto.request.ModifyCouponMonthRequestDto;
import com.nhnacademy.bookpubshop.couponmonth.dto.response.GetCouponMonthResponseDto;
import com.nhnacademy.bookpubshop.couponmonth.entity.CouponMonth;
import com.nhnacademy.bookpubshop.couponmonth.exception.CouponMonthNotFoundException;
import com.nhnacademy.bookpubshop.couponmonth.repository.CouponMonthRepository;
import com.nhnacademy.bookpubshop.couponmonth.service.CouponMonthService;
import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import com.nhnacademy.bookpubshop.coupontemplate.exception.CouponTemplateNotFoundException;
import com.nhnacademy.bookpubshop.coupontemplate.repository.CouponTemplateRepository;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CouponMonthService 구현체입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponMonthServiceImpl implements CouponMonthService {

    private final CouponMonthRepository couponMonthRepository;
    private final CouponTemplateRepository couponTemplateRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void createCouponMonth(CreateCouponMonthRequestDto createRequestDto) {
        CouponTemplate couponTemplate =
                couponTemplateRepository.findById(createRequestDto.getTemplateNo())
                        .orElseThrow(() ->
                                new CouponTemplateNotFoundException(
                                        createRequestDto.getTemplateNo()));

        couponMonthRepository.save(new CouponMonth(
                null,
                couponTemplate,
                createRequestDto.getOpenedAt(),
                createRequestDto.getMonthQuantity()
        ));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void modifyCouponMonth(ModifyCouponMonthRequestDto modifyRequestDto) {
        CouponMonth couponMonth = couponMonthRepository.findById(modifyRequestDto.getMonthNo())
                .orElseThrow(() ->
                        new CouponMonthNotFoundException(modifyRequestDto.getMonthNo()));

        couponMonth.modifyCouponMonth(modifyRequestDto.getOpenedAt(),
                modifyRequestDto.getMonthQuantity());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteCouponMonth(Long monthNo) {
        CouponMonth couponMonth = couponMonthRepository.findById(monthNo)
                .orElseThrow(() -> new CouponMonthNotFoundException(monthNo));

        couponMonthRepository.delete(couponMonth);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GetCouponMonthResponseDto getCouponMonth(Long monthNo) {
        return couponMonthRepository.getCouponMonth(monthNo)
                .orElseThrow(() -> new CouponMonthNotFoundException(monthNo));
    }

    /**
     * {@inheritDoc}
     *
     * @throws IOException 파일 입출력 에러
     */
    @Override
    public List<GetCouponMonthResponseDto> getCouponMonths() {

        return couponMonthRepository.getCouponMonths();
    }
}
