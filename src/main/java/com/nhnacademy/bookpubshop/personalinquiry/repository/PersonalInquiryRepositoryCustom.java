package com.nhnacademy.bookpubshop.personalinquiry.repository;

import com.nhnacademy.bookpubshop.personalinquiry.dto.response.GetPersonalInquiryResponseDto;
import com.nhnacademy.bookpubshop.personalinquiry.dto.response.GetSimplePersonalInquiryResponseDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 1대1문의 custom 레포지토리입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@NoRepositoryBean
public interface PersonalInquiryRepositoryCustom {
    /**
     * 해당 회원의 1대1문의의 간단한 정보 리스트를 담은 페이징 조회를 위한 메서드입니다.
     *
     * @param pageable 페이징 정보
     * @param memberNo 회원 번호
     * @return 1대1문의의 간단한 정보를 담은 페이징
     */
    Page<GetSimplePersonalInquiryResponseDto> findMemberPersonalInquiries(
            Pageable pageable, Long memberNo);

    /**
     * 1대1문의의 간단한 정보 리스트를 담은 페이징 조회를 위한 메서드입니다.
     *
     * @param pageable 페이징 정보
     * @return 1대1문의의 간단한 정보 리스트를 담은 페이징
     */
    Page<GetSimplePersonalInquiryResponseDto> findPersonalInquiries(Pageable pageable);

    /**
     * 1대1문의 단건 상세 조회를 위한 메서드입니다.
     *
     * @param personalInquiryNo 조회할 1대1문의 번호
     * @return 1대1문의 단건 상세 정보를 담은 optional
     */
    Optional<GetPersonalInquiryResponseDto> findPersonalInquiry(Long personalInquiryNo);
}
