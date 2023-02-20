package com.nhnacademy.bookpubshop.personalinquiry.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.exception.MemberNotFoundException;
import com.nhnacademy.bookpubshop.member.repository.MemberRepository;
import com.nhnacademy.bookpubshop.personalinquiry.dto.request.CreatePersonalInquiryRequestDto;
import com.nhnacademy.bookpubshop.personalinquiry.dto.response.GetPersonalInquiryResponseDto;
import com.nhnacademy.bookpubshop.personalinquiry.dto.response.GetSimplePersonalInquiryResponseDto;
import com.nhnacademy.bookpubshop.personalinquiry.dummy.PersonalInquiryDummy;
import com.nhnacademy.bookpubshop.personalinquiry.entity.PersonalInquiry;
import com.nhnacademy.bookpubshop.personalinquiry.exception.PersonalInquiryNotFoundException;
import com.nhnacademy.bookpubshop.personalinquiry.repository.PersonalInquiryRepository;
import com.nhnacademy.bookpubshop.personalinquiry.service.PersonalInquiryService;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 1:1문의 서비스 테스트.
 *
 * @author : 임태원
 * @since : 1.0
 **/
class PersonalInquiryServiceImplTest {

    PersonalInquiryService personalInquiryService;
    PersonalInquiryRepository personalInquiryRepository;
    MemberRepository memberRepository;

    BookPubTier tier;
    Member member;
    PersonalInquiry personalInquiry;

    CreatePersonalInquiryRequestDto requestDto;

    @BeforeEach
    void setUp() {
        personalInquiryRepository = Mockito.mock(PersonalInquiryRepository.class);
        memberRepository = Mockito.mock(MemberRepository.class);
        personalInquiryService = new PersonalInquiryServiceImpl(personalInquiryRepository, memberRepository);

        tier = TierDummy.dummy();
        member = MemberDummy.dummy(tier);
        personalInquiry = PersonalInquiryDummy.dummy(member);

        requestDto = new CreatePersonalInquiryRequestDto();
    }

    @Test
    @DisplayName("1:1문의 생성 성공")
    void createPersonalInquiry() {
        ReflectionTestUtils.setField(requestDto, "memberNo", 1L);
        ReflectionTestUtils.setField(requestDto, "inquiryTitle", "title");
        ReflectionTestUtils.setField(requestDto, "inquiryContent", "content");

        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));

        personalInquiryService.createPersonalInquiry(requestDto);

        verify(personalInquiryRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("1:1문의 생성 실패")
    void createPersonalInquiry_fail() {
        ReflectionTestUtils.setField(requestDto, "memberNo", 1L);
        ReflectionTestUtils.setField(requestDto, "inquiryTitle", "title");
        ReflectionTestUtils.setField(requestDto, "inquiryContent", "content");

        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> personalInquiryService.createPersonalInquiry(requestDto))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining(MemberNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("1:1 문의 삭제 성공")
    void deletePersonalInquiry() {
        when(personalInquiryRepository.findById(anyLong()))
                .thenReturn(Optional.of(personalInquiry));

        personalInquiryService.deletePersonalInquiry(1L);

        verify(personalInquiryRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("1:1 문의 삭제 실패")
    void deletePersonalInquiry_fail() {
        when(personalInquiryRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> personalInquiryService.deletePersonalInquiry(personalInquiry.getPersonalInquiryNo()))
                .isInstanceOf(PersonalInquiryNotFoundException.class)
                .hasMessageContaining(PersonalInquiryNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("회원의 1:1문의 리스트를 가져오는 메소드.")
    void getMemberPersonalInquiries() {
        GetSimplePersonalInquiryResponseDto dto = new GetSimplePersonalInquiryResponseDto(
                1L,
                "nickname",
                "title",
                true,
                LocalDateTime.of(2023, 2, 19, 0, 0)
        );
        List<GetSimplePersonalInquiryResponseDto> dto1 = new ArrayList<>();
        dto1.add(dto);
        Pageable pageable = Pageable.ofSize(10);

        Page<GetSimplePersonalInquiryResponseDto> page = PageableExecutionUtils.getPage(dto1, pageable, dto1::size);

        when(personalInquiryRepository.findMemberPersonalInquiries(any(), anyLong()))
                .thenReturn(page);

        personalInquiryService.getMemberPersonalInquiries(pageable, 1L);

        verify(personalInquiryRepository, times(1)).findMemberPersonalInquiries(any(), anyLong());
    }

    @Test
    @DisplayName("모든 회원의 문의 내용을 가져오는 메소드")
    void getPersonalInquiries() {
        GetSimplePersonalInquiryResponseDto dto = new GetSimplePersonalInquiryResponseDto(
                1L,
                "nickname",
                "title",
                true,
                LocalDateTime.of(2023, 2, 19, 0, 0)
        );
        List<GetSimplePersonalInquiryResponseDto> dto1 = new ArrayList<>();
        dto1.add(dto);
        Pageable pageable = Pageable.ofSize(10);

        Page<GetSimplePersonalInquiryResponseDto> page = PageableExecutionUtils.getPage(dto1, pageable, dto1::size);

        when(personalInquiryRepository.findPersonalInquiries(any()))
                .thenReturn(page);

        personalInquiryService.getPersonalInquiries(pageable);

        verify(personalInquiryRepository, times(1)).findPersonalInquiries(any());
    }

    @Test
    @DisplayName("1:1문의 상세내용 가져오기 성공")
    void getPersonalInquiry() {
        GetPersonalInquiryResponseDto dto = new GetPersonalInquiryResponseDto(
                1L,
                "nickname",
                "title",
                "content",
                true,
                LocalDateTime.of(2023, 2, 19, 0, 0),
                1L,
                "answerContent",
                LocalDateTime.of(2023, 2, 19, 0, 0)
        );

        when(personalInquiryRepository.findPersonalInquiry(anyLong()))
                .thenReturn(Optional.of(dto));

        personalInquiryService.getPersonalInquiry(1L);

        verify(personalInquiryRepository, times(1)).findPersonalInquiry(anyLong());
    }

    @Test
    @DisplayName("1:1문의 상세내용 가져오기 실패")
    void getPersonalInquiry_fail() {
        when(personalInquiryRepository.findPersonalInquiry(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> personalInquiryService.getPersonalInquiry(1L))
                .isInstanceOf(PersonalInquiryNotFoundException.class)
                .hasMessageContaining(PersonalInquiryNotFoundException.MESSAGE);
    }
}