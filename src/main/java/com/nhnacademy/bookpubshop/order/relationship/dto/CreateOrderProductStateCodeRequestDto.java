package com.nhnacademy.bookpubshop.order.relationship.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 주문상품상태코드를 위한 Dto.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class CreateOrderProductStateCodeRequestDto {

    @NotBlank(message = "주문 상품 상태 코드명은 필수 입력사항입니다.")
    @Length(max = 20, message = "주문 상품 상태 코드명은 최대 20글자 가능합니다.")
    private String codeName;
    private boolean codeUsed;
    @Length(max = 100, message = "정보는 최대 100글자 가능합니다.")
    private String codeInfo;
}
