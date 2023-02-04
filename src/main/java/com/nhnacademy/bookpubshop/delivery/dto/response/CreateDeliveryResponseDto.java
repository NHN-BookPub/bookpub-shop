package com.nhnacademy.bookpubshop.delivery.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 배송 정보값 uuid 가 반환됩니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateDeliveryResponseDto {
    private String invoiceNo;

}
