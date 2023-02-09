package com.nhnacademy.bookpubshop.tier.controller;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
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
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
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
@AutoConfigureRestDocs(outputDir = "target/snippets")
class TierControllerTest {
    @Autowired
    MockMvc mvc;

    ObjectMapper objectMapper;
    @MockBean
    TierService tierService;

    String path = "/api/tiers";
    String tokenPath = "/token/tiers";
    CreateTierRequestDto createTierRequestDto;

    ModifyTierRequestDto modifyTierRequestDto;

    @BeforeEach
    void setUp() {
        createTierRequestDto = new CreateTierRequestDto();
        modifyTierRequestDto = new ModifyTierRequestDto();
        objectMapper = new ObjectMapper();
    }

    @DisplayName("Validation Exception name 으로 생성실패")
    @Test
    void tierAddFailNameTest() throws Exception {
        //given
        ReflectionTestUtils.setField(createTierRequestDto, "tierName", "");
        ReflectionTestUtils.setField(createTierRequestDto, "tierValue", 1);
        ReflectionTestUtils.setField(createTierRequestDto, "tierPrice", 1L);
        ReflectionTestUtils.setField(createTierRequestDto, "tierPoint", 1L);
        doNothing().when(tierService).addTier(createTierRequestDto);

        //when && then
        mvc.perform(post(tokenPath)
                        .content(objectMapper.writeValueAsString(createTierRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("등급의 이름을 기입하여야 합니다."))
                .andDo(print())
                .andDo(document("tier-add-name-fail",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("등급의 이름을 기입하여야 합니다.")
                        )
                ));

    }

    @DisplayName("Validation Exception value 으로 생성실패")
    @Test
    void tierAddFailTest() throws Exception {
        //given
        ReflectionTestUtils.setField(createTierRequestDto, "tierName", "asdf");
        ReflectionTestUtils.setField(createTierRequestDto, "tierValue", null);
        ReflectionTestUtils.setField(createTierRequestDto, "tierPrice", 1L);
        ReflectionTestUtils.setField(createTierRequestDto, "tierPoint", 1L);
        doNothing().when(tierService).addTier(createTierRequestDto);

        //when && then
        mvc.perform(post(tokenPath)
                        .content(objectMapper.writeValueAsString(createTierRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("등급 값을 기입해야 합니다."))
                .andDo(print())
                .andDo(document("tier-add-value-fail",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("등급 값을 기입해야 합니다.")
                        )
                ));

    }

    @DisplayName("Validation Exception price null 로 생성실패")
    @Test
    void tierAddPriceFailTest() throws Exception {
        //given
        ReflectionTestUtils.setField(createTierRequestDto, "tierName", "asdf");
        ReflectionTestUtils.setField(createTierRequestDto, "tierValue", 1);
        ReflectionTestUtils.setField(createTierRequestDto, "tierPrice", null);
        ReflectionTestUtils.setField(createTierRequestDto, "tierPoint", 1L);
        doNothing().when(tierService).addTier(createTierRequestDto);

        //when && then
        mvc.perform(post(tokenPath)
                        .content(objectMapper.writeValueAsString(createTierRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("등급시 필요한 가격을 기입해야합니다."))
                .andDo(print())
                .andDo(document("tier-add-price-fail",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("등급시 필요한 가격을 기입해야합니다.")
                        )
                ));

    }

    @DisplayName("Validation Exception Point null 로 생성실패")
    @Test
    void tierAddFailPointTest() throws Exception {
        //given
        ReflectionTestUtils.setField(createTierRequestDto, "tierName", "asdf");
        ReflectionTestUtils.setField(createTierRequestDto, "tierValue", 1);
        ReflectionTestUtils.setField(createTierRequestDto, "tierPrice", 1L);
        ReflectionTestUtils.setField(createTierRequestDto, "tierPoint", null);
        doNothing().when(tierService).addTier(createTierRequestDto);

        //when && then
        mvc.perform(post(tokenPath)
                        .content(objectMapper.writeValueAsString(createTierRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("등급시 필요한 포인트량을 기입해야합니다."))
                .andDo(print())
                .andDo(document("tier-add-point-fail",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("등급시 필요한 포인트량을 기입해야합니다.")
                        )
                ));

    }

    @DisplayName("등급 생성 성공 테스트")
    @Test
    void tierAddSuccessTest() throws Exception {
        //given
        ReflectionTestUtils.setField(createTierRequestDto, "tierName", "GOLD");
        ReflectionTestUtils.setField(createTierRequestDto, "tierValue", 1);
        ReflectionTestUtils.setField(createTierRequestDto, "tierPrice", 1L);
        ReflectionTestUtils.setField(createTierRequestDto, "tierPoint", 1L);
        doNothing().when(tierService).addTier(createTierRequestDto);

        //when && then
        mvc.perform(post(tokenPath)
                        .content(objectMapper.writeValueAsString(createTierRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("tier-add",
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("tierName").description("등급명 기입"),
                                fieldWithPath("tierValue").description("등급 값 기입"),
                                fieldWithPath("tierPrice").description("등급시 필요한 가격 기입"),
                                fieldWithPath("tierPoint").description("등급의 지급 포인트 반환")
                        )));
    }

    @DisplayName("등급수정 validation 검증 실패 테스트")
    @Test
    void tierModifyFailTest() throws Exception {
        //given
        ReflectionTestUtils.setField(modifyTierRequestDto, "tierName", "GOLD");
        doNothing().when(tierService).modifyTier(modifyTierRequestDto);

        //when && then
        mvc.perform(put(tokenPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyTierRequestDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("등급번호는 필수값입니다."))
                .andDo(print())
                .andDo(document("tier-modify-fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
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

        mvc.perform(put(tokenPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyTierRequestDto)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("tier-modify",
                        preprocessResponse(prettyPrint()),
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
        TierResponseDto tierResponseDto = new TierResponseDto(1, "tierResponseDto", 1, 0L, 1000L);
        when(tierService.getTier(anyInt())).thenReturn(tierResponseDto);

        //when && then
        mvc.perform(RestDocumentationRequestBuilders.get(path + "/{tierNo}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tierName").value(tierResponseDto.getTierName()))
                .andExpect(jsonPath("$.tierNo").value(tierResponseDto.getTierNo()))
                .andExpect(jsonPath("$.tierValue").value(tierResponseDto.getTierValue()))
                .andExpect(jsonPath("$.tierPrice").value(tierResponseDto.getTierPrice()))
                .andExpect(jsonPath("$.tierPoint").value(tierResponseDto.getTierPoint()))

                .andDo(print())
                .andDo(document("get-tier",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("tierNo").description("Path 로 등급번호 기입")),
                        responseFields(
                                fieldWithPath("tierName").description("등급의 이름이 반환"),
                                fieldWithPath("tierNo").description("등급 번호가 반환"),
                                fieldWithPath("tierValue").description("등급 값이 반환"),
                                fieldWithPath("tierPrice").description("등급시 필요한 금액이 반환"),
                                fieldWithPath("tierPoint").description("등급의 지급 포인트 반환")
                        )));

        then(tierService).should().getTier(anyInt());
    }

    @DisplayName("등급명을 통한 등급 조회")
    @Test
    void getTierByName() throws Exception {
        when(tierService.getTierName(anyString()))
                .thenReturn(true);

        mvc.perform(RestDocumentationRequestBuilders.get(tokenPath + "/check-tierName?tierName=hi"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("get-tier-name",
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("tierName").description("등급 이름이 기입")
                        )));

        then(tierService).should().getTierName("hi");
    }

    @DisplayName("등급에 대한 리스트 조회")
    @Test
    void tierListTest() throws Exception {
        //given
        TierResponseDto tierResponseDto = new TierResponseDto(1, "tierTest", 1, 0L, 1000L);
        when(tierService.getTiers())
                .thenReturn(List.of(tierResponseDto));

        //when && then
        mvc.perform(get(tokenPath)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tierNo").value(tierResponseDto.getTierNo()))
                .andExpect(jsonPath("$[0].tierName").value(tierResponseDto.getTierName()))
                .andExpect(jsonPath("$[0].tierPrice").value(tierResponseDto.getTierPrice()))
                .andExpect(jsonPath("$[0].tierValue").value(tierResponseDto.getTierValue()))
                .andExpect(jsonPath("$[0].tierPoint").value(tierResponseDto.getTierPoint()))
                .andDo(print())
                .andDo(document("get-tiers",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].tierNo").description("등급 번호가 반환"),
                                fieldWithPath("[].tierName").description("등급명이 반환"),
                                fieldWithPath("[].tierValue").description("등급값이 반환"),
                                fieldWithPath("[].tierPrice").description("등급시 필요한 금액이 반환"),
                                fieldWithPath("[].tierPoint").description("등급의 지급 포인트 반환")
                        )
                ));

        then(tierService).should().getTiers();
    }
}