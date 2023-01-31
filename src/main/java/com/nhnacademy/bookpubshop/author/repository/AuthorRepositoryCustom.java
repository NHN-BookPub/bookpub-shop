package com.nhnacademy.bookpubshop.author.repository;

import com.nhnacademy.bookpubshop.author.dto.response.GetAuthorResponseDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 쿼리 dsl 하기 위한 위한 custom 레포지토리입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@NoRepositoryBean
public interface AuthorRepositoryCustom {
    /**
     * 저자 목록을 페이징처리하여 보여주는 메서드입니다.
     *
     * @param pageable pagealbe 객체를 받습니다.
     * @return 저자 리스트를 반한합니다.
     */
    Page<GetAuthorResponseDto> getAuthorsByPage(Pageable pageable);

    /**
     * 상품 번호로 모든 저자를 반환하는 메서드입니다.
     *
     * @param productNo 상품 번호입니다.
     * @return 한 상품에 대한 모든 저자를 반환합니다.
     */
    List<GetAuthorResponseDto> getAuthorsByProductNo(Long productNo);

    /**
     * 저자 이름으로 저자들을 반환하는 메서드입니다.
     *
     * @param name 저자 이름입니다.
     * @return 같은 이름의 저자 리스트를 반환합니다.
     */
    List<GetAuthorResponseDto> getAuthorByName(String name);
}
