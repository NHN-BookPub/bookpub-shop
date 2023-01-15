package com.nhnacademy.bookpubshop.file.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Some description here.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class CreateFileRequestDto {
    private Long reviewNo;

    private Long personalInquiryNo;

    private Long templateNo;

    private Long productNo;

    private Integer serviceNo;

    @NotBlank(message = "파일 종류를 입력해주세요.")
    @Size(max = 20, message = "최대 20자까지 입력가능합니다.")
    private String fileCategory;

    @NotBlank(message = "파일 종류를 입력해주세요.")
    @Size(max = 255, message = "최대 255자까지 입력가능합니다.")
    private String filePath;

    @NotBlank(message = "파일 종류를 입력해주세요.")
    @Size(max = 10, message = "최대 10자까지 입력가능합니다.")
    private String fileExtension;

    @NotBlank(message = "파일 종류를 입력해주세요.")
    @Size(max = 255, message = "최대 255자까지 입력가능합니다.")
    private String nameOrigin;

    @NotBlank(message = "파일 종류를 입력해주세요.")
    @Size(max = 255, message = "최대 255자까지 입력가능합니다.")
    private String nameSaved;
}
