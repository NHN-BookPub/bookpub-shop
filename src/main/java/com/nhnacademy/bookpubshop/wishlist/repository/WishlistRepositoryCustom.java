package com.nhnacademy.bookpubshop.wishlist.repository;

import com.nhnacademy.bookpubshop.wishlist.dto.response.GetWishlistResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 커스텀 위시리스트 레포지토리.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@NoRepositoryBean
public interface WishlistRepositoryCustom {

    /**
     * 위시리스트를 페이징 단위로 조회하는 메서드.
     *
     * @param pageable 페이징 정보
     * @param memberNo 멤버 번호
     * @return 페이징 처리가 된 위시리스트 정보
     */
    Page<GetWishlistResponseDto> findWishlistByMember(Pageable pageable, Long memberNo);
}
