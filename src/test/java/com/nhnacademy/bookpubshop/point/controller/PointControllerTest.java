package com.nhnacademy.bookpubshop.point.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import com.nhnacademy.bookpubshop.point.dto.request.PointGiftRequestDto;
import com.nhnacademy.bookpubshop.point.dto.response.GetPointAdminResponseDto;
import com.nhnacademy.bookpubshop.point.dto.response.GetPointResponseDto;
import com.nhnacademy.bookpubshop.point.service.PointService;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import java.time.LocalDateTime;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 포인트내역 컨트롤러 테스트.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@WebMvcTest(PointController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class PointControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PointService pointService;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("회원의 포인트 사용 내역 기록을 모두 불러오는 메소드")
    void getPointHistory() throws Exception {
        GetPointResponseDto dto = new GetPointResponseDto(
                100L,
                "reason",
                LocalDateTime.of(2023, 2, 18, 0, 0),
                true
        );
        List<GetPointResponseDto> dto1 = new ArrayList<>();
        dto1.add(dto);
        Pageable pageable = Pageable.ofSize(10);

        Page<GetPointResponseDto> page = PageableExecutionUtils.getPage(dto1, pageable, dto1::size);
        PageResponse<GetPointResponseDto> response = new PageResponse<>(page);

        when(pointService.getPointHistory(any(), anyString(), anyLong()))
                .thenReturn(response);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/token/point/{memberNo}", 1L)
                        .queryParam("type", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].pointHistoryAmount").value(100L))
                .andExpect(jsonPath("$.content[0].pointHistoryReason").value("reason"))
                .andExpect(jsonPath("$.content[0].createdAt").value("2023-02-18T00:00:00"))
                .andExpect(jsonPath("$.content[0].increased").value(true))
                .andDo(document("point-history-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("회원의 번호 입니다.")
                        ),
                        responseFields(
                                fieldWithPath("content[].pointHistoryAmount").description("포인트 금액"),
                                fieldWithPath("content[].pointHistoryReason").description("포인트 이용 내용"),
                                fieldWithPath("content[].createdAt").description("포인트 내역 생성 일시"),
                                fieldWithPath("content[].increased").description("증감여부"),
                                fieldWithPath("totalPages").description("총 페이지 수"),
                                fieldWithPath("number").description("현재 페이지 번호"),
                                fieldWithPath("previous").description("이전 페이지 번호"),
                                fieldWithPath("next").description("다음 페이지 번호")
                        )));
    }

    @Test
    @DisplayName("포인트 선물 메소드")
    void giftPoint() throws Exception {
        doNothing().when(pointService).giftPoint(anyLong(), any());

        PointGiftRequestDto dto = new PointGiftRequestDto();
        ReflectionTestUtils.setField(dto, "nickname", "nickname");
        ReflectionTestUtils.setField(dto, "pointAmount", 100L);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/token/point/{memberNo}", 1L)
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("point-gift",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("회원의 번호")
                        ),
                        requestFields(
                                PayloadDocumentation.subsectionWithPath("nickname").description("선물 받을 사람의 닉네임"),
                                PayloadDocumentation.subsectionWithPath("pointAmount").description("선물 할 포인트 액수")
                        )));
    }

    @Test
    @DisplayName("관리자가 포인트 내역을 조회하는 메소드")
    void getPoints() throws Exception {
        GetPointAdminResponseDto dto = new GetPointAdminResponseDto(
                "memberId",
                100L,
                "reason",
                LocalDateTime.of(2023, 2, 18, 0, 0),
                true
        );
        List<GetPointAdminResponseDto> dto1 = new ArrayList<>();
        dto1.add(dto);
        Pageable pageable = Pageable.ofSize(10);

        Page<GetPointAdminResponseDto> page = PageableExecutionUtils.getPage(dto1, pageable, dto1::size);
        PageResponse<GetPointAdminResponseDto> response = new PageResponse<>(page);

        when(pointService.getPoints(any(), any(), any()))
                .thenReturn(response);

        mockMvc.perform(get("/token/points")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].memberId").value("memberId"))
                .andExpect(jsonPath("$.content[0].pointHistoryAmount").value(100L))
                .andExpect(jsonPath("$.content[0].pointHistoryReason").value("reason"))
                .andExpect(jsonPath("$.content[0].createdAt").value("2023-02-18T00:00:00"))
                .andExpect(jsonPath("$.content[0].increased").value(true))
                .andDo(document("admin-point-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("content[].memberId").description("조회할 멤버 아이디"),
                                fieldWithPath("content[].pointHistoryAmount").description("포인트 내역 금액"),
                                fieldWithPath("content[].pointHistoryReason").description("포인트 내역 작성 이유"),
                                fieldWithPath("content[].createdAt").description("포인트 내역 생성시간"),
                                fieldWithPath("content[].increased").description("포인트 증감 여부"),
                                fieldWithPath("totalPages").description("총 페이지 수"),
                                fieldWithPath("number").description("현재 페이지 번호"),
                                fieldWithPath("previous").description("이전 페이지 번호"),
                                fieldWithPath("next").description("다음 페이지 번호")
                        )));
    }
}