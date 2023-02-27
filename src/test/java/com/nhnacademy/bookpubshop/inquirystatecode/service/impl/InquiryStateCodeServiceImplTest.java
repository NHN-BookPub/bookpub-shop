package com.nhnacademy.bookpubshop.inquirystatecode.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import com.nhnacademy.bookpubshop.inquirystatecode.dto.response.GetInquiryStateCodeResponseDto;
import com.nhnacademy.bookpubshop.inquirystatecode.dummy.InquiryCodeDummy;
import com.nhnacademy.bookpubshop.inquirystatecode.entity.InquiryStateCode;
import com.nhnacademy.bookpubshop.inquirystatecode.repository.InquiryStateCodeRepository;
import com.nhnacademy.bookpubshop.inquirystatecode.service.InquiryStateCodeService;
import com.nhnacademy.bookpubshop.state.InquiryState;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * 상품문의상태코드 서비스 테스트.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@ExtendWith(SpringExtension.class)
@Import(InquiryStateCodeServiceImpl.class)
class InquiryStateCodeServiceImplTest {
    @Autowired
    InquiryStateCodeService inquiryStateCodeService;

    @MockBean
    InquiryStateCodeRepository inquiryStateCodeRepository;

    InquiryStateCode inquiryStateCode;

    GetInquiryStateCodeResponseDto inquiryResponseDto;

    @BeforeEach
    void setUp() {
        inquiryStateCode = InquiryCodeDummy.dummy();

        inquiryResponseDto = new GetInquiryStateCodeResponseDto(1, InquiryState.NORMAL.getName());
    }

    @Test
    @DisplayName("멤버들이 사용하는 상품문의상태코드 조회 테스트")
    void getUsedCodeForMember() {
        when(inquiryStateCodeRepository.findUsedCodeForMember())
                .thenReturn(List.of(inquiryResponseDto));

        List<GetInquiryStateCodeResponseDto> result =
                inquiryStateCodeService.getUsedCodeForMember();

        assertThat(result.get(0).getInquiryCodeNo()).isEqualTo(inquiryResponseDto.getInquiryCodeNo());
        assertThat(result.get(0).getInquiryCodeName()).isEqualTo(inquiryResponseDto.getInquiryCodeName());

        verify(inquiryStateCodeRepository, times(1)).findUsedCodeForMember();
    }

    @Test
    @DisplayName("관리자가 사용하는 상품문의상태코드 조회 테스트")
    void getUsedCodeForAdmin() {
        //given
        GetInquiryStateCodeResponseDto adminInquiryResponseDto =
                new GetInquiryStateCodeResponseDto(1, InquiryState.ANSWER.getName());

        when(inquiryStateCodeRepository.findUsedCodeForAdmin())
                .thenReturn(List.of(adminInquiryResponseDto));

        List<GetInquiryStateCodeResponseDto> result =
                inquiryStateCodeService.getUsedCodeForAdmin();

        assertThat(result.get(0).getInquiryCodeNo()).isEqualTo(adminInquiryResponseDto.getInquiryCodeNo());
        assertThat(result.get(0).getInquiryCodeName()).isEqualTo(adminInquiryResponseDto.getInquiryCodeName());

        verify(inquiryStateCodeRepository, times(1)).findUsedCodeForAdmin();
    }
}