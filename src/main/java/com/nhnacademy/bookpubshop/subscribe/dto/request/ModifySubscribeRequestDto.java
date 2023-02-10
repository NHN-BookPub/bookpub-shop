package com.nhnacademy.bookpubshop.subscribe.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 구독정보를 수정하기위한 클래스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class ModifySubscribeRequestDto {
    private String name;
    private Long price;
    private Long salePrice;
    private Integer saleRate;
    private boolean renewed;
}
