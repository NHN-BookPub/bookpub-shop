package com.nhnacademy.bookpubshop.inquiry.service;

import com.nhnacademy.bookpubshop.inquiry.dto.request.CreateInquiryRequestDto;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquiryResponseDto;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquirySummaryMemberResponseDto;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquirySummaryProductResponseDto;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquirySummaryResponseDto;
import java.io.IOException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * 상품문의 서비스 인터페이스.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public interface InquiryService {
    /**
     * 해당 회원이 해당 상품의 상품문의를 작성할 수 있는지 확인하기 위한 메셔드입니다.
     *
     * @param memberNo  회원 번호
     * @param productNo 상품 번호
     * @return the boolean
     */
    boolean verifyWritableInquiry(Long memberNo, Long productNo);

    /**
     * 상품문의를 등록하기 위한 메서드입니다.
     *
     * @param memberNo 상품문의를 작성한 회원 번호
     * @param request  상품문의 등록 시 필요한 정보를 담은 dto
     */
    void createInquiry(Long memberNo, CreateInquiryRequestDto request);

    /**
     * 상품문의의 이미지를 저장하기 위한 메서드입니다.
     *
     * @param image 저장할 이미지 파일
     * @return 이미지의 경로
     * @throws IOException 이미지 저장 시 발생 에러
     */
    String addInquiryImage(MultipartFile image) throws IOException;

    /**
     * 상품문의의 답변을 삭제하기 위한 메서드입니다.
     * 자기 참조이므로 답변의 상품문의 번호를 사용합니다.
     *
     * @param inquiryNo 삭제할 상품문의 번호
     */
    void deleteInquiryAnswer(Long inquiryNo);

    /**
     * 상품문의를 삭제하기 위한 메서드입니다.
     *
     * @param inquiryNo 삭제할 상품문의 번호
     */
    void deleteInquiry(Long inquiryNo);

    /**
     * 해당 상품문의의 답변여부 변경을 위한 메서드입니다.
     *
     * @param inquiryNo 변경할 상품문의 번호
     */
    void modifyCompleteInquiry(Long inquiryNo);

    /**
     * 해당 상품 조회 시, 해당 상품에 등록된 상품문의의 간단한 정보 리스트를 조회하기 위한 메서드입니다.
     *
     * @param pageable  페이징 정보
     * @param productNo 조회할 상품 번호
     * @return 상품문의의 간단한 정보 리스트가 담긴 페이징 정보
     */
    Page<GetInquirySummaryProductResponseDto> getSummaryInquiriesByProduct(Pageable pageable, Long productNo);

    /**
     * 불량상품 문의의 간단한 정보 리스트를 조회하기 위한 메서드입니다.
     *
     * @param pageable 페이징 정보
     * @return 불량 상품문의의 간단한 정보 리스트가 담긴 페이징 정보
     */
    Page<GetInquirySummaryResponseDto> getSummaryErrorInquiries(Pageable pageable);

    /**
     * 모든 상품문의의 간단한 정보 리스트를 조회하기 위한 메서드입니다.
     *
     * @param pageable       페이징 정보
     * @param searchKeyFir   the search key fir
     * @param searchValueFir the search value fir
     * @param searchKeySec   the search key sec
     * @param searchValueSec the search value sec
     * @return the summary inquiries
     */
    Page<GetInquirySummaryResponseDto> getSummaryInquiries(Pageable pageable, String searchKeyFir, String searchValueFir,
                                                           String searchKeySec, String searchValueSec);

    /**
     * 마이페이지의 해당 회원이 작성한 상품문의의 간단한 정보 리스트를 조회하기 위한 메서드입니다.
     *
     * @param pageable 페이징 정보
     * @param memberNo 회원 번호
     * @return 해당 회원이 작성한 상품문의의 간단한 정보 리스트가 담긴 페이징 정보
     */
    Page<GetInquirySummaryMemberResponseDto> getMemberInquiries(Pageable pageable, Long memberNo);

    /**
     * 상품문의 단건 조회를 위한 메서드입니다.
     * 상품문의의 상세 정보들이 조회됩니다.
     *
     * @param inquiryNo 조회할 상품문의 번호
     * @return 상품문의의 상세 정보가 담긴 dto
     */
    GetInquiryResponseDto getInquiry(Long inquiryNo);
}
