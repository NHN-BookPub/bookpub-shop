package com.nhnacademy.bookpubshop.inquirystatecode.repository;

import com.nhnacademy.bookpubshop.inquirystatecode.dto.response.GetInquiryStateCodeResponseDto;
import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 상품문의상태코드 custom 레포지토리입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@NoRepositoryBean
public interface InquiryStateCodeRepositoryCustom {
    /**
     * 회원이 사용 가능한 상품문의상태코드 리스트를 조회하기 위한 메서드입니다.
     *
     * @return 상품문의상태코드 정보가 담긴 dto 리스트
     */
    List<GetInquiryStateCodeResponseDto> findUsedCodeForMember();

    /**
     * 관리자가 사용 가능한 상품문의상태코드 리스트를 조회하기 위한 메서드입니다.
     *
     * @return 상품문의상태코드 정보가 담긴 dto 리스트
     */
    List<GetInquiryStateCodeResponseDto> findUsedCodeForAdmin();
}
