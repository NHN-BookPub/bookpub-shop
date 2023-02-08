package com.nhnacademy.bookpubshop.subscribe.dto.request;

import java.time.LocalDateTime;
import lombok.Getter;

/**
 * 구독 연관상품을 추가하기위한 Dto 입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Getter
public class CreateRelationProductRequestDto {
    private Long productNo;
    private LocalDateTime finishedAt;
}
