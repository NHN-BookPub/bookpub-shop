package com.nhnacademy.bookpubshop.inquirystatecode.repository;

import com.nhnacademy.bookpubshop.inquirystatecode.dto.response.GetInquiryStateCodeResponseDto;
import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Some description here.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@NoRepositoryBean
public interface InquiryStateCodeRepositoryCustom {
    List<GetInquiryStateCodeResponseDto> findUsedCodeForMember();

    List<GetInquiryStateCodeResponseDto> findUsedCodeForAdmin();
}
