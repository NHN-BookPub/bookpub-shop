package com.nhnacademy.bookpubshop.order.relationship.controller;

import static com.nhnacademy.bookpubshop.state.OrderProductState.COMPLETE_PAYMENT;
import static com.nhnacademy.bookpubshop.state.OrderState.COMPLETE_DELIVERY;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import com.nhnacademy.bookpubshop.order.relationship.dto.CreateOrderProductStateCodeRequestDto;
import com.nhnacademy.bookpubshop.order.relationship.dto.GetOrderProductStateCodeResponseDto;
import com.nhnacademy.bookpubshop.order.relationship.service.OrderProductStateCodeService;
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
 * 주문상품상태코드 컨트롤러 테스트입니다.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@WebMvcTest(OrderProductStateCodeController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class OrderProductStateCodeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderProductStateCodeService orderProductStateCodeService;
    ObjectMapper mapper;
    CreateOrderProductStateCodeRequestDto createOrderProductStateCodeRequestDto;

    GetOrderProductStateCodeResponseDto getOrderProductStateCodeResponseDto;

    String path = "/api/state/orderproducts";


    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        createOrderProductStateCodeRequestDto = new CreateOrderProductStateCodeRequestDto();
        getOrderProductStateCodeResponseDto = new GetOrderProductStateCodeResponseDto(null,
                COMPLETE_PAYMENT.getName(), COMPLETE_PAYMENT.isUsed(), "test_info");

    }

    @Test
    @DisplayName("주문상품 상태코드 리스트 조회 테스트")
    void getOrderProductStateCodeListTest() throws Exception {

        //when
        when(orderProductStateCodeService.getOrderProductStateCodes()).thenReturn(
                List.of(getOrderProductStateCodeResponseDto));

        //then
        mockMvc.perform(get(path).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codeNo").value(
                        getOrderProductStateCodeResponseDto.getCodeNo()))
                .andExpect(jsonPath("$[0].codeName").value(
                        getOrderProductStateCodeResponseDto.getCodeName()))
                .andExpect(jsonPath("$[0].codeUsed").value(
                        getOrderProductStateCodeResponseDto.isCodeUsed()))
                .andExpect(jsonPath("$[0].codeInfo").value(
                        getOrderProductStateCodeResponseDto.getCodeInfo()))
                .andDo(print())
                .andDo(document("order-product-state-code-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].codeNo").description("주문 상품 상태 코드 번호"),
                                fieldWithPath("[].codeName").description("주문 상품 상태 코드명"),
                                fieldWithPath("[].codeUsed").description("주문 상품 상태 사용 여부"),
                                fieldWithPath("[].codeInfo").description("주문 상태 코드 설명"))));

        verify(orderProductStateCodeService, times(1)).getOrderProductStateCodes();

    }

    @Test
    @DisplayName("주문상품 상태코드 단건 조회 테스트")
    void getOrderProductStateDetail() throws Exception {

        //when
        when(orderProductStateCodeService.getOrderProductStateCode(anyInt())).thenReturn(
                getOrderProductStateCodeResponseDto);

        //given
        mockMvc.perform(RestDocumentationRequestBuilders.get(path + "/{codeNo}", anyInt())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.codeNo").value(getOrderProductStateCodeResponseDto.getCodeNo()))
                .andExpect(jsonPath("$.codeName").value(
                        getOrderProductStateCodeResponseDto.getCodeName()))
                .andExpect(jsonPath("$.codeUsed").value(
                        getOrderProductStateCodeResponseDto.isCodeUsed()))
                .andExpect(jsonPath("$.codeInfo").value(
                        getOrderProductStateCodeResponseDto.getCodeInfo()))
                .andDo(print())
                .andDo(document("order-product-state-code-detail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("codeNo").description("주문 상품 상태 코드 번호")
                        ),
                        responseFields(
                                fieldWithPath("codeNo").description("주문 상품 상태 코드 번호"),
                                fieldWithPath("codeName").description("주문 상품 상태 코드명"),
                                fieldWithPath("codeUsed").description("주문 상품 상태 사용 여부"),
                                fieldWithPath("codeInfo").description("주문 상태 코드 설명")
                        )));

        verify(orderProductStateCodeService, times(1)).getOrderProductStateCode(anyInt());
    }

    @Test
    @DisplayName("주문 상품 상태 코드 생성 성공 테스트")
    void createOrderProductStateCodeTest() throws Exception {

        //given
        ReflectionTestUtils.setField(createOrderProductStateCodeRequestDto, "codeName",
                COMPLETE_PAYMENT.getName());
        ReflectionTestUtils.setField(createOrderProductStateCodeRequestDto, "codeUsed",
                COMPLETE_PAYMENT.isUsed());
        ReflectionTestUtils.setField(createOrderProductStateCodeRequestDto, "codeInfo",
                "test_info");

        ArgumentCaptor<CreateOrderProductStateCodeRequestDto> captor = ArgumentCaptor.forClass(
                CreateOrderProductStateCodeRequestDto.class);

        //when
        doNothing().when(orderProductStateCodeService)
                .createOrderProductStateCode(createOrderProductStateCodeRequestDto);

        //then
        mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createOrderProductStateCodeRequestDto)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("order-product-state-code-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("codeName").description("주문 상품 상태 코드명"),
                                fieldWithPath("codeUsed").description("주문 상품 상태 사용 여부"),
                                fieldWithPath("codeInfo").description("주문 상태 코드 설명")
                        )));
        verify(orderProductStateCodeService, times(1)).createOrderProductStateCode(
                captor.capture());
    }

    @Test
    @DisplayName("주문 상품 상태 코드 생성 실패 테스트 (Validation Exception) - CodeName NotBlank Validation")
    void createOrderProductStateCodeNotBlankValidationFailTest() throws Exception {
        //given
        ReflectionTestUtils.setField(createOrderProductStateCodeRequestDto, "codeName", "    ");
        ReflectionTestUtils.setField(createOrderProductStateCodeRequestDto, "codeUsed",
                COMPLETE_PAYMENT.isUsed());
        ReflectionTestUtils.setField(createOrderProductStateCodeRequestDto, "codeInfo",
                "test_info");

        ArgumentCaptor<CreateOrderProductStateCodeRequestDto> captor = ArgumentCaptor.forClass(
                CreateOrderProductStateCodeRequestDto.class);

        //when
        doNothing().when(orderProductStateCodeService)
                .createOrderProductStateCode(createOrderProductStateCodeRequestDto);

        //then
        mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createOrderProductStateCodeRequestDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("주문 상품 상태 코드명은 필수 입력사항입니다."))
                .andDo(print())
                .andDo(document("order-product-state-code-create-codeName-notBlank-Fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("codeName").description("주문 상품 상태 코드명"),
                                fieldWithPath("codeUsed").description("주문 상품 상태 사용 여부"),
                                fieldWithPath("codeInfo").description("주문 상태 코드 설명")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("주문 상품 상태 코드명은 필수 입력사항입니다.")
                        )));
        verify(orderProductStateCodeService, times(0)).createOrderProductStateCode(
                captor.capture());

    }

    @Test
    @DisplayName("주문 상품 상태 코드 생성 실패 테스트 (Validation Exception) - CodeName Length Validation")
    void createOrderProductStateCodeLengthValidationFailTest() throws Exception {
        //given
        ReflectionTestUtils.setField(createOrderProductStateCodeRequestDto, "codeName",
                "주문상품상태코드명20자초과검증테스트입니다.");
        ReflectionTestUtils.setField(createOrderProductStateCodeRequestDto, "codeUsed",
                COMPLETE_DELIVERY.isUsed());
        ReflectionTestUtils.setField(createOrderProductStateCodeRequestDto, "codeInfo",
                "test_info");

        ArgumentCaptor<CreateOrderProductStateCodeRequestDto> captor = ArgumentCaptor.forClass(
                CreateOrderProductStateCodeRequestDto.class);

        //when
        doNothing().when(orderProductStateCodeService)
                .createOrderProductStateCode(createOrderProductStateCodeRequestDto);

        //then
        mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createOrderProductStateCodeRequestDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("주문 상품 상태 코드명은 최대 20글자 가능합니다."))
                .andDo(print())
                .andDo(document("order-product-state-code-create-codeName-Length-Fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("codeName").description("주문 상품 상태 코드명"),
                                fieldWithPath("codeUsed").description("주문 상품 상태 사용 여부"),
                                fieldWithPath("codeInfo").description("주문 상태 코드 설명")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description(
                                        "주문 상품 상태 코드명은 최대 20글자 가능합니다.")
                        )));
        verify(orderProductStateCodeService, times(0)).createOrderProductStateCode(
                captor.capture());

    }

    @Test
    @DisplayName("주문 상품 상태 코드 생성 실패 테스트 (Validation Exception) - codeInfo Length Validation")
    void createOrderProductStateCodeInfoLengthValidationFailTest() throws Exception {
        //given
        ReflectionTestUtils.setField(createOrderProductStateCodeRequestDto, "codeName",
                COMPLETE_PAYMENT.getName());
        ReflectionTestUtils.setField(createOrderProductStateCodeRequestDto, "codeUsed",
                COMPLETE_PAYMENT.isUsed());
        ReflectionTestUtils.setField(createOrderProductStateCodeRequestDto, "codeInfo",
                "안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요");

        ArgumentCaptor<CreateOrderProductStateCodeRequestDto> captor = ArgumentCaptor.forClass(
                CreateOrderProductStateCodeRequestDto.class);

        //when
        doNothing().when(orderProductStateCodeService)
                .createOrderProductStateCode(createOrderProductStateCodeRequestDto);

        //then
        mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createOrderProductStateCodeRequestDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("정보는 최대 100글자 가능합니다."))
                .andDo(print())
                .andDo(document("order-product-state-code-create-codeInfo-Length-Fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("codeName").description("주문 상품 상태 코드명"),
                                fieldWithPath("codeUsed").description("주문 상품 상태 사용 여부"),
                                fieldWithPath("codeInfo").description("주문 상태 코드 설명")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("정보는 최대 100글자 가능합니다.")
                        )));
        verify(orderProductStateCodeService, times(0)).createOrderProductStateCode(
                captor.capture());

    }


    @Test
    @DisplayName("주문 상품 상태 코드 사용여부 수정 테스트")
    void modifyOrderProductStateCodeUsedTest() throws Exception {

        //when
        doNothing().when(orderProductStateCodeService)
                .modifyUsedOrderProductStateCode(anyInt(), anyBoolean());

        //then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.put(path + "/{codeNo}", anyInt())
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("used", mapper.writeValueAsString(anyBoolean())))

                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("order-product-state-code-modify",
                        pathParameters(
                                parameterWithName("codeNo").description("주문 상품 번호")
                        ),
                        requestParameters(
                                parameterWithName("used").description("주문 상품 상태 코드 사용 여부")
                        ))
                );

        verify(orderProductStateCodeService, times(1)).modifyUsedOrderProductStateCode(anyInt(),
                anyBoolean());
    }

}