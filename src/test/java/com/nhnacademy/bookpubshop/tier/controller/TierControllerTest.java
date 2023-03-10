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
 * TierController ??????????????????.
 *
 * @author : ?????????
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

    @DisplayName("Validation Exception name ?????? ????????????")
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
                .andExpect(jsonPath("$[0].message").value("????????? ????????? ??????????????? ?????????."))
                .andDo(print())
                .andDo(document("tier-add-name-fail",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("????????? ????????? ??????????????? ?????????.")
                        )
                ));

    }

    @DisplayName("Validation Exception value ?????? ????????????")
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
                .andExpect(jsonPath("$[0].message").value("?????? ?????? ???????????? ?????????."))
                .andDo(print())
                .andDo(document("tier-add-value-fail",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("?????? ?????? ???????????? ?????????.")
                        )
                ));

    }

    @DisplayName("Validation Exception price null ??? ????????????")
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
                .andExpect(jsonPath("$[0].message").value("????????? ????????? ????????? ?????????????????????."))
                .andDo(print())
                .andDo(document("tier-add-price-fail",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("????????? ????????? ????????? ?????????????????????.")
                        )
                ));

    }

    @DisplayName("Validation Exception Point null ??? ????????????")
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
                .andExpect(jsonPath("$[0].message").value("????????? ????????? ??????????????? ?????????????????????."))
                .andDo(print())
                .andDo(document("tier-add-point-fail",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("????????? ????????? ??????????????? ?????????????????????.")
                        )
                ));

    }

    @DisplayName("?????? ?????? ?????? ?????????")
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
                                fieldWithPath("tierName").description("????????? ??????"),
                                fieldWithPath("tierValue").description("?????? ??? ??????"),
                                fieldWithPath("tierPrice").description("????????? ????????? ?????? ??????"),
                                fieldWithPath("tierPoint").description("????????? ?????? ????????? ??????")
                        )));
    }

    @DisplayName("???????????? validation ?????? ?????? ?????????")
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
                .andExpect(jsonPath("$[0].message").value("??????????????? ??????????????????."))
                .andDo(print())
                .andDo(document("tier-modify-fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("tierNo").description("??????????????? ??????"),
                                fieldWithPath("tierName").description("????????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("??????????????? ??????????????????.")
                        )
                ));
    }

    @DisplayName("???????????? ???????????????")
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
                                fieldWithPath("tierNo").description("???????????? ??????"),
                                fieldWithPath("tierName").description("????????? ??????")
                        )));

        then(tierService).should().modifyTier(any(ModifyTierRequestDto.class));
    }

    @DisplayName("????????? ?????? ????????? ?????? ?????????")
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
                        pathParameters(parameterWithName("tierNo").description("Path ??? ???????????? ??????")),
                        responseFields(
                                fieldWithPath("tierName").description("????????? ????????? ??????"),
                                fieldWithPath("tierNo").description("?????? ????????? ??????"),
                                fieldWithPath("tierValue").description("?????? ?????? ??????"),
                                fieldWithPath("tierPrice").description("????????? ????????? ????????? ??????"),
                                fieldWithPath("tierPoint").description("????????? ?????? ????????? ??????")
                        )));

        then(tierService).should().getTier(anyInt());
    }

    @DisplayName("???????????? ?????? ?????? ??????")
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
                                parameterWithName("tierName").description("?????? ????????? ??????")
                        )));

        then(tierService).should().getTierName("hi");
    }

    @DisplayName("????????? ?????? ????????? ??????")
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
                                fieldWithPath("[].tierNo").description("?????? ????????? ??????"),
                                fieldWithPath("[].tierName").description("???????????? ??????"),
                                fieldWithPath("[].tierValue").description("???????????? ??????"),
                                fieldWithPath("[].tierPrice").description("????????? ????????? ????????? ??????"),
                                fieldWithPath("[].tierPoint").description("????????? ?????? ????????? ??????")
                        )
                ));

        then(tierService).should().getTiers();
    }
}