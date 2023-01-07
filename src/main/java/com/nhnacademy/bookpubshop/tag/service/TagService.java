package com.nhnacademy.bookpubshop.tag.service;

import com.nhnacademy.bookpubshop.tag.dto.request.AddTagRequestDto;
import com.nhnacademy.bookpubshop.tag.dto.request.ModifyTagRequestDto;
import com.nhnacademy.bookpubshop.tag.dto.response.GetTagResponseDto;
import java.util.List;

/**
 * Tag 서비스를 위한 인터페이스.
 *
 * @author : 박경서
 * @since : 1.0
 **/
public interface TagService {

    /**
     * 단건 태그를 얻기 위한 메서드.
     *
     * @param tagNo 태그 번호
     * @return 태그 정보
     */
    GetTagResponseDto getTag(Integer tagNo);

    /**
     * 전체 태그 리스트를 얻기 위한 메서드.
     *
     * @return 전체 태그 리스트
     */
    List<GetTagResponseDto> getTags();

    /**
     * 태그 생성을 위한 메서드.
     *
     * @param request 태그 생성을 위한 정보
     */
    void addTag(AddTagRequestDto request);

    /**
     * 태그 수정을 위한 메서드.
     *
     * @param request 태그 수정을 위한 정보
     */
    void modifyTagInformation(ModifyTagRequestDto request);

    /**
     * 태그 삭제를 위한 메서드.
     *
     * @param tagNo 태그 번호
     */
    void deleteTagByTagNumber(Integer tagNo);
}
