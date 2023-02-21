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
 * @author : 여운석
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
    @DisplayName("최신순 전체 매입이력 조회 성공")
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
                                fieldWithPath("content[].purchaseNo").description("매입이력의 고유 number가 반환됩니다."),
                                fieldWithPath("content[].purchaseAmount").description("매입 수량이 반환됩니다."),
                                fieldWithPath("content[].purchasePrice").description("매입한 금액이 반환됩니다."),
                                fieldWithPath("content[].productNo").description("매입한 상품의 고유번호가 반환됩니다."),
                                fieldWithPath("content[].productTitle").description("상품명입니다."),
                                fieldWithPath("content[].createdAt").description("등록시간입니다."),
                                fieldWithPath("totalPages").description("총 페이지 수 입니다."),
                                fieldWithPath("number").description("현재 페이지 입니다."),
                                fieldWithPath("previous").description("이전페이지 존재 여부 입니다."),
                                fieldWithPath("next").description("다음페이지 존재 여부 입니다.")
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
    @DisplayName("상품 번호로 매입이력 조회 성공")
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
                                fieldWithPath("content[].productNo").description("상품 번호입니다."),
                                fieldWithPath("content[].productTitle").description("상품 제목입니다."),
                                fieldWithPath("content[].purchaseNo").description("매입이력의 고유 number가 반환됩니다."),
                                fieldWithPath("content[].purchaseAmount").description("매입 수량이 반환됩니다."),
                                fieldWithPath("content[].purchasePrice").description("매입한 금액이 반환됩니다."),
                                fieldWithPath("content[].createdAt").description("등록 시간입니다."),
                                fieldWithPath("totalPages").description("총 페이지 수 입니다."),
                                fieldWithPath("number").description("현재 페이지 입니다."),
                                fieldWithPath("previous").description("이전페이지 존재 여부 입니다."),
                                fieldWithPath("next").description("다음페이지 존재 여부 입니다.")
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
    @DisplayName("매입이력 수정 성공")
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
                                fieldWithPath("productNo").description("변경하려는 매입이력의 상품번호를 기입합니다."),
                                fieldWithPath("purchasePrice").description("변경하려는 매입이력의 가격입니다."),
                                fieldWithPath("purchaseAmount").description("변경하려는 매입이력의 양입니다."),
                                fieldWithPath("productType").description("변경하려는 상품 유형 타입 번호입니다.")
                        )
                ));

        verify(purchaseService, times(1))
                .modifyPurchase(anyLong(), any());
    }

    @Test
    @DisplayName("매입이력 등록 성공")
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
                                fieldWithPath("productNo").description("구매한 상품의 번호가 기입됩니다."),
                                fieldWithPath("purchasePrice").description("상품의 매입 가격이 기입됩니다."),
                                fieldWithPath("purchaseAmount").description("상품의 매입 수량이 기입됩니다."),
                                fieldWithPath("productType").description("변경하고자하는 상품 유형이 기입됩니다.")
                        )
                ));

        verify(purchaseService, times(1))
                .createPurchase(any());
    }

    @Test
    @DisplayName("매입이력 등록시 상품재고 증가 성공")
    void createPurchaseMerged() throws Exception {
        GetAppliedMemberResponseDto responseDto = new GetAppliedMemberResponseDto();
        ReflectionTestUtils.setField(responseDto, "memberNo", 1L);
        ReflectionTestUtils.setField(responseDto, "memberNickname", "닉네임");
        ReflectionTestUtils.setField(responseDto, "memberPhone", "01012341234");
        ReflectionTestUtils.setField(responseDto, "productNo", 1L);
        ReflectionTestUtils.setField(responseDto, "title", "책 제목");

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