package com.nhnacademy.bookpubshop.coupon.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
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
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.bookpubshop.coupon.dto.request.CreateCouponRequestDto;
import com.nhnacademy.bookpubshop.coupon.dto.response.GetCouponResponseDto;
import com.nhnacademy.bookpubshop.coupon.dto.response.GetOrderCouponResponseDto;
import com.nhnacademy.bookpubshop.coupon.dummy.CouponDummy;
import com.nhnacademy.bookpubshop.coupon.service.CouponService;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import com.nhnacademy.bookpubshop.utils.PageResponse;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

/**
 * CouponController 테스트.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@WebMvcTest(CouponController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class CouponControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    CouponService couponService;

    ObjectMapper mapper;
    String uri = "/api/coupons";
    String authUri = "/token/coupons";
    GetCouponResponseDto getCouponResponseDto;

    @BeforeEach
    void setUp() {
        getCouponResponseDto = CouponDummy.getCouponResponseDtoDummy();
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("쿠폰 전체 리스트 조회 api 테스트")
    void couponList() throws Exception {
        // given
        GetCouponResponseDto response = new GetCouponResponseDto(1L, "member", "template", "image", "일반", true,
                10L, 1000L, 1000L, LocalDateTime.now(), true);
        PageRequest pageable = PageRequest.of(0, 10);
        PageImpl<GetCouponResponseDto> page = new PageImpl<>(List.of(response), pageable, 1);

        // when
        when(couponService.getCoupons(pageable, null, null))
                .thenReturn(page);

        // then
        mockMvc.perform(get(authUri)
                        .param("page", mapper.writeValueAsString(pageable.getPageNumber()))
                        .param("size", mapper.writeValueAsString(pageable.getPageSize()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].couponNo").value(response.getCouponNo()))
                .andExpect(jsonPath("$.content[0].memberId").value(response.getMemberId()))
                .andExpect(jsonPath("$.content[0].templateName").value(response.getTemplateName()))
                .andExpect(jsonPath("$.content[0].templateImage").value(response.getTemplateImage()))
                .andExpect(jsonPath("$.content[0].policyFixed").value(response.isPolicyFixed()))
                .andExpect(jsonPath("$.content[0].policyPrice").value(response.getPolicyPrice()))
                .andExpect(jsonPath("$.content[0].policyMinimum").value(response.getPolicyMinimum()))
                .andExpect(jsonPath("$.content[0].maxDiscount").value(response.getMaxDiscount()))
                .andExpect(jsonPath("$.content[0].couponUsed").value(response.isCouponUsed()))
                .andDo(print())
                .andDo(document("couponList-get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("content[].couponNo").description("쿠폰의 고유 번호가 반환됩니다."),
                                fieldWithPath("content[].memberId").description("발급받은 유저의 아이디가 반환됩니다."),
                                fieldWithPath("content[].templateName").description("쿠폰을 만든 템플릿의 번호가 반환됩니다."),
                                fieldWithPath("content[].templateImage").description("발급받은 쿠폰의 이미지가 경로가 반환됩니다."),
                                fieldWithPath("content[].typeName").description("발급받은 쿠폰의 타입이 반환됩니다."),
                                fieldWithPath("content[].policyFixed").description("정율 여부가 반환됩니다."),
                                fieldWithPath("content[].policyPrice").description("할인 퍼센트가 반환됩니다."),
                                fieldWithPath("content[].policyMinimum").description("최소 주문금액이 반환됩니다."),
                                fieldWithPath("content[].maxDiscount").description("최대 할인금액이 반환됩니다,"),
                                fieldWithPath("content[].couponUsed").description("쿠폰 사용 여부가 반환됩니다."),
                                fieldWithPath("content[].finishedAt").description("완료시간"),
                                fieldWithPath("totalPages").description("총 페이지 수 입니다."),
                                fieldWithPath("number").description("현재 페이지 입니다."),
                                fieldWithPath("previous").description("이전페이지 존재 여부 입니다."),
                                fieldWithPath("next").description("다음페이지 존재 여부 입니다.")
                        )));

        then(couponService).should()
                .getCoupons(any(), any(), any());
    }

    @Test
    @DisplayName("단건 쿠폰을 조회 api 테스트")
    void couponDetail_Test() throws Exception {
        // given
        GetCouponResponseDto response = new GetCouponResponseDto(1L, "member", "template", "image", "일반", true,
                10L, 1000L, 1000L, LocalDateTime.now(), true);

        // when
        when(couponService.getCoupon(anyLong()))
                .thenReturn(response);

        // then
        mockMvc.perform(get(authUri + "/{couponNo}", anyLong())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("coupon-get.adoc",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("couponNo").description("쿠폰의 고유 번호가 반환됩니다."),
                                fieldWithPath("memberId").description("발급받은 유저의 아이디가 반환됩니다."),
                                fieldWithPath("templateName").description("쿠폰을 만든 템플릿의 번호가 반환됩니다."),
                                fieldWithPath("templateImage").description("발급받은 쿠폰의 이미지가 경로가 반환됩니다."),
                                fieldWithPath("typeName").description("발급받은 쿠폰의 타입이 반환됩니다."),
                                fieldWithPath("policyFixed").description("정율 여부가 반환됩니다."),
                                fieldWithPath("policyPrice").description("할인 퍼센트가 반환됩니다."),
                                fieldWithPath("policyMinimum").description("최소 주문금액이 반환됩니다."),
                                fieldWithPath("maxDiscount").description("최대 할인금액이 반환됩니다,"),
                                fieldWithPath("couponUsed").description("쿠폰 사용 여부가 반환됩니다."),
                                fieldWithPath("finishedAt").description("완료시간")
                        )));

        then(couponService).should()
                .getCoupon(anyLong());
    }

    @Test
    @DisplayName("쿠폰 생성 api 테스트")
    void AddCoupon_Test() throws Exception {
        // given
        CreateCouponRequestDto request = new CreateCouponRequestDto();
        ReflectionTestUtils.setField(request, "templateNo", 1L);
        ReflectionTestUtils.setField(request, "memberId", "idId");

        // when
        doNothing().when(couponService).createCoupon(request);

        // then
        mockMvc.perform(post(authUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("coupon-add",
                        requestFields(
                                fieldWithPath("templateNo").description("쿠폰 틀의 번호가 기입됩니다."),
                                fieldWithPath("memberId").description("발급받을 유저의 아이디가 기입됩니다.")
                        )));

        then(couponService).should()
                .createCoupon(any(CreateCouponRequestDto.class));
    }

    @Test
    @DisplayName("쿠폰 생성 api templateNo Validation 테스트")
    void addCoupon_NullTemplateNo_Validation_Test() throws Exception {
        // given
        CreateCouponRequestDto request = new CreateCouponRequestDto();
        ReflectionTestUtils.setField(request, "templateNo", null);
        ReflectionTestUtils.setField(request, "memberId", "idId");

        // when
        doNothing().when(couponService).createCoupon(request);

        // then
        mockMvc.perform(post(authUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("쿠폰템플릿 번호를 입력해주세요."))
                .andDo(print())
                .andDo(document("coupon-add-no-templateNo-validation",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("templateNo").description("쿠폰을 생성할 틀 고유번호입니다."),
                                fieldWithPath("memberId").description("발급받는 유저의 아이디 입니다.")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("template 번호는 null이 될 수 없습니다")
                        )));
    }

    @Test
    @DisplayName("쿠폰 생성 api memberNo Validation 테스트")
    void addCoupon_NullMemberId_Validation_Test() throws Exception {
        // given
        CreateCouponRequestDto request = new CreateCouponRequestDto();
        ReflectionTestUtils.setField(request, "templateNo", 1L);
        ReflectionTestUtils.setField(request, "memberId", null);

        // when
        doNothing().when(couponService).createCoupon(request);

        // then
        mockMvc.perform(post(authUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("멤버 아이디를 입력해주세요."))
                .andDo(print())
                .andDo(document("coupon-add-no-memberId-validation",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("templateNo").description("쿠폰을 생성할 틀 고유번호입니다."),
                                fieldWithPath("memberId").description("발급받는 유저의 아이디 입니다.")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("memberId는 null이 될 수 없습니다")
                        )));
    }


    @Test
    @DisplayName("쿠폰 사용여부 수정 api 테스트")
    void modifyCouponUsed_Test() throws Exception {
        // when
        doNothing().when(couponService).modifyCouponUsed(anyLong());

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.put(authUri + "/{couponNo}" + "/used", 1L))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("coupon-modify",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("couponNo").description("수정할 쿠폰 번호")
                        )));

        then(couponService).should()
                .modifyCouponUsed(any(Long.class));
    }

    @Test
    @DisplayName("주문에 필요한 쿠폰리스트 조회 api 테스트")
    void orderCouponList_Test() throws Exception {

        // given
        GetOrderCouponResponseDto orderCouponResponseDto = new GetOrderCouponResponseDto(
                1L, "testName", 1L, 1, true, 1000L, 1000L, 1000L, true
        );

        // when
        when(couponService.getOrderCoupons(anyLong(), anyLong())).thenReturn(List.of(orderCouponResponseDto));

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.get(uri + "/members/{memberNo}/order", 1L)
                        .param("productNo", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andDo(document("couponOrderList-get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("조회할 멤버 번호입니다.")
                        ),
                        requestParameters(
                                parameterWithName("productNo").description("주문할 상품 번호 리스트입니다")
                        ),
                        responseFields(
                                fieldWithPath("[].couponNo").description("조회된 쿠폰 번호입니다."),
                                fieldWithPath("[].templateName").description("조회된 쿠폰 이름입니다."),
                                fieldWithPath("[].productNo").description("쿠폰이 적용될 수 있는 상품 번호입니다."),
                                fieldWithPath("[].categoryNo").description("쿠폰이 적용될 수 있는 카테고리 번호입니다."),
                                fieldWithPath("[].policyFixed").description("쿠폰 정책 정액여부입니다."),
                                fieldWithPath("[].policyPrice").description("쿠폰 정책 할인가격(할인율)입니다."),
                                fieldWithPath("[].policyMinimum").description("쿠폰 사용 시 최소주문금액입니다."),
                                fieldWithPath("[].maxDiscount").description("쿠폰 사용 시 최대 할인 금액입니다."),
                                fieldWithPath("[].templateBundled").description("쿠폰 묶음 여부입니다.")
                        )));
    }

    @Test
    @DisplayName("마이페이지에서 사용가능한 쿠폰을 조회하는 메서드")
    void memberPositiveCouponList() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 10);
        PageImpl<GetCouponResponseDto> page = new PageImpl<>(List.of(getCouponResponseDto));
        PageResponse<GetCouponResponseDto> pageResponse = new PageResponse<>(page);
        when(couponService.getPositiveCouponList(any(), any()))
                .thenReturn(page);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/token/coupons/members/{memberNo}/positive", 1L)
                        .param("page", mapper.writeValueAsString(pageRequest.getPageNumber()))
                        .param("size", mapper.writeValueAsString(pageRequest.getPageSize()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].couponNo").value(mapper.writeValueAsString(getCouponResponseDto.getCouponNo())))
                .andExpect(jsonPath("$.content[0].memberId").value((getCouponResponseDto.getMemberId())))
                .andExpect(jsonPath("$.content[0].templateName").value((getCouponResponseDto.getTemplateName())))
                .andExpect(jsonPath("$.content[0].templateImage").value((getCouponResponseDto.getTemplateImage())))
                .andExpect(jsonPath("$.content[0].typeName").value((getCouponResponseDto.getTypeName())))
                .andExpect(jsonPath("$.content[0].policyFixed").value(getCouponResponseDto.isPolicyFixed()))
                .andExpect(jsonPath("$.content[0].policyPrice").value(mapper.writeValueAsString(getCouponResponseDto.getPolicyPrice())))
                .andExpect(jsonPath("$.content[0].policyMinimum").value(mapper.writeValueAsString(getCouponResponseDto.getPolicyMinimum())))
                .andExpect(jsonPath("$.content[0].maxDiscount").value(mapper.writeValueAsString(getCouponResponseDto.getMaxDiscount())))
                .andExpect(jsonPath("$.content[0].couponUsed").value(getCouponResponseDto.isCouponUsed()))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("coupon-get-positive",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("회원 번호")),
                        requestParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 사이즈")),
                        responseFields(
                                fieldWithPath("content[].couponNo").description("쿠폰 번호"),
                                fieldWithPath("content[].memberId").description("멤버 아이디"),
                                fieldWithPath("content[].templateName").description("쿠폰 템플릿 이름"),
                                fieldWithPath("content[].templateImage").description("쿠폰 템플릿 이미지"),
                                fieldWithPath("content[].typeName").description("유형 이름"),
                                fieldWithPath("content[].policyFixed").description("정률 정액 여부"),
                                fieldWithPath("content[].policyPrice").description("정률 정액 금액"),
                                fieldWithPath("content[].policyMinimum").description("쿠폰 최소 금액"),
                                fieldWithPath("content[].maxDiscount").description("최대 할인가격"),
                                fieldWithPath("content[].finishedAt").description("만료일자"),
                                fieldWithPath("content[].couponUsed").description("사용여부"),
                                fieldWithPath("totalPages").description("총 페이지 수"),
                                fieldWithPath("number").description("현재 페이지 번호"),
                                fieldWithPath("previous").description("이전 이동 가능 여부"),
                                fieldWithPath("next").description("다음 이동 가능 여부")
                        )));
    }

    @Test
    @DisplayName("회원이 포인트 포인트 쿠폰 사용시 상태변경 및 포인트 적립")
    void pointCouponModifyUsed() throws Exception {
        doNothing().when(couponService)
                .modifyPointCouponUsed(any(), any());

        mockMvc.perform(RestDocumentationRequestBuilders.put("/token/coupons/{couponNo}/point/members/{memberNo}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("coupon-put-point",
                        pathParameters(
                                parameterWithName("couponNo").description("쿠폰 번호"),
                                parameterWithName("memberNo").description("회원 번호"))
                ));
    }

    @Test
    @DisplayName("멤버의 등급쿠폰 발급 유무확인")
    void existsCouponListByMemberNo() throws Exception {
        when(couponService.existsCouponsByMemberNo(any(), any()))
                .thenReturn(true);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/token/coupons/{memberNo}/tier-coupons", 1L)
                        .param("tierCoupons", mapper.writeValueAsString(1L))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("coupon-get-tierCoupon",
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("memberNo").description("회원 번호")),
                        requestParameters(
                                parameterWithName("tierCoupons").description("등급 쿠폰 번호들이 기입"))
                ));
    }

    @Test
    @DisplayName("멤버에게 등급 쿠폰을 발급")
    void issueTierCoupons() throws Exception {
        doNothing().when(couponService).issueTierCouponsByMemberNo(any(), any());

        mockMvc.perform(RestDocumentationRequestBuilders.post("/token/coupons/{memberNo}/tier-coupons", 1L)
                        .param("tierCoupons", mapper.writeValueAsString(1L))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("coupon-post-tierCoupon",
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("memberNo").description("회원 번호")),
                        requestParameters(
                                parameterWithName("tierCoupons").description("등급 쿠폰 번호들이 기입"))
                ));


    }

    @Test
    @DisplayName("이달의 쿠폰 발급")
    void issueMonthCoupon() throws Exception {
        doNothing().when(couponService).issueCouponMonth(any(), any());

        mockMvc.perform(RestDocumentationRequestBuilders.post("/token/coupons/{memberNo}/month-coupon", 1L)
                        .param("templateNo", mapper.writeValueAsString(1L))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("coupon-post-month-coupon",
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("memberNo").description("회원 번호")),
                        requestParameters(
                                parameterWithName("templateNo").description("탬플릿 번호가 기입"))
                ));
    }

    @Test
    @DisplayName("이달의 쿠폰 발행 여부 확인")
    void checkCouponMonthIssued() throws Exception {
        when(couponService.existsCouponMonthIssued(any(), any()))
                .thenReturn(true);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/token/coupons/{memberNo}/month-coupon", 1L)
                        .param("templateNo", mapper.writeValueAsString(1L))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("coupon-get-month-coupon",
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("memberNo").description("회원 번호")),
                        requestParameters(
                                parameterWithName("templateNo").description("탬플릿 번호가 기입"))
                ));
    }

    @Test
    @DisplayName("이달의 쿠폰 중복 발급을 확인")
    void checkCouponMonthListIssued() throws Exception {
        when(couponService.existsCouponMonthListIssued(any(), any()))
                .thenReturn(List.of(true));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/token/coupons/{memberNo}/month-coupons/issue-check", 1L)
                        .param("couponList", mapper.writeValueAsString(1L))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("coupon-get-issue-check",
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("memberNo").description("회원 번호")),
                        requestParameters(
                                parameterWithName("couponList").description("이달의 쿠폰 번호들 기입"))
                ));
    }

    @Test
    @DisplayName("사용불가능한 쿠폰을 조회")
    void memberNegativeCouponList() throws Exception {
        PageImpl<GetCouponResponseDto> page = new PageImpl<>(List.of(getCouponResponseDto));
        PageRequest pageRequest = PageRequest.of(0, 10);
        when(couponService.getNegativeCouponList(any(), any()))
                .thenReturn(page);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/token/coupons/members/{memberNo}/negative", 1L)
                        .param("page", mapper.writeValueAsString(pageRequest.getPageNumber()))
                        .param("size", mapper.writeValueAsString(pageRequest.getPageSize()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andExpect(jsonPath("$.content[0].couponNo").value(mapper.writeValueAsString(getCouponResponseDto.getCouponNo())))
                .andExpect(jsonPath("$.content[0].memberId").value((getCouponResponseDto.getMemberId())))
                .andExpect(jsonPath("$.content[0].templateName").value((getCouponResponseDto.getTemplateName())))
                .andExpect(jsonPath("$.content[0].templateImage").value((getCouponResponseDto.getTemplateImage())))
                .andExpect(jsonPath("$.content[0].typeName").value((getCouponResponseDto.getTypeName())))
                .andExpect(jsonPath("$.content[0].policyFixed").value(getCouponResponseDto.isPolicyFixed()))
                .andExpect(jsonPath("$.content[0].policyPrice").value(mapper.writeValueAsString(getCouponResponseDto.getPolicyPrice())))
                .andExpect(jsonPath("$.content[0].policyMinimum").value(mapper.writeValueAsString(getCouponResponseDto.getPolicyMinimum())))
                .andExpect(jsonPath("$.content[0].maxDiscount").value(mapper.writeValueAsString(getCouponResponseDto.getMaxDiscount())))
                .andExpect(jsonPath("$.content[0].couponUsed").value(getCouponResponseDto.isCouponUsed()))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("coupon-get-negative",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("회원 번호")),
                        requestParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 사이즈")),
                        responseFields(
                                fieldWithPath("content[].couponNo").description("쿠폰 번호"),
                                fieldWithPath("content[].memberId").description("멤버 아이디"),
                                fieldWithPath("content[].templateName").description("쿠폰 템플릿 이름"),
                                fieldWithPath("content[].templateImage").description("쿠폰 템플릿 이미지"),
                                fieldWithPath("content[].typeName").description("유형 이름"),
                                fieldWithPath("content[].policyFixed").description("정률 정액 여부"),
                                fieldWithPath("content[].policyPrice").description("정률 정액 금액"),
                                fieldWithPath("content[].policyMinimum").description("쿠폰 최소 금액"),
                                fieldWithPath("content[].maxDiscount").description("최대 할인가격"),
                                fieldWithPath("content[].finishedAt").description("만료일자"),
                                fieldWithPath("content[].couponUsed").description("사용여부"),
                                fieldWithPath("totalPages").description("총 페이지 수"),
                                fieldWithPath("number").description("현재 페이지 번호"),
                                fieldWithPath("previous").description("이전 이동 가능 여부"),
                                fieldWithPath("next").description("다음 이동 가능 여부")
                        )));

    }

}