package com.nhnacademy.bookpubshop.couponmonth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.bookpubshop.couponmonth.dto.request.CreateCouponMonthRequestDto;
import com.nhnacademy.bookpubshop.couponmonth.dto.request.ModifyCouponMonthRequestDto;
import com.nhnacademy.bookpubshop.couponmonth.dto.response.GetCouponMonthResponseDto;
import com.nhnacademy.bookpubshop.couponmonth.service.CouponMonthService;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import java.time.LocalDateTime;
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
 * 이달의 쿠폰 컨트롤러 테스트.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@WebMvcTest(CouponMonthController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class CouponMonthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CouponMonthService couponMonthService;

    ObjectMapper mapper;
    String url = "/api/coupon-months";
    String authUrl = "/token/coupon-months";

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("쿠폰 등록 api 테스트")
    void couponMonthAdd_Success_Test() throws Exception {
        // given
        CreateCouponMonthRequestDto request = new CreateCouponMonthRequestDto();
        ReflectionTestUtils.setField(request, "templateNo", 1L);
        ReflectionTestUtils.setField(request, "openedAt", LocalDateTime.now());
        ReflectionTestUtils.setField(request, "monthQuantity", 100);

        // when
        doNothing().when(couponMonthService).createCouponMonth(request);

        // then
        mockMvc.perform(post(authUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("coupon-month-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("templateNo").description("쿠쿤템플릿 번호 기입"),
                                        fieldWithPath("openedAt").description("쿠폰 오픈일자 기입"),
                                        fieldWithPath("monthQuantity").description("쿠폰 수량")
                                )
                        )
                );

        then(couponMonthService).should()
                .createCouponMonth(any(CreateCouponMonthRequestDto.class));
    }

    @Test
    @DisplayName("쿠폰 등록 api 실패 테스트 (templateNo validation)")
    void couponMonthAdd_TemplateNo_Validation_Fail_Test() throws Exception {
        // given
        CreateCouponMonthRequestDto request = new CreateCouponMonthRequestDto();
        ReflectionTestUtils.setField(request, "templateNo", null);
        ReflectionTestUtils.setField(request, "openedAt", LocalDateTime.now());
        ReflectionTestUtils.setField(request, "monthQuantity", 100);

        // when
        doNothing().when(couponMonthService).createCouponMonth(request);

        // then
        mockMvc.perform(post(authUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("쿠폰 템플릿 번호를 입력하세요."))
                .andDo(print())
                .andDo(document("coupon-month-create-templateNoFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("templateNo").description("쿠쿤템플릿 번호 기입"),
                                        fieldWithPath("openedAt").description("쿠폰 오픈일자 기입"),
                                        fieldWithPath("monthQuantity").description("쿠폰 수량")
                                ),
                                responseFields(
                                        fieldWithPath("[].message").description("쿠폰 템플릿 번호를 입력하세요.")
                                )
                        )
                );

    }

    @Test
    @DisplayName("쿠폰 등록 api 실패 테스트 (openedAt validation)")
    void couponMonthAdd_OpenedAt_Validation_Fail_Test() throws Exception {
        // given
        CreateCouponMonthRequestDto request = new CreateCouponMonthRequestDto();
        ReflectionTestUtils.setField(request, "templateNo", 1L);
        ReflectionTestUtils.setField(request, "openedAt", null);
        ReflectionTestUtils.setField(request, "monthQuantity", 100);

        // when
        doNothing().when(couponMonthService).createCouponMonth(request);

        // then
        mockMvc.perform(post(authUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("쿠폰 오픈 시간을 입력하세요."))
                .andDo(print())
                .andDo(document("coupon-month-create-openAtFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("templateNo").description("쿠쿤템플릿 번호 기입"),
                                        fieldWithPath("openedAt").description("쿠폰 오픈일자 기입"),
                                        fieldWithPath("monthQuantity").description("쿠폰 수량")
                                ),
                                responseFields(
                                        fieldWithPath("[].message").description("쿠폰 템플릿 번호를 입력하세요.")
                                )
                        )
                );
    }

    @Test
    @DisplayName("쿠폰 등록 api 실패 테스트 (monthQuantity null)")
    void couponMonthAdd_NullMonthQuantity_Validation_Fail_Test() throws Exception {
        // given
        CreateCouponMonthRequestDto request = new CreateCouponMonthRequestDto();
        ReflectionTestUtils.setField(request, "templateNo", 1L);
        ReflectionTestUtils.setField(request, "openedAt", LocalDateTime.now());
        ReflectionTestUtils.setField(request, "monthQuantity", null);

        // when
        doNothing().when(couponMonthService).createCouponMonth(request);

        // then
        mockMvc.perform(post(authUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("수량을 입력하세요."))
                .andDo(print())
                .andDo(document("coupon-month-create-monthQuantityFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("templateNo").description("쿠쿤템플릿 번호 기입"),
                                        fieldWithPath("openedAt").description("쿠폰 오픈일자 기입"),
                                        fieldWithPath("monthQuantity").description("쿠폰 수량")
                                ),
                                responseFields(
                                        fieldWithPath("[].message").description("수량을 입력하세요.")
                                )
                        )
                );
    }

    @Test
    @DisplayName("쿠폰 등록 api 실패 테스트 (monthQuantity null)")
    void couponMonthAdd_NegativeMonthQuantity_Validation_Fail_Test() throws Exception {
        // given
        CreateCouponMonthRequestDto request = new CreateCouponMonthRequestDto();
        ReflectionTestUtils.setField(request, "templateNo", 1L);
        ReflectionTestUtils.setField(request, "openedAt", LocalDateTime.now());
        ReflectionTestUtils.setField(request, "monthQuantity", -1);

        // when
        doNothing().when(couponMonthService).createCouponMonth(request);

        // then
        mockMvc.perform(post(authUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("0이상의 수량을 입력하세요."))
                .andDo(print())
                .andDo(document("coupon-month-create-monthQuantityFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("templateNo").description("쿠쿤템플릿 번호 기입"),
                                        fieldWithPath("openedAt").description("쿠폰 오픈일자 기입"),
                                        fieldWithPath("monthQuantity").description("쿠폰 수량")
                                ),
                                responseFields(
                                        fieldWithPath("[].message").description("0이상의 수량을 입력하세요.")
                                )
                        )
                );
    }

    @Test
    @DisplayName("이달의 쿠폰 수정 성공 테스트")
    void couponMonthModify_Success_Test() throws Exception {
        // given
        ModifyCouponMonthRequestDto request = new ModifyCouponMonthRequestDto(1L, LocalDateTime.now(), 100);

        // when
        doNothing().when(couponMonthService).modifyCouponMonth(request);

        // then
        mockMvc.perform(put(authUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("coupon-month-modify",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("monthNo").description("수정할 쿠폰의 번호"),
                                fieldWithPath("openedAt").description("쿠폰의 오픈 시각 "),
                                fieldWithPath("monthQuantity").description("수정할 쿠폰의 수량"))
                ));

        then(couponMonthService).should()
                .modifyCouponMonth(any(ModifyCouponMonthRequestDto.class));
    }

    @Test
    @DisplayName("쿠폰 수정 api 실패 테스트 (쿠폰 번호가 없는 경우)")
    void couponMonthModify_monthNo_Validation_Test() throws Exception {
        // given
        ModifyCouponMonthRequestDto request = new ModifyCouponMonthRequestDto(null, LocalDateTime.now(), 100);

        // when
        doNothing().when(couponMonthService).modifyCouponMonth(request);

        // then
        mockMvc.perform(put(authUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(jsonPath("$[0].message").value("수정할 이달의 쿠폰 번호를 입력하세요."))
                .andDo(print())
                .andDo(document("coupon-month-modify-monthNoFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("monthNo").description("수정할 쿠폰의 번호"),
                                fieldWithPath("openedAt").description("쿠폰의 오픈 시각 "),
                                fieldWithPath("monthQuantity").description("수정할 쿠폰의 수량")),
                        responseFields(
                                fieldWithPath("[].message").description("수정할 이달의 쿠폰 번호를 입력하세요.")
                        )
                ));
    }

    @Test
    @DisplayName("쿠폰 수정 api 실패 테스트 (오픈시간 null)")
    void couponMonthModify_openedAt_Validation_Test() throws Exception {
        // given
        ModifyCouponMonthRequestDto request = new ModifyCouponMonthRequestDto(1L, null, 100);

        // when
        doNothing().when(couponMonthService).modifyCouponMonth(request);

        // then
        mockMvc.perform(put(authUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("쿠폰 오픈 시간을 입력하세요."))
                .andDo(print())
                .andDo(document("coupon-month-modify-openFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("monthNo").description("수정할 쿠폰의 번호"),
                                fieldWithPath("openedAt").description("쿠폰의 오픈 시각 "),
                                fieldWithPath("monthQuantity").description("수정할 쿠폰의 수량")),
                        responseFields(
                                fieldWithPath("[].message").description("수정할 이달의 쿠폰 번호를 입력하세요.")
                        )
                ));
    }

    @Test
    @DisplayName("쿠폰 수정 api 실패 테스트 (monthQuantity null)")
    void couponMonthModify_NUllQuantity_Validation_Test() throws Exception {
        // given
        ModifyCouponMonthRequestDto request = new ModifyCouponMonthRequestDto(1L, LocalDateTime.now(), null);

        // when
        doNothing().when(couponMonthService).modifyCouponMonth(request);

        // then
        mockMvc.perform(put(authUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("수량을 입력하세요."))
                .andDo(print())
                .andDo(document("coupon-month-modify-quantityFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("monthNo").description("수정할 쿠폰의 번호"),
                                fieldWithPath("openedAt").description("쿠폰의 오픈 시각 "),
                                fieldWithPath("monthQuantity").description("수정할 쿠폰의 수량")),
                        responseFields(
                                fieldWithPath("[].message").description("수량을 입력하세요.")
                        )
                ));
    }

    @Test
    @DisplayName("쿠폰 수정 api 실패 테스트 (monthQuantity 음수")
    void couponMonthModify_NegativeQuantity_Validation_Test() throws Exception {
        // given
        ModifyCouponMonthRequestDto request = new ModifyCouponMonthRequestDto(1L, LocalDateTime.now(), -1);

        // when
        doNothing().when(couponMonthService).modifyCouponMonth(request);

        // then
        mockMvc.perform(put(authUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("0이상의 수량을 입력하세요."))
                .andDo(print())
                .andDo(document("coupon-month-modify-quantityMinusFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("monthNo").description("수정할 쿠폰의 번호"),
                                fieldWithPath("openedAt").description("쿠폰의 오픈 시각 "),
                                fieldWithPath("monthQuantity").description("수정할 쿠폰의 수량")),
                        responseFields(
                                fieldWithPath("[].message").description("0이상의 수량을 입력하세요.")
                        )
                ));
    }

    @Test
    @DisplayName("이달의 쿠폰 삭제 성공 테스트")
    void couponMonthDelete_Success_Test() throws Exception {
        // given

        // when
        doNothing().when(couponMonthService).deleteCouponMonth(anyLong());

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.delete(authUrl + "/{monthNo}", anyLong())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("coupon-month-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("monthNo").description("이달의 쿠폰 번호")
                        ))
                );
    }

    @Test
    @DisplayName("이달의 쿠폰 전체 테스트")
    void couponMonthList_Success_Test() throws Exception {
        // given
        GetCouponMonthResponseDto response = new GetCouponMonthResponseDto(1L, 1L, "name", "image", LocalDateTime.now(), 100);
        List<GetCouponMonthResponseDto> list = List.of(response);

        // when
        when(couponMonthService.getCouponMonths())
                .thenReturn(list);

        // then
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].monthNo").value(response.getMonthNo()))
                .andExpect(jsonPath("$[0].templateNo").value(response.getTemplateNo()))
                .andExpect(jsonPath("$[0].templateName").value(response.getTemplateName()))
                .andExpect(jsonPath("$[0].templateImage").value(response.getTemplateImage()))
                .andExpect(jsonPath("$[0].monthQuantity").value(response.getMonthQuantity()))
                .andDo(print())
                .andDo(document("coupon-month-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].monthNo").description("이달의 쿠폰 번호"),
                                fieldWithPath("[].templateNo").description("쿠폰템플릿 번호"),
                                fieldWithPath("[].templateName").description("쿠폰템플릿 이름"),
                                fieldWithPath("[].templateImage").description("쿠폰템플릿 이미지"),
                                fieldWithPath("[].monthQuantity").description("쿠폰 수량"),
                                fieldWithPath("[].openedAt").description("쿠폰 오픈 일시")
                        )));

        then(couponMonthService).should()
                .getCouponMonths();
    }

}