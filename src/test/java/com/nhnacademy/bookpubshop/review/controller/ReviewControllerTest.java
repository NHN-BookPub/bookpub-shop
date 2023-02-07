package com.nhnacademy.bookpubshop.review.controller;

import static java.time.LocalDateTime.now;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductSimpleResponseDto;
import com.nhnacademy.bookpubshop.review.dto.request.CreateReviewRequestDto;
import com.nhnacademy.bookpubshop.review.dto.request.ModifyReviewRequestDto;
import com.nhnacademy.bookpubshop.review.dto.response.GetMemberReviewResponseDto;
import com.nhnacademy.bookpubshop.review.dto.response.GetProductReviewInfoResponseDto;
import com.nhnacademy.bookpubshop.review.dto.response.GetProductReviewResponseDto;
import com.nhnacademy.bookpubshop.review.service.ReviewService;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
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
 * 상품평 컨트롤러 테스트입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@WebMvcTest(ReviewController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class ReviewControllerTest {

    @Autowired
    MockMvc mockMvc;
    ObjectMapper objectMapper;

    @MockBean
    ReviewService reviewService;

    String path = "/api/reviews";
    String authPath = "/token/reviews";

    CreateReviewRequestDto createRequestDto;
    ModifyReviewRequestDto modifyRequestDto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        createRequestDto = new CreateReviewRequestDto();
        modifyRequestDto = new ModifyReviewRequestDto();

        ReflectionTestUtils.setField(createRequestDto, "memberNo", 1L);
        ReflectionTestUtils.setField(createRequestDto, "productNo", 1L);
        ReflectionTestUtils.setField(createRequestDto, "reviewStar", 5);
        ReflectionTestUtils.setField(createRequestDto, "reviewContent", "funny book!");

        ReflectionTestUtils.setField(modifyRequestDto, "reviewStar", 5);
        ReflectionTestUtils.setField(modifyRequestDto, "reviewContent", "sad book!!");
    }

    @Test
    @DisplayName("상품에 따른 상품평 리스트 조회 성공 테스트")
    void productReviewListSuccess() throws Exception {
        //given
        GetProductReviewResponseDto dto =
                new GetProductReviewResponseDto(1L, "nickname",
                        5, "content", "imagePath", now());
        List<GetProductReviewResponseDto> responses = new ArrayList<>();
        responses.add(dto);

        Pageable pageable = PageRequest.of(0, 10);
        Page<GetProductReviewResponseDto> page =
                PageableExecutionUtils.getPage(responses, pageable, () -> 1L);

        //when
        when(reviewService.getProductReviews(any(), anyLong())).thenReturn(page);

        //then
        mockMvc.perform(RestDocumentationRequestBuilders.get(path + "/product/{productNo}", 1L)
                        .param("page", objectMapper.writeValueAsString(pageable.getPageNumber()))
                        .param("size", objectMapper.writeValueAsString(pageable.getPageSize()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].reviewNo").value(responses.get(0).getReviewNo()))
                .andExpect(jsonPath("$.content[0].memberNickname").value(responses.get(0).getMemberNickname()))
                .andExpect(jsonPath("$.content[0].reviewStar").value(responses.get(0).getReviewStar()))
                .andExpect(jsonPath("$.content[0].reviewContent").value(responses.get(0).getReviewContent()))
                .andExpect(jsonPath("$.content[0].imagePath").value(responses.get(0).getImagePath()))
                .andExpect(jsonPath("$.content[0].createdAt").value(responses.get(0).getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andDo(document("get-reviews-product",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("productNo").description("상품평을 조회하고 싶은 상품 번호입니다.")
                        ),
                        requestParameters(
                                parameterWithName("page").description("페이지 번호입니다."),
                                parameterWithName("size").description("한 페이지 당 데이터 개수입니다.")
                        ),
                        responseFields(
                                fieldWithPath("totalPages").description("총 페이지 개수"),
                                fieldWithPath("number").description("현재 페이지 번호"),
                                fieldWithPath("previous").description("이전 페이지 존재 여부"),
                                fieldWithPath("next").description("다음 페이지 존재 여부"),
                                fieldWithPath("content[].reviewNo").description("조회된 상품평 번호입니다."),
                                fieldWithPath("content[].memberNickname").description("상품평 작성자의 닉네임입니다."),
                                fieldWithPath("content[].reviewStar").description("상품평의 별점개수입니다."),
                                fieldWithPath("content[].reviewContent").description("상품평의 내용입니다."),
                                fieldWithPath("content[].imagePath").description("상품평에 첨부된 이미지 주소입니다."),
                                fieldWithPath("content[].createdAt").description("상품평 작성 날짜입니다.")
                        )));

        verify(reviewService, times(1)).getProductReviews(any(), anyLong());
    }

    @Test
    @DisplayName("상품평 단건 조회 성공 테스트")
    void reviewDetailsSuccess() throws Exception {
        //given
        GetMemberReviewResponseDto dto =
                new GetMemberReviewResponseDto(1L, 1L, "title", "publisher",
                        List.of("writer"), "productImagePath", 5, "content",
                        "reviewImagePath", now());

        //when
        when(reviewService.getReview(anyLong())).thenReturn(dto);

        //then
        mockMvc.perform(RestDocumentationRequestBuilders.get(path + "/{reviewNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewNo").value(dto.getReviewNo()))
                .andExpect(jsonPath("$.productNo").value(dto.getProductNo()))
                .andExpect(jsonPath("$.productTitle").value(dto.getProductTitle()))
                .andExpect(jsonPath("$.productPublisher").value(dto.getProductPublisher()))
                .andExpect(jsonPath("$.productAuthorNames[0]").value(dto.getProductAuthorNames().get(0)))
                .andExpect(jsonPath("$.productImagePath").value(dto.getProductImagePath()))
                .andExpect(jsonPath("$.reviewStar").value(dto.getReviewStar()))
                .andExpect(jsonPath("$.reviewContent").value(dto.getReviewContent()))
                .andExpect(jsonPath("$.reviewImagePath").value(dto.getReviewImagePath()))
                .andExpect(jsonPath("$.createdAt").value(dto.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andDo(document("get-review",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("reviewNo").description("조회할 상품번호입니다.")
                        ),
                        responseFields(
                                fieldWithPath("reviewNo").description("조회된 상품평번호입니다."),
                                fieldWithPath("productNo").description("상품평의 상품번호입니다."),
                                fieldWithPath("productTitle").description("상품평의 상품제목입니다."),
                                fieldWithPath("productPublisher").description("상품평의 상품 출판사입니다."),
                                fieldWithPath("productAuthorNames[]").description("상품평의 상품 작가목록입니다."),
                                fieldWithPath("productImagePath").description("상품평의 상품 이미지 경로입니다."),
                                fieldWithPath("reviewStar").description("상품평의 별점개수입니다."),
                                fieldWithPath("reviewContent").description("상품평의 내용입니다."),
                                fieldWithPath("reviewImagePath").description("상품평에 첨부된 이미지 주소입니다."),
                                fieldWithPath("createdAt").description("상품평 작성 날짜입니다.")
                        )));

        verify(reviewService, times(1)).getReview(anyLong());
    }

    @Test
    @DisplayName("회원이 작성한 상품평 조회 성공 테스트")
    void memberReviewListSuccess() throws Exception {
        //given
        GetMemberReviewResponseDto dto =
                new GetMemberReviewResponseDto(1L, 1L, "title", "publisher",
                        List.of("writer"), "productImagePath", 5, "content",
                        "reviewImagePath", now());
        List<GetMemberReviewResponseDto> responses = new ArrayList<>();
        responses.add(dto);

        Pageable pageable = PageRequest.of(0, 10);
        Page<GetMemberReviewResponseDto> page =
                PageableExecutionUtils.getPage(responses, pageable, () -> 1L);

        //when
        when(reviewService.getMemberReviews(any(), anyLong())).thenReturn(page);

        //then
        mockMvc.perform(RestDocumentationRequestBuilders.get(authPath + "/member/{memberNo}", 1L)
                        .param("page", objectMapper.writeValueAsString(pageable.getPageNumber()))
                        .param("size", objectMapper.writeValueAsString(pageable.getPageSize()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].reviewNo").value(responses.get(0).getReviewNo()))
                .andExpect(jsonPath("$.content[0].productNo").value(responses.get(0).getProductNo()))
                .andExpect(jsonPath("$.content[0].productTitle").value(responses.get(0).getProductTitle()))
                .andExpect(jsonPath("$.content[0].productPublisher").value(responses.get(0).getProductPublisher()))
                .andExpect(jsonPath("$.content[0].productAuthorNames[0]").value(responses.get(0).getProductAuthorNames().get(0)))
                .andExpect(jsonPath("$.content[0].productImagePath").value(responses.get(0).getProductImagePath()))
                .andExpect(jsonPath("$.content[0].reviewStar").value(responses.get(0).getReviewStar()))
                .andExpect(jsonPath("$.content[0].reviewContent").value(responses.get(0).getReviewContent()))
                .andExpect(jsonPath("$.content[0].reviewImagePath").value(responses.get(0).getReviewImagePath()))
                .andExpect(jsonPath("$.content[0].createdAt").value(responses.get(0).getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andDo(document("get-reviews-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("작성한 상품평 리스트를 조회할 회원 번호입니다.")
                        ),
                        requestParameters(
                                parameterWithName("page").description("페이지 번호입니다."),
                                parameterWithName("size").description("한 페이지 당 데이터 개수입니다.")
                        ),
                        responseFields(
                                fieldWithPath("totalPages").description("총 페이지 개수"),
                                fieldWithPath("number").description("현재 페이지 번호"),
                                fieldWithPath("previous").description("이전 페이지 존재 여부"),
                                fieldWithPath("next").description("다음 페이지 존재 여부"),
                                fieldWithPath("content[].reviewNo").description("조회된 상품평번호입니다."),
                                fieldWithPath("content[].productNo").description("상품평의 상품번호입니다."),
                                fieldWithPath("content[].productTitle").description("상품평의 상품제목입니다."),
                                fieldWithPath("content[].productPublisher").description("상품평의 상품 출판사입니다."),
                                fieldWithPath("content[].productAuthorNames[]").description("상품평의 상품 작가목록입니다."),
                                fieldWithPath("content[].productImagePath").description("상품평의 상품 이미지 경로입니다."),
                                fieldWithPath("content[].reviewStar").description("상품평의 별점개수입니다."),
                                fieldWithPath("content[].reviewContent").description("상품평의 내용입니다."),
                                fieldWithPath("content[].reviewImagePath").description("상품평에 첨부된 이미지 주소입니다."),
                                fieldWithPath("content[].createdAt").description("상품평 작성 날짜입니다.")
                        )));

        verify(reviewService, times(1)).getMemberReviews(any(), anyLong());
    }

    @Test
    @DisplayName("회원이 상품평 작성 가능한 상품정보들 조회 성공 테스트")
    void memberWritableReviewListSuccess() throws Exception {
        //given
        GetProductSimpleResponseDto dto =
                new GetProductSimpleResponseDto(1L, "title", "productIsbn", "publisher",
                        List.of("writer"), "imagePath");
        List<GetProductSimpleResponseDto> responses = new ArrayList<>();
        responses.add(dto);

        Pageable pageable = PageRequest.of(0, 10);
        Page<GetProductSimpleResponseDto> page =
                PageableExecutionUtils.getPage(responses, pageable, () -> 1L);

        //when
        when(reviewService.getWritableMemberReviews(any(), anyLong())).thenReturn(page);

        //then
        mockMvc.perform(RestDocumentationRequestBuilders.get(authPath + "/member/{memberNo}/writable", 1L)
                        .param("page", objectMapper.writeValueAsString(pageable.getPageNumber()))
                        .param("size", objectMapper.writeValueAsString(pageable.getPageSize()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].productNo").value(responses.get(0).getProductNo()))
                .andExpect(jsonPath("$.content[0].title").value(responses.get(0).getTitle()))
                .andExpect(jsonPath("$.content[0].productIsbn").value(responses.get(0).getProductIsbn()))
                .andExpect(jsonPath("$.content[0].productPublisher").value(responses.get(0).getProductPublisher()))
                .andExpect(jsonPath("$.content[0].productAuthorNames[0]").value(responses.get(0).getProductAuthorNames().get(0)))
                .andExpect(jsonPath("$.content[0].productImagePath").value(responses.get(0).getProductImagePath()))
                .andDo(document("get-reviews-member-writable",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("해당 회원 번호의 작성 가능한 상품을 조회합니다.")
                        ),
                        requestParameters(
                                parameterWithName("page").description("페이지 번호입니다."),
                                parameterWithName("size").description("한 페이지 당 데이터 개수입니다.")
                        ),
                        responseFields(
                                fieldWithPath("totalPages").description("총 페이지 개수"),
                                fieldWithPath("number").description("현재 페이지 번호"),
                                fieldWithPath("previous").description("이전 페이지 존재 여부"),
                                fieldWithPath("next").description("다음 페이지 존재 여부"),
                                fieldWithPath("content[].productNo").description("조회된 상품번호입니다."),
                                fieldWithPath("content[].title").description("상품 제목입니다."),
                                fieldWithPath("content[].productIsbn").description("상품의 ISBN 입니다."),
                                fieldWithPath("content[].productPublisher").description("상품의 출판사입니다."),
                                fieldWithPath("content[].productAuthorNames[]").description("상품의 작가목록입니다."),
                                fieldWithPath("content[].productImagePath").description("상품의 이미지 경로입니다.")
                        )));

        verify(reviewService, times(1)).getWritableMemberReviews(any(), anyLong());
    }

    @Test
    @DisplayName("상품에 대한 상품평 요약정보 조회 성공 테스트")
    void reviewInfoProductSuccess() throws Exception {
        //given
        GetProductReviewInfoResponseDto dto =
                new GetProductReviewInfoResponseDto(500L, 4);

        //when
        when(reviewService.getReviewInfo(anyLong())).thenReturn(dto);

        //then
        mockMvc.perform(RestDocumentationRequestBuilders.get(path + "/info/product/{productNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewCount").value(dto.getReviewCount()))
                .andExpect(jsonPath("$.productStar").value(dto.getProductStar()))
                .andDo(document("get-review-info-product",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("productNo").description("상품평 정보를 조회할 상품 번호입니다.")
                        ),
                        responseFields(
                                fieldWithPath("reviewCount").description("상품의 상품평 개수입니다."),
                                fieldWithPath("productStar").description("상품의 총 평균평점입니다.")
                        )
                ));
    }

    @Test
    @DisplayName("상품평 등록 성공 테스트")
    void reviewAddSuccess() throws Exception {
        //given & when
        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MockMultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/jpeg", imageContent.getBytes());

        String dtoToJson = objectMapper.writeValueAsString(createRequestDto);
        MockMultipartFile createRequestDto = new MockMultipartFile("createRequestDto", "createRequestDto", "application/json", dtoToJson.getBytes(StandardCharsets.UTF_8));

        //then
        mockMvc.perform(multipart(authPath + "/members/{memberNo}", 1L)
                        .file(multipartFile)
                        .file(createRequestDto))
                .andExpect(status().isCreated())
                .andDo(document("review-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("image").description("이미지 기입"),
                                partWithName("createRequestDto").description("쿠폰 생성 정보기입")),
                        requestPartFields("createRequestDto",
                                fieldWithPath("memberNo").description("상품평을 작성한 회원 번호 기입"),
                                fieldWithPath("productNo").description("상품평이 작성되는 상품 번호 기입"),
                                fieldWithPath("reviewStar").description("상품평 별점 기입"),
                                fieldWithPath("reviewContent").description("상품평 내용 기입")
                        )
                ));
    }

    @Test
    @DisplayName("상품평 등록 실패 테스트_회원번호가 기입되지 않은 경우")
    void reviewAddFail_MemberNoIsNull() throws Exception {
        //given & when
        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MockMultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/jpeg", imageContent.getBytes());

        ReflectionTestUtils.setField(createRequestDto, "memberNo", null);

        String dtoToJson = objectMapper.writeValueAsString(createRequestDto);
        MockMultipartFile createRequestDto = new MockMultipartFile("createRequestDto", "createRequestDto", "application/json", dtoToJson.getBytes(StandardCharsets.UTF_8));

        //then
        mockMvc.perform(multipart(authPath + "/members/{memberNo}", 1L)
                        .file(multipartFile)
                        .file(createRequestDto)
                )
                .andExpect(status().is4xxClientError())
                .andDo(document("review-create-fail-memberNo-null",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("image").description("이미지 기입"),
                                partWithName("createRequestDto").description("쿠폰 생성 정보기입")),
                        requestPartFields("createRequestDto",
                                fieldWithPath("memberNo").description("상품평을 작성한 회원 번호 기입"),
                                fieldWithPath("productNo").description("상품평이 작성되는 상품 번호 기입"),
                                fieldWithPath("reviewStar").description("상품평 별점 기입"),
                                fieldWithPath("reviewContent").description("상품평 내용 기입")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("회원 번호를 입력해주세요")
                        )
                ));
    }

    @Test
    @DisplayName("상품평 등록 실패 테스트_상품번호가 기입되지 않은 경우")
    void reviewAddFail_ProductNoIsNull() throws Exception {
        //given & when
        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MockMultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/jpeg", imageContent.getBytes());

        ReflectionTestUtils.setField(createRequestDto, "productNo", null);

        String dtoToJson = objectMapper.writeValueAsString(createRequestDto);
        MockMultipartFile createRequestDto = new MockMultipartFile("createRequestDto", "createRequestDto", "application/json", dtoToJson.getBytes(StandardCharsets.UTF_8));

        //then
        mockMvc.perform(multipart(authPath + "/members/{memberNo}", 1L)
                        .file(multipartFile)
                        .file(createRequestDto))
                .andExpect(status().is4xxClientError())
                .andDo(document("review-create-fail-productNo-null",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("image").description("이미지 기입"),
                                partWithName("createRequestDto").description("쿠폰 생성 정보기입")),
                        requestPartFields("createRequestDto",
                                fieldWithPath("memberNo").description("상품평을 작성한 회원 번호 기입"),
                                fieldWithPath("productNo").description("상품평이 작성되는 상품 번호 기입"),
                                fieldWithPath("reviewStar").description("상품평 별점 기입"),
                                fieldWithPath("reviewContent").description("상품평 내용 기입")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("상품 번호를 입력해주세요")
                        )
                ));
    }

    @Test
    @DisplayName("상품평 등록 실패 테스트_평점이 기입되지 않은 경우")
    void reviewAddFail_ReviewStarIsNull() throws Exception {
        //given & when
        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MockMultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/jpeg", imageContent.getBytes());

        ReflectionTestUtils.setField(createRequestDto, "reviewStar", null);

        String dtoToJson = objectMapper.writeValueAsString(createRequestDto);
        MockMultipartFile createRequestDto = new MockMultipartFile("createRequestDto", "createRequestDto", "application/json", dtoToJson.getBytes(StandardCharsets.UTF_8));

        //then
        mockMvc.perform(multipart(authPath + "/members/{memberNo}", 1L)
                        .file(multipartFile)
                        .file(createRequestDto))
                .andExpect(status().is4xxClientError())
                .andDo(document("review-create-fail-reviewStar-null",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("image").description("이미지 기입"),
                                partWithName("createRequestDto").description("쿠폰 생성 정보기입")),
                        requestPartFields("createRequestDto",
                                fieldWithPath("memberNo").description("상품평을 작성한 회원 번호 기입"),
                                fieldWithPath("productNo").description("상품평이 작성되는 상품 번호 기입"),
                                fieldWithPath("reviewStar").description("상품평 별점 기입"),
                                fieldWithPath("reviewContent").description("상품평 내용 기입")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("별점을 입력해주세요")
                        )
                ));
    }

    @Test
    @DisplayName("상품평 등록 실패 테스트_상품평 내용이 기입되지 않은 경우")
    void reviewAddFail_ReviewContentIsBlank() throws Exception {
        //given & when
        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MockMultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/jpeg", imageContent.getBytes());

        ReflectionTestUtils.setField(createRequestDto, "reviewContent", null);

        String dtoToJson = objectMapper.writeValueAsString(createRequestDto);
        MockMultipartFile createRequestDto = new MockMultipartFile("createRequestDto", "createRequestDto", "application/json", dtoToJson.getBytes(StandardCharsets.UTF_8));

        //then
        mockMvc.perform(multipart(authPath + "/members/{memberNo}", 1L)
                        .file(multipartFile)
                        .file(createRequestDto))
                .andExpect(status().is4xxClientError())
                .andDo(document("review-create-fail-reviewContent-null",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("image").description("이미지 기입"),
                                partWithName("createRequestDto").description("쿠폰 생성 정보기입")),
                        requestPartFields("createRequestDto",
                                fieldWithPath("memberNo").description("상품평을 작성한 회원 번호 기입"),
                                fieldWithPath("productNo").description("상품평이 작성되는 상품 번호 기입"),
                                fieldWithPath("reviewStar").description("상품평 별점 기입"),
                                fieldWithPath("reviewContent").description("상품평 내용 기입")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("별점을 입력해주세요")
                        )
                ));
    }

    @Test
    @DisplayName("상품평 수정 성공 테스트")
    void reviewModifySuccess() throws Exception {
        //given & when
        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MockMultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/jpeg", imageContent.getBytes());

        String dtoToJson = objectMapper.writeValueAsString(modifyRequestDto);
        MockMultipartFile modifyRequestDto = new MockMultipartFile("modifyRequestDto", "modifyRequestDto", "application/json", dtoToJson.getBytes(StandardCharsets.UTF_8));

        //then
        mockMvc.perform(multipart(authPath + "/{reviewNo}/content/members/{memberNo}", 1L, 1L)
                        .file(modifyRequestDto)
                        .file(multipartFile)
                        .with(req -> {
                            req.setMethod("PUT");
                            return req;
                        }))
                .andExpect(status().isCreated())
                .andDo(document("review-modify",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("image").description("이미지 기입"),
                                partWithName("modifyRequestDto").description("쿠폰 생성 정보기입")),
                        requestPartFields("modifyRequestDto",
                                fieldWithPath("reviewStar").description("상품평 별점 기입"),
                                fieldWithPath("reviewContent").description("상품평 내용 기입")
                        )
                ));
    }

    @Test
    @DisplayName("상품평 수정 실패 테스트_별점이 기입되지 않은 경우")
    void reviewModifyFail_ReviewStarIsNull() throws Exception {
        //given & when
        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MockMultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/jpeg", imageContent.getBytes());

        ReflectionTestUtils.setField(modifyRequestDto, "reviewStar", null);

        String dtoToJson = objectMapper.writeValueAsString(modifyRequestDto);
        MockMultipartFile modifyRequestDto = new MockMultipartFile("modifyRequestDto", "modifyRequestDto", "application/json", dtoToJson.getBytes(StandardCharsets.UTF_8));

        //then
        mockMvc.perform(multipart(authPath + "/{reviewNo}/content/members/{memberNo}", 1L, 1L)
                        .file(modifyRequestDto)
                        .file(multipartFile)
                        .with(req -> {
                            req.setMethod("PUT");
                            return req;
                        }))
                .andExpect(status().is4xxClientError())
                .andDo(document("review-modify-fail-reviewStar-null",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("image").description("이미지 기입"),
                                partWithName("modifyRequestDto").description("쿠폰 생성 정보기입")),
                        requestPartFields("modifyRequestDto",
                                fieldWithPath("reviewStar").description("상품평 별점 기입"),
                                fieldWithPath("reviewContent").description("상품평 내용 기입")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("별점을 입력해주세요")
                        )

                ));
    }

    @Test
    @DisplayName("상품평 수정 실패 테스트_상품평이 기입되지 않은 경우")
    void reviewModifyFail_ContentIsNull() throws Exception {
        //given & when
        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MockMultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/jpeg", imageContent.getBytes());

        ReflectionTestUtils.setField(modifyRequestDto, "reviewContent", null);
        String dtoToJson = objectMapper.writeValueAsString(modifyRequestDto);
        MockMultipartFile modifyRequestDto = new MockMultipartFile("modifyRequestDto", "modifyRequestDto", "application/json", dtoToJson.getBytes(StandardCharsets.UTF_8));

        //then
        mockMvc.perform(multipart(authPath + "/{reviewNo}/content/members/{memberNo}", 1L, 1L)
                        .file(modifyRequestDto)
                        .file(multipartFile)
                        .with(req -> {
                            req.setMethod("PUT");
                            return req;
                        }))
                .andExpect(status().is4xxClientError())
                .andDo(document("review-modify-fail-reviewContent-null",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("image").description("이미지 기입"),
                                partWithName("modifyRequestDto").description("쿠폰 생성 정보기입")),
                        requestPartFields("modifyRequestDto",
                                fieldWithPath("reviewStar").description("상품평 별점 기입"),
                                fieldWithPath("reviewContent").description("상품평 내용 기입")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("상품평 내용을 입력해주세요")
                        )
                ));
    }

    @Test
    @DisplayName("상품평 수정 이미지 삭제 성공 테스트")
    void reviewDeleteFileSuccess() throws Exception {
        //given & when
        doNothing().when(reviewService).deleteReviewImage(anyLong());

        //then
        mockMvc.perform(RestDocumentationRequestBuilders.put(authPath + "/{reviewNo}/file/members/{memberNo}", 1L, 1L))
                .andExpect(status().isCreated())
                .andDo(document("review-modify-image-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("상품평을 작성한 회원 번호입니다"),
                                parameterWithName("reviewNo").description("이미지를 삭제할 상품평 번호입니다")
                        )));
    }

    @Test
    @DisplayName("상품평 소프트 삭제 성공 테스트")
    void reviewDelete() throws Exception {
        //given & when
        doNothing().when(reviewService).deleteReview(anyLong());

        //then
        mockMvc.perform(RestDocumentationRequestBuilders.put(authPath + "/{reviewNo}/members/{memberNo}", 1L, 1L))
                .andExpect(status().isCreated())
                .andDo(document("review-modify-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("상품평을 작성한 멤버 번호입니다"),
                                parameterWithName("reviewNo").description("삭제할 상품평 번호입니다")
                        )));
    }
}