package com.nhnacademy.bookpubshop.paymenttypestatecode.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 결제유형 정보를 담고있는 responseDto.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetPaymentTypeResponseDto {
    private Integer codeNo;
    private String codeName;
    private boolean codeUsed;
    private String codeInfo;
}
