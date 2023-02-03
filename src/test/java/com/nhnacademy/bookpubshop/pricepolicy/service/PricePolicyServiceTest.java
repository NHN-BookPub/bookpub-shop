package com.nhnacademy.bookpubshop.pricepolicy.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.nhnacademy.bookpubshop.pricepolicy.dto.request.CreatePricePolicyRequestDto;
import com.nhnacademy.bookpubshop.pricepolicy.dto.response.GetOrderPolicyResponseDto;
import com.nhnacademy.bookpubshop.pricepolicy.dto.response.GetPricePolicyResponseDto;
import com.nhnacademy.bookpubshop.pricepolicy.dummy.PricePolicyDummy;
import com.nhnacademy.bookpubshop.pricepolicy.entity.PricePolicy;
import com.nhnacademy.bookpubshop.pricepolicy.exception.NotFoundPricePolicyException;
import com.nhnacademy.bookpubshop.pricepolicy.repository.PricePolicyRepository;
import com.nhnacademy.bookpubshop.pricepolicy.service.impl.PricePolicyServiceImpl;
import com.nhnacademy.bookpubshop.state.PricePolicyState;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 가격 정책 서비스 테스트.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@ExtendWith(SpringExtension.class)
@Import(PricePolicyServiceImpl.class)
class PricePolicyServiceTest {
    PricePolicyService pricePolicyService;

    @MockBean
    PricePolicyRepository pricePolicyRepository;

    ArgumentCaptor<PricePolicy> captor;

    @BeforeEach
    void setUp() {
        captor = ArgumentCaptor.forClass(PricePolicy.class);
        pricePolicyService = new PricePolicyServiceImpl(pricePolicyRepository);
    }

    @Test
    @DisplayName("가격 정책 등록 서비스 테스트")
    void createPricePolicy_Success_Test() {
        // given
        CreatePricePolicyRequestDto dto = new CreatePricePolicyRequestDto();
        ReflectionTestUtils.setField(dto, "policyName", PricePolicyState.PACKAGING.getName());
        ReflectionTestUtils.setField(dto, "policyFee", 3000L);

        // when

        // then
        pricePolicyService.createPricePolicy(dto);

        verify(pricePolicyRepository, times(1))
                .save(captor.capture());
    }

    @Test
    @DisplayName("가걱 정책 수정 서비스 성공 테스트")
    void modifyPricePolicy_Success_Test() {
        // given
        Long fee = 2000L;
        PricePolicy pricePolicy = PricePolicyDummy.dummy();

        // when
        when(pricePolicyRepository.findById(anyInt()))
                .thenReturn(Optional.of(pricePolicy));

        // then
        pricePolicyService.modifyPricePolicyFee(anyInt(), fee);

        verify(pricePolicyRepository, times(1))
                .findById(anyInt());
    }

    @Test
    @DisplayName("가격 정책 수정 서비스 실패 테스트")
    void modifyPricePolicy_Fail_Test() {
        // given

        // when
        when(pricePolicyRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> pricePolicyService.modifyPricePolicyFee(1, 2L))
                .isInstanceOf(NotFoundPricePolicyException.class)
                .hasMessageContaining(NotFoundPricePolicyException.MESSAGE);
    }

    @Test
    @DisplayName("가격정책 리스트 조회 서비스 성공 테스트")
    void getPricePolicy_Success_Test() {
        // given
        GetPricePolicyResponseDto dto = new GetPricePolicyResponseDto(1, "배송비", 3000L, LocalDateTime.now());

        // when
        when(pricePolicyRepository.getPricePolicyByName(anyString()))
                .thenReturn(List.of(dto));

        // then
        pricePolicyService.getPricePoliciesByName(anyString());
        verify(pricePolicyRepository, times(1))
                .getPricePolicyByName(anyString());
    }

    @Test
    @DisplayName("가격정책 전체 조회 서비스 테스트")
    void getPricePolicies_Test() {
        // given
        GetPricePolicyResponseDto dto = new GetPricePolicyResponseDto(1, "배송비", 3000L, LocalDateTime.now());
        List<GetPricePolicyResponseDto> list = List.of(dto);

        // when
        when(pricePolicyRepository.findAllPolicies())
                .thenReturn(list);

        // then
        pricePolicyService.getPricePolicies();
        verify(pricePolicyRepository, times(1))
                .findAllPolicies();
    }

    @Test
    @DisplayName("최근 가격 정책 조회 성공 테스트")
    void getLatestPricePolicy_Success_Test() {
        // given
        PricePolicy pricePolicy = PricePolicyDummy.dummy();

        // when
        when(pricePolicyRepository.getLatestPricePolicyByName(anyString()))
                .thenReturn(Optional.of(pricePolicy));

        // then
        pricePolicyService.getLatestPricePolicyByName(anyString());
        verify(pricePolicyRepository, times(1))
                .getLatestPricePolicyByName(anyString());
    }

    @Test
    @DisplayName("최근 가격 정책 조회 실패 테스트")
    void getLatestPricePolicy_Fail_Test() {
        // given

        // when
        when(pricePolicyRepository.getLatestPricePolicyByName(anyString()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> pricePolicyService.getLatestPricePolicyByName("aa"))
                .isInstanceOf(NotFoundPricePolicyException.class)
                .hasMessageContaining(NotFoundPricePolicyException.MESSAGE);
    }

    @Test
    @DisplayName("주문에 필요한 배송비, 포장비 정책 조회 테스트")
    void getOrderRequiredPricePolicy_Test() throws Exception {
        GetOrderPolicyResponseDto pack = new GetOrderPolicyResponseDto(1, "포장비", 2000L);
        GetOrderPolicyResponseDto ship = new GetOrderPolicyResponseDto(2, "배송비", 3000L);

        when(pricePolicyRepository.getShipAndPackagePolicy())
                .thenReturn(List.of(pack,ship));

        pricePolicyService.getOrderRequestPolicy();
        verify(pricePolicyRepository, times(1))
                .getShipAndPackagePolicy();
    }

}