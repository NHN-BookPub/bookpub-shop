package com.nhnacademy.bookpubshop.filemanager.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 파일 다운로드를 위한 정보를 담은 Dto입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetDownloadInfo {
    private String path;
    private String token;
    private String nameOrigin;
    private String nameSaved;
    private String fileExtension;
}
