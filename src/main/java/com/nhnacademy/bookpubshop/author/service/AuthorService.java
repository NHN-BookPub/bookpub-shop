package com.nhnacademy.bookpubshop.author.service;

import com.nhnacademy.bookpubshop.author.dto.request.CreateAuthorRequestDto;
import com.nhnacademy.bookpubshop.author.dto.request.ModifyAuthorRequestDto;
import com.nhnacademy.bookpubshop.author.dto.response.GetAuthorResponseDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 저자 service입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public interface AuthorService {
    /**
     * 저자를 생성하는 메서드입니다.
     *
     * @param author 저자를 받습니다.
     */
    void createAuthor(CreateAuthorRequestDto author);

    /**
     * 저자를 수정하는 서비스.
     *
     * @param authorNo 저자 번호
     * @param dto      수정할 저자
     */
    void modifyAuthor(Integer authorNo, ModifyAuthorRequestDto dto);

    /**
     * 모든 저자를 반환하는 메서드입니다.
     *
     * @param pageable 페이징을 위해 받습니다.
     * @return 모든 저자를 페이지에 따라 반환합니다.
     */
    Page<GetAuthorResponseDto> getAuthorsByPage(Pageable pageable);

    /**
     * 같은 이름의 모든 저자를 반환하는 메서드입니다.
     *
     * @param name 저자 이름입니다.
     * @return 같은 이름의 모든 저자를 반환합니다.
     */
    List<GetAuthorResponseDto> getAuthorsByName(String name);

    /**
     * 상품 번호로 모든 저자를 반환하는 메서드입니다.
     *
     * @param productNo 상품 제목입니다.
     * @return 한 상품의 모든 저자를 반환합니다.
     */
    List<GetAuthorResponseDto> getAuthorsByProductNo(Long productNo);
}
