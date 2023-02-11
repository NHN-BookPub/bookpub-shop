package com.nhnacademy.bookpubshop.inquiry.service.impl;

import com.nhnacademy.bookpubshop.filemanager.FileManagement;
import com.nhnacademy.bookpubshop.inquiry.dto.request.CreateInquiryRequestDto;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquiryResponseDto;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquirySummaryMemberResponseDto;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquirySummaryProductResponseDto;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquirySummaryResponseDto;
import com.nhnacademy.bookpubshop.inquiry.entity.Inquiry;
import com.nhnacademy.bookpubshop.inquiry.exception.InquiryNotFoundException;
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
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@Slf4j
public class InquiryServiceImpl implements InquiryService {
    private final InquiryRepository inquiryRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final InquiryStateCodeRepository inquiryStateCodeRepository;
    private final FileManagement fileManagement;

    @Override
    public boolean verifyWritableInquiry(Long memberNo, Long productNo) {
        return inquiryRepository.existsPurchaseHistoryByMemberNo(memberNo, productNo);
    }

    @Transactional
    @Override
    public void createInquiry(Long memberNo, CreateInquiryRequestDto request) {

        Inquiry parentInquiry = null;
        if (Objects.nonNull(request.getInquiryParentNo())) {
            parentInquiry = inquiryRepository.findById(request.getInquiryParentNo())
                    .orElseThrow(InquiryNotFoundException::new);
        }

        Member member = memberRepository.findById(memberNo)
                .orElseThrow(MemberNotFoundException::new);

        Product product = productRepository.findById(request.getProductNo())
                .orElseThrow(ProductNotFoundException::new);

        InquiryStateCode inquiryStateCode = inquiryStateCodeRepository.findById(request.getInquiryStateCodeNo())
                .orElseThrow(InquiryStateCodeNotFoundException::new);

        inquiryRepository.save(Inquiry.builder()
                .parentInquiry(parentInquiry)
                .member(member)
                .product(product)
                .inquiryStateCode(inquiryStateCode)
                .inquiryTitle(request.getInquiryTitle())
                .inquiryContent(request.getInquiryContent())
                .inquiryDisplayed(request.isInquiryDisplayed())
                .build());
    }

//    @Override
//    public String addInquiryImage(MultipartFile image) throws IOException {
//        // 이미지 파일 받아 오브젝트 스토리지에 저장,
//        // 파일에 저장
//        // 그러나 파일은 문의 객체를 가지고 있지 않아 따로 넣어줘야함....
//        // 문의 객체가 생성되지 않은 상태이므로 이미지 패쓰만 가지고 있는 상태임
//        String imagePath = fileManagement.saveFile(null, null, null, null,
//                null, null, image, FileCategory.INQUIRY.getCategory(), FileCategory.INQUIRY.getPath())
//                .getFilePath();
//
//        return imagePath;
//    }

    @Transactional
    @Override
    public void deleteInquiryAnswer(Long inquiryNo) {
        Inquiry inquiry = inquiryRepository.findById(inquiryNo)
                .orElseThrow(InquiryNotFoundException::new);

        inquiryRepository.delete(inquiry);
    }

    @Transactional
    @Override
    public void deleteInquiry(Long inquiryNo) {
        List<Inquiry> parentInquiries = inquiryRepository.findByParentInquiry_InquiryNo(inquiryNo);

        Inquiry inquiry = inquiryRepository.findById(inquiryNo)
                .orElseThrow(InquiryNotFoundException::new);

        for (Inquiry parent : parentInquiries) {
            inquiryRepository.delete(parent);
        }
        inquiryRepository.delete(inquiry);
    }


    @Transactional
    @Override
    public void modifyCompleteInquiry(Long inquiryNo) {
        Inquiry inquiry = inquiryRepository.findById(inquiryNo)
                .orElseThrow(InquiryNotFoundException::new);

        inquiry.modifyAnswered();

    }

    @Override
    public Page<GetInquirySummaryProductResponseDto> getSummaryInquiriesByProduct(Pageable pageable, Long productNo) {
        return inquiryRepository.findSummaryInquiriesByProduct(pageable, productNo);
    }

    @Override
    public Page<GetInquirySummaryResponseDto> getSummaryInquiries(Pageable pageable) {
        return inquiryRepository.findSummaryInquiries(pageable);
    }

    @Override
    public Page<GetInquirySummaryMemberResponseDto> getMemberInquiries(Pageable pageable, Long memberNo) {
        return inquiryRepository.findMemberInquiries(pageable, memberNo);
    }

    @Override
    public GetInquiryResponseDto getInquiry(Long inquiryNo) {
        return inquiryRepository.findInquiry(inquiryNo)
                .orElseThrow(InquiryNotFoundException::new);
    }
}
