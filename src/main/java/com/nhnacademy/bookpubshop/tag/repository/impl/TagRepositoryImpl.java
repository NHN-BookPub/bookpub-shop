package com.nhnacademy.bookpubshop.tag.repository.impl;

import com.nhnacademy.bookpubshop.tag.dto.response.GetTagResponseDto;
import com.nhnacademy.bookpubshop.tag.entity.QTag;
import com.nhnacademy.bookpubshop.tag.entity.Tag;
import com.nhnacademy.bookpubshop.tag.repository.TagRepositoryCustom;
import com.querydsl.core.types.Projections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * TagRepositoryCustom 구현체.
 *
 * @author : 박경서
 * @since : 1.0
 **/
public class TagRepositoryImpl extends QuerydslRepositorySupport implements TagRepositoryCustom {

    public TagRepositoryImpl() {
        super(Tag.class);
    }

    /**
     * {@inheritDoc}
     *
     * @param tagName 태그 조회를 위한 태그 이름
     * @return 태그 entity
     */
    @Override
    public Optional<GetTagResponseDto> findTagName(String tagName) {
        QTag tag = QTag.tag;

        return Optional.of(
                from(tag)
                        .select(Projections
                                .constructor(GetTagResponseDto.class,
                                    tag.tagNo,
                                    tag.tagName,
                                    tag.colorCode))
                .where(tag.tagName.eq(tagName))
                .fetchOne());
    }

    /**
     * {@inheritDoc}
     *
     * @param tagNo 태그 조회를 위한 태그 번호
     * @return 태그 entity
     */
    @Override
    public Optional<GetTagResponseDto> findTag(Integer tagNo) {
        QTag tag = QTag.tag;

        return Optional.of(
                from(tag)
                        .select(Projections
                                .constructor(GetTagResponseDto.class,
                                        tag.tagNo,
                                        tag.tagName,
                                        tag.colorCode))
                .where(tag.tagNo.eq(tagNo))
                .fetchOne());
    }

    /**
     * {@inheritDoc}
     *
     * @return 전체 태그 리스트
     */
    @Override
    public List<GetTagResponseDto> findTags() {
        QTag tag = QTag.tag;

        return from(tag)
                .select(Projections
                        .constructor(GetTagResponseDto.class,
                                tag.tagNo,
                                tag.tagName,
                                tag.colorCode))
                .fetch();
    }

}
