package com.nhnacademy.bookpubshop.inquirystatecode.service;

import com.nhnacademy.bookpubshop.inquirystatecode.dto.response.GetInquiryStateCodeResponseDto;
import java.util.List;

/**
 * 상품문의상태코드 서비스 인터페이스입니다.
 *
 * @author : 정유진
 * @since : 1.0
 */
public interface InquiryStateCodeService {
    /**
     * 회원이 사용가능한 상품문의상태코드 리스트를 조회하기 위한 메서드입니다.
     *
     * @return 상품문의상태코드 정보가 담긴 Dto 리스트
     */
    List<GetInquiryStateCodeResponseDto> getUsedCodeForMember();

    /**
     * 관리자가 사용가능한 상품문의상태코드 리스트를 조회하기 위한 메서드입니다.
     *
     * @return 상품문의상태코드 정보가 담긴 Dto 리스트
     */
    List<GetInquiryStateCodeResponseDto> getUsedCodeForAdmin();
}
