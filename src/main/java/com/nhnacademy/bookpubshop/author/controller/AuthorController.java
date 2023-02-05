package com.nhnacademy.bookpubshop.author.controller;

import com.nhnacademy.bookpubshop.annotation.AdminAuth;
import com.nhnacademy.bookpubshop.author.dto.request.CreateAuthorRequestDto;
import com.nhnacademy.bookpubshop.author.dto.request.ModifyAuthorRequestDto;
import com.nhnacademy.bookpubshop.author.dto.response.GetAuthorResponseDto;
import com.nhnacademy.bookpubshop.author.service.AuthorService;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 저자 controller 입니다.
 *
 * @author : 여운석, 박경서, 김서현
 * @since : 1.0
 **/
@RestController
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    /**
     * 저자 생성 메서드입니다.
     *
     * @param requestDto 저자 이름을 가진 Dto입니다.
     */
    @AdminAuth
    @PostMapping("/token/authors")
    public ResponseEntity<Void> createAuthor(
            @Valid @RequestBody CreateAuthorRequestDto requestDto) {
        authorService.createAuthor(requestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON).build();
    }

    /**
     * 저자 정보를 변경하는 컨트롤러.
     *
     * @param authorNo 저자 번호
     * @param dto      modify DTO
     * @return 200
     */
    @AdminAuth
    @PutMapping("/token/authors/{authorNo}")
    public ResponseEntity<Void> modifyAuthor(@PathVariable("authorNo") Integer authorNo,
                                             @Valid @RequestBody ModifyAuthorRequestDto dto) {
        authorService.modifyAuthor(authorNo, dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON).build();
    }


    /**
     * 모든 저자를 page에 따라 반환하는 메서드입니다.
     *
     * @param pageable 페이징을 위해 pageable을 받습니다.
     * @return 모든 저자를 반환합니다.
     */
    @AdminAuth
    @GetMapping("/token/authors")
    public ResponseEntity<PageResponse<GetAuthorResponseDto>> getAuthorsByPage(Pageable pageable) {
        Page<GetAuthorResponseDto> authors =
                authorService.getAuthorsByPage(pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PageResponse<>(authors));
    }

    /**
     * 저자 이름으로 검색한 모든 저자를 반환하는 메서드입니다.
     *
     * @param name 저자 이름입니다.
     * @return 같은 이름의 모든 저자를 반환합니다.
     */
    @GetMapping("/api/authors/search")
    public ResponseEntity<List<GetAuthorResponseDto>> getAuthorsByName(@RequestParam String name) {
        List<GetAuthorResponseDto> authors = authorService.getAuthorsByName(name);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(authors);
    }

}