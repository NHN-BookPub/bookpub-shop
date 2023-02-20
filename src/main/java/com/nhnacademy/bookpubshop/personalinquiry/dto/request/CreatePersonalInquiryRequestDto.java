package com.nhnacademy.bookpubshop.personalinquiry.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 1대1문의를 생성하기 위해 필요한 정보를 담을 dto.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class CreatePersonalInquiryRequestDto {
    @NotNull(message = "회원 번호를 입력해주세요.")
    private Long memberNo;

    @NotBlank(message = "문의 제목를 입력해주세요.")
    @Size(max = 100, message = "문의제목은 100자를 넘길 수 없습니다")
    private String inquiryTitle;

    @NotBlank(message = "문의 내용을 입력해주세요.")
    @Size(max = 2000, message = "문의 내용은 2000자를 넘길 수 없습니다")
    private String inquiryContent;
}

