package com.nhnacademy.bookpubshop.personalinquiryanswer.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.personalinquiry.dummy.PersonalInquiryDummy;
import com.nhnacademy.bookpubshop.personalinquiry.entity.PersonalInquiry;
import com.nhnacademy.bookpubshop.personalinquiry.exception.PersonalInquiryNotFoundException;
import com.nhnacademy.bookpubshop.personalinquiry.repository.PersonalInquiryRepository;
import com.nhnacademy.bookpubshop.personalinquiryanswer.dto.request.CreatePersonalInquiryAnswerRequestDto;
import com.nhnacademy.bookpubshop.personalinquiryanswer.dummy.PersonalInquiryAnswerDummy;
import com.nhnacademy.bookpubshop.personalinquiryanswer.entity.PersonalInquiryAnswer;
import com.nhnacademy.bookpubshop.personalinquiryanswer.exception.PersonalInquiryAnswerNotFoundException;
import com.nhnacademy.bookpubshop.personalinquiryanswer.repsitory.PersonalInquiryAnswerRepository;
import com.nhnacademy.bookpubshop.personalinquiryanswer.service.PersonalInquiryAnswerService;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 1:1 문의 답변 서비스 테스트.
 *
 * @author : 임태원
 * @since : 1.0
 **/
class PersonalInquiryAnswerServiceImplTest {
    PersonalInquiryAnswerService personalInquiryAnswerService;
    PersonalInquiryAnswerRepository personalInquiryAnswerRepository;
    PersonalInquiryRepository personalInquiryRepository;

    BookPubTier tier;
    Member member;
    PersonalInquiry personalInquiry;
    PersonalInquiryAnswer personalInquiryAnswer;
    CreatePersonalInquiryAnswerRequestDto requestDto;

    @BeforeEach
    void setUp() {
        personalInquiryAnswerRepository = Mockito.mock(PersonalInquiryAnswerRepository.class);
        personalInquiryRepository = Mockito.mock(PersonalInquiryRepository.class);

        personalInquiryAnswerService = new PersonalInquiryAnswerServiceImpl(personalInquiryAnswerRepository, personalInquiryRepository);

        requestDto = new CreatePersonalInquiryAnswerRequestDto();

        tier = TierDummy.dummy();
        member = MemberDummy.dummy(tier);
        personalInquiry = PersonalInquiryDummy.dummy2(member);
        personalInquiryAnswer = PersonalInquiryAnswerDummy.dummy2(personalInquiry);
    }

    @Test
    @DisplayName("문의 답변 생성 성공")
    void createPersonalInquiryAnswer() {
        ReflectionTestUtils.setField(requestDto, "personalInquiryNo", 1L);
        ReflectionTestUtils.setField(requestDto, "personalInquiryAnswerContent", "content");

        when(personalInquiryRepository.findById(anyLong()))
                .thenReturn(Optional.of(personalInquiry));

        personalInquiryAnswerService.createPersonalInquiryAnswer(requestDto);

        verify(personalInquiryRepository, times(1)).findById(anyLong());
        verify(personalInquiryAnswerRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("문의 답변 생성 실패 - 문의 null")
    void createPersonalInquiryAnswer_fail() {
        when(personalInquiryRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> personalInquiryAnswerService.createPersonalInquiryAnswer(requestDto))
                .isInstanceOf(PersonalInquiryNotFoundException.class)
                .hasMessageContaining(PersonalInquiryNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("1:1문의 답변 삭제 성공")
    void deletePersonalInquiryAnswer() {
        when(personalInquiryAnswerRepository.findById(anyLong()))
                .thenReturn(Optional.of(personalInquiryAnswer));
        when(personalInquiryRepository.findById(anyLong()))
                .thenReturn(Optional.of(personalInquiry));

        personalInquiryAnswerService.deletePersonalInquiryAnswer(1L);

        verify(personalInquiryRepository, times(1)).findById(anyLong());
        verify(personalInquiryAnswerRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("1:1문의 답변 삭제 실패_문의 null")
    void deletePersonalInquiryAnswer_fail_inquiry() {
        when(personalInquiryAnswerRepository.findById(anyLong()))
                .thenReturn(Optional.of(personalInquiryAnswer));
        when(personalInquiryRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> personalInquiryAnswerService.deletePersonalInquiryAnswer(1L))
                .isInstanceOf(PersonalInquiryNotFoundException.class)
                .hasMessageContaining(PersonalInquiryNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("1:1문의 답변 삭제 실패_문의답변 null")
    void deletePersonalInquiryAnswer_fail_inquiryAnswer() {
        when(personalInquiryAnswerRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        when(personalInquiryRepository.findById(anyLong()))
                .thenReturn(Optional.of(personalInquiry));

        assertThatThrownBy(() -> personalInquiryAnswerService.deletePersonalInquiryAnswer(1L))
                .isInstanceOf(PersonalInquiryAnswerNotFoundException.class)
                .hasMessageContaining(PersonalInquiryAnswerNotFoundException.MESSAGE);
    }
}