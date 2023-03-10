package com.nhnacademy.bookpubshop.purchase.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import com.nhnacademy.bookpubshop.product.dummy.ProductDummy;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductPolicyDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductSaleStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.purchase.dto.CreatePurchaseRequestDto;
import com.nhnacademy.bookpubshop.purchase.dto.GetPurchaseListResponseDto;
import com.nhnacademy.bookpubshop.purchase.dummy.PurchaseDummy;
import com.nhnacademy.bookpubshop.purchase.entity.Purchase;
import com.nhnacademy.bookpubshop.purchase.service.PurchaseService;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import com.nhnacademy.bookpubshop.wishlist.dto.response.GetAppliedMemberResponseDto;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

/**
 * PurchaseControllerTest.
 *
 * @author : ?????????
 * @since : 1.0
 **/
@WebMvcTest(PurchaseController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class PurchaseControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    PurchaseService purchaseService;
    ObjectMapper objectMapper;
    ProductPolicy productPolicy;
    ProductTypeStateCode typeStateCode;
    ProductSaleStateCode saleStateCode;
    Product product;
    Purchase purchase;
    CreatePurchaseRequestDto request;
    GetPurchaseListResponseDto listResponse;
    String tokenUrl="/token/purchases";


    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        productPolicy = ProductPolicyDummy.dummy(1);
        typeStateCode = ProductTypeStateCodeDummy.dummy(1);
        saleStateCode = ProductSaleStateCodeDummy.dummy(1);
        product = ProductDummy.dummy(productPolicy, typeStateCode, saleStateCode, 1L);
        purchase = PurchaseDummy.dummy(product, 1L);

        request = new CreatePurchaseRequestDto();
        ReflectionTestUtils.setField(request, "productNo", product.getProductNo());
        ReflectionTestUtils.setField(request, "purchasePrice", purchase.getPurchasePrice());
        ReflectionTestUtils.setField(request, "purchaseAmount", purchase.getPurchaseAmount());
        ReflectionTestUtils.setField(request, "productType", 1);

        listResponse = new GetPurchaseListResponseDto(
                product.getProductNo(),
                product.getTitle(),
                purchase.getPurchaseNo(),
                purchase.getPurchaseAmount(),
                purchase.getPurchasePrice(),
                purchase.getCreatedAt());
    }

    @Test
    @DisplayName("????????? ?????? ???????????? ?????? ??????")
    void getPurchaseListDesc() throws Exception {
        List<GetPurchaseListResponseDto> responses = new ArrayList<>();
        responses.add(listResponse);

        Pageable pageable = Pageable.ofSize(5);
        Page<GetPurchaseListResponseDto> page =
                PageableExecutionUtils.getPage(responses, pageable, () -> 1L);
        PageResponse<GetPurchaseListResponseDto> pageResult =
                new PageResponse<>(page);

        when(purchaseService.getPurchaseListDesc(pageable))
                .thenReturn(page);

        mockMvc.perform(get(tokenUrl + "?page=0&size=5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pageResult)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("purchaseList-get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("content[].purchaseNo").description("??????????????? ?????? number??? ???????????????."),
                                fieldWithPath("content[].purchaseAmount").description("?????? ????????? ???????????????."),
                                fieldWithPath("content[].purchasePrice").description("????????? ????????? ???????????????."),
                                fieldWithPath("content[].productNo").description("????????? ????????? ??????????????? ???????????????."),
                                fieldWithPath("content[].productTitle").description("??????????????????."),
                                fieldWithPath("content[].createdAt").description("?????????????????????."),
                                fieldWithPath("totalPages").description("??? ????????? ??? ?????????."),
                                fieldWithPath("number").description("?????? ????????? ?????????."),
                                fieldWithPath("previous").description("??????????????? ?????? ?????? ?????????."),
                                fieldWithPath("next").description("??????????????? ?????? ?????? ?????????.")
                        )));

        verify(purchaseService, times(1))
                .getPurchaseListDesc(pageable);

        assertThat(purchaseService.getPurchaseListDesc(pageable)
                .getContent().get(0).getPurchaseNo())
                .isEqualTo(responses.get(0).getPurchaseNo());
        assertThat(purchaseService.getPurchaseListDesc(pageable)
                .getContent().get(0).getPurchaseAmount())
                .isEqualTo(responses.get(0).getPurchaseAmount());
        assertThat(purchaseService.getPurchaseListDesc(pageable)
                .getContent().get(0).getPurchasePrice())
                .isEqualTo(responses.get(0).getPurchasePrice());
        assertThat(purchaseService.getPurchaseListDesc(pageable)
                .getContent().get(0).getProductNo())
                .isEqualTo(responses.get(0).getPurchaseNo());
    }

    @Test
    @DisplayName("?????? ????????? ???????????? ?????? ??????")
    void getPurchaseByProductNo() throws Exception {
        List<GetPurchaseListResponseDto> responses = new ArrayList<>();
        responses.add(listResponse);

        Pageable pageable = Pageable.ofSize(5);
        Page<GetPurchaseListResponseDto> page =
                PageableExecutionUtils.getPage(responses, pageable, () -> 1L);
        PageResponse<GetPurchaseListResponseDto> pageResult =
                new PageResponse<>(page);

        when(purchaseService.getPurchaseByProductNo(product.getProductNo(), pageable))
                .thenReturn(pageResult);

        mockMvc.perform(get(tokenUrl + "/{productNo}?page=0&size=5",
                        product.getProductNo().intValue())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pageResult)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("purchase-get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("content[].productNo").description("?????? ???????????????."),
                                fieldWithPath("content[].productTitle").description("?????? ???????????????."),
                                fieldWithPath("content[].purchaseNo").description("??????????????? ?????? number??? ???????????????."),
                                fieldWithPath("content[].purchaseAmount").description("?????? ????????? ???????????????."),
                                fieldWithPath("content[].purchasePrice").description("????????? ????????? ???????????????."),
                                fieldWithPath("content[].createdAt").description("?????? ???????????????."),
                                fieldWithPath("totalPages").description("??? ????????? ??? ?????????."),
                                fieldWithPath("number").description("?????? ????????? ?????????."),
                                fieldWithPath("previous").description("??????????????? ?????? ?????? ?????????."),
                                fieldWithPath("next").description("??????????????? ?????? ?????? ?????????.")
                        )));

        verify(purchaseService, times(1))
                .getPurchaseByProductNo(product.getProductNo(), pageable);

        assertThat(purchaseService.getPurchaseByProductNo(product.getProductNo(), pageable)
                .getContent().get(0).getPurchaseNo())
                .isEqualTo(responses.get(0).getPurchaseNo());
        assertThat(purchaseService.getPurchaseByProductNo(product.getProductNo(), pageable)
                .getContent().get(0).getPurchaseAmount())
                .isEqualTo(responses.get(0).getPurchaseAmount());
        assertThat(purchaseService.getPurchaseByProductNo(product.getProductNo(), pageable)
                .getContent().get(0).getPurchasePrice())
                .isEqualTo(responses.get(0).getPurchasePrice());
        assertThat(purchaseService.getPurchaseByProductNo(product.getProductNo(), pageable)
                .getContent().get(0).getProductNo())
                .isEqualTo(responses.get(0).getPurchaseNo());
    }

    @Test
    @DisplayName("???????????? ?????? ??????")
    void modifyPurchase() throws Exception {
        doNothing().when(purchaseService)
                .modifyPurchase(purchase.getPurchaseNo(), request);

        mockMvc.perform(put(tokenUrl + "/{purchaseNo}", purchase.getPurchaseNo())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("purchase-put",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("productNo").description("??????????????? ??????????????? ??????????????? ???????????????."),
                                fieldWithPath("purchasePrice").description("??????????????? ??????????????? ???????????????."),
                                fieldWithPath("purchaseAmount").description("??????????????? ??????????????? ????????????."),
                                fieldWithPath("productType").description("??????????????? ?????? ?????? ?????? ???????????????.")
                        )
                ));

        verify(purchaseService, times(1))
                .modifyPurchase(anyLong(), any());
    }

    @Test
    @DisplayName("???????????? ?????? ??????")
    void createPurchase() throws Exception {
        doNothing().when(purchaseService)
                .createPurchase(request);

        mockMvc.perform(post(tokenUrl)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("purchase-add",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("productNo").description("????????? ????????? ????????? ???????????????."),
                                fieldWithPath("purchasePrice").description("????????? ?????? ????????? ???????????????."),
                                fieldWithPath("purchaseAmount").description("????????? ?????? ????????? ???????????????."),
                                fieldWithPath("productType").description("????????????????????? ?????? ????????? ???????????????.")
                        )
                ));

        verify(purchaseService, times(1))
                .createPurchase(any());
    }

    @Test
    @DisplayName("???????????? ????????? ???????????? ?????? ??????")
    void createPurchaseMerged() throws Exception {
        GetAppliedMemberResponseDto responseDto = new GetAppliedMemberResponseDto();
        ReflectionTestUtils.setField(responseDto, "memberNo", 1L);
        ReflectionTestUtils.setField(responseDto, "memberNickname", "?????????");
        ReflectionTestUtils.setField(responseDto, "memberPhone", "01012341234");
        ReflectionTestUtils.setField(responseDto, "productNo", 1L);
        ReflectionTestUtils.setField(responseDto, "title", "??? ??????");

        List<GetAppliedMemberResponseDto> list = List.of(responseDto);

        when(purchaseService.createPurchaseMerged(request))
                .thenReturn(list);

        mockMvc.perform(post(tokenUrl + "/absorption")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

        verify(purchaseService, times(1))
                .createPurchaseMerged(any());
    }
}