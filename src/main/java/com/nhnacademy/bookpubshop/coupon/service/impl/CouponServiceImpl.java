package com.nhnacademy.bookpubshop.coupon.service.impl;

import com.nhnacademy.bookpubshop.coupon.dto.request.CreateCouponRequestDto;
import com.nhnacademy.bookpubshop.coupon.dto.response.GetCouponResponseDto;
import com.nhnacademy.bookpubshop.coupon.dto.response.GetOrderCouponResponseDto;
import com.nhnacademy.bookpubshop.coupon.entity.Coupon;
import com.nhnacademy.bookpubshop.coupon.exception.CouponNotFoundException;
import com.nhnacademy.bookpubshop.coupon.repository.CouponRepository;
import com.nhnacademy.bookpubshop.coupon.service.CouponService;
import com.nhnacademy.bookpubshop.couponmonth.entity.CouponMonth;
import com.nhnacademy.bookpubshop.couponmonth.exception.CouponMonthNotFoundException;
import com.nhnacademy.bookpubshop.couponmonth.repository.CouponMonthRepository;
import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import com.nhnacademy.bookpubshop.coupontemplate.exception.CouponTemplateNotFoundException;
import com.nhnacademy.bookpubshop.coupontemplate.repository.CouponTemplateRepository;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.exception.MemberNotFoundException;
import com.nhnacademy.bookpubshop.member.repository.MemberRepository;
import com.nhnacademy.bookpubshop.product.exception.ProductNotFoundException;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CouponService 구현체.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;
    private final CouponTemplateRepository couponTemplateRepository;
    private final ProductRepository productRepository;
    private final CouponMonthRepository couponMonthRepository;

    /**
     * {@inheritDoc}
     *
     * @throws MemberNotFoundException         멤버를 찾을 수 없을 때 나오는 에러
     * @throws CouponTemplateNotFoundException 쿠폰템플릿을 찾을 수 없을 때 나오는 에러
     */
    @Override
    @Transactional
    public void createCoupon(CreateCouponRequestDto createRequestDto) {
        Member member = memberRepository.findByMemberId(createRequestDto.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        CouponTemplate couponTemplate =
                couponTemplateRepository.findById(createRequestDto.getTemplateNo())
                        .orElseThrow(() ->
                                new CouponTemplateNotFoundException(
                                        createRequestDto.getTemplateNo()));

        couponRepository.save(Coupon.builder()
                .couponTemplate(couponTemplate)
                .member(member)
                .build());
    }

    /**
     * {@inheritDoc}
     *
     * @throws CouponNotFoundException 쿠폰이 없을 때 나오는 에러
     */
    @Override
    @Transactional
    public void modifyCouponUsed(Long couponNo) {
        Coupon coupon = couponRepository.findById(couponNo)
                .orElseThrow(() -> new CouponNotFoundException(couponNo));

        coupon.modifyCouponUsed();
        if (!coupon.isCouponUsed()) {
            coupon.transferEmpty();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws CouponNotFoundException 쿠폰이 없을 때 나오는 에러
     */
    @Override
    public GetCouponResponseDto getCoupon(Long couponNo) {
        return couponRepository.findByCouponNo(couponNo)
                .orElseThrow(() -> new CouponNotFoundException(couponNo));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetCouponResponseDto> getCoupons(Pageable pageable, String searchKey,
            String search) {
        return couponRepository.findAllBy(pageable, searchKey, search);

    }

    /**
     * {@inheritDoc}
     *
     * @throws MemberNotFoundException  멤버가 없을 때 나오는 에러
     * @throws ProductNotFoundException 상품이 없을 때 나오는 에러
     */
    @Override
    public List<GetOrderCouponResponseDto> getOrderCoupons(Long memberNo, Long productNo) {
        if (!memberRepository.existsById(memberNo)) {
            throw new MemberNotFoundException();
        }

        if (!productRepository.existsById(productNo)) {
            throw new ProductNotFoundException();
        }

        return couponRepository.findByProductNo(memberNo, productNo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetCouponResponseDto> getPositiveCouponList(Pageable pageable, Long memberNo) {
        if (!memberRepository.existsById(memberNo)) {
            throw new MemberNotFoundException();
        }

        return couponRepository.findPositiveCouponByMemberNo(pageable, memberNo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetCouponResponseDto> getNegativeCouponList(Pageable pageable, Long memberNo) {
        if (!memberRepository.existsById(memberNo)) {
            throw new MemberNotFoundException();
        }

        return couponRepository.findNegativeCouponByMemberNo(pageable, memberNo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsCouponsByMemberNo(Long memberNo, List<Long> tierCoupons) {
        return couponRepository.existsTierCouponsByMemberNo(memberNo, tierCoupons);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void issueTierCouponsByMemberNo(Long memberNo, List<Long> tierCoupons) {

        for (Long couponNo : tierCoupons) {
            CouponTemplate couponTemplate = couponTemplateRepository.findById(
                    couponNo).orElseThrow(() -> new CouponTemplateNotFoundException(couponNo));

            Member member = memberRepository.findById(memberNo)
                    .orElseThrow(MemberNotFoundException::new);

            Coupon coupon = Coupon.builder()
                    .couponTemplate(couponTemplate)
                    .member(member)
                    .build();
            couponRepository.save(coupon);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkedMonthCouponByMemberNo(Long memberNo, Long templateNo) {
        return couponRepository.existsMonthCouponsByMemberNo(memberNo, templateNo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void issueMonthCouponByMemberNo(Long memberNo, Long templateNo) {

        CouponTemplate couponTemplate = couponTemplateRepository.findById(templateNo)
                .orElseThrow(() -> new CouponTemplateNotFoundException(templateNo));

        CouponMonth couponMonth = couponMonthRepository.findByCouponTemplate(couponTemplate)
                .orElseThrow(() -> new CouponMonthNotFoundException(templateNo));

        couponMonth.minusCouponMonthQuantity();

        Member member = memberRepository.findById(memberNo)
                .orElseThrow(MemberNotFoundException::new);

        Coupon coupon = Coupon.builder()
                .couponTemplate(couponTemplate)
                .member(member)
                .build();

        couponRepository.save(coupon);
    }
}
