package com.nhnacademy.bookpubshop.wishlist.service;

import com.nhnacademy.bookpubshop.wishlist.dto.request.CreateWishlistRequestDto;
import com.nhnacademy.bookpubshop.wishlist.dto.request.DeleteWishlistRequestDto;
import com.nhnacademy.bookpubshop.wishlist.dto.request.ModifyWishlistAlarmRequestDto;
import com.nhnacademy.bookpubshop.wishlist.dto.response.GetWishlistCountResponseDto;
import com.nhnacademy.bookpubshop.wishlist.dto.response.GetWishlistResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 위시리스트 서비스 인터페이스.
 *
 * @author : 박경서
 * @since : 1.0
 **/
public interface WishlistService {

    /**
     * 위시리스트 생성 메서드.
     *
     * @param memberNo 멤버 번호
     * @param request  생성을 위한 dto
     */
    void createWishlist(Long memberNo, CreateWishlistRequestDto request);

    /**
     * 멤버별 위시리스트를 조회하는 메서드.
     *
     * @param pageable 페이징 정보
     * @param memberNo 멤버 번호
     * @return 페이징 처리가 된 위시리스트 정보
     */
    Page<GetWishlistResponseDto> getWishlist(Pageable pageable, Long memberNo);

    /**
     * 위시리스트에서 상품을 삭제하는 메서드.
     *
     * @param memberNo 멤버 번호
     * @param request  삭제를 위한 dto
     */
    void deleteWishlist(Long memberNo, DeleteWishlistRequestDto request);

    /**
     * 상품 알림 여부를 수정하는 메서드.
     *
     * @param memberNo 멤버 번호
     * @param request  수정을 위한 dto
     */
    void modifyWishlistAlarm(Long memberNo, ModifyWishlistAlarmRequestDto request);

    /**
     * 좋아요 카운트 조회하는 메서드.
     *
     * @param categoryNo 카테고리 번호
     * @param pageable   페이징 정보
     * @return 좋아요 카운트 반환.
     */
    Page<GetWishlistCountResponseDto> getCountWishList(Integer categoryNo, Pageable pageable);
}
