package com.nhnacademy.bookpubshop.tier.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import com.nhnacademy.bookpubshop.tier.dto.request.CreateTierRequestDto;
import com.nhnacademy.bookpubshop.tier.dto.request.ModifyTierRequestDto;
import com.nhnacademy.bookpubshop.tier.dto.response.TierResponseDto;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import com.nhnacademy.bookpubshop.tier.exception.TierAlreadyExists;
import com.nhnacademy.bookpubshop.tier.exception.TierNotFoundException;
import com.nhnacademy.bookpubshop.tier.repository.TierRepository;
import com.nhnacademy.bookpubshop.tier.service.impl.TierServiceImpl;
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
 * 등급서비스 테스트입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@ExtendWith(SpringExtension.class)
@Import(TierServiceImpl.class)
class TierServiceTest {

    @Autowired
    private TierService tierService;

    @MockBean
    private TierRepository tierRepository;

    BookPubTier tier;

    ArgumentCaptor<BookPubTier> captor;
    CreateTierRequestDto createTierRequestDtoDummy;

    TierResponseDto tierResponseDto;
    String notFound = "등급이 존재하지않습니다.";

    ModifyTierRequestDto modifyTierRequestDto;

    @BeforeEach
    void setUp() {
        tierResponseDto = new TierResponseDto();
        modifyTierRequestDto = new ModifyTierRequestDto();
        createTierRequestDtoDummy = new CreateTierRequestDto();
        captor = ArgumentCaptor.forClass(BookPubTier.class);
        tier = TierDummy.dummy();
    }

    @DisplayName("등급등록시 회원등급을 찾을시 실패한 경우")
    @Test
    void addTierFailTest() {
        //given
        ReflectionTestUtils.setField(createTierRequestDtoDummy, "tierName", "GOLD");

        //when
        when(tierRepository.existsByTierName(anyString()))
                .thenReturn(true);

        //then
        assertThatThrownBy(() -> tierService.addTier(createTierRequestDtoDummy))
                .isInstanceOf(TierAlreadyExists.class)
                .hasMessageContaining(" 은 이미존재하는 등급입니다.");

    }

    @DisplayName("회원등급 등록시 성공한경우")
    @Test
    void addTierSuccessTest() {
        //given
        ReflectionTestUtils.setField(createTierRequestDtoDummy, "tierName", "GOLD");

        //when
        when(tierRepository.existsByTierName(anyString()))
                .thenReturn(false);

        //then
        tierService.addTier(createTierRequestDtoDummy);

        verify(tierRepository, times(1))
                .save(captor.capture());

        BookPubTier result = captor.getValue();
        assertThat(createTierRequestDtoDummy.getTierName())
                .isEqualTo(result.getTierName());
    }


    @DisplayName("회원등급 수정시 조회에 실패한경우")
    @Test
    void modifyTierFindFail() {
        //given
        ReflectionTestUtils.setField(modifyTierRequestDto, "tierName", "GOLD");
        //when & then
        when(tierRepository.findTier(anyInt()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> tierService.modifyTier(modifyTierRequestDto))
                .isInstanceOf(TierNotFoundException.class)
                .hasMessageContaining(notFound);

    }

    @DisplayName("회원등급 수정시 등급명이 겹한경우")
    @Test
    void modifyTierNameFail() {
        //given
        ReflectionTestUtils.setField(modifyTierRequestDto, "tierNo", 1);
        ReflectionTestUtils.setField(modifyTierRequestDto, "tierName", "GOLD");
        //when & then
        when(tierRepository.findById(anyInt()))
                .thenReturn(Optional.of(tier));
        when(tierRepository.existsByTierName(anyString()))
                .thenReturn(true);

        assertThatThrownBy(() -> tierService.modifyTier(modifyTierRequestDto))
                .isInstanceOf(TierAlreadyExists.class)
                .hasMessageContaining(" 은 이미존재하는 등급입니다.");
    }

    @DisplayName("회원등급 수정시 성공한경우")
    @Test
    void modifyTierSuccess() {
        //given
        ReflectionTestUtils.setField(modifyTierRequestDto, "tierNo", 1);
        ReflectionTestUtils.setField(modifyTierRequestDto, "tierName", "GOLD");

        //when & then
        when(tierRepository.findById(anyInt()))
                .thenReturn(Optional.of(tier));
        when(tierRepository.existsByTierName(anyString()))
                .thenReturn(false);

        tierService.modifyTier(modifyTierRequestDto);

        verify(tierRepository, times(1))
                .findById(anyInt());
        verify(tierRepository, times(1))
                .existsByTierName(anyString());
    }

    @DisplayName("등급조회 실패테스트")
    @Test
    void getTierFail() {
        //given & when
        when(tierRepository.findTier(anyInt()))
                .thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> tierService.getTier(1))
                .isInstanceOf(TierNotFoundException.class)
                .hasMessageContaining("등급이 존재하지않습니다.");
    }

    @DisplayName("등급조회성공 테스트")
    @Test
    void getTierSuccessTest() {
        //given
        tierRepository.save(tier);
        ReflectionTestUtils.setField(tierResponseDto, "tierName", tier.getTierName());

        when(tierRepository.save(any()))
                .thenReturn(tier);
        when(tierRepository.findTier(anyInt()))
                .thenReturn(Optional.of(tierResponseDto));

        //then
        TierResponseDto result = tierService.getTier(anyInt());
        assertThat(result.getTierName()).isEqualTo(tierResponseDto.getTierName());
        verify(tierRepository, times(1))
                .findTier(anyInt());
    }

    @DisplayName("등급 존재여부 확인 테스트")
    @Test
    void getTierByName() {
        when(tierRepository.existsByTierName(anyString()))
                .thenReturn(true);

        Boolean aa = tierService.getTierName("aa");
        assertThat(aa).isTrue();
    }

    @DisplayName("등급 전체리스트 조회 테스트")
    @Test
    void getTiersTest() {
        //given && when
        ReflectionTestUtils.setField(tierResponseDto, "tierNo", 1);
        ReflectionTestUtils.setField(tierResponseDto, "tierName", tier.getTierName());

        when(tierRepository.findTiers())
                .thenReturn(List.of(tierResponseDto));
        // then
        List<TierResponseDto> tiers = tierService.getTiers();
        assertThat(tiers.get(0).getTierName()).isEqualTo(tierResponseDto.getTierName());
        assertThat(tiers.get(0).getTierNo()).isEqualTo(tierResponseDto.getTierNo());

        verify(tierRepository, times(1))
                .findTiers();
    }
}