package com.nhnacademy.bookpubshop.wishlist.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 좋아요 현황 조회를 위한 dto.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class GetWishlistCountResponseDto {

    private Long productNo;
    private String title;
    private Long wishCount;

}
