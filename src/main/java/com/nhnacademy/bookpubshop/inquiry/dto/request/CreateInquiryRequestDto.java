package com.nhnacademy.bookpubshop.inquiry.dto.request;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품문의를 등록하기 위해 사용되는 dto.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class CreateInquiryRequestDto {
    Long inquiryParentNo;

    @NotNull(message = "상품번호를 입력해주세요.")
    Long productNo;

    @NotNull(message = "문의코드번호를 입력해주세요.")
    Integer inquiryStateCodeNo;

    @NotBlank(message = "문의 제목을 입력해주세요.")
    @Size(max = 50, message = "문의 제목은 최대 50자입니다.")
    String inquiryTitle;

    @NotBlank(message = "문의 내용을 입력해주세요.")
    @Size(max = 1000, message = "문의 내용은 최대 1000자입니다.")
    String inquiryContent;

    @NotNull(message = "문의 공개 여부를 입력해주세요.")
    boolean inquiryDisplayed;

    List<String> imagePaths = new ArrayList<>();
}
