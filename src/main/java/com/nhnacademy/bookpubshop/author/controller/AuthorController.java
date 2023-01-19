package com.nhnacademy.bookpubshop.author.controller;

import com.nhnacademy.bookpubshop.author.dto.CreateAuthorRequestDto;
import com.nhnacademy.bookpubshop.author.dto.GetAuthorResponseDto;
import com.nhnacademy.bookpubshop.author.service.AuthorService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 저자 controller 입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authors")
public class AuthorController {
    private final AuthorService authorService;

    /**
     * 저자 생성 메서드입니다.
     *
     * @param requestDto 저자 이름을 가진 Dto입니다.
     * @return 등록한 저자가 반환됩니다.
     */
    @PostMapping
    public ResponseEntity<GetAuthorResponseDto> createAuthor(
            @RequestBody CreateAuthorRequestDto requestDto) {
        GetAuthorResponseDto author = authorService.createAuthor(requestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(author);
    }

    /**
     * 모든 저자를 page에 따라 반환하는 메서드입니다.
     *
     * @param pageable 페이징을 위해 pageable을 받습니다.
     * @return 모든 저자를 반환합니다.
     */
    @GetMapping()
    public ResponseEntity<Page<GetAuthorResponseDto>> getAuthorsByPage(Pageable pageable) {
        Page<GetAuthorResponseDto> authors =
                authorService.getAuthorsByPage(pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(authors);
    }

    /**
     * 저자 이름으로 검색한 모든 저자를 반환하는 메서드입니다.
     *
     * @param name 저자 이름입니다.
     * @return 같은 이름의 모든 저자를 반환합니다.
     */
    @GetMapping("/search/name")
    public ResponseEntity<List<GetAuthorResponseDto>> getAuthorsByName(@RequestParam String name) {
        List<GetAuthorResponseDto> authors = authorService.getAuthorsByName(name);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(authors);
    }

    /**
     * 상품 번호로 모든 저자를 검색하여 반환하는 메서드입니다.
     *
     * @param productNo 상품 번호입니다.
     * @return 같은 책에 대한 모든 저자를 반환합니다.
     */
    @GetMapping("/search/productNo")
    public ResponseEntity<List<GetAuthorResponseDto>> getAuthorsByProductNo(
            @RequestParam Long productNo) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(authorService.getAuthorsByProductNo(productNo));
    }
}