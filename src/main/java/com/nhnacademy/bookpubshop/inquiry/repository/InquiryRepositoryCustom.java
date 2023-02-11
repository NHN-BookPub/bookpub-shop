package com.nhnacademy.bookpubshop.inquiry.repository;

import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquiryResponseDto;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquirySummaryMemberResponseDto;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquirySummaryProductResponseDto;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquirySummaryResponseDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 상품문의 custom 레포지토리.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@NoRepositoryBean
public interface InquiryRepositoryCustom {
    boolean existsPurchaseHistoryByMemberNo(Long memberNo, Long productNo);

    Page<GetInquirySummaryProductResponseDto> findSummaryInquiriesByProduct(Pageable pageable, Long productNo);

    Page<GetInquirySummaryResponseDto> findSummaryInquiries(Pageable pageable);

    Page<GetInquirySummaryMemberResponseDto> findMemberInquiries(Pageable pageable, Long memberNo);

    Optional<GetInquiryResponseDto> findInquiry(Long inquiryNo);
}
