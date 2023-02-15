package com.nhnacademy.bookpubshop.personalinquiryanswer.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 1대1문의답변 등록 시 필요한 정보를 담을 dto.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class CreatePersonalInquiryAnswerRequestDto {
    @NotNull(message = "1대1문의 질문 번호를 입력해주세요.")
    private Long personalInquiryNo;

    @NotBlank(message = "1대1문의 답변을 입력해주세요.")
    private String personalInquiryAnswerContent;
}
