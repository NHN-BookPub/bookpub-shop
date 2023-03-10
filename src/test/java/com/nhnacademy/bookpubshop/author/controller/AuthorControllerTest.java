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
 * ?????? ???????????? ??????????????????.
 *
 * @author : ?????????, ?????????, ?????????
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
        responseDto = new GetAuthorResponseDto(1, "?????????", "??????");
    }

    @Test
    @DisplayName("?????? ?????? ??????")
    void createAuthorTest() throws Exception {
        ReflectionTestUtils.setField(requestDto,
                "authorName",
                "???????????????");

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
                                fieldWithPath("authorName").description("?????? ??????"),
                                fieldWithPath("mainBook").description("?????????")
                        )));

        verify(authorService, times(1))
                .createAuthor(any(CreateAuthorRequestDto.class));
    }

    @Test
    @DisplayName("?????? ?????? Validation Exception ?????? ?????? ?????? ??????")
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
                                parameterWithName("authorNo").description("?????? ??????")),
                        requestFields(
                                fieldWithPath("authorName").description("?????? ??????"),
                                fieldWithPath("mainBook").description("?????????")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("?????? ????????? ?????? ???????????????.")
                        )
                ));

        verify(authorService, times(0))
                .modifyAuthor(anyInt(), any(ModifyAuthorRequestDto.class));

    }

    @Test
    @DisplayName("?????? ?????? Validation Exception ?????? ?????? ?????? ?????? ??????")
    void createAuthorNameTooLongFailTest() throws Exception {
        CreateAuthorRequestDto dto = new CreateAuthorRequestDto();

        StringBuilder sb = new StringBuilder();
        String name = "??????????????????????????????";
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
                                parameterWithName("authorNo").description("?????? ??????")),
                        requestFields(
                                fieldWithPath("authorName").description("?????? ??????"),
                                fieldWithPath("mainBook").description("?????????")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("?????? ????????? 200?????? ?????? ??? ????????????.")
                        )
                ));

        verify(authorService, times(0))
                .modifyAuthor(anyInt(), any(ModifyAuthorRequestDto.class));

    }

    @Test
    @DisplayName("?????? ?????? ??????")
    void modifyAuthorTest() throws Exception {
        ModifyAuthorRequestDto dto = new ModifyAuthorRequestDto();
        ReflectionTestUtils.setField(dto, "authorName", "????????????");
        ReflectionTestUtils.setField(dto, "mainBook", "????????? ?????????");

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
                                parameterWithName("authorNo").description("?????? ??????")),
                        requestFields(
                                fieldWithPath("authorName").description("?????? ??????"),
                                fieldWithPath("mainBook").description("?????????")
                        )
                ));

        verify(authorService, times(1))
                .modifyAuthor(anyInt(), any(ModifyAuthorRequestDto.class));
    }

    @Test
    @DisplayName("?????? ?????? Validation Exception ?????? ?????? ?????? ??????")
    void modifyAuthorNameIsNullFailTest() throws Exception {
        ModifyAuthorRequestDto dto = new ModifyAuthorRequestDto();
        ReflectionTestUtils.setField(dto, "authorName", "");
        ReflectionTestUtils.setField(dto, "mainBook", "?????????");

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
                                parameterWithName("authorNo").description("?????? ??????")),
                        requestFields(
                                fieldWithPath("authorName").description("?????? ??????"),
                                fieldWithPath("mainBook").description("?????????")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("?????? ????????? ?????? ???????????????.")
                        )
                ));

        verify(authorService, times(0))
                .modifyAuthor(anyInt(), any(ModifyAuthorRequestDto.class));

    }

    @Test
    @DisplayName("?????? ?????? Validation Exception ?????? ?????? ?????? ?????? ??????")
    void modifyAuthorNameTooLongFailTest() throws Exception {
        ModifyAuthorRequestDto dto = new ModifyAuthorRequestDto();

        StringBuilder sb = new StringBuilder();
        String name = "??????????????????????????????";
        for (int i = 0; i < 25; i++) {
            sb.append(name);
        }

        ReflectionTestUtils.setField(dto, "authorName", String.valueOf(sb));
        ReflectionTestUtils.setField(dto, "mainBook", "?????????");

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
                                parameterWithName("authorNo").description("?????? ??????")),
                        requestFields(
                                fieldWithPath("authorName").description("?????? ??????"),
                                fieldWithPath("mainBook").description("?????????")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("?????? ????????? 200?????? ?????? ??? ????????????.")
                        )
                ));

        verify(authorService, times(0))
                .modifyAuthor(anyInt(), any(ModifyAuthorRequestDto.class));

    }

    @Test
    @DisplayName("?????? ?????? Validation Exception ????????? ?????? ?????? ?????? ??????")
    void modifyAuthorMainBookTooLongFailTest() throws Exception {
        ModifyAuthorRequestDto dto = new ModifyAuthorRequestDto();

        StringBuilder sb = new StringBuilder();
        String mainBook = "??????????????????";
        for (int i = 0; i < 20; i++) {
            sb.append(mainBook);
        }

        ReflectionTestUtils.setField(dto, "authorName", "??????");
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
                                parameterWithName("authorNo").description("?????? ??????")),
                        requestFields(
                                fieldWithPath("authorName").description("?????? ??????"),
                                fieldWithPath("mainBook").description("?????????")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("???????????? 100????????? ?????? ??? ????????????.")
                        )
                ));

        verify(authorService, times(0))
                .modifyAuthor(anyInt(), any(ModifyAuthorRequestDto.class));

    }

    @Test
    @DisplayName("?????? ?????? ??????")
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
                                parameterWithName("page").description("????????? ??????"),
                                parameterWithName("size").description("????????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("totalPages").description("??? ????????? ??????"),
                                fieldWithPath("number").description("?????? ????????? ??????"),
                                fieldWithPath("previous").description("?????? ????????? ?????? ??????"),
                                fieldWithPath("next").description("?????? ????????? ?????? ??????"),
                                fieldWithPath("content[].authorNo").description("?????? ??????"),
                                fieldWithPath("content[].authorName").description("?????? ??????"),
                                fieldWithPath("content[].mainBook").description("?????????")
                        )));

        verify(authorService,
                times(1))
                .getAuthorsByPage(pageable);
    }

    @Test
    @DisplayName("???????????????????????? ??????")
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
                                parameterWithName("name").description("????????? ?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("[].authorNo").description("?????? ??????"),
                                fieldWithPath("[].authorName").description("?????? ??????"),
                                fieldWithPath("[].mainBook").description("?????????")
                        )));

        verify(authorService, times(1))
                .getAuthorsByName(responseDto.getAuthorName());
    }

}