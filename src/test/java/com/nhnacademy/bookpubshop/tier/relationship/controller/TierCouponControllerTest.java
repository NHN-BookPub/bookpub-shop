package com.nhnacademy.bookpubshop.tier.relationship.controller;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookpubshop.category.dummy.CategoryDummy;
import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.couponpolicy.dummy.CouponPolicyDummy;
import com.nhnacademy.bookpubshop.couponpolicy.entity.CouponPolicy;
import com.nhnacademy.bookpubshop.couponstatecode.dummy.CouponStateCodeDummy;
import com.nhnacademy.bookpubshop.couponstatecode.entity.CouponStateCode;
import com.nhnacademy.bookpubshop.coupontemplate.dummy.CouponTemplateDummy;
import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import com.nhnacademy.bookpubshop.coupontype.dummy.CouponTypeDummy;
import com.nhnacademy.bookpubshop.coupontype.entity.CouponType;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import com.nhnacademy.bookpubshop.tier.relationship.dto.request.CreateTierCouponRequestDto;
import com.nhnacademy.bookpubshop.tier.relationship.dto.response.GetTierCouponResponseDto;
import com.nhnacademy.bookpubshop.tier.relationship.dummy.TierCouponDummy;
import com.nhnacademy.bookpubshop.tier.relationship.entity.TierCoupon;
import com.nhnacademy.bookpubshop.tier.relationship.service.TierCouponService;
import java.util.ArrayList;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

/**
 * ?????? ?????? Controller ?????????.
 *
 * @author : ?????????
 * @since : 1.0
 **/
