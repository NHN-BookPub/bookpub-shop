package com.nhnacademy.bookpubshop.tag.repository;

import com.nhnacademy.bookpubshop.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 태그(Tag) 테이블 레포지토리.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Repository
public interface TagRepository extends JpaRepository<Tag, Integer>, TagRepositoryCustom {

    boolean existsByTagName(String tagName);
}