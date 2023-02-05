package com.nhnacademy.bookpubshop.author.controller;

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
import com.nhnacademy.bookpubshop.author.dto.request.CreateAuthorRequestDto;
import com.nhnacademy.bookpubshop.author.dto.request.ModifyAuthorRequestDto;
import com.nhnacademy.bookpubshop.author.dto.response.GetAuthorResponseDto;
import com.nhnacademy.bookpubshop.author.dummy.AuthorDummy;
import com.nhnacademy.bookpubshop.author.entity.Author;
import com.nhnacademy.bookpubshop.author.service.AuthorService;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 저자 컨트롤러 테스트입니다.
 *
 * @author : 여운석, 박경서, 김서현
 * @since : 1.0
 **/
@WebMvcTest(AuthorController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class AuthorControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    AuthorService authorService;
    ObjectMapper objectMapper;
    Author author;
    CreateAuthorRequestDto requestDto;
    GetAuthorResponseDto responseDto;

    String path = "/api/authors";
    String authPath = "/token/authors";

    @BeforeEach
    void setUp() {
        author = AuthorDummy.dummy();
        objectMapper = new ObjectMapper();
        requestDto = new CreateAuthorRequestDto();
        responseDto = new GetAuthorResponseDto(1, "저자킴", "해리");
    }

    @Test
    @DisplayName("저자 등록 성공")
    void createAuthorTest() throws Exception {
        ReflectionTestUtils.setField(requestDto,
                "authorName",
                "메이즈러너");

        ArgumentCaptor<CreateAuthorRequestDto> captor =
                ArgumentCaptor.forClass(CreateAuthorRequestDto.class);

        mockMvc.perform(post(authPath)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("author-create-success",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("authorName").description("저자 이름"),
                                fieldWithPath("mainBook").description("대표작")
                        )));

        verify(authorService, times(1))
                .createAuthor(any(CreateAuthorRequestDto.class));
    }

    @Test
    @DisplayName("저자 생성 Validation Exception 저자 생성 이름 없음")
    void createAuthorNameIsNullFailTest() throws Exception {
        CreateAuthorRequestDto dto = new CreateAuthorRequestDto();
        ReflectionTestUtils.setField(dto, "authorName", "");

        doNothing().when(authorService).createAuthor(dto);

        mockMvc.perform(RestDocumentationRequestBuilders.put(authPath + "/{authorNo}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("author-create-authorName-null-fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("authorNo").description("저자 번호")),
                        requestFields(
                                fieldWithPath("authorName").description("저자 이름"),
                                fieldWithPath("mainBook").description("대표작")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("저자 이름은 필수 항목입니다.")
                        )
                ));

        verify(authorService, times(0))
                .modifyAuthor(anyInt(), any(ModifyAuthorRequestDto.class));

    }

    @Test
    @DisplayName("저자 생성 Validation Exception 저자 생성 이름 길이 초과")
    void createAuthorNameTooLongFailTest() throws Exception {
        CreateAuthorRequestDto dto = new CreateAuthorRequestDto();

        StringBuilder sb = new StringBuilder();
        String name = "이름이름이름이름이름";
        for (int i = 0; i < 25; i++) {
            sb.append(name);
        }

        ReflectionTestUtils.setField(dto, "authorName", String.valueOf(sb));

        doNothing().when(authorService).createAuthor(dto);

        mockMvc.perform(RestDocumentationRequestBuilders.put(authPath + "/{authorNo}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("author-create-authorName-over-fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("authorNo").description("저자 번호")),
                        requestFields(
                                fieldWithPath("authorName").description("저자 이름"),
                                fieldWithPath("mainBook").description("대표작")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("저자 이름은 200자를 넘길 수 없습니다.")
                        )
                ));

        verify(authorService, times(0))
                .modifyAuthor(anyInt(), any(ModifyAuthorRequestDto.class));

    }

    @Test
    @DisplayName("저자 수정 성공")
    void modifyAuthorTest() throws Exception {
        ModifyAuthorRequestDto dto = new ModifyAuthorRequestDto();
        ReflectionTestUtils.setField(dto, "authorName", "변경이름");
        ReflectionTestUtils.setField(dto, "mainBook", "변경할 대표작");

        ArgumentCaptor<ModifyAuthorRequestDto> captor =
                ArgumentCaptor.forClass(ModifyAuthorRequestDto.class);

        doNothing().when(authorService).modifyAuthor(1, dto);

        mockMvc.perform(RestDocumentationRequestBuilders.put(authPath + "/{authorNo}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("author-modify",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("authorNo").description("저자 번호")),
                        requestFields(
                                fieldWithPath("authorName").description("저자 이름"),
                                fieldWithPath("mainBook").description("대표작")
                        )
                ));

        verify(authorService, times(1))
                .modifyAuthor(anyInt(), any(ModifyAuthorRequestDto.class));
    }

    @Test
    @DisplayName("저자 수정 Validation Exception 저자 수정 이름 없음")
    void modifyAuthorNameIsNullFailTest() throws Exception {
        ModifyAuthorRequestDto dto = new ModifyAuthorRequestDto();
        ReflectionTestUtils.setField(dto, "authorName", "");
        ReflectionTestUtils.setField(dto, "mainBook", "대표작");

        doNothing().when(authorService).modifyAuthor(1, dto);

        mockMvc.perform(RestDocumentationRequestBuilders.put(authPath + "/{authorNo}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("author-modify-authorName-null-fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("authorNo").description("저자 번호")),
                        requestFields(
                                fieldWithPath("authorName").description("저자 이름"),
                                fieldWithPath("mainBook").description("대표작")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("저자 이름은 필수 항목입니다.")
                        )
                ));

        verify(authorService, times(0))
                .modifyAuthor(anyInt(), any(ModifyAuthorRequestDto.class));

    }

    @Test
    @DisplayName("저자 수정 Validation Exception 저자 수정 이름 길이 초과")
    void modifyAuthorNameTooLongFailTest() throws Exception {
        ModifyAuthorRequestDto dto = new ModifyAuthorRequestDto();

        StringBuilder sb = new StringBuilder();
        String name = "이름이름이름이름이름";
        for (int i = 0; i < 25; i++) {
            sb.append(name);
        }

        ReflectionTestUtils.setField(dto, "authorName", String.valueOf(sb));
        ReflectionTestUtils.setField(dto, "mainBook", "대표작");

        doNothing().when(authorService).modifyAuthor(1, dto);

        mockMvc.perform(RestDocumentationRequestBuilders.put(authPath + "/{authorNo}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("author-modify-authorName-over-fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("authorNo").description("저자 번호")),
                        requestFields(
                                fieldWithPath("authorName").description("저자 이름"),
                                fieldWithPath("mainBook").description("대표작")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("저자 이름은 200자를 넘길 수 없습니다.")
                        )
                ));

        verify(authorService, times(0))
                .modifyAuthor(anyInt(), any(ModifyAuthorRequestDto.class));

    }

    @Test
    @DisplayName("저자 수정 Validation Exception 대표책 수정 이름 길이 초과")
    void modifyAuthorMainBookTooLongFailTest() throws Exception {
        ModifyAuthorRequestDto dto = new ModifyAuthorRequestDto();

        StringBuilder sb = new StringBuilder();
        String mainBook = "대표책대표책";
        for (int i = 0; i < 20; i++) {
            sb.append(mainBook);
        }

        ReflectionTestUtils.setField(dto, "authorName", "저자");
        ReflectionTestUtils.setField(dto, "mainBook", String.valueOf(sb));

        doNothing().when(authorService).modifyAuthor(1, dto);

        mockMvc.perform(RestDocumentationRequestBuilders.put(authPath + "/{authorNo}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("author-modify-mainBook-over-fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("authorNo").description("저자 번호")),
                        requestFields(
                                fieldWithPath("authorName").description("저자 이름"),
                                fieldWithPath("mainBook").description("대표작")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("대표작은 100글자를 넘을 수 없습니다.")
                        )
                ));

        verify(authorService, times(0))
                .modifyAuthor(anyInt(), any(ModifyAuthorRequestDto.class));

    }

    @Test
    @DisplayName("저자 조회 성공")
    void getAuthorsByPageTest() throws Exception {
        List<GetAuthorResponseDto> responses = new ArrayList<>();
        responses.add(responseDto);

        Pageable pageable = PageRequest.of(0, 10);
        Page<GetAuthorResponseDto> page = PageableExecutionUtils.getPage(responses, pageable,
                () -> 1L);

        when(authorService.getAuthorsByPage(pageable))
                .thenReturn(page);

        mockMvc.perform(get(authPath)
                        .param("page", objectMapper.writeValueAsString(pageable.getPageNumber()))
                        .param("size", objectMapper.writeValueAsString(pageable.getPageSize()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(page)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("author-findAll",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 사이즈")
                        ),
                        responseFields(
                                fieldWithPath("totalPages").description("총 페이지 개수"),
                                fieldWithPath("number").description("현재 페이지 번호"),
                                fieldWithPath("previous").description("이전 페이지 존재 여부"),
                                fieldWithPath("next").description("다음 페이지 존재 여부"),
                                fieldWithPath("content[].authorNo").description("저자 번호"),
                                fieldWithPath("content[].authorName").description("저자 이름"),
                                fieldWithPath("content[].mainBook").description("대표작")
                        )));

        verify(authorService,
                times(1))
                .getAuthorsByPage(pageable);
    }

    @Test
    @DisplayName("저자이름검색조회 성공")
    void getAuthorsByNameTest() throws Exception {
        List<GetAuthorResponseDto> responses = new ArrayList<>();
        responses.add(responseDto);

        when(authorService.getAuthorsByName(responseDto.getAuthorName()))
                .thenReturn(responses);

        mockMvc.perform(get(path + "/search?name=" + responseDto.getAuthorName())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(responses)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("author-getAuthor-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("name").description("검색할 저자 이름")
                        ),
                        responseFields(
                                fieldWithPath("[].authorNo").description("저자 번호"),
                                fieldWithPath("[].authorName").description("저자 이름"),
                                fieldWithPath("[].mainBook").description("대표작")
                        )));

        verify(authorService, times(1))
                .getAuthorsByName(responseDto.getAuthorName());
    }

}