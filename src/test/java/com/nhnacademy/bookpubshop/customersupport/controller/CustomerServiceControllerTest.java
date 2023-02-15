package com.nhnacademy.bookpubshop.customersupport.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookpubshop.customersupport.dto.CreateCustomerServiceRequestDto;
import com.nhnacademy.bookpubshop.customersupport.dto.GetCustomerServiceListResponseDto;
import com.nhnacademy.bookpubshop.customersupport.dummy.CustomerServiceDummy;
import com.nhnacademy.bookpubshop.customersupport.entity.CustomerService;
import com.nhnacademy.bookpubshop.customersupport.service.CustomerServiceService;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.servicecode.entity.CustomerServiceStateCode;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 고객서비스 컨트롤러 테스트.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@WebMvcTest(CustomerServiceController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class CustomerServiceControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    CustomerServiceService customerServiceService;
    @Autowired
    ObjectMapper mapper;
    CreateCustomerServiceRequestDto requestDto;
    GetCustomerServiceListResponseDto responseDto;
    CustomerService customerService;
    CustomerServiceStateCode customerServiceStateCode;
    BookPubTier tier;
    Member member;
    MockMultipartFile image;
    MockMultipartFile requestDtoFile;
    Pageable pageable;
    PageResponse<GetCustomerServiceListResponseDto> response;
    String tokenUrl = "/token/service";
    String url = "/api/service";

    @BeforeEach
    void setUp() {
        customerServiceStateCode = new CustomerServiceStateCode("faq", true, "FAQ");
        ReflectionTestUtils.setField(customerServiceStateCode, "serviceCodeNo", 1);

        tier = TierDummy.dummy();
        member = MemberDummy.dummy(tier);

        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        image = new MockMultipartFile(
                "image",
                "imageName.jpeg",
                "image/jpeg",
                imageContent.getBytes());

        customerService = CustomerServiceDummy.dummy(customerServiceStateCode, member);
        ReflectionTestUtils.setField(customerService, "serviceNo", 1);

        responseDto = new GetCustomerServiceListResponseDto(
                customerService.getServiceNo(),
                customerService.getCustomerServiceStateCode().getServiceCodeName(),
                member.getMemberId(),
                image.getOriginalFilename(),
                customerService.getServiceCategory(),
                customerService.getServiceTitle(),
                customerService.getServiceContent(),
                customerService.getCreatedAt()
        );

        requestDto = new CreateCustomerServiceRequestDto();
        ReflectionTestUtils.setField(requestDto,
                "customerServiceStateCode",
                customerService.getCustomerServiceStateCode().getServiceCodeName());
        ReflectionTestUtils.setField(requestDto, "memberNo",
                1L);
        ReflectionTestUtils.setField(requestDto, "serviceCategory",
                "faqUsing");
        ReflectionTestUtils.setField(requestDto, "serviceTitle",
                customerService.getServiceTitle());
        ReflectionTestUtils.setField(requestDto, "serviceContent",
                customerService.getServiceContent());

        pageable = Pageable.ofSize(10);
        response = new PageResponse<>(PageableExecutionUtils.getPage(List.of(responseDto), pageable, () -> 1L));
    }

    @Test
    @DisplayName("고객서비스 생성 성공")
    void createCustomerService() throws Exception {
        requestDtoFile = new MockMultipartFile("requestDto",
                "",
                "application/json",
                mapper.writeValueAsString(requestDto)
                        .getBytes(StandardCharsets.UTF_8));

        doNothing().when(customerServiceService).createCustomerService(requestDto, image);

        mockMvc.perform(multipart(tokenUrl + "s")
                .file(requestDtoFile)
                .file(image)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("customerService-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("image").description("고객서비스 이미지"),
                                partWithName("requestDto").description("고객서비스 생성시 사용되는 Dto")
                        )))
        ;

        then(customerServiceService)
                .should()
                .createCustomerService(any(CreateCustomerServiceRequestDto.class), any());

    }

    @Test
    @DisplayName("고객서비스 생성 실패 - 제목길이")
    void createCustomerServiceFailedTitle() throws Exception {
        ReflectionTestUtils.setField(requestDto, "serviceTitle", "");

        requestDtoFile = new MockMultipartFile("requestDto",
                "",
                "application/json",
                mapper.writeValueAsString(requestDto)
                        .getBytes(StandardCharsets.UTF_8));

        doNothing().when(customerServiceService).createCustomerService(requestDto, image);

        mockMvc.perform(multipart(tokenUrl + "s")
                        .file(requestDtoFile)
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("customerService-create-fail-title",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("image").description("고객서비스 이미지"),
                                partWithName("requestDto").description("고객서비스 생성시 사용되는 Dto")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("검증 실패시 메시지 ")
                        )));
    }

    @Test
    @DisplayName("고객서비스 생성 실패 - 유형 검증")
    void createCustomerServiceFailedState() throws Exception {
        ReflectionTestUtils.setField(requestDto, "customerServiceStateCode", "fab");

        requestDtoFile = new MockMultipartFile("requestDto",
                "",
                "application/json",
                mapper.writeValueAsString(requestDto)
                        .getBytes(StandardCharsets.UTF_8));

        doNothing().when(customerServiceService).createCustomerService(requestDto, image);

        mockMvc.perform(multipart(tokenUrl + "s")
                        .file(requestDtoFile)
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("customerService-create-fail-state",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("image").description("고객서비스 이미지"),
                                partWithName("requestDto").description("고객서비스 생성시 사용되는 Dto")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("검증 실패시 메시지 ")
                        )));
    }

    @Test
    @DisplayName("고객서비스 생성 실패 - 카테고리 검증")
    void createCustomerServiceFailedCategory() throws Exception {
        ReflectionTestUtils.setField(requestDto, "serviceCategory", "fab");

        requestDtoFile = new MockMultipartFile("requestDto",
                "",
                "application/json",
                mapper.writeValueAsString(requestDto)
                        .getBytes(StandardCharsets.UTF_8));

        doNothing().when(customerServiceService).createCustomerService(requestDto, image);

        mockMvc.perform(multipart(tokenUrl + "s")
                        .file(requestDtoFile)
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("customerService-create-fail-category",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("image").description("고객서비스 이미지"),
                                partWithName("requestDto").description("고객서비스 생성시 사용되는 Dto")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("검증 실패시 메시지 ")
                        )));
    }

    @Test
    @DisplayName("고객서비스 생성 실패 - 내용 검증")
    void createCustomerServiceFailedContents() throws Exception {
        ReflectionTestUtils.setField(requestDto, "serviceContent", "");

        requestDtoFile = new MockMultipartFile("requestDto",
                "",
                "application/json",
                mapper.writeValueAsString(requestDto)
                        .getBytes(StandardCharsets.UTF_8));

        doNothing().when(customerServiceService).createCustomerService(requestDto, image);

        mockMvc.perform(multipart(tokenUrl + "s")
                        .file(requestDtoFile)
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("customerService-create-fail-contents",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("image").description("고객서비스 이미지"),
                                partWithName("requestDto").description("고객서비스 생성시 사용되는 Dto")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("검증 실패시 메시지 ")
                        )));
    }

    @Test
    @DisplayName("고객서비스 전체 조회 성공")
    void getCustomerServices() throws Exception {
        when(customerServiceService.getCustomerServices(pageable))
                .thenReturn(response);

        mockMvc.perform(get(tokenUrl + "s")
                .param("page", mapper.writeValueAsString(pageable.getPageNumber()))
                .param("size", mapper.writeValueAsString(pageable.getPageSize()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].customerServiceNo").value(customerService.getServiceNo()))
                .andExpect(jsonPath("$.content[0].customerServiceStateCode").value(customerService.getCustomerServiceStateCode().getServiceCodeName()))
                .andExpect(jsonPath("$.content[0].image").value(image.getOriginalFilename()))
                .andExpect(jsonPath("$.content[0].serviceCategory").value(customerService.getServiceCategory()))
                .andExpect(jsonPath("$.content[0].serviceTitle").value(customerService.getServiceTitle()))
                .andExpect(jsonPath("$.content[0].serviceContent").value(customerService.getServiceContent()))
                .andExpect(jsonPath("$.totalPages").value(response.getTotalPages()))
                .andExpect(jsonPath("$.number").value(response.getNumber()))
                .andExpect(jsonPath("$.previous").value(response.isPrevious()))
                .andExpect(jsonPath("$.next").value(response.isNext()))
                .andDo(print())
                .andDo(document("customerService-get-total",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("조회할 페이지 번호"),
                                parameterWithName("size").description("한 페이지 당 데이터 개수")
                        ),
                        responseFields(
                                fieldWithPath("totalPages").description("총 페이지 개수"),
                                fieldWithPath("number").description("현재 페이지 번호"),
                                fieldWithPath("previous").description("이전 페이지 존재 여부"),
                                fieldWithPath("next").description("다음 페이지 존재 여부"),
                                fieldWithPath("content[].customerServiceNo").description("고객서비스 번호"),
                                fieldWithPath("content[].customerServiceStateCode").description("고객서비스 상태코드"),
                                fieldWithPath("content[].memberId").description("작성자"),
                                fieldWithPath("content[].image").description("이미지"),
                                fieldWithPath("content[].serviceCategory").description("고객서비스 카테고리"),
                                fieldWithPath("content[].serviceTitle").description("고객서비스 제목"),
                                fieldWithPath("content[].serviceContent").description("고객서비스 내용"),
                                fieldWithPath("content[].createdAt").description("등록일시")
                        )));
    }

    @Test
    @DisplayName("고객서비스 코드명 조회")
    void getCustomerServicesByCodeName() throws Exception {
        when(customerServiceService.getCustomerServicesByCodeName(customerServiceStateCode.getServiceCodeName(), pageable))
                .thenReturn(response);

        mockMvc.perform(get(url + "s/{codeName}", customerServiceStateCode.getServiceCodeName())
                        .param("page", mapper.writeValueAsString(pageable.getPageNumber()))
                        .param("size", mapper.writeValueAsString(pageable.getPageSize()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].customerServiceNo").value(customerService.getServiceNo()))
                .andExpect(jsonPath("$.content[0].customerServiceStateCode").value(customerService.getCustomerServiceStateCode().getServiceCodeName()))
                .andExpect(jsonPath("$.content[0].image").value(image.getOriginalFilename()))
                .andExpect(jsonPath("$.content[0].serviceCategory").value(customerService.getServiceCategory()))
                .andExpect(jsonPath("$.content[0].serviceTitle").value(customerService.getServiceTitle()))
                .andExpect(jsonPath("$.content[0].serviceContent").value(customerService.getServiceContent()))
                .andExpect(jsonPath("$.totalPages").value(response.getTotalPages()))
                .andExpect(jsonPath("$.number").value(response.getNumber()))
                .andExpect(jsonPath("$.previous").value(response.isPrevious()))
                .andExpect(jsonPath("$.next").value(response.isNext()))
                .andDo(print())
                .andDo(document("customerService-get-codeName",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("조회할 페이지 번호"),
                                parameterWithName("size").description("한 페이지 당 데이터 개수")
                        ),
                        responseFields(
                                fieldWithPath("totalPages").description("총 페이지 개수"),
                                fieldWithPath("number").description("현재 페이지 번호"),
                                fieldWithPath("previous").description("이전 페이지 존재 여부"),
                                fieldWithPath("next").description("다음 페이지 존재 여부"),
                                fieldWithPath("content[].customerServiceNo").description("고객서비스 번호"),
                                fieldWithPath("content[].customerServiceStateCode").description("고객서비스 상태코드"),
                                fieldWithPath("content[].memberId").description("작성자"),
                                fieldWithPath("content[].image").description("이미지"),
                                fieldWithPath("content[].serviceCategory").description("고객서비스 카테고리"),
                                fieldWithPath("content[].serviceTitle").description("고객서비스 제목"),
                                fieldWithPath("content[].serviceContent").description("고객서비스 내용"),
                                fieldWithPath("content[].createdAt").description("등록일시")
                        )));
    }

    @Test
    @DisplayName("고객서비스 카테고리 조회")
    void getCustomerServicesByCategory() throws Exception {
        when(customerServiceService.getCustomerServicesByCategory(customerService.getServiceCategory(), pageable))
                .thenReturn(response);

        mockMvc.perform(get(url + "s/category/" + customerService.getServiceCategory())
                        .param("page", mapper.writeValueAsString(pageable.getPageNumber()))
                        .param("size", mapper.writeValueAsString(pageable.getPageSize()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].customerServiceNo").value(customerService.getServiceNo()))
                .andExpect(jsonPath("$.content[0].customerServiceStateCode").value(customerService.getCustomerServiceStateCode().getServiceCodeName()))
                .andExpect(jsonPath("$.content[0].image").value(image.getOriginalFilename()))
                .andExpect(jsonPath("$.content[0].serviceCategory").value(customerService.getServiceCategory()))
                .andExpect(jsonPath("$.content[0].serviceTitle").value(customerService.getServiceTitle()))
                .andExpect(jsonPath("$.content[0].serviceContent").value(customerService.getServiceContent()))
                .andExpect(jsonPath("$.totalPages").value(response.getTotalPages()))
                .andExpect(jsonPath("$.number").value(response.getNumber()))
                .andExpect(jsonPath("$.previous").value(response.isPrevious()))
                .andExpect(jsonPath("$.next").value(response.isNext()))
                .andDo(print())
                .andDo(document("customerService-get-category",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("조회할 페이지 번호"),
                                parameterWithName("size").description("한 페이지 당 데이터 개수")
                        ),
                        responseFields(
                                fieldWithPath("totalPages").description("총 페이지 개수"),
                                fieldWithPath("number").description("현재 페이지 번호"),
                                fieldWithPath("previous").description("이전 페이지 존재 여부"),
                                fieldWithPath("next").description("다음 페이지 존재 여부"),
                                fieldWithPath("content[].customerServiceNo").description("고객서비스 번호"),
                                fieldWithPath("content[].customerServiceStateCode").description("고객서비스 상태코드"),
                                fieldWithPath("content[].memberId").description("작성자"),
                                fieldWithPath("content[].image").description("이미지"),
                                fieldWithPath("content[].serviceCategory").description("고객서비스 카테고리"),
                                fieldWithPath("content[].serviceTitle").description("고객서비스 제목"),
                                fieldWithPath("content[].serviceContent").description("고객서비스 내용"),
                                fieldWithPath("content[].createdAt").description("등록일시")
                        )));
    }

    @Test
    @DisplayName("고객서비스 단건 조회")
    void getCustomerService() throws Exception {
        when(customerServiceService.findCustomerServiceByNo(customerService.getServiceNo()))
                .thenReturn(responseDto);

        mockMvc.perform(get(url + "/" + customerService.getServiceNo())
                        .param("page", mapper.writeValueAsString(pageable.getPageNumber()))
                        .param("size", mapper.writeValueAsString(pageable.getPageSize()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerServiceNo").value(customerService.getServiceNo()))
                .andExpect(jsonPath("$.customerServiceStateCode").value(customerService.getCustomerServiceStateCode().getServiceCodeName()))
                .andExpect(jsonPath("$.image").value(image.getOriginalFilename()))
                .andExpect(jsonPath("$.serviceCategory").value(customerService.getServiceCategory()))
                .andExpect(jsonPath("$.serviceTitle").value(customerService.getServiceTitle()))
                .andExpect(jsonPath("$.serviceContent").value(customerService.getServiceContent()))
                .andDo(print())
                .andDo(document("customerService-get-one",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("customerServiceNo").description("고객서비스 번호"),
                                fieldWithPath("customerServiceStateCode").description("고객서비스 상태코드"),
                                fieldWithPath("memberId").description("작성자"),
                                fieldWithPath("image").description("이미지"),
                                fieldWithPath("serviceCategory").description("고객서비스 카테고리"),
                                fieldWithPath("serviceTitle").description("고객서비스 제목"),
                                fieldWithPath("serviceContent").description("고객서비스 내용"),
                                fieldWithPath("createdAt").description("등록일시")
                        )));
    }

    @Test
    @DisplayName("고객서비스 삭제")
    void deleteCustomerService() throws Exception {
        doNothing().when(customerServiceService).deleteCustomerServiceByNo(customerService.getServiceNo());

        mockMvc.perform(delete(tokenUrl + "s/" + customerService.getServiceNo()))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("customerService-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }
}