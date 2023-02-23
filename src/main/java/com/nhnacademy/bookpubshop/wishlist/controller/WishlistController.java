package com.nhnacademy.bookpubshop.wishlist.controller;

import com.nhnacademy.bookpubshop.annotation.AdminAuth;
import com.nhnacademy.bookpubshop.annotation.MemberAuth;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import com.nhnacademy.bookpubshop.wishlist.dto.request.CreateWishlistRequestDto;
import com.nhnacademy.bookpubshop.wishlist.dto.request.DeleteWishlistRequestDto;
import com.nhnacademy.bookpubshop.wishlist.dto.request.ModifyWishlistAlarmRequestDto;
import com.nhnacademy.bookpubshop.wishlist.dto.response.GetWishlistCountResponseDto;
import com.nhnacademy.bookpubshop.wishlist.dto.response.GetWishlistResponseDto;
import com.nhnacademy.bookpubshop.wishlist.service.WishlistService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 위시리스트 Rest Controller.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    /**
     * 위시리스트 생성하는 API.
     *
     * @param memberNo 멤버 번호
     * @param request  생성을 위한 dto
     * @return 201
     */
    @MemberAuth
    @PostMapping("/token/members/{memberNo}/wishlist")
    public ResponseEntity<Void> createWishlist(
            @PathVariable("memberNo") Long memberNo,
            @Valid @RequestBody CreateWishlistRequestDto request) {
        wishlistService.createWishlist(memberNo, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 멤버별 위시리스트 목록 조회 API.
     *
     * @param memberNo 멤버 번호
     * @param pageable 페이징 정보
     * @return 페이징 처리된 위시리스트 목록
     */
    @MemberAuth
    @GetMapping("/token/members/{memberNo}/wishlist")
    public ResponseEntity<PageResponse<GetWishlistResponseDto>> getWishlistByMember(
            @PathVariable("memberNo") Long memberNo,
            Pageable pageable) {
        Page<GetWishlistResponseDto> content = wishlistService.getWishlist(pageable, memberNo);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PageResponse<>(content));
    }


    /**
     * 관리자 좋아요 카운트 조회 메서드.
     *
     * @param categoryNo 카테고리 번호
     * @param pageable   페이징 정보
     * @return 좋아요 카운트 반환.
     */
    @AdminAuth
    @GetMapping("/token/wishlist")
    public ResponseEntity<PageResponse<GetWishlistCountResponseDto>> getWishlistCount(
            @RequestParam(required = false) Integer categoryNo, Pageable pageable) {

        Page<GetWishlistCountResponseDto> content = wishlistService.getCountWishList(categoryNo,
                pageable);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PageResponse<>(content));

    }

    /**
     * 위시리스트 상품 삭제 API.
     *
     * @param memberNo 멤버 번호
     * @param request  상품 삭제를 위한 dto
     * @return 200
     */
    @MemberAuth
    @DeleteMapping("/token/members/{memberNo}/wishlist")
    public ResponseEntity<Void> deleteWishlist(@PathVariable("memberNo") Long memberNo,
                                               @Valid
                                               @RequestBody DeleteWishlistRequestDto request) {
        wishlistService.deleteWishlist(memberNo, request);

        return ResponseEntity.ok()
                .build();
    }

    /**
     * 위시리스트 상품 알림 여부 변경 API.
     *
     * @param memberNo 멤버 번호
     * @param request  수정을 위한 dto
     * @return 200
     */
    @MemberAuth
    @PutMapping("/token/members/{memberNo}/wishlist")
    public ResponseEntity<Void> modifyWishlistAlarm(@PathVariable("memberNo") Long memberNo,
                                                    @Valid @RequestBody
                                                    ModifyWishlistAlarmRequestDto request) {
        wishlistService.modifyWishlistAlarm(memberNo, request);

        return ResponseEntity.ok()
                .build();
    }
}
