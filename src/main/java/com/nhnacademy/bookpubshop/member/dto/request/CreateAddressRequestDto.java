package com.nhnacademy.bookpubshop.member.dto.request;

import javax.validation.constraints.NotNull;
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
    @NotNull(message = "주소값은 비어있을 수 없습니다.")
    private String address;
    @NotNull(message = "상세주소는 비어있을 수 없습니다.")
    private String addressDetail;
}
