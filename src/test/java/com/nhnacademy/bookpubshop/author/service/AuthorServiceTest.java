package com.nhnacademy.bookpubshop.author.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;
import com.nhnacademy.bookpubshop.author.dto.request.CreateAuthorRequestDto;
import com.nhnacademy.bookpubshop.author.dto.request.ModifyAuthorRequestDto;
import com.nhnacademy.bookpubshop.author.dto.response.GetAuthorResponseDto;
import com.nhnacademy.bookpubshop.author.entity.Author;
import com.nhnacademy.bookpubshop.author.exception.NotFoundAuthorException;
import com.nhnacademy.bookpubshop.author.repository.AuthorRepository;
import com.nhnacademy.bookpubshop.author.service.impl.AuthorServiceImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Author 서비스 테스트.
 *
 * @author : 여운석
 * @since : 1.0
 **/

class AuthorServiceTest {
    AuthorService authorService;
    AuthorRepository authorRepository;
    Author author;
    CreateAuthorRequestDto requestDto;
    GetAuthorResponseDto responseDto;
    ArgumentCaptor<Author> captor;


    @BeforeEach
    void setUp() {
        authorRepository = Mockito.mock(AuthorRepository.class);
        authorService = new AuthorServiceImpl(authorRepository);

        author = new Author(1, "남기준", "해리포터");

        requestDto = new CreateAuthorRequestDto();
        ReflectionTestUtils.setField(requestDto, "authorName", "남기준");
        ReflectionTestUtils.setField(requestDto, "mainBook", "메인책");

        responseDto = new GetAuthorResponseDto(1, "남기준", "해리포터");
        captor = ArgumentCaptor.forClass(Author.class);
    }

    @Test
    @DisplayName("저자 등록 성공")
    void createAuthor() {

        // when
        when(authorRepository.save(any()))
                .thenReturn(author);

        // then
        authorService.createAuthor(requestDto);

        verify(authorRepository, times(1)).save(captor.capture());
    }

    @Test
    @DisplayName("저자 수정 성공")
    void modifyAuthor() {
        // given
        ModifyAuthorRequestDto modifyDto = new ModifyAuthorRequestDto();
        ReflectionTestUtils.setField(modifyDto, "authorName", "경서바보");
        ReflectionTestUtils.setField(modifyDto, "mainBook", "하이루");

        // when
        when(authorRepository.findById(anyInt())).thenReturn(Optional.of(author));

        // then
        authorService.modifyAuthor(anyInt(), modifyDto);

        verify(authorRepository, times(1)).findById(anyInt());

    }


    @Test
    @DisplayName("저자 전체 조회 성공")
    void getAuthorsByPage() {
        List<GetAuthorResponseDto> responses = new ArrayList<>();
        responses.add(responseDto);

        Pageable pageable = Pageable.ofSize(5);
        Page<GetAuthorResponseDto> page =
                PageableExecutionUtils.getPage(responses, pageable, () -> 1L);

        when(authorRepository.getAuthorsByPage(pageable))
                .thenReturn(page);

        assertThat(authorService.getAuthorsByPage(pageable))
                .isEqualTo(page);
        assertThat(authorService.getAuthorsByPage(pageable).getContent().get(0).getAuthorName())
                .isEqualTo(responses.get(0).getAuthorName());
    }

    @Test
    @DisplayName("저자 이름으로 조회 성공")
    void getAuthorsByName() {
        List<GetAuthorResponseDto> responses = new ArrayList<>();
        responses.add(responseDto);

        when(authorRepository.getAuthorByName("남기준"))
                .thenReturn(responses);

        assertThat(authorService
                .getAuthorsByName(responseDto.getAuthorName())
                .get(0).getAuthorName())
                .isEqualTo(responseDto.getAuthorName());
    }

    @Test
    @DisplayName("상품번호로 저자 조회 성공")
    void getAuthorsByProductNo() {
        List<GetAuthorResponseDto> responses = new ArrayList<>();
        responses.add(responseDto);

        when(authorRepository.getAuthorsByProductNo(1L))
                .thenReturn(responses);

        assertThat(authorService
                .getAuthorsByProductNo(1L)
                .get(0).getAuthorName())
                .isEqualTo(responseDto.getAuthorName());
    }

    @Test
    @DisplayName("저자 이름으로 조회시 결과 없을 경우")
    void getAuthorsByNameFailed() {
        List<GetAuthorResponseDto> responses = new ArrayList<>();
        responses.add(responseDto);

        when(authorRepository.getAuthorByName(requestDto.getAuthorName()))
                .thenReturn(Collections.EMPTY_LIST);

        assertThatThrownBy(() -> authorService
                .getAuthorsByName(requestDto.getAuthorName()))
                .isInstanceOf(NotFoundAuthorException.class);
    }

    @Test
    @DisplayName("상품 번호로 저자 조회시 결과 없는 경우")
    void getAuthorsByProductNoFailed() {
        List<GetAuthorResponseDto> responses = new ArrayList<>();
        responses.add(responseDto);

        when(authorRepository.getAuthorsByProductNo(123L))
                .thenReturn(Collections.EMPTY_LIST);

        assertThatThrownBy(() -> authorService
                .getAuthorsByProductNo(123L))
                .isInstanceOf(NotFoundAuthorException.class);
    }
}