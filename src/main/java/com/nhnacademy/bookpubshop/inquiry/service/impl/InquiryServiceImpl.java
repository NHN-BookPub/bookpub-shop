package com.nhnacademy.bookpubshop.inquiry.service.impl;

import com.nhnacademy.bookpubshop.inquiry.dto.request.CreateInquiryRequestDto;
import com.nhnacademy.bookpubshop.inquiry.entity.Inquiry;
import com.nhnacademy.bookpubshop.inquiry.repository.InquiryRepository;
import com.nhnacademy.bookpubshop.inquiry.service.InquiryService;
import com.nhnacademy.bookpubshop.inquirystatecode.entity.InquiryStateCode;
import com.nhnacademy.bookpubshop.inquirystatecode.exception.InquiryStateCodeNotFoundException;
import com.nhnacademy.bookpubshop.inquirystatecode.repository.InquiryStateCodeRepository;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.exception.MemberNotFoundException;
import com.nhnacademy.bookpubshop.member.repository.MemberRepository;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.exception.ProductNotFoundException;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 상품문의 서비스 구현체입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {
    private final InquiryRepository inquiryRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final InquiryStateCodeRepository inquiryStateCodeRepository;

    public void createInquiry(Long memberNo, CreateInquiryRequestDto request) {
        Member member = memberRepository.findById(memberNo)
                .orElseThrow(MemberNotFoundException::new);

        Product product = productRepository.findById(request.getProductNo())
                .orElseThrow(ProductNotFoundException::new);

        InquiryStateCode inquiryStateCode = inquiryStateCodeRepository.findById(request.getInquiryStateCodeNo())
                .orElseThrow(InquiryStateCodeNotFoundException::new);

        Inquiry parentInquiry = inquiryRepository.findById(request.getInquiryParentNo())
                .orElse(null);


        inquiryRepository.save(Inquiry.builder()
                .parentInquiry(parentInquiry)
                .member(member)
                .product(product)
                .inquiryStateCode(inquiryStateCode)
                .inquiryContent(request.getInquiryContent())
                .inquiryDisplayed(request.isInquiryDisplayed())
                .inquiryAnswered(request.isInquiryAnswered())
                .build());
    }

}
