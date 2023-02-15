package com.nhnacademy.bookpubshop.personalinquiry.service;

import com.nhnacademy.bookpubshop.personalinquiry.dto.request.CreatePersonalInquiryRequestDto;
import com.nhnacademy.bookpubshop.personalinquiry.dto.response.GetPersonalInquiryResponseDto;
import com.nhnacademy.bookpubshop.personalinquiry.dto.response.GetSimplePersonalInquiryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 1대1문의를 다루기 위한 서비스 인터페이스입니다.
 *
 * @author : 정유진
 * @since : 1.0
 */
public interface PersonalInquiryService {
    /**
     * 1대1문의를 생성하기 위한 메서드입니다.
     *
     * @param createDto 1대1문의 생성 시 필요한 정보를 담은 dto
     */
    void createPersonalInquiry(CreatePersonalInquiryRequestDto createDto);

    /**
     * 1대1문의를 삭제하기 위한 메서드입니다.
     * soft delete 로써 작동합니다.
     *
     * @param personalInquiryNo 삭제할 1대1문의 번호
     */
    void deletePersonalInquiry(Long personalInquiryNo);

    /**
     * 해당 회원의 1대1문의 리스트가 담긴 페이징 정보를 조회하기 위한 메서드입니다.
     *
     * @param pageable 페이징 정보
     * @param memberNo 회원 번호
     * @return 해당 회원의 1대1문의 리스트가 담긴 페이징 정보
     */
    Page<GetSimplePersonalInquiryResponseDto> getMemberPersonalInquiries(
            Pageable pageable, Long memberNo);

    /**
     * 1대1문의 리스트가 담긴 페이징 정보를 조회하기 위한 메서드입니다.
     *
     * @param pageable the pageable
     * @return 1대1문의 리스트가 담긴 페이징 정보
     */
    Page<GetSimplePersonalInquiryResponseDto> getPersonalInquiries(Pageable pageable);

    /**
     * 1대1문의 단건 상세 조회를 위한 메서드입니다.
     *
     * @param inquiryNo 조회할 1대1문의 번호
     * @return 해당 1대1문의 상세 정보가 담긴 Dto
     */
    GetPersonalInquiryResponseDto getPersonalInquiry(Long inquiryNo);
}
