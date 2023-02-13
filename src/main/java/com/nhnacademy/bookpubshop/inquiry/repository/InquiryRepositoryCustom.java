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
    /**
     * 상품문의 작성 시, 해당 회원이 해당 상품을 구입한 적이 있는지 확인하기 위한 메서드입니다.
     *
     * @param memberNo  회원 번호
     * @param productNo 상품 번호
     * @return the boolean
     */
    boolean existsPurchaseHistoryByMemberNo(Long memberNo, Long productNo);

    /**
     * 상품에 따른 상품문의의 간단한 정보들을 조회하기 위한 메서드입니다.
     *
     * @param pageable  페이징 정보
     * @param productNo 상품문의를 조회할 상품 번호
     * @return 간단한 상품문의 정보를 담고 있는 페이징 정보
     */
    Page<GetInquirySummaryProductResponseDto> findSummaryInquiriesByProduct(Pageable pageable, Long productNo);

    /**
     * 불량상품 문의의 간단한 정보들을 조회하기 위한 메서드입니다.
     *
     * @param pageable 페이징 정보
     * @return 간단한 상품문의 정보를 담고 있는 페이징 정보
     */
    Page<GetInquirySummaryResponseDto> findSummaryErrorInquiries(Pageable pageable);

    /**
     * 모든 상품문의의 간단한 정보들을 조회하기 위한 메서드입니다.
     *
     * @param pageable       페이징 정보
     * @param searchKeyFir   검색 조건
     * @param searchValueFir 검색 값
     * @param searchKeySec   검색 조건 두번째
     * @param searchValueSec 검색 값 두번째
     * @return 간단한 상품문의 정보를 담고 있는 페이징 정보
     */
    Page<GetInquirySummaryResponseDto> findSummaryInquiries(Pageable pageable, String searchKeyFir, String searchValueFir,
                                                            String searchKeySec, String searchValueSec);

    /**
     * 마이페이지의 해당 회원의 상품문의의 간단한 정보들을 조회하기 위한 메서드입니다.
     *
     * @param pageable 페이징 정보
     * @param memberNo 회원 번호
     * @return 간단한 상품문의 정보를 담고 있는 페이징 정보
     */
    Page<GetInquirySummaryMemberResponseDto> findMemberInquiries(Pageable pageable, Long memberNo);

    /**
     * 상품문의 단건 조회를 위한 메서드입니다.
     * 상품문의의 자세한 정보들이 조회됩니다.
     *
     * @param inquiryNo 조회할 상품문의 번호
     * @return 상품문의의 자세한 정보가 담긴 dto
     */
    Optional<GetInquiryResponseDto> findInquiry(Long inquiryNo);
}
