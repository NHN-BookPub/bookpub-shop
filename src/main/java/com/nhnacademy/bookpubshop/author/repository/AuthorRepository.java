package com.nhnacademy.bookpubshop.author.repository;

import com.nhnacademy.bookpubshop.author.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 저자 레포지토리.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public interface AuthorRepository
        extends JpaRepository<Author, Integer>, AuthorRepositoryCustom {

}