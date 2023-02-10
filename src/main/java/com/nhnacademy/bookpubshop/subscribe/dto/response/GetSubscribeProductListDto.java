package com.nhnacademy.bookpubshop.subscribe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 구독안에 연관상품의 정보를 받기위한 dto 입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetSubscribeProductListDto {
    private Long productNo;
    private String title;
    private String filePath;
}
