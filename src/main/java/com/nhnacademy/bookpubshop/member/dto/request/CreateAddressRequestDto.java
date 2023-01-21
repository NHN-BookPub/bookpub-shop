package com.nhnacademy.bookpubshop.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원의 주소를 생성하기위한 클래스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class CreateAddressRequestDto {
    private String address;
    private String addressDetail;
}
