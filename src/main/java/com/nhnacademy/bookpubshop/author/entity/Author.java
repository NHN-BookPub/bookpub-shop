package com.nhnacademy.bookpubshop.author.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 저자(author) 테이블.
 *
 * @author : 박경서, 김서현
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "author")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_number")
    private Integer authorNo;

    @NotNull
    @Column(name = "author_name")
    private String authorName;

    @Column(name = "author_main_book")
    private String mainBook;


    /**
     * 저자 이름 변경하는 메서드.
     *
     * @param authorName 변경할 저자 이름
     */
    public void modifyAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
