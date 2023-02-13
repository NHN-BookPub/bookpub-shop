package com.nhnacademy.bookpubshop.servicecode.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 고객서비스 상태코드를 불러오기 위한 dto.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetCustomerServiceStateCodeResponseDto {
    private Integer serviceCodeNo;
    private String serviceCodeName;
    private boolean serviceCodeUsed;
    private String serviceCodeInfo;
}
