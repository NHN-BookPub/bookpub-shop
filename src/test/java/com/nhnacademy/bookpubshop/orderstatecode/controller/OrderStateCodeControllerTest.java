package com.nhnacademy.bookpubshop.orderstatecode.controller;

import static com.nhnacademy.bookpubshop.state.OrderState.COMPLETE_DELIVERY;
import static com.nhnacademy.bookpubshop.state.OrderState.COMPLETE_PAYMENT;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import com.nhnacademy.bookpubshop.orderstatecode.dto.CreateOrderStateCodeRequestDto;
import com.nhnacademy.bookpubshop.orderstatecode.dto.GetOrderStateCodeResponseDto;
import com.nhnacademy.bookpubshop.orderstatecode.service.OrderStateCodeService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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
 * 주문 상태 코드 컨트롤러 테스트입니다.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@WebMvcTest(OrderStateCodeController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class OrderStateCodeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderStateCodeService orderStateCodeService;

    ObjectMapper mapper;

    CreateOrderStateCodeRequestDto createOrderStateCodeRequestDto;

    GetOrderStateCodeResponseDto getOrderStateCodeResponseDto;


    String path = "/api/state/orderstates";
    String tokenPath = "/token/state/orderstates";

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        createOrderStateCodeRequestDto = new CreateOrderStateCodeRequestDto();
        getOrderStateCodeResponseDto = new GetOrderStateCodeResponseDto(null,
                COMPLETE_PAYMENT.getName(), COMPLETE_PAYMENT.isUsed(), "test_info");
    }

    @Test
    @DisplayName("주문 상태 코드 리스트 조회 테스트")
    void getOrderStateCodeListTest() throws Exception {
        //given & when
        when(orderStateCodeService.getOrderStateCodes()).thenReturn(
                List.of(getOrderStateCodeResponseDto));

        //then
        mockMvc.perform(RestDocumentationRequestBuilders.get(path))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codeNo").value(getOrderStateCodeResponseDto.getCodeNo()))
                .andExpect(
                        jsonPath("$[0].codeName").value(getOrderStateCodeResponseDto.getCodeName()))
                .andExpect(
                        jsonPath("$[0].codeUsed").value(getOrderStateCodeResponseDto.isCodeUsed()))
                .andExpect(
                        jsonPath("$[0].codeInfo").value(getOrderStateCodeResponseDto.getCodeInfo()))
                .andDo(print())
                .andDo(document("order-state-code-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].codeNo").description("주문 상태 코드 번호"),
                                fieldWithPath("[].codeName").description("주문 상태 코드명"),
                                fieldWithPath("[].codeUsed").description("사용 여부"),
                                fieldWithPath("[].codeInfo").description("정보")
                        )));
        verify(orderStateCodeService, times(1)).getOrderStateCodes();

    }

    @Test
    @DisplayName("단건 주문 상태 코드 조회 테스트")
    void getOrderStateCodeDetailTest() throws Exception {

        //when
        when(orderStateCodeService.getOrderStateCodeById(anyInt())).thenReturn(
                getOrderStateCodeResponseDto);

        //then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get(path + "/{codeNo}", anyInt()).contentType(
                                MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codeNo").value(getOrderStateCodeResponseDto.getCodeNo()))
                .andExpect(jsonPath("$.codeName").value(getOrderStateCodeResponseDto.getCodeName()))
                .andExpect(jsonPath("$.codeUsed").value(getOrderStateCodeResponseDto.isCodeUsed()))
                .andExpect(jsonPath("$.codeInfo").value(getOrderStateCodeResponseDto.getCodeInfo()))
                .andDo(print())
                .andDo(document("order-state-code-detail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("codeNo").description("주문 상태코드 번호")
                        ),
                        responseFields(
                                fieldWithPath("codeNo").description("주문 상태 코드 번호"),
                                fieldWithPath("codeName").description("주문 상태 코드명"),
                                fieldWithPath("codeUsed").description("사용 여부"),
                                fieldWithPath("codeInfo").description("정보")
                        )));
        verify(orderStateCodeService, times(1)).getOrderStateCodeById(anyInt());

    }

    @Test
    @DisplayName("주문 상태 코드 생성 성공 테스트")
    void createOrderStateCodeTest() throws Exception {

        //given
        ReflectionTestUtils.setField(createOrderStateCodeRequestDto, "codeName",
                COMPLETE_DELIVERY.getName());
        ReflectionTestUtils.setField(createOrderStateCodeRequestDto, "codeUsed",
                COMPLETE_DELIVERY.isUsed());
        ReflectionTestUtils.setField(createOrderStateCodeRequestDto, "codeInfo", "test_info");

        ArgumentCaptor<CreateOrderStateCodeRequestDto> captor = ArgumentCaptor.forClass(
                CreateOrderStateCodeRequestDto.class);

        //when
        doNothing().when(orderStateCodeService).createPricePolicy(createOrderStateCodeRequestDto);

        //then
        mockMvc.perform(post(tokenPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createOrderStateCodeRequestDto)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("order-state-code-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("codeName").description("주문 상태 코드명"),
                                fieldWithPath("codeUsed").description("사용 여부"),
                                fieldWithPath("codeInfo").description("정보")
                        )));
        verify(orderStateCodeService, times(1)).createPricePolicy(captor.capture());
    }

    @Test
    @DisplayName("주문 상태 코드 생성 실패 테스트 (Validation Exception) - CodeName NotBlank Validation")
    void createOrderStateCodeNotBlankValidationFailTest() throws Exception {
        //given
        ReflectionTestUtils.setField(createOrderStateCodeRequestDto, "codeName", "    ");
        ReflectionTestUtils.setField(createOrderStateCodeRequestDto, "codeUsed",
                COMPLETE_DELIVERY.isUsed());
        ReflectionTestUtils.setField(createOrderStateCodeRequestDto, "codeInfo", "test_info");

        ArgumentCaptor<CreateOrderStateCodeRequestDto> captor = ArgumentCaptor.forClass(
                CreateOrderStateCodeRequestDto.class);

        //when
        doNothing().when(orderStateCodeService).createPricePolicy(createOrderStateCodeRequestDto);

        //then
        mockMvc.perform(post(tokenPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createOrderStateCodeRequestDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("주문 상태 코드명은 필수 입력사항입니다."))
                .andDo(print())
                .andDo(document("order-state-code-create-codeName-notBlank-Fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("codeName").description("주문 상태 코드명"),
                                fieldWithPath("codeUsed").description("사용 여부"),
                                fieldWithPath("codeInfo").description("정보")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("주문 상태 코드명은 필수 입력사항입니다.")
                        )));
        verify(orderStateCodeService, times(0)).createPricePolicy(captor.capture());

    }

    @Test
    @DisplayName("주문 상태 코드 생성 실패 테스트 (Validation Exception) - CodeName Length Validation")
    void createOrderStateCodeLengthValidationFailTest() throws Exception {
        //given
        ReflectionTestUtils.setField(createOrderStateCodeRequestDto, "codeName",
                "주문상태코드명20자초과검증테스트입니다.");
        ReflectionTestUtils.setField(createOrderStateCodeRequestDto, "codeUsed",
                COMPLETE_DELIVERY.isUsed());
        ReflectionTestUtils.setField(createOrderStateCodeRequestDto, "codeInfo", "test_info");

        ArgumentCaptor<CreateOrderStateCodeRequestDto> captor = ArgumentCaptor.forClass(
                CreateOrderStateCodeRequestDto.class);

        //when
        doNothing().when(orderStateCodeService).createPricePolicy(createOrderStateCodeRequestDto);

        //then
        mockMvc.perform(post(tokenPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createOrderStateCodeRequestDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("주문 상태 코드명은 최대 20글자 가능합니다."))
                .andDo(print())
                .andDo(document("order-state-code-create-codeName-Length-Fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("codeName").description("주문 상태 코드명"),
                                fieldWithPath("codeUsed").description("사용 여부"),
                                fieldWithPath("codeInfo").description("정보")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("주문 상태 코드명은 최대 20글자 가능합니다.")
                        )));
        verify(orderStateCodeService, times(0)).createPricePolicy(captor.capture());

    }

    @Test
    @DisplayName("주문 상태 코드 생성 실패 테스트 (Validation Exception) - codeInfo Length Validation")
    void createOrderStateCodeInfoLengthValidationFailTest() throws Exception {
        //given
        ReflectionTestUtils.setField(createOrderStateCodeRequestDto, "codeName",
                COMPLETE_DELIVERY.getName());
        ReflectionTestUtils.setField(createOrderStateCodeRequestDto, "codeUsed",
                COMPLETE_DELIVERY.isUsed());
        ReflectionTestUtils.setField(createOrderStateCodeRequestDto, "codeInfo",
                "안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요");

        ArgumentCaptor<CreateOrderStateCodeRequestDto> captor = ArgumentCaptor.forClass(
                CreateOrderStateCodeRequestDto.class);

        //when
        doNothing().when(orderStateCodeService).createPricePolicy(createOrderStateCodeRequestDto);

        //then
        mockMvc.perform(post(tokenPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createOrderStateCodeRequestDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("정보는 최대 100글자 가능합니다."))
                .andDo(print())
                .andDo(document("order-state-code-create-codeInfo-Length-Fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("codeName").description("주문 상태 코드명"),
                                fieldWithPath("codeUsed").description("사용 여부"),
                                fieldWithPath("codeInfo").description("정보")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("정보는 최대 100글자 가능합니다.")
                        )));
        verify(orderStateCodeService, times(0)).createPricePolicy(captor.capture());

    }


    @Test
    @DisplayName("주문 상태 코드 사용여부 수정 테스트")
    void modifyOrderStateCodeUsedTest() throws Exception {

        //when
        doNothing().when(orderStateCodeService).modifyOrderStateCodeUsed(anyInt());

        //then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.put(path + "/{codeNo}", anyInt()).contentType(
                                MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("order-state-code-modify",
                        pathParameters(
                                parameterWithName("codeNo").description("주문 상태 코드 번호")
                        ))
                );

        verify(orderStateCodeService, times(1)).modifyOrderStateCodeUsed(anyInt());
    }
}