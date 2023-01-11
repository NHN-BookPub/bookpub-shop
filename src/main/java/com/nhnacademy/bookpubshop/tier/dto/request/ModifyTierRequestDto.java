package com.nhnacademy.bookpubshop.tier.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 등급의 수정을위한 Dto 클래스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class ModifyTierRequestDto {
    @NotNull(message = "등급번호는 필수값입니다.")
    private Integer tierNo;
    @Length(max = 10, message = "등급명의 길이가 맞지않습니다.")
    @NotBlank(message = "수정할 등급명을 기입해주세요")
    private String tierName;
}
