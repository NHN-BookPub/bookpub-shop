package com.nhnacademy.bookpubshop.author.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookpubshop.author.dto.CreateAuthorRequestDto;
import com.nhnacademy.bookpubshop.author.dto.GetAuthorResponseDto;
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
 * 저자 컨트롤러 테스트입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@WebMvcTest(AuthorController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
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

    @BeforeEach
    void setUp() {
        author = AuthorDummy.dummy();
        objectMapper = new ObjectMapper();
        requestDto = new CreateAuthorRequestDto();
        responseDto = new GetAuthorResponseDto(
                author.getAuthorNo(),
                author.getAuthorName());
    }

    @Test
    @DisplayName("저자 등록 성공")
    void createAuthorTest() throws Exception {
        ReflectionTestUtils.setField(requestDto,
                "authorName",
                author.getAuthorName());

        ArgumentCaptor<CreateAuthorRequestDto> captor =
                ArgumentCaptor.forClass(CreateAuthorRequestDto.class);

        mockMvc.perform(post(path)
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());

        verify(authorService, times(1))
                .createAuthor(captor.capture());

        CreateAuthorRequestDto result = captor.getValue();

        assertThat(result.getAuthorName()).isEqualTo(requestDto.getAuthorName());
    }

    @Test
    @DisplayName("저자 조회 성공")
    void getAuthorsByPageTest() throws Exception {
        List<GetAuthorResponseDto> responses = new ArrayList<>();
        responses.add(responseDto);

        Pageable pageable = Pageable.ofSize(5);
        Page<GetAuthorResponseDto> page = PageableExecutionUtils.getPage(responses, pageable, () -> 1L);

        when(authorService.getAuthorsByPage(pageable))
                .thenReturn(page);

        mockMvc.perform(get(path + "?page=0&size=5")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(page)))
                .andExpect(status().isOk())
                .andDo(print());

        verify(authorService,
                times(1))
                .getAuthorsByPage(pageable);

        assertThat((long) authorService.getAuthorsByPage(pageable)
                .getContent().size())
                .isEqualTo(responses.size());
    }

    @Test
    @DisplayName("저자이름검색조회 성공")
    void getAuthorsByNameTest() throws Exception {
        List<GetAuthorResponseDto> responses = new ArrayList<>();
        responses.add(responseDto);

        when(authorService.getAuthorsByName(responseDto.getAuthorName()))
                .thenReturn(responses);

        mockMvc.perform(get(path + "/search/name?name=" + responseDto.getAuthorName())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(responses)))
                .andExpect(status().isOk())
                .andDo(print());

        verify(authorService, times(1))
                .getAuthorsByName(responseDto.getAuthorName());
    }

    @Test
    @DisplayName("상품번호로 저자조회 성공")
    void getAuthorsByProductNoTest() throws Exception {
        List<GetAuthorResponseDto> responses = new ArrayList<>();
        responses.add(responseDto);
        responses.add(new GetAuthorResponseDto(2, "남아들"));

        when(authorService.getAuthorsByProductNo(1L))
                .thenReturn(responses);

        mockMvc.perform(get(path + "/search/productNo?productNo=" + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(responses)))
                .andExpect(status().isOk())
                .andDo(print());

        verify(authorService, times(1))
                .getAuthorsByProductNo(1L);
    }
}