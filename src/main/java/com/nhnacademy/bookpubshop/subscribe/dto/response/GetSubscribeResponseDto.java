package com.nhnacademy.bookpubshop.subscribe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 구독정보를 반환하는 클래스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetSubscribeResponseDto {
    private Long subscribeNo;
    private String subscribeName;
    private Long price;
    private Long salePrice;
    private Integer salesRate;
    private Long viewCnt;
    private boolean deleted;
    private boolean renewed;
    private String imagePath;

}
