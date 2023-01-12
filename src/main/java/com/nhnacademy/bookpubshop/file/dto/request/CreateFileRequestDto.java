package com.nhnacademy.bookpubshop.file.dto.request;

import javax.validation.constraints.NotNull;
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

    @NotNull
    @Size(max = 20)
    private String fileCategory;

    @NotNull
    @Size(max = 255)
    private String filePath;

    @NotNull
    @Size(max = 10)
    private String fileExtension;

    @NotNull
    @Size(max = 255)
    private String nameOrigin;

    @NotNull
    @Size(max = 255)
    private String nameSaved;
}