@WebMvcTest(TierCouponController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class TierCouponControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TierCouponService tierCouponService;

    TierCoupon tierCoupon;


    CouponTemplate couponTemplate;

    BookPubTier tier;


    CouponPolicy couponPolicy;

    CouponType couponType;
    Product product;
    Category category;
    CouponStateCode couponStateCode;

    CreateTierCouponRequestDto createTierCouponRequestDto;
    GetTierCouponResponseDto getTierCouponResponseDto;

    ObjectMapper mapper;

    String path = "/api/tier-coupons";
    String authPath = "/token/tier-coupons";


    @BeforeEach
    void setUp() {
        couponPolicy = CouponPolicyDummy.dummy();
        couponType = CouponTypeDummy.dummy();
        category = CategoryDummy.dummy();
        couponStateCode = CouponStateCodeDummy.dummy();
        couponTemplate = CouponTemplateDummy.dummy(couponPolicy, couponType, product, category,
                couponStateCode);
        tier = TierDummy.dummy();
        tierCoupon = TierCouponDummy.dummy(couponTemplate, tier);

        getTierCouponResponseDto = new GetTierCouponResponseDto(1L, "?????? ??????", 1, "white");
        createTierCouponRequestDto = new CreateTierCouponRequestDto();

        mapper = new ObjectMapper();
    }

    @Test
    @DisplayName("?????? ?????? ?????? ??????")
    void findTierCouponsTest() throws Exception {
        List<GetTierCouponResponseDto> response = new ArrayList<>();

        ReflectionTestUtils.setField(createTierCouponRequestDto, "templateNo", 1L);
        ReflectionTestUtils.setField(createTierCouponRequestDto, "tierNo", 1);

        response.add(getTierCouponResponseDto);

        Pageable pageable = Pageable.ofSize(10);

        Page<GetTierCouponResponseDto> page = PageableExecutionUtils.getPage(response, pageable,
                () -> 1L);

        when(tierCouponService.getTierCoupons(pageable)).thenReturn(page);

        mockMvc.perform(get(authPath)
                        .param("page", mapper.writeValueAsString(pageable.getPageNumber()))
                        .param("size", mapper.writeValueAsString(pageable.getPageSize()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(page)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("tier-Coupon-findAll",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("????????? ??????"),
                                parameterWithName("size").description("????????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("totalPages").description("??? ????????? ??????"),
                                fieldWithPath("number").description("?????? ????????? ??????"),
                                fieldWithPath("previous").description("?????? ????????? ?????? ??????"),
                                fieldWithPath("next").description("?????? ????????? ?????? ??????"),
                                fieldWithPath("content[].tierNo").description("?????? ??????"),
                                fieldWithPath("content[].tierName").description("?????? ??????"),
                                fieldWithPath("content[].templateNo").description("?????? ????????? ??????"),
                                fieldWithPath("content[].templateName").description("?????? ????????? ??????")
                        )
                ));

        verify(tierCouponService, times(1)).getTierCoupons(pageable);

    }

    @Test
    @DisplayName("????????? ?????? ?????? ??????")
    void findTierCouponByTierNoTest() throws Exception {
        List<Long> responses = new ArrayList<>();
        responses.add(1L);

        when(tierCouponService.getTierCouponsByTierNo(1)).thenReturn(responses);

        mockMvc.perform(RestDocumentationRequestBuilders.get(path + "/{tierNo}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("tier-Coupon-find-by-tierNo",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("tierNo").description("?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("[]").description("?????? ????????? ??????")
                        )
                ));

        verify(tierCouponService, times(1)).getTierCouponsByTierNo(anyInt());
    }

    @Test
    @DisplayName("?????? ?????? ?????? ??????")
    void createTierCouponTest() throws Exception {
        ReflectionTestUtils.setField(createTierCouponRequestDto, "templateNo", 1L);
        ReflectionTestUtils.setField(createTierCouponRequestDto, "tierNo", 1);

        ArgumentCaptor<CreateTierCouponRequestDto> captor = ArgumentCaptor.forClass(
                CreateTierCouponRequestDto.class);

        mockMvc.perform(post(authPath)
                        .content(mapper.writeValueAsString(createTierCouponRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("tier-coupon-create-success",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("templateNo").description("?????? ????????? ??????"),
                                fieldWithPath("tierNo").description("?????? ??????")
                        )));
        verify(tierCouponService, times(1)).createTierCoupon(captor.capture());

    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? ????????? Validation Exception ?????? ?????? ??????")
    void createTierCouponFailCauseTierNoIsNullTest() throws Exception {
        ReflectionTestUtils.setField(createTierCouponRequestDto, "templateNo", 1L);
        ReflectionTestUtils.setField(createTierCouponRequestDto, "tierNo", null);

        mockMvc.perform(post(authPath)
                        .content(mapper.writeValueAsString(createTierCouponRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("tier-coupon-create-tierNo-null-fail",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("templateNo").description("?????? ????????? ??????"),
                                fieldWithPath("tierNo").description("?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("?????? ????????? ??????????????????")
                        )));
        verify(tierCouponService, times(0)).createTierCoupon(any(CreateTierCouponRequestDto.class));

    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? ????????? Validation Exception ?????? ????????? ?????? ??????")
    void createTierCouponFailCauseTemplateNoIsNullTest() throws Exception {
        ReflectionTestUtils.setField(createTierCouponRequestDto, "templateNo", null);
        ReflectionTestUtils.setField(createTierCouponRequestDto, "tierNo", 1);

        mockMvc.perform(post(authPath)
                        .content(mapper.writeValueAsString(createTierCouponRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("tier-coupon-create-templateNo-null-fail",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("templateNo").description("?????? ????????? ??????"),
                                fieldWithPath("tierNo").description("?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("??????????????? ????????? ??????????????????.")
                        )));
        verify(tierCouponService, times(0)).createTierCoupon(any(CreateTierCouponRequestDto.class));

    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? ?????????")
    void deleteTierCouponTest() throws Exception {

        doNothing().when(tierCouponService).deleteTierCoupon(anyLong(), anyInt());

        mockMvc.perform(RestDocumentationRequestBuilders.delete(authPath)
                        .param("templateNo", mapper.writeValueAsString(1L))
                        .param("tierNo", mapper.writeValueAsString(1)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("tier-coupon-delete",
                        preprocessRequest(prettyPrint()),
                        requestParameters(
                                parameterWithName("templateNo").description("?????? ????????? ??????"),
                                parameterWithName("tierNo").description("?????? ??????")
                        )

                ));

        verify(tierCouponService, times(1))
                .deleteTierCoupon(anyLong(), anyInt());

    }

}