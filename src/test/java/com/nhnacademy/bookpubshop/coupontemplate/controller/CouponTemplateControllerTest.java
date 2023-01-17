//package com.nhnacademy.bookpubshop.coupontemplate.controller;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.BDDMockito.then;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.nhnacademy.bookpubshop.coupontemplate.dto.request.CreateCouponTemplateRequestDto;
//import com.nhnacademy.bookpubshop.coupontemplate.dto.request.ModifyCouponTemplateRequestDto;
//import com.nhnacademy.bookpubshop.coupontemplate.dto.response.GetDetailCouponTemplateResponseDto;
//import com.nhnacademy.bookpubshop.coupontemplate.dto.response.RestGetDetailCouponTemplateResponseDto;
//import com.nhnacademy.bookpubshop.coupontemplate.service.CouponTemplateService;
//import com.nhnacademy.bookpubshop.error.ShopAdviceController;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
//import org.springframework.data.support.PageableExecutionUtils;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
///**
// * 쿠폰템플릿 컨트롤러 테스트입니다.
// *
// * @author : 정유진
// * @since : 1.0
// **/
//@WebMvcTest(CouponTemplateController.class)
//@Import(ShopAdviceController.class)
//@MockBean(JpaMetamodelMappingContext.class)
//class CouponTemplateControllerTest {
//
//    @Autowired
//    MockMvc mockMvc;
//    @MockBean
//    private CouponTemplateService couponTemplateService;
//
//    String path = "/api/coupon-templates";
//    private ObjectMapper objectMapper;
//
//    CreateCouponTemplateRequestDto createRequestDto;
//    ModifyCouponTemplateRequestDto modifyRequestDto;
//
//    @BeforeEach
//    void setUp() {
//        createRequestDto = new CreateCouponTemplateRequestDto();
//        modifyRequestDto = new ModifyCouponTemplateRequestDto();
//
//        objectMapper = new ObjectMapper();
//    }
//
//    @Test
//    @DisplayName("쿠폰템플릿 상세 정보 조회 성공 테스트")
//    void couponTemplateDetail_Success() throws Exception {
//        RestGetDetailCouponTemplateResponseDto dto = new RestGetDetailCouponTemplateResponseDto(1L, true, 1L, 1L, 1L, "test_typeName", "test_title", "test_categoryName", "test_target", "test_name", "test_image", LocalDateTime.now(), LocalDateTime.now(), true, true);
//
//        when(couponTemplateService.getDetailCouponTemplate(anyLong())).thenReturn(dto);
//
//        mockMvc.perform(get(path + "/details/{templateNo}", 1)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(jsonPath("$.templateNo").value(objectMapper.writeValueAsString(dto.getTemplateNo())))
//                .andExpect(jsonPath("$.policyFixed").value(objectMapper.writeValueAsString(dto.isPolicyFixed())))
//                .andExpect(jsonPath("$.policyPrice").value(objectMapper.writeValueAsString(dto.getPolicyPrice())))
//                .andExpect(jsonPath("$.policyMinimum").value(objectMapper.writeValueAsString(dto.getPolicyMinimum())))
//                .andExpect(jsonPath("$.maxDiscount").value(objectMapper.writeValueAsString(dto.getMaxDiscount())))
//                .andExpect(jsonPath("$.typeName").value(dto.getTypeName()))
//                .andExpect(jsonPath("$.productTitle").value(dto.getProductTitle()))
//                .andExpect(jsonPath("$.categoryName").value(dto.getCategoryName()))
//                .andExpect(jsonPath("$.codeTarget").value(dto.getCodeTarget()))
//                .andExpect(jsonPath("$.templateName").value(dto.getTemplateName()))
//                .andExpect(jsonPath("$.templateImage").value(dto.getTemplateImage()))
//                .andExpect(jsonPath("$.finishedAt").value(dto.getFinishedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
//                .andExpect(jsonPath("$.issuedAt").value(dto.getIssuedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
//                .andExpect(jsonPath("$.templateOverlapped").value(objectMapper.writeValueAsString(dto.isTemplateOverlapped())))
//                .andExpect(jsonPath("$.templateBundled").value(objectMapper.writeValueAsString(dto.isTemplateBundled())));
//
//        then(couponTemplateService)
//                .should().getDetailCouponTemplate(anyLong());
//    }
//
//    @DisplayName("쿠폰 상세페이지 리스트를 반환합니다.")
//    @Test
//    void couponTemplateDetailList() throws Exception {
//        // given
//        GetDetailCouponTemplateResponseDto dto = new GetDetailCouponTemplateResponseDto(1L, true, 1L, 1L, 1L, "test_typeName", "test_title", "test_categoryName", "test_target", "test_name", "test_image", LocalDateTime.now(), LocalDateTime.now(), true, true);
//        List<GetDetailCouponTemplateResponseDto> list = List.of(dto);
//
//        Pageable pageable = PageRequest.of(0, 10);
//        Page<GetDetailCouponTemplateResponseDto> page =
//                PageableExecutionUtils.getPage(list, pageable, () -> 1L);
//
//        // when
//        when(couponTemplateService.getDetailCouponTemplates(pageable))
//                .thenReturn(page);
//
//        // then
//        mockMvc.perform(get(path + "/details")
//                        .param("page", objectMapper.writeValueAsString(pageable.getPageNumber()))
//                        .param("size", objectMapper.writeValueAsString(pageable.getPageSize()))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.content[0].templateNo").value(objectMapper.writeValueAsString(list.get(0).getTemplateNo())))
//                .andExpect(jsonPath("$.content[0].policyFixed").value(objectMapper.writeValueAsString(list.get(0).isPolicyFixed())))
//                .andExpect(jsonPath("$.content[0].templateNo").value(objectMapper.writeValueAsString(list.get(0).getTemplateNo())))
//                .andExpect(jsonPath("$.content[0].policyPrice").value(objectMapper.writeValueAsString(list.get(0).getPolicyPrice())))
//                .andExpect(jsonPath("$.content[0].policyMinimum").value(objectMapper.writeValueAsString(list.get(0).getMaxDiscount())))
//                .andExpect(jsonPath("$.content[0].maxDiscount").value(objectMapper.writeValueAsString(list.get(0).getTemplateNo())))
//                .andExpect(jsonPath("$.content[0].typeName").value(list.get(0).getTypeName()))
//                .andExpect(jsonPath("$.content[0].productTitle").value(list.get(0).getProductTitle()))
//                .andExpect(jsonPath("$.content[0].categoryName").value(list.get(0).getCategoryName()))
//                .andExpect(jsonPath("$.content[0].codeTarget").value(list.get(0).getCodeTarget()))
//                .andExpect(jsonPath("$.content[0].templateName").value(list.get(0).getTemplateName()))
//                .andExpect(jsonPath("$.content[0].templateImage").value(list.get(0).getTemplateImage()))
//                .andExpect(jsonPath("$.content[0].templateOverlapped").value(list.get(0).isTemplateOverlapped()))
//                .andExpect(jsonPath("$.content[0].templateBundled").value(list.get(0).isTemplateBundled()))
//                .andDo(print());
//        then(couponTemplateService).should().getDetailCouponTemplates(any());
//    }
//
//    @DisplayName("")
//    @Test
//    void couponTemplateList() {
//    }
//
//    @DisplayName("쿠폰 템플릿 생성")
//    @Test
//    void couponTemplateAdd() {
//    }
//
//    @Test
//    void couponTemplateModify() {
//    }
//}