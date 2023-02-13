package com.nhnacademy.bookpubshop.wishlist.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 재입고 알람을 설정한 사용자를 받는 dto.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetAppliedMemberResponseDto {
    private Long memberNo;
    private String memberNickname;
    private String memberPhone;
    private Long productNo;
    private String title;
}
