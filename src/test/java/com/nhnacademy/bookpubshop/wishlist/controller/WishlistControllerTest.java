package com.nhnacademy.bookpubshop.wishlist.controller;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import com.nhnacademy.bookpubshop.wishlist.dto.request.CreateWishlistRequestDto;
import com.nhnacademy.bookpubshop.wishlist.dto.request.DeleteWishlistRequestDto;
import com.nhnacademy.bookpubshop.wishlist.dto.request.ModifyWishlistAlarmRequestDto;
import com.nhnacademy.bookpubshop.wishlist.dto.response.GetWishlistResponseDto;
import com.nhnacademy.bookpubshop.wishlist.service.WishlistService;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

/**
 * wishlistController test.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@WebMvcTest(WishlistController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class WishlistControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    WishlistService wishlistService;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("위시리스트 생성 api 테스트")
    void createWishlist() throws Exception {
        // given
        CreateWishlistRequestDto dto = new CreateWishlistRequestDto();
        ReflectionTestUtils.setField(dto, "productNo", 1L);

        // when
        doNothing().when(wishlistService).createWishlist(1L, dto);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/token/members/{memberNo}/wishlist", 1)
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("wishlist-create-success",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("멤버 번호")
                        ),
                        requestFields(
                                fieldWithPath("productNo").description("상품 번호")
                        )));

        verify(wishlistService, times(1))
                .createWishlist(anyLong(), any());
    }

    @Test
    @DisplayName("위시리스트 생성 실패 테스트 (Null productNo)")
    void createWishlist_Fail_Test_NullProductNo() throws Exception {
        // given
        CreateWishlistRequestDto dto = new CreateWishlistRequestDto();
        ReflectionTestUtils.setField(dto, "productNo", null);

        // when
        doNothing().when(wishlistService).createWishlist(1L, dto);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/token/members/{memberNo}/wishlist", 1)
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("wishlist-create-fail-productNo-null",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("멤버 번호")
                        ),
                        requestFields(
                                fieldWithPath("productNo").description("상품 번호")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("빈 값은 안됩니다.")
                        )));
    }

    @Test
    @DisplayName("멤버별 위시리스트 조회 테스트")
    void getWishlistByMember_Test() throws Exception {
        // given
        GetWishlistResponseDto dto = new GetWishlistResponseDto();
        ReflectionTestUtils.setField(dto, "productNo", 1L);
        ReflectionTestUtils.setField(dto, "title", "제목");
        ReflectionTestUtils.setField(dto, "productPublisher", "출판사");
        ReflectionTestUtils.setField(dto, "thumbnail", "Image");
        ReflectionTestUtils.setField(dto, "codeCategory", "판매중");
        ReflectionTestUtils.setField(dto, "wishlistApplied", false);

        List<GetWishlistResponseDto> list = List.of(dto);

        Pageable pageable = PageRequest.of(0, 10);
        Page<GetWishlistResponseDto> page =
                PageableExecutionUtils.getPage(list, pageable, () -> 1L);

        // when
        when(wishlistService.getWishlist(pageable, 1L))
                .thenReturn(page);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/token/members/{memberNo}/wishlist", 1)
                        .param("page", objectMapper.writeValueAsString(pageable.getPageNumber()))
                        .param("size", objectMapper.writeValueAsString(pageable.getPageSize()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("wishlist-get-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("멤버 번호")
                        ),
                        requestParameters(
                                parameterWithName("page").description("페이지 정보"),
                                parameterWithName("size").description("페이즈 사이즈")
                        ),
                        responseFields(
                                fieldWithPath("content[].productNo").description("상품 번호"),
                                fieldWithPath("content[].title").description("상품 제목"),
                                fieldWithPath("content[].productPublisher").description("출판사"),
                                fieldWithPath("content[].thumbnail").description("썸네일 이미지 경로"),
                                fieldWithPath("content[].codeCategory").description("상품 판매 코드 유형"),
                                fieldWithPath("content[].wishlistApplied").description("재고 알림 여부"),
                                fieldWithPath("totalPages").description("총 페이지 수 반환"),
                                fieldWithPath("number").description("현재 페이지 반환"),
                                fieldWithPath("previous").description("이전 페이지 여부"),
                                fieldWithPath("next").description("다음 페이지 여부")
                        )));

        then(wishlistService).should().getWishlist(pageable, 1L);
    }

    @Test
    @DisplayName("위시리스트 상품 삭제 테스트")
    void deleteWishlist_Success_Test() throws Exception {
        // given
        DeleteWishlistRequestDto dto = new DeleteWishlistRequestDto();
        ReflectionTestUtils.setField(dto, "productNo", 1L);

        // when
        doNothing().when(wishlistService).deleteWishlist(1L, dto);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/token/members/{memberNo}/wishlist", 1)
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("wishlist-delete-success",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("멤버 번호")
                        ),
                        requestFields(
                                fieldWithPath("productNo").description("상품 번호")
                        )));

        then(wishlistService).should()
                .deleteWishlist(anyLong(), any(DeleteWishlistRequestDto.class));
    }

    @Test
    @DisplayName("위시리스트 상품 삭제 실패 테스트 (null productNo)")
    void deleteWishlist_Fail_Test_NullProductNo() throws Exception {
        // given
        DeleteWishlistRequestDto dto = new DeleteWishlistRequestDto();
        ReflectionTestUtils.setField(dto, "productNo", null);

        // when
        doNothing().when(wishlistService).deleteWishlist(1L, dto);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/token/members/{memberNo}/wishlist", 1)
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("wishlist-delete-fail-null-productNo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("멤버 번호")
                        ),
                        requestFields(
                                fieldWithPath("productNo").description("상품 번호")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("빈 값은 안됩니다.")
                        )));
    }

    @Test
    @DisplayName("위시리스트 알림 여부 수정 성공 테스트")
    void modifyAlarm_Success_Test() throws Exception {
        // given
        ModifyWishlistAlarmRequestDto dto = new ModifyWishlistAlarmRequestDto();
        ReflectionTestUtils.setField(dto, "productNo", 1L);

        // when
        doNothing().when(wishlistService).modifyWishlistAlarm(1L, dto);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/token/members/{memberNo}/wishlist", 1)
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("wishlist-modify-alarm-success",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("멤버 번호")
                        ),
                        requestFields(
                                fieldWithPath("productNo").description("상품 번호")
                        )));

        then(wishlistService).should()
                .modifyWishlistAlarm(anyLong(), any(ModifyWishlistAlarmRequestDto.class));
    }

    @Test
    @DisplayName("위시리스트 알림 여부 수정 실패 테스트(Null productNo)")
    void modifyAlarm_Fail_Test() throws Exception {
        // given
        ModifyWishlistAlarmRequestDto dto = new ModifyWishlistAlarmRequestDto();
        ReflectionTestUtils.setField(dto, "productNo", null);

        // when
        doNothing().when(wishlistService).modifyWishlistAlarm(1L, dto);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/token/members/{memberNo}/wishlist", 1L)
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("wishlist-modify-alarm-fail-null-productNo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("멤버 번호")
                        ),
                        requestFields(
                                fieldWithPath("productNo").description("상품 번호")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("빈 값은 안됩니다.")
                        )));
    }


}
