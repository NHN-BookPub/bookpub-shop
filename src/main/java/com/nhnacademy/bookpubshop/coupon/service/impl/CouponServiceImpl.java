package com.nhnacademy.bookpubshop.coupon.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookpubshop.coupon.dto.request.CreateCouponRequestDto;
import com.nhnacademy.bookpubshop.coupon.dto.request.IssueCouponMonthDto;
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
import com.nhnacademy.bookpubshop.point.entity.PointHistory;
import com.nhnacademy.bookpubshop.point.repository.PointHistoryRepository;
import com.nhnacademy.bookpubshop.product.exception.ProductNotFoundException;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
@Slf4j
@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {


    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;
    private final CouponTemplateRepository couponTemplateRepository;
    private final ProductRepository productRepository;
    private final CouponMonthRepository couponMonthRepository;
    private final PointHistoryRepository pointHistoryRepository;

    private final RabbitTemplate rabbitTemplate;

    private final ObjectMapper objectMapper;

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
    @Transactional(readOnly = true)
    public GetCouponResponseDto getCoupon(Long couponNo) {
        return couponRepository.findByCouponNo(couponNo)
                .orElseThrow(() -> new CouponNotFoundException(couponNo));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public Page<GetCouponResponseDto> getNegativeCouponList(Pageable pageable, Long memberNo) {
        if (!memberRepository.existsById(memberNo)) {
            throw new MemberNotFoundException();
        }

        return couponRepository.findNegativeCouponByMemberNo(pageable, memberNo);
    }

    /**
     * {@inheritDoc}
     *
     * @throws CouponNotFoundException 쿠폰이 존재하지 않을 경우 나오는 에러
     * @throws MemberNotFoundException 회원이 존재하지 않을 경우 나오는 에러
     */
    @Transactional
    @Override
    public void modifyPointCouponUsed(Long couponNo, Long memberNo) {
        Coupon coupon = couponRepository.findById(couponNo)
                .orElseThrow(() -> new CouponNotFoundException(couponNo));
        coupon.modifyCouponUsed();

        Long pointAmount = coupon.getCouponTemplate().getCouponPolicy().getPolicyPrice();

        Member member = memberRepository.findById(memberNo)
                .orElseThrow(MemberNotFoundException::new);
        member.increaseMemberPoint(pointAmount);

        pointHistoryRepository.save(PointHistory.builder()
                .member(member)
                .pointHistoryIncreased(true)
                .pointHistoryAmount(pointAmount)
                .pointHistoryReason("포인트 쿠폰 사용")
                .build());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existsCouponsByMemberNo(Long memberNo, List<Long> tierCoupons) {
        return couponRepository.existsTierCouponsByMemberNo(memberNo, tierCoupons);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existsCouponMonthIssued(Long memberNo, Long templateNo) {
        return couponRepository.existsMonthCoupon(memberNo, templateNo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Boolean> existsCouponMonthListIssued(Long memberNo, List<Long> couponList) {
        List<Long> couponMonthList = couponRepository.existsMonthCouponList(memberNo, couponList);
        List<Boolean> checkCouponMonthIssued = new ArrayList<>();

        for (Long coupon : couponList) {
            if (couponMonthList.contains(coupon)) {
                checkCouponMonthIssued.add(true);
            } else {
                checkCouponMonthIssued.add(false);
            }
        }
        return checkCouponMonthIssued;
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
    @Transactional
    public void issueCouponMonth(Long memberNo, Long templateNo)
            throws JsonProcessingException {
        IssueCouponMonthDto issueCouponMonthDto = new IssueCouponMonthDto(memberNo, templateNo);
        // 이달의 쿠폰 정보 메세지 큐에 발행
        couponMonthQueueProducer(issueCouponMonthDto);
    }


    /**
     * rabbitMQ Producer 로 메세지를 생성하는 메서드입니다.
     *
     * @param dto 이달의 쿠폰 발행 정보 dto
     * @throws JsonProcessingException json error
     */
    private void couponMonthQueueProducer(IssueCouponMonthDto dto) throws JsonProcessingException {

        String request = objectMapper.writeValueAsString(dto);

        rabbitTemplate.convertAndSend("amq.direct", "coupon.month", request);
    }

    /**
     * rabbitMQ Consumer 로 큐에 메시지를 소비합니다.
     *
     * @param message 소비하는 메시지
     * @throws JsonProcessingException json error
     */
    @RabbitListener(queues = "coupon.month.queue")
    public void issueMonthCouponConsumer(String message) throws JsonProcessingException {

        message = message.replace("\\", "");

        String realMessage = message.substring(1, message.length() - 1);

        IssueCouponMonthDto issueCouponMonthDto = objectMapper.readValue(realMessage,
                IssueCouponMonthDto.class);

        Long memberNo = issueCouponMonthDto.getMemberNo();

        Long templateNo = issueCouponMonthDto.getTemplateNo();

        //쿠폰 확인
        CouponTemplate couponTemplate = couponTemplateRepository.findById(templateNo)
                .orElseThrow(() -> new CouponTemplateNotFoundException(templateNo));

        // 이달의 쿠폰 확인
        CouponMonth couponMonth = couponMonthRepository.findByCouponTemplate(couponTemplate)
                .orElseThrow(() -> new CouponMonthNotFoundException(templateNo));

        boolean result = couponRepository.existsMonthCoupon(memberNo, templateNo);

        if (!result) {
            // 쿠폰이 발급되지 않았으면 발급.

            Member member = memberRepository.findById(memberNo)
                    .orElseThrow(MemberNotFoundException::new);

            couponMonth.minusCouponMonthQuantity();

            Coupon coupon = Coupon.builder()
                    .couponTemplate(couponTemplate)
                    .member(member)
                    .build();

            couponRepository.save(coupon);

        }
    }

}