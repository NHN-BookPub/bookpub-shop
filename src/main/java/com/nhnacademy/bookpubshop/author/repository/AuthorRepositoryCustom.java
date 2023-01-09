package com.nhnacademy.bookpubshop.author.repository;

import com.nhnacademy.bookpubshop.author.dto.GetAuthorResponseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 쿼리dsl을 위한 custom 레포지토리입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@NoRepositoryBean
public interface AuthorRepositoryCustom {
    List<GetAuthorResponseDto> getAuthorsByPage(Pageable pageable);
    /**
     * 상품 번호로 모든 저자를 반환하는 메서드입니다.
     *
     * @param productNo 상품 번호입니다.
     * @return 한 상품에 대한 모든 저자를 반환합니다.
     */
    List<GetAuthorResponseDto> getAuthorsByProductNo(Long productNo);
}
