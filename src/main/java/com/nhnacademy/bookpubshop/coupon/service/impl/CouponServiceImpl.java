package com.nhnacademy.bookpubshop.coupon.service.impl;

import com.nhnacademy.bookpubshop.category.repository.CategoryRepository;
import com.nhnacademy.bookpubshop.coupon.dto.request.CreateCouponRequestDto;
import com.nhnacademy.bookpubshop.coupon.dto.response.GetCouponResponseDto;
import com.nhnacademy.bookpubshop.coupon.dto.response.GetOrderCouponResponseDto;
import com.nhnacademy.bookpubshop.coupon.entity.Coupon;
import com.nhnacademy.bookpubshop.coupon.exception.CouponNotFoundException;
import com.nhnacademy.bookpubshop.coupon.repository.CouponRepository;
import com.nhnacademy.bookpubshop.coupon.service.CouponService;
import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import com.nhnacademy.bookpubshop.coupontemplate.exception.CouponTemplateNotFoundException;
import com.nhnacademy.bookpubshop.coupontemplate.repository.CouponTemplateRepository;
import com.nhnacademy.bookpubshop.filemanager.FileManagement;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.exception.MemberNotFoundException;
import com.nhnacademy.bookpubshop.member.repository.MemberRepository;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductDetailResponseDto;
import com.nhnacademy.bookpubshop.product.exception.ProductNotFoundException;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    private final CategoryRepository categoryRepository;

    private final FileManagement fileManagement;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void createCoupon(CreateCouponRequestDto createRequestDto) {
        Member member = memberRepository.findByMemberId(createRequestDto.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        CouponTemplate couponTemplate =
                couponTemplateRepository.findById(createRequestDto.getTemplateNo())
                        .orElseThrow(() ->
                                new CouponTemplateNotFoundException(createRequestDto.getTemplateNo()));

        couponRepository.save(Coupon.builder()
                .couponTemplate(couponTemplate)
                .member(member)
                .build());
    }

    /**
     * {@inheritDoc}
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
    public Page<GetCouponResponseDto> getCoupons(Pageable pageable, String searchKey, String search) throws IOException {

        Page<GetCouponResponseDto> dto = couponRepository.findAllBy(pageable, searchKey, URLDecoder.decode(search, StandardCharsets.UTF_8));

        List<GetCouponResponseDto> dtoList = dto.getContent();
        List<GetCouponResponseDto> transformList = new ArrayList<>();

        for (GetCouponResponseDto tmpDto : dtoList) {
            if (Objects.nonNull(tmpDto.getTemplateImage())) {
                transformList.add(tmpDto.transform(
                        fileManagement.loadFile(tmpDto.getTemplateImage()
                        )));
            } else
                transformList.add(tmpDto.transform(null));
        }

        return new PageImpl<>(transformList, pageable, dto.getTotalElements());
    }

    @Override
    public List<GetOrderCouponResponseDto> getOrderCoupons(String memberId, List<Long> productNoList) {
        if (memberRepository.existsByMemberId(memberId))
            throw new MemberNotFoundException(memberId);

        productNoList.stream()
                .filter(no -> productRepository.existsById(no) == false)
                .findAny()
                .orElseThrow(() -> new ProductNotFoundException());

        for (Long no : productNoList) {
            Optional<GetProductDetailResponseDto> tmpProduct = productRepository.getProductDetailById(no);
            tmpProduct.get().getCategories();
        }

        return couponRepository.findByProductNo(memberId, productNoList);
    }
}
