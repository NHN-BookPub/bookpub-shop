package com.nhnacademy.bookpubshop.tier.controller;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * TierController 테스트입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@WebMvcTest(TierController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
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
                .andDo(print())
            .andDo(document("tier-add-fail",
                requestFields(
                    fieldWithPath("tierName").description("등급명이 기입됩니다.")
                )
            ));

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
                .andDo(print())
            .andDo(document("tier-add",
                    requestFields(
                        fieldWithPath("tierName").description("등급명 기입")
                    )));
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
                .andDo(print())
            .andDo(document("tier-modify-fail",
                requestFields(
                    fieldWithPath("tierNo").description("등급번호가 기입"),
                    fieldWithPath("tierName").description("등급명 기입")
                ),
                responseFields(
                    fieldWithPath("[].message").description("등급번호는 필수값입니다.")
                )
            ));
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
                .andDo(print())
                    .andDo(document("tier-modify",
                        requestFields(
                            fieldWithPath("tierNo").description("등급번호 기입"),
                            fieldWithPath("tierName").description("등급명 기입")
                        )));

        then(tierService).should().modifyTier(any(ModifyTierRequestDto.class));
    }

    @DisplayName("등급에 대한 단일값 조회 테스트")
    @Test
    void tierDetailsTest() throws Exception {
        //given
        TierResponseDto tierResponseDto = new TierResponseDto(1, "tierResponseDto");
        when(tierService.getTier(anyInt())).thenReturn(tierResponseDto);

        //when && then
        mvc.perform(RestDocumentationRequestBuilders.get(path + "/{tierNo}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tierName").value(tierResponseDto.getTierName()))
                .andExpect(jsonPath("$.tierNo").value(tierResponseDto.getTierNo()))
                .andDo(print())
                    .andDo(document("get-tier",
                        pathParameters(parameterWithName("tierNo").description("Path 로 등급번호 기입")),
                        responseFields(
                            fieldWithPath("tierName").description("등급의 이름이 반환"),
                            fieldWithPath("tierNo").description("등급 번호가 반환")
                        )));

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
                .andDo(print())
                .andDo(document("get-tiers",
                    responseFields(
                        fieldWithPath("[].tierNo").description("등급 번호가 반환"),
                        fieldWithPath("[].tierName").description("등급명이 반환")
                    )
                ));

        then(tierService).should().getTiers();
    }
}