package com.nhnacademy.bookpubshop.reviewpolicy.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import com.nhnacademy.bookpubshop.reviewpolicy.dto.request.CreateReviewPolicyRequestDto;
import com.nhnacademy.bookpubshop.reviewpolicy.dto.request.ModifyPointReviewPolicyRequestDto;
import com.nhnacademy.bookpubshop.reviewpolicy.dto.response.GetReviewPolicyResponseDto;
import com.nhnacademy.bookpubshop.reviewpolicy.dummy.ReviewPolicyDummy;
import com.nhnacademy.bookpubshop.reviewpolicy.entity.ReviewPolicy;
import com.nhnacademy.bookpubshop.reviewpolicy.exception.ReviewPolicyNotFoundException;
import com.nhnacademy.bookpubshop.reviewpolicy.repository.ReviewPolicyRepository;
import com.nhnacademy.bookpubshop.reviewpolicy.service.impl.ReviewPolicyServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 상품평 정책 서비스 테스트입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@ExtendWith(SpringExtension.class)
@Import(ReviewPolicyServiceImpl.class)
class ReviewPolicyServiceTest {
    @Autowired
    ReviewPolicyService reviewPolicyService;

    @MockBean
    ReviewPolicyRepository reviewPolicyRepository;

    ArgumentCaptor<ReviewPolicy> captor;
    CreateReviewPolicyRequestDto createDto = new CreateReviewPolicyRequestDto();
    ModifyPointReviewPolicyRequestDto modifyDto = new ModifyPointReviewPolicyRequestDto();
    ReviewPolicy reviewPolicy;

    @BeforeEach
    void setUp() {
        reviewPolicy = ReviewPolicyDummy.dummy();
        captor = ArgumentCaptor.forClass(ReviewPolicy.class);
    }

    @Test
    @DisplayName("상품평 정책 등록 성공 테스트")
    void createReviewPolicyTest_Success() {
        // given
        ReflectionTestUtils.setField(createDto, "sendPoint", 100L);

        // when&then
        reviewPolicyService.createReviewPolicy(createDto);

        verify(reviewPolicyRepository, times(1))
                .save(captor.capture());
        ReviewPolicy result = captor.getValue();

        assertThat(result.getSendPoint()).isEqualTo(createDto.getSendPoint());
    }

    @Test
    @DisplayName("상품평 정책 지급포인트 수정 성공 테스트")
    void modifyPointReviewPolicyTest_Success() {
        // given
        ReflectionTestUtils.setField(modifyDto, "sendPoint", 100L);
        ReflectionTestUtils.setField(modifyDto, "policyNo", 1);

        // when
        when(reviewPolicyRepository.findById(anyInt())).thenReturn(Optional.of(reviewPolicy));

        // then
        reviewPolicyService.modifyReviewPolicy(modifyDto);

        verify(reviewPolicyRepository, times(1)).findById(anyInt());
    }

    @Test
    @DisplayName("상품평 정책 지급포인트 수정 실패 테스트_상품평정책이 없는 경우")
    void modifyPointReviewPolicyTest_Fail_PolicyNo() {
        // given
        ReflectionTestUtils.setField(modifyDto, "sendPoint", 100L);
        ReflectionTestUtils.setField(modifyDto, "policyNo", 1);

        // when
        when(reviewPolicyRepository.findById(anyInt())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> reviewPolicyService.modifyReviewPolicy(modifyDto))
                .isInstanceOf(ReviewPolicyNotFoundException.class)
                .hasMessageContaining(ReviewPolicyNotFoundException.MESSAGE);

        verify(reviewPolicyRepository, times(1)).findById(anyInt());
    }

    @Test
    @DisplayName("상품평 정책 사용여부 수정 성공 테스트")
    void modifyUsedReviewPolicyTest_Success() {
        // given
        Integer policyNo = 1;

        // when
        when(reviewPolicyRepository.existsByPolicyUsedIsTrue()).thenReturn(true);
        when(reviewPolicyRepository.findByPolicyUsedIsTrue()).thenReturn(Optional.of(reviewPolicy));
        when(reviewPolicyRepository.findById(anyInt())).thenReturn(Optional.of(reviewPolicy));

        // then
        reviewPolicyService.modifyUsedReviewPolicy(policyNo);

        verify(reviewPolicyRepository, times(1)).existsByPolicyUsedIsTrue();
        verify(reviewPolicyRepository, times(1)).findByPolicyUsedIsTrue();
        verify(reviewPolicyRepository, times(1)).findById(anyInt());
    }

    @Test
    @DisplayName("상품평 정책 사용여부 수정 실패 테스트_상품평정책이 없는 경우")
    void modifyUsedReviewPolicyTest_Fail_PolicyNo() {
        // when
        when(reviewPolicyRepository.existsByPolicyUsedIsTrue()).thenReturn(true);
        when(reviewPolicyRepository.findByPolicyUsedIsTrue()).thenReturn(Optional.empty());
        when(reviewPolicyRepository.findById(anyInt())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> reviewPolicyService.modifyUsedReviewPolicy(1))
                .isInstanceOf(ReviewPolicyNotFoundException.class)
                .hasMessageContaining(ReviewPolicyNotFoundException.MESSAGE);

        verify(reviewPolicyRepository, times(1)).existsByPolicyUsedIsTrue();
        verify(reviewPolicyRepository, times(1)).findByPolicyUsedIsTrue();
        verify(reviewPolicyRepository, times(0)).findById(anyInt());
    }

    @Test
    @DisplayName("상품평 정책 리스트 조회 성공 테스트")
    void getReviewPoliciesTest_Success() {
        // given
        GetReviewPolicyResponseDto getDto = new GetReviewPolicyResponseDto(1, 100L, true);

        // when
        when(reviewPolicyRepository.findReviewPolicies()).thenReturn(List.of(getDto));

        // then
        List<GetReviewPolicyResponseDto> result = reviewPolicyService.getReviewPolicies();

        assertThat(result.get(0).getPolicyNo()).isEqualTo(getDto.getPolicyNo());
        assertThat(result.get(0).getSendPoint()).isEqualTo(getDto.getSendPoint());

        verify(reviewPolicyRepository, times(1)).findReviewPolicies();
    }
}