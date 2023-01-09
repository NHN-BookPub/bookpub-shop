package com.nhnacademy.bookpubshop.author.repository;

import com.nhnacademy.bookpubshop.author.entity.Author;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 저자 레포지토리.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public interface AuthorRepository extends JpaRepository<Author, Integer>, AuthorRepositoryCustom {
    /**
     * 저자 이름으로 같은 이름의 저자를 반환하는 메서드입니다.
     *
     * @param name 저자 이름입니다.
     * @return 같은 이름의 모든 저자를 반환합니다.
     */
    List<Author> getAuthorByAuthorName(String name);
}