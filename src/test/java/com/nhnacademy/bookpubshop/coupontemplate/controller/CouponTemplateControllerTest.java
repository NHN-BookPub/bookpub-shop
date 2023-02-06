package com.nhnacademy.bookpubshop.coupontemplate.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.bookpubshop.coupontemplate.dto.request.CreateCouponTemplateRequestDto;
import com.nhnacademy.bookpubshop.coupontemplate.dto.request.ModifyCouponTemplateRequestDto;
import com.nhnacademy.bookpubshop.coupontemplate.dto.response.GetCouponTemplateResponseDto;
import com.nhnacademy.bookpubshop.coupontemplate.dto.response.GetDetailCouponTemplateResponseDto;
import com.nhnacademy.bookpubshop.coupontemplate.service.CouponTemplateService;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 쿠폰템플릿 컨트롤러 테스트입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@WebMvcTest(CouponTemplateController.class)
@Import(ShopAdviceController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
@MockBean(JpaMetamodelMappingContext.class)
class CouponTemplateControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    private CouponTemplateService couponTemplateService;
    String authPath = "/token/coupon-templates";
    private ObjectMapper objectMapper;

    CreateCouponTemplateRequestDto createRequestDto;
    ModifyCouponTemplateRequestDto modifyRequestDto;

    @BeforeEach
    void setUp() {
        createRequestDto = new CreateCouponTemplateRequestDto();
        modifyRequestDto = new ModifyCouponTemplateRequestDto();

        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("쿠폰템플릿 상세 정보 조회 성공 테스트")
    void couponTemplateDetail_Success() throws Exception {
        GetDetailCouponTemplateResponseDto dto = new GetDetailCouponTemplateResponseDto(1L, true, 1L, 1L, 1L, "test_typeName", "test_title", "test_categoryName", "test_target", "test_name", "test_image", LocalDateTime.now(), true);

        when(couponTemplateService.getDetailCouponTemplate(anyLong())).thenReturn(dto);

        mockMvc.perform(RestDocumentationRequestBuilders.get(authPath + "/{templateNo}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.templateNo").value(objectMapper.writeValueAsString(dto.getTemplateNo())))
                .andExpect(jsonPath("$.policyFixed").value(objectMapper.writeValueAsString(dto.isPolicyFixed())))
                .andExpect(jsonPath("$.policyPrice").value(objectMapper.writeValueAsString(dto.getPolicyPrice())))
                .andExpect(jsonPath("$.policyMinimum").value(objectMapper.writeValueAsString(dto.getPolicyMinimum())))
                .andExpect(jsonPath("$.maxDiscount").value(objectMapper.writeValueAsString(dto.getMaxDiscount())))
                .andExpect(jsonPath("$.typeName").value(dto.getTypeName()))
                .andExpect(jsonPath("$.productTitle").value(dto.getProductTitle()))
                .andExpect(jsonPath("$.categoryName").value(dto.getCategoryName()))
                .andExpect(jsonPath("$.codeTarget").value(dto.getCodeTarget()))
                .andExpect(jsonPath("$.templateName").value(dto.getTemplateName()))
                .andExpect(jsonPath("$.templateImage").value(dto.getTemplateImage()))
                .andExpect(jsonPath("$.finishedAt").value(dto.getFinishedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.templateBundled").value(objectMapper.writeValueAsString(dto.isTemplateBundled())))
                .andDo(document("coupon_template_detail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("templateNo").description("Path 로 쿠폰템플릿 번호기입")),
                        responseFields(
                                fieldWithPath("templateNo").description("템플릿 번호 반환"),
                                fieldWithPath("policyFixed").description("쿠폰 정액 여부 반환"),
                                fieldWithPath("policyPrice").description("쿠폰 할인가격 반환"),
                                fieldWithPath("policyMinimum").description("쿠폰 최소주문 금액 반환"),
                                fieldWithPath("maxDiscount").description("쿠폰 최대할인 가격 반환"),
                                fieldWithPath("typeName").description("쿠폰 유형이름 반환"),
                                fieldWithPath("productTitle").description("상품 이름 반환"),
                                fieldWithPath("categoryName").description("카테고리 이름 반환"),
                                fieldWithPath("codeTarget").description("쿠폰 적용타겟 반환"),
                                fieldWithPath("templateName").description("쿠폰 템플릿 이름 반환"),
                                fieldWithPath("templateImage").description("쿠폰 이미지 경로 반환"),
                                fieldWithPath("finishedAt").description("쿠폰 만료일자 반환"),
                                fieldWithPath("templateBundled").description("쿠폰 묶음 할인여부 반환")
                        )
                ));

        then(couponTemplateService)
                .should().getDetailCouponTemplate(anyLong());
    }

    @Test
    @DisplayName("쿠폰템플릿 정보 리스트 조회 성공 테스트")
    void couponTemplateList_Success() throws Exception {
        // given
        GetCouponTemplateResponseDto dto = new GetCouponTemplateResponseDto(1L, "test_name", "test_imagePath", LocalDateTime.of(1, 1, 1, 1, 1));
        List<GetCouponTemplateResponseDto> list = List.of(dto);

        Pageable pageable = PageRequest.of(0, 10);
        Page<GetCouponTemplateResponseDto> page =
                PageableExecutionUtils.getPage(list, pageable, () -> 1L);

        // when
        when(couponTemplateService.getCouponTemplates(pageable))
                .thenReturn(page);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.get(authPath)
                        .param("page", objectMapper.writeValueAsString(pageable.getPageNumber()))
                        .param("size", objectMapper.writeValueAsString(pageable.getPageSize()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].templateNo").value(objectMapper.writeValueAsString(list.get(0).getTemplateNo())))
                .andExpect(jsonPath("$.content[0].templateName").value(list.get(0).getTemplateName()))
                .andExpect(jsonPath("$.content[0].templateImage").value(list.get(0).getTemplateImage()))
                .andExpect(jsonPath("$.content[0].finishedAt").value(dto.getFinishedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andDo(print())
                .andDo(document("coupon_template_list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("페이지 정보 기입"),
                                parameterWithName("size").description("페이지 사이즈 기입")
                        ),
                        responseFields(
                                fieldWithPath("content[].templateNo").description("쿠폰 템플릿 번호 반환"),
                                fieldWithPath("content[].templateName").description("쿠폰 템플릿 이름 반환"),
                                fieldWithPath("content[].templateImage").description("쿠폰 템플릿 이미지 경로 반환"),
                                fieldWithPath("content[].finishedAt").description("쿠폰 만료일자 반환"),
                                fieldWithPath("totalPages").description("총 페이지 수 반환"),
                                fieldWithPath("number").description("현재 페이지 반환"),
                                fieldWithPath("previous").description("이전 페이지 여부"),
                                fieldWithPath("next").description("다음 페이지 여부")
                        )
                ));

        then(couponTemplateService).should().getCouponTemplates(any());
    }

    @DisplayName("쿠폰 템플릿 생성 테스트")
    @Test
    void couponTemplateAdd() throws Exception {
        ReflectionTestUtils.setField(createRequestDto, "policyNo", 1);
        ReflectionTestUtils.setField(createRequestDto, "typeNo", 1L);
        ReflectionTestUtils.setField(createRequestDto, "productNo", 1L);
        ReflectionTestUtils.setField(createRequestDto, "categoryNo", 1);
        ReflectionTestUtils.setField(createRequestDto, "codeNo", 1);
        ReflectionTestUtils.setField(createRequestDto, "templateName", "templateName");
        ReflectionTestUtils.setField(createRequestDto, "finishedAt", LocalDateTime.of(1, 1, 1, 1, 1));
        ReflectionTestUtils.setField(createRequestDto, "templateBundled", true);

        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MockMultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/jpeg", imageContent.getBytes());

        String dtoToJson = objectMapper.writeValueAsString(createRequestDto);
        MockMultipartFile createRequestDto = new MockMultipartFile("createRequestDto", "createRequestDto", "application/json", dtoToJson.getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart(authPath)
                        .file(multipartFile)
                        .file(createRequestDto))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("coupon_template_create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("image").description("이미지 기입"),
                                partWithName("createRequestDto").description("쿠폰 생성 정보기입")),
                        requestPartFields("createRequestDto",
                                fieldWithPath("policyNo").description("쿠폰등급 번호 기입"),
                                fieldWithPath("typeNo").description("쿠폰타입 번호 기입"),
                                fieldWithPath("productNo").description("상품 번호 기입"),
                                fieldWithPath("categoryNo").description("카테고리 번호 기입"),
                                fieldWithPath("codeNo").description("코드번호 기입"),
                                fieldWithPath("templateName").description("등록할 쿠폰 템플릿 명 기입"),
                                fieldWithPath("finishedAt").description("만료일자 기입"),
                                fieldWithPath("templateBundled").description("중복 쿠폰사용여부 기입")
                        )
                ));
    }

    @DisplayName("쿠폰 템플릿 생성 validation 오류_policyNoIsNull")
    @Test
    void couponTemplateAddFail_PolicyNoIsNull() throws Exception {
        ReflectionTestUtils.setField(createRequestDto, "policyNo", null);
        ReflectionTestUtils.setField(createRequestDto, "typeNo", 1L);
        ReflectionTestUtils.setField(createRequestDto, "productNo", 1L);
        ReflectionTestUtils.setField(createRequestDto, "categoryNo", 1);
        ReflectionTestUtils.setField(createRequestDto, "codeNo", 1);
        ReflectionTestUtils.setField(createRequestDto, "templateName", "templateName");
        ReflectionTestUtils.setField(createRequestDto, "finishedAt", LocalDateTime.of(1, 1, 1, 1, 1));
        ReflectionTestUtils.setField(createRequestDto, "templateBundled", true);

        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MockMultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/jpeg", imageContent.getBytes());

        String dtoToJson = objectMapper.writeValueAsString(createRequestDto);
        MockMultipartFile createRequestDto = new MockMultipartFile("createRequestDto", "createRequestDto", "application/json", dtoToJson.getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart(authPath)
                        .file(createRequestDto)
                        .file(multipartFile))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("정책번호를 기입해주세요."))
                .andDo(print())
                .andDo(document("coupon_template_create_policyNoIsNull",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("image").description("이미지 기입"),
                                partWithName("createRequestDto").description("쿠폰 생성 정보기입")),
                        requestPartFields("createRequestDto",
                                fieldWithPath("policyNo").description("쿠폰등급 번호 기입"),
                                fieldWithPath("typeNo").description("쿠폰타입 번호 기입"),
                                fieldWithPath("productNo").description("상품 번호 기입"),
                                fieldWithPath("categoryNo").description("카테고리 번호 기입"),
                                fieldWithPath("codeNo").description("코드번호 기입"),
                                fieldWithPath("templateName").description("등록할 쿠폰 템플릿 명 기입"),
                                fieldWithPath("finishedAt").description("만료일자 기입"),
                                fieldWithPath("templateBundled").description("중복 쿠폰사용여부 기입")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("정책번호를 기입해주세요.")
                        )
                ));
    }

    @DisplayName("쿠폰 템플릿 생성 validation 오류_typeNoIsNull")
    @Test
    void couponTemplateAddFail_TypeNoIsNull() throws Exception {
        ReflectionTestUtils.setField(createRequestDto, "policyNo", 1);
        ReflectionTestUtils.setField(createRequestDto, "typeNo", null);
        ReflectionTestUtils.setField(createRequestDto, "productNo", 1L);
        ReflectionTestUtils.setField(createRequestDto, "categoryNo", 1);
        ReflectionTestUtils.setField(createRequestDto, "codeNo", 1);
        ReflectionTestUtils.setField(createRequestDto, "templateName", "templateName");
        ReflectionTestUtils.setField(createRequestDto, "finishedAt", LocalDateTime.of(1, 1, 1, 1, 1));
        ReflectionTestUtils.setField(createRequestDto, "templateBundled", true);

        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MockMultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/jpeg", imageContent.getBytes());

        String dtoToJson = objectMapper.writeValueAsString(createRequestDto);
        MockMultipartFile createRequestDto = new MockMultipartFile("createRequestDto", "createRequestDto", "application/json", dtoToJson.getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart(authPath)
                        .file(createRequestDto)
                        .file(multipartFile))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("유형번호를 기입해주세요."))
                .andDo(print())
                .andDo(document("coupon_template_create_typeNoIsNull",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("image").description("이미지 기입"),
                                partWithName("createRequestDto").description("쿠폰 생성 정보기입")),
                        requestPartFields("createRequestDto",
                                fieldWithPath("policyNo").description("쿠폰등급 번호 기입"),
                                fieldWithPath("typeNo").description("쿠폰타입 번호 기입"),
                                fieldWithPath("productNo").description("상품 번호 기입"),
                                fieldWithPath("categoryNo").description("카테고리 번호 기입"),
                                fieldWithPath("codeNo").description("코드번호 기입"),
                                fieldWithPath("templateName").description("등록할 쿠폰 템플릿 명 기입"),
                                fieldWithPath("finishedAt").description("만료일자 기입"),
                                fieldWithPath("templateBundled").description("중복 쿠폰사용여부 기입")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("유형번호를 기입해주세요.")
                        )
                ));
    }

    @DisplayName("쿠폰 템플릿 생성 validation 오류_CodeNoIsNull")
    @Test
    void couponTemplateAddFail_CodeNoIsNull() throws Exception {
        ReflectionTestUtils.setField(createRequestDto, "policyNo", 1);
        ReflectionTestUtils.setField(createRequestDto, "typeNo", 1L);
        ReflectionTestUtils.setField(createRequestDto, "productNo", 1L);
        ReflectionTestUtils.setField(createRequestDto, "categoryNo", 1);
        ReflectionTestUtils.setField(createRequestDto, "codeNo", null);
        ReflectionTestUtils.setField(createRequestDto, "templateName", "templateName");
        ReflectionTestUtils.setField(createRequestDto, "finishedAt", LocalDateTime.of(1, 1, 1, 1, 1));
        ReflectionTestUtils.setField(createRequestDto, "templateBundled", true);

        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MockMultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/jpeg", imageContent.getBytes());

        String dtoToJson = objectMapper.writeValueAsString(createRequestDto);
        MockMultipartFile createRequestDto = new MockMultipartFile("createRequestDto", "createRequestDto", "application/json", dtoToJson.getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart(authPath)
                        .file(createRequestDto)
                        .file(multipartFile))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("상태번호를 기입해주세요."))
                .andDo(print())
                .andDo(document("coupon_template_create_CodeNoIsNull",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("image").description("이미지 기입"),
                                partWithName("createRequestDto").description("쿠폰 생성 정보기입")),
                        requestPartFields("createRequestDto",
                                fieldWithPath("policyNo").description("쿠폰등급 번호 기입"),
                                fieldWithPath("typeNo").description("쿠폰타입 번호 기입"),
                                fieldWithPath("productNo").description("상품 번호 기입"),
                                fieldWithPath("categoryNo").description("카테고리 번호 기입"),
                                fieldWithPath("codeNo").description("코드번호 기입"),
                                fieldWithPath("templateName").description("등록할 쿠폰 템플릿 명 기입"),
                                fieldWithPath("finishedAt").description("만료일자 기입"),
                                fieldWithPath("templateBundled").description("중복 쿠폰사용여부 기입")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("상태번호를 기입해주세요.")
                        )
                ));
    }

    @DisplayName("쿠폰 템플릿 생성 validation 오류_TemplateNameIsNull")
    @Test
    void couponTemplateAddFail_TemplateNameIsNull() throws Exception {
        ReflectionTestUtils.setField(createRequestDto, "policyNo", 1);
        ReflectionTestUtils.setField(createRequestDto, "typeNo", 1L);
        ReflectionTestUtils.setField(createRequestDto, "productNo", 1L);
        ReflectionTestUtils.setField(createRequestDto, "categoryNo", 1);
        ReflectionTestUtils.setField(createRequestDto, "codeNo", 1);
        ReflectionTestUtils.setField(createRequestDto, "templateName", null);
        ReflectionTestUtils.setField(createRequestDto, "finishedAt", LocalDateTime.of(1, 1, 1, 1, 1));
        ReflectionTestUtils.setField(createRequestDto, "templateBundled", true);

        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MockMultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/jpeg", imageContent.getBytes());

        String dtoToJson = objectMapper.writeValueAsString(createRequestDto);
        MockMultipartFile createRequestDto = new MockMultipartFile("createRequestDto", "createRequestDto", "application/json", dtoToJson.getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart(authPath)
                        .file(createRequestDto)
                        .file(multipartFile))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("쿠폰이름을 기입해주세요."))
                .andDo(print())
                .andDo(document("coupon_template_create_TemplateNameIsNull",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("image").description("이미지 기입"),
                                partWithName("createRequestDto").description("쿠폰 생성 정보기입")),
                        requestPartFields("createRequestDto",
                                fieldWithPath("policyNo").description("쿠폰등급 번호 기입"),
                                fieldWithPath("typeNo").description("쿠폰타입 번호 기입"),
                                fieldWithPath("productNo").description("상품 번호 기입"),
                                fieldWithPath("categoryNo").description("카테고리 번호 기입"),
                                fieldWithPath("codeNo").description("코드번호 기입"),
                                fieldWithPath("templateName").description("등록할 쿠폰 템플릿 명 기입"),
                                fieldWithPath("finishedAt").description("만료일자 기입"),
                                fieldWithPath("templateBundled").description("중복 쿠폰사용여부 기입")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("쿠폰이름을 기입해주세요.")
                        )
                ));
    }

    @DisplayName("쿠폰 템플릿 생성 validation 오류_TemplateNameIsTooLong")
    @Test
    void couponTemplateAddFail_TemplateNameIsTooLong() throws Exception {
        ReflectionTestUtils.setField(createRequestDto, "policyNo", 1);
        ReflectionTestUtils.setField(createRequestDto, "typeNo", 1L);
        ReflectionTestUtils.setField(createRequestDto, "productNo", 1L);
        ReflectionTestUtils.setField(createRequestDto, "categoryNo", 1);
        ReflectionTestUtils.setField(createRequestDto, "codeNo", 1);
        ReflectionTestUtils.setField(createRequestDto, "templateName", "asdfasdfasdfsdfsafsasdfasdfasdfsdfsafsasdfasdfasdfsdfsafsasdfasdfasdfsdfsafsasdfasdfasdfsdfsafs");
        ReflectionTestUtils.setField(createRequestDto, "finishedAt", LocalDateTime.of(1, 1, 1, 1, 1));
        ReflectionTestUtils.setField(createRequestDto, "templateBundled", true);

        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MockMultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/jpeg", imageContent.getBytes());

        String dtoToJson = objectMapper.writeValueAsString(createRequestDto);
        MockMultipartFile createRequestDto = new MockMultipartFile("createRequestDto", "createRequestDto", "application/json", dtoToJson.getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart(authPath)
                        .file(createRequestDto)
                        .file(multipartFile))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("쿠폰이름의 최대 글자는 50글자입니다."))
                .andDo(print())
                .andDo(document("coupon_template_create_TemplateNameIsTooLong",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("image").description("이미지 기입"),
                                partWithName("createRequestDto").description("쿠폰 생성 정보기입")),
                        requestPartFields("createRequestDto",
                                fieldWithPath("policyNo").description("쿠폰등급 번호 기입"),
                                fieldWithPath("typeNo").description("쿠폰타입 번호 기입"),
                                fieldWithPath("productNo").description("상품 번호 기입"),
                                fieldWithPath("categoryNo").description("카테고리 번호 기입"),
                                fieldWithPath("codeNo").description("코드번호 기입"),
                                fieldWithPath("templateName").description("등록할 쿠폰 템플릿 명 기입"),
                                fieldWithPath("finishedAt").description("만료일자 기입"),
                                fieldWithPath("templateBundled").description("중복 쿠폰사용여부 기입")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("쿠폰이름의 최대 글자는 50글자입니다.")
                        )
                ));

    }

    @DisplayName("쿠폰 템플릿 수정 성공 테스트")
    @Test
    void couponTemplateModifySuccess() throws Exception {
        ReflectionTestUtils.setField(modifyRequestDto, "policyNo", 1);
        ReflectionTestUtils.setField(modifyRequestDto, "typeNo", 1L);
        ReflectionTestUtils.setField(modifyRequestDto, "productNo", 1L);
        ReflectionTestUtils.setField(modifyRequestDto, "categoryNo", 1);
        ReflectionTestUtils.setField(modifyRequestDto, "codeNo", 1);
        ReflectionTestUtils.setField(modifyRequestDto, "templateName", "templateName");
        ReflectionTestUtils.setField(modifyRequestDto, "finishedAt", LocalDateTime.of(1, 1, 1, 1, 1));
        ReflectionTestUtils.setField(modifyRequestDto, "templateBundled", true);

        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MockMultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/jpeg", imageContent.getBytes());

        String dtoToJson = objectMapper.writeValueAsString(modifyRequestDto);
        MockMultipartFile modifyRequestDto = new MockMultipartFile("modifyRequestDto", "modifyRequestDto", "application/json", dtoToJson.getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart(authPath + "/{templateNo}", 1L)
                        .file(modifyRequestDto)
                        .file(multipartFile)
                        .with(req -> {
                            req.setMethod("PUT");
                            return req;
                        }))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("coupon_template_modify",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("image").description("이미지 기입"),
                                partWithName("modifyRequestDto").description("쿠폰 수정 정보기입")),
                        requestPartFields("modifyRequestDto",
                                fieldWithPath("policyNo").description("쿠폰등급 번호 기입"),
                                fieldWithPath("typeNo").description("쿠폰타입 번호 기입"),
                                fieldWithPath("productNo").description("상품 번호 기입"),
                                fieldWithPath("categoryNo").description("카테고리 번호 기입"),
                                fieldWithPath("codeNo").description("코드번호 기입"),
                                fieldWithPath("templateName").description("등록할 쿠폰 템플릿 명 기입"),
                                fieldWithPath("finishedAt").description("만료일자 기입"),
                                fieldWithPath("templateBundled").description("중복 쿠폰사용여부 기입")
                        )
                ));
    }

    @DisplayName("쿠폰 템플릿 수정 실패 validation 오류_PolicyNoIsNull")
    @Test
    void couponTemplateModifyFail_PolicyNoIsNull() throws Exception {
        ReflectionTestUtils.setField(modifyRequestDto, "policyNo", null);
        ReflectionTestUtils.setField(modifyRequestDto, "typeNo", 1L);
        ReflectionTestUtils.setField(modifyRequestDto, "productNo", 1L);
        ReflectionTestUtils.setField(modifyRequestDto, "categoryNo", 1);
        ReflectionTestUtils.setField(modifyRequestDto, "codeNo", 1);
        ReflectionTestUtils.setField(modifyRequestDto, "templateName", "templateName");
        ReflectionTestUtils.setField(modifyRequestDto, "finishedAt", LocalDateTime.of(1, 1, 1, 1, 1));
        ReflectionTestUtils.setField(modifyRequestDto, "templateBundled", true);

        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MockMultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/jpeg", imageContent.getBytes());

        String dtoToJson = objectMapper.writeValueAsString(modifyRequestDto);
        MockMultipartFile modifyRequestDto = new MockMultipartFile("modifyRequestDto", "modifyRequestDto", "application/json", dtoToJson.getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart(authPath + "/{templateNo}", 1L)
                        .file(modifyRequestDto)
                        .file(multipartFile)
                        .with(req -> {
                            req.setMethod("PUT");
                            return req;
                        }))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("정책번호를 기입해주세요."))
                .andDo(print())
                .andDo(document("coupon_template_modify_policyEx",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("image").description("이미지 기입"),
                                partWithName("modifyRequestDto").description("쿠폰 수정 정보기입")),
                        requestPartFields("modifyRequestDto",
                                fieldWithPath("policyNo").description("쿠폰등급 번호 기입"),
                                fieldWithPath("typeNo").description("쿠폰타입 번호 기입"),
                                fieldWithPath("productNo").description("상품 번호 기입"),
                                fieldWithPath("categoryNo").description("카테고리 번호 기입"),
                                fieldWithPath("codeNo").description("코드번호 기입"),
                                fieldWithPath("templateName").description("등록할 쿠폰 템플릿 명 기입"),
                                fieldWithPath("finishedAt").description("만료일자 기입"),
                                fieldWithPath("templateBundled").description("중복 쿠폰사용여부 기입")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("정책번호를 기입해주세요.")
                        )
                ));

    }

    @DisplayName("쿠폰 템플릿 수정 실패 validation 오류_TypeNoIsNull")
    @Test
    void couponTemplateModifyFail_TypeNoIsNull() throws Exception {
        ReflectionTestUtils.setField(modifyRequestDto, "policyNo", 1);
        ReflectionTestUtils.setField(modifyRequestDto, "typeNo", null);
        ReflectionTestUtils.setField(modifyRequestDto, "productNo", 1L);
        ReflectionTestUtils.setField(modifyRequestDto, "categoryNo", 1);
        ReflectionTestUtils.setField(modifyRequestDto, "codeNo", 1);
        ReflectionTestUtils.setField(modifyRequestDto, "templateName", "templateName");
        ReflectionTestUtils.setField(modifyRequestDto, "finishedAt", LocalDateTime.of(1, 1, 1, 1, 1));
        ReflectionTestUtils.setField(modifyRequestDto, "templateBundled", true);

        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MockMultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/jpeg", imageContent.getBytes());

        String dtoToJson = objectMapper.writeValueAsString(modifyRequestDto);
        MockMultipartFile modifyRequestDto = new MockMultipartFile("modifyRequestDto", "modifyRequestDto", "application/json", dtoToJson.getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart(authPath + "/{templateNo}", 1L)
                        .file(modifyRequestDto)
                        .file(multipartFile)
                        .with(req -> {
                            req.setMethod("PUT");
                            return req;
                        }))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("유형번호를 기입해주세요."))
                .andDo(print())
                .andDo(document("coupon_template_modify_typeEx",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("image").description("이미지 기입"),
                                partWithName("modifyRequestDto").description("쿠폰 수정 정보기입")),
                        requestPartFields("modifyRequestDto",
                                fieldWithPath("policyNo").description("쿠폰등급 번호 기입"),
                                fieldWithPath("typeNo").description("쿠폰타입 번호 기입"),
                                fieldWithPath("productNo").description("상품 번호 기입"),
                                fieldWithPath("categoryNo").description("카테고리 번호 기입"),
                                fieldWithPath("codeNo").description("코드번호 기입"),
                                fieldWithPath("templateName").description("등록할 쿠폰 템플릿 명 기입"),
                                fieldWithPath("finishedAt").description("만료일자 기입"),
                                fieldWithPath("templateBundled").description("중복 쿠폰사용여부 기입")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("유형번호를 기입해주세요.")
                        )
                ));
    }

    @DisplayName("쿠폰 템플릿 수정 실패 validation 오류_TemplateNameIsNull")
    @Test
    void couponTemplateModifyFail_TemplateNameIsNull() throws Exception {
        ReflectionTestUtils.setField(modifyRequestDto, "policyNo", 1);
        ReflectionTestUtils.setField(modifyRequestDto, "typeNo", 1L);
        ReflectionTestUtils.setField(modifyRequestDto, "productNo", 1L);
        ReflectionTestUtils.setField(modifyRequestDto, "categoryNo", 1);
        ReflectionTestUtils.setField(modifyRequestDto, "codeNo", 1);
        ReflectionTestUtils.setField(modifyRequestDto, "templateName", null);
        ReflectionTestUtils.setField(modifyRequestDto, "finishedAt", LocalDateTime.of(1, 1, 1, 1, 1));
        ReflectionTestUtils.setField(modifyRequestDto, "templateBundled", true);

        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MockMultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/jpeg", imageContent.getBytes());

        String dtoToJson = objectMapper.writeValueAsString(modifyRequestDto);
        MockMultipartFile modifyRequestDto = new MockMultipartFile("modifyRequestDto", "modifyRequestDto", "application/json", dtoToJson.getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart(authPath + "/{templateNo}", 1L)
                        .file(modifyRequestDto)
                        .file(multipartFile)
                        .with(req -> {
                            req.setMethod("PUT");
                            return req;
                        }))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("쿠폰이름을 기입해주세요."))
                .andDo(print())
                .andDo(document("coupon_template_modify_templateNameEx",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("image").description("이미지 기입"),
                                partWithName("modifyRequestDto").description("쿠폰 수정 정보기입")),
                        requestPartFields("modifyRequestDto",
                                fieldWithPath("policyNo").description("쿠폰등급 번호 기입"),
                                fieldWithPath("typeNo").description("쿠폰타입 번호 기입"),
                                fieldWithPath("productNo").description("상품 번호 기입"),
                                fieldWithPath("categoryNo").description("카테고리 번호 기입"),
                                fieldWithPath("codeNo").description("코드번호 기입"),
                                fieldWithPath("templateName").description("등록할 쿠폰 템플릿 명 기입"),
                                fieldWithPath("finishedAt").description("만료일자 기입"),
                                fieldWithPath("templateBundled").description("중복 쿠폰사용여부 기입")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("쿠폰이름을 기입해주세요.")
                        )
                ));
    }

    @DisplayName("쿠폰 템플릿 수정 실패 validation 오류_TemplateNameIsTooLong")
    @Test
    void couponTemplateModifyFail_TemplateNameIsTooLong() throws Exception {
        ReflectionTestUtils.setField(modifyRequestDto, "policyNo", 1);
        ReflectionTestUtils.setField(modifyRequestDto, "typeNo", 1L);
        ReflectionTestUtils.setField(modifyRequestDto, "productNo", 1L);
        ReflectionTestUtils.setField(modifyRequestDto, "categoryNo", 1);
        ReflectionTestUtils.setField(modifyRequestDto, "codeNo", 1);
        ReflectionTestUtils.setField(modifyRequestDto, "templateName", "asdfasdfasdfasdfasdfsadfasdfasdfasdfasdfasdfasdfasdfsadfasfdasdfsadfsadfsadf");
        ReflectionTestUtils.setField(modifyRequestDto, "finishedAt", LocalDateTime.of(1, 1, 1, 1, 1));
        ReflectionTestUtils.setField(modifyRequestDto, "templateBundled", true);

        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MockMultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/jpeg", imageContent.getBytes());

        String dtoToJson = objectMapper.writeValueAsString(modifyRequestDto);
        MockMultipartFile modifyRequestDto = new MockMultipartFile("modifyRequestDto", "modifyRequestDto", "application/json", dtoToJson.getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart(authPath + "/{templateNo}", 1L)
                        .file(modifyRequestDto)
                        .file(multipartFile)
                        .with(req -> {
                            req.setMethod("PUT");
                            return req;
                        }))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("쿠폰이름의 최대 글자는 50글자입니다."))
                .andDo(print())
                .andDo(document("coupon_template_modify_nameLongEx",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("image").description("이미지 기입"),
                                partWithName("modifyRequestDto").description("쿠폰 수정 정보기입")),
                        requestPartFields("modifyRequestDto",
                                fieldWithPath("policyNo").description("쿠폰등급 번호 기입"),
                                fieldWithPath("typeNo").description("쿠폰타입 번호 기입"),
                                fieldWithPath("productNo").description("상품 번호 기입"),
                                fieldWithPath("categoryNo").description("카테고리 번호 기입"),
                                fieldWithPath("codeNo").description("코드번호 기입"),
                                fieldWithPath("templateName").description("등록할 쿠폰 템플릿 명 기입"),
                                fieldWithPath("finishedAt").description("만료일자 기입"),
                                fieldWithPath("templateBundled").description("중복 쿠폰사용여부 기입")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("쿠폰이름의 최대 글자는 50글자입니다.")
                        )
                ));
    }

}