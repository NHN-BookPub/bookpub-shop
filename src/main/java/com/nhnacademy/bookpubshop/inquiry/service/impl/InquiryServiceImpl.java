package com.nhnacademy.bookpubshop.inquiry.service.impl;

import com.nhnacademy.bookpubshop.file.entity.File;
import com.nhnacademy.bookpubshop.file.exception.FileNotFoundException;
import com.nhnacademy.bookpubshop.file.repository.FileRepository;
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
import com.nhnacademy.bookpubshop.state.FileCategory;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final FileRepository fileRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean verifyWritableInquiry(Long memberNo, Long productNo) {
        return inquiryRepository.existsPurchaseHistoryByMemberNo(memberNo, productNo);
    }

    /**
     * {@inheritDoc}
     *
     * @throws InquiryNotFoundException          상품문의를 찾지 못했을 때 나는 에러
     * @throws MemberNotFoundException           회원을 찾지 못했을 때 나는 에러
     * @throws ProductNotFoundException          상품을 찾지 못했을 때 나는 에러
     * @throws InquiryStateCodeNotFoundException 상품문의상태코드를 찾지 못했을 때 나는 에러
     */
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

        Inquiry inquiry = inquiryRepository.save(Inquiry.builder()
                .parentInquiry(parentInquiry)
                .member(member)
                .product(product)
                .inquiryStateCode(inquiryStateCode)
                .inquiryTitle(request.getInquiryTitle())
                .inquiryContent(request.getInquiryContent())
                .inquiryDisplayed(request.isInquiryDisplayed())
                .build());

        for (String path : request.getImagePaths()) {
            if (!path.isBlank()) {
                File file = fileRepository.findByFilePath(path);
                inquiry.getInquiryImages().add(file);
                file.addInquiry(inquiry);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public String addInquiryImage(MultipartFile image) throws IOException {
        return fileManagement.saveFile(null, null, null, null,
                        null, null, image, FileCategory.INQUIRY.getCategory(), FileCategory.INQUIRY.getPath())
                .getFilePath();
    }

    /**
     * {@inheritDoc}
     *
     * @throws FileNotFoundException 파일을 찾지 못했을 때 나는 에러
     */
    @Transactional
    @Override
    public void deleteInquiryAnswer(Long inquiryNo) {
        Inquiry inquiry = inquiryRepository.findById(inquiryNo)
                .orElseThrow(InquiryNotFoundException::new);

        if (!inquiry.getInquiryImages().isEmpty()) {
            try {
                for (File file : inquiry.getInquiryImages())
                    fileManagement.deleteFile(file.getFilePath());
            } catch (IOException e) {
                throw new FileNotFoundException();
            }
        }

        inquiryRepository.delete(inquiry);
    }

    /**
     * {@inheritDoc}
     *
     * @throws InquiryNotFoundException 상품문의를 찾지 못했을 때 나는 에러
     * @throws FileNotFoundException    파일을 찾지 못했을 때 나는 에러
     */
    @Transactional
    @Override
    public void deleteInquiry(Long inquiryNo) {
        List<Inquiry> parentInquiries = inquiryRepository.findByParentInquiry_InquiryNo(inquiryNo);

        Inquiry inquiry = inquiryRepository.findById(inquiryNo)
                .orElseThrow(InquiryNotFoundException::new);

        if (!inquiry.getInquiryImages().isEmpty()) {
            try {
                for (File file : inquiry.getInquiryImages())
                    fileManagement.deleteFile(file.getFilePath());
            } catch (IOException e) {
                throw new FileNotFoundException();
            }
        }

        for (Inquiry parent : parentInquiries) {
            if (!inquiry.getInquiryImages().isEmpty()) {
                try {
                    for (File file : inquiry.getInquiryImages())
                        fileManagement.deleteFile(file.getFilePath());
                } catch (IOException e) {
                    throw new FileNotFoundException();
                }
            }
            inquiryRepository.delete(parent);
        }
        inquiryRepository.delete(inquiry);
    }

    /**
     * {@inheritDoc}
     *
     * @throws InquiryNotFoundException 상품문의를 찾지 못했을 때 나는 에러
     */
    @Transactional
    @Override
    public void modifyCompleteInquiry(Long inquiryNo) {
        Inquiry inquiry = inquiryRepository.findById(inquiryNo)
                .orElseThrow(InquiryNotFoundException::new);

        inquiry.modifyAnswered();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetInquirySummaryProductResponseDto> getSummaryInquiriesByProduct(Pageable pageable, Long productNo) {
        return inquiryRepository.findSummaryInquiriesByProduct(pageable, productNo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetInquirySummaryResponseDto> getSummaryErrorInquiries(Pageable pageable) {
        return inquiryRepository.findSummaryErrorInquiries(pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetInquirySummaryResponseDto> getSummaryInquiries(Pageable pageable,
                                                                  String searchKeyFir, String searchValueFir,
                                                                  String searchKeySec, String searchValueSec) {
        return inquiryRepository.findSummaryInquiries(pageable, searchKeyFir, searchValueFir, searchKeySec, searchValueSec);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetInquirySummaryMemberResponseDto> getMemberInquiries(Pageable pageable, Long memberNo) {
        return inquiryRepository.findMemberInquiries(pageable, memberNo);
    }

    /**
     * {@inheritDoc}
     *
     * @throws InquiryNotFoundException 상품문의가 존재하지 않을 때 나는 에러
     */
    @Override
    public GetInquiryResponseDto getInquiry(Long inquiryNo) {
        return inquiryRepository.findInquiry(inquiryNo)
                .orElseThrow(InquiryNotFoundException::new);
    }
}
