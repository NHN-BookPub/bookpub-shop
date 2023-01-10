package com.nhnacademy.bookpubshop.tag.repository;

import com.nhnacademy.bookpubshop.tag.dto.response.GetTagResponseDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Tag 테이블 Querydsl 사용하기 위한 인터페이.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@NoRepositoryBean
public interface TagRepositoryCustom {

    /**
     * 태그 이름을 통해 태그를 조회하는 메서드.
     *
     * @param tagName 태그 조회를 위한 태그 이름
     * @return 태그 entity
     */
    Optional<GetTagResponseDto> findTagName(String tagName);

    /**
     * 태그 번호를 통해 태그를 조회하는 메서드.
     *
     * @param tagNo 태그 조회를 위한 태그 번호
     * @return 태그 entity
     */
    Optional<GetTagResponseDto> findTag(Integer tagNo);

    /**
     * 전체 태그 리스트를 조회하는 메서드.
     *
     * @return 전체 태그 리스트
     */
    List<GetTagResponseDto> findTags();
}
