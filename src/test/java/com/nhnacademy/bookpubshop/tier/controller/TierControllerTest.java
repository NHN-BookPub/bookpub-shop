package com.nhnacademy.bookpubshop.tier.controller;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import com.nhnacademy.bookpubshop.tier.dto.request.CreateTierRequestDto;
import com.nhnacademy.bookpubshop.tier.dto.request.ModifyTierRequestDto;
import com.nhnacademy.bookpubshop.tier.dto.response.TierResponseDto;
import com.nhnacademy.bookpubshop.tier.service.TierService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

/**
 * TierController 테스트입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@WebMvcTest(TierController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
class TierControllerTest {
    @Autowired
    MockMvc mvc;
    ObjectMapper objectMapper;
    @MockBean
    TierService tierService;

    String path = "/api/tiers";
    CreateTierRequestDto createTierRequestDto;

    ModifyTierRequestDto modifyTierRequestDto;

    @BeforeEach
    void setUp() {
        createTierRequestDto = new CreateTierRequestDto();
        modifyTierRequestDto = new ModifyTierRequestDto();
        objectMapper = new ObjectMapper();
    }

    @DisplayName("Validation Exception 으로 생성실패")
    @Test
    void tierAddFailTest() throws Exception {
        //given
        ReflectionTestUtils.setField(createTierRequestDto, "tierName", "");
        doNothing().when(tierService).addTier(createTierRequestDto);

        //when && then
        mvc.perform(post(path)
                        .content(objectMapper.writeValueAsString(createTierRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("등급의 이름을 기입하여야 합니다."))
                .andDo(print());

    }

    @DisplayName("등급 생성 성공 테스트")
    @Test
    void tierAddSuccessTest() throws Exception {
        //given
        ReflectionTestUtils.setField(createTierRequestDto, "tierName", "GOLD");
        doNothing().when(tierService).addTier(createTierRequestDto);

        //when && then
        mvc.perform(post(path)
                        .content(objectMapper.writeValueAsString(createTierRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

    @DisplayName("등급수정 validation 검증 실패 테스트")
    @Test
    void tierModifyFailTest() throws Exception {
        //given
        ReflectionTestUtils.setField(modifyTierRequestDto, "tierName", "GOLD");
        doNothing().when(tierService).modifyTier(modifyTierRequestDto);

        //when && then
        mvc.perform(put(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyTierRequestDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("등급번호는 필수값입니다."))
                .andDo(print());
    }

    @DisplayName("등급수정 성공테스트")
    @Test
    void tierModifySuccessTest() throws Exception {
        //given
        ReflectionTestUtils.setField(modifyTierRequestDto, "tierNo", 1);
        ReflectionTestUtils.setField(modifyTierRequestDto, "tierName", "GOLD");
        doNothing().when(tierService).modifyTier(modifyTierRequestDto);

        mvc.perform(put(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyTierRequestDto)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());

        then(tierService).should().modifyTier(any(ModifyTierRequestDto.class));
    }

    @DisplayName("등급에 대한 단일값 조회 테스트")
    @Test
    void tierDetailsTest() throws Exception {
        //given
        TierResponseDto tierResponseDto = new TierResponseDto(1, "tierResponseDto");
        when(tierService.getTier(anyInt())).thenReturn(tierResponseDto);

        //when && then
        mvc.perform(get(path + "/{tierNo}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tierName").value(tierResponseDto.getTierName()))
                .andExpect(jsonPath("$.tierNo").value(tierResponseDto.getTierNo()))
                .andDo(print());

        then(tierService).should().getTier(anyInt());
    }

    @DisplayName("등급에 대한 리스트 조회")
    @Test
    void tierListTest() throws Exception {
        //given
        TierResponseDto tierResponseDto = new TierResponseDto(1, "tierTest");
        when(tierService.getTiers())
                .thenReturn(List.of(tierResponseDto));

        //when && then
        mvc.perform(get(path)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tierNo").value(tierResponseDto.getTierNo()))
                .andExpect(jsonPath("$[0].tierName").value(tierResponseDto.getTierName()))
                .andDo(print());

        then(tierService).should().getTiers();
    }
}