package com.nhnacademy.bookpubshop.author.entity;

import com.nhnacademy.bookpubshop.author.dto.request.ModifyAuthorRequestDto;
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
     * 저자 정보를 수정하는 메소드.
     *
     * @param dto 저자 수정 DTO.
     */
    public void modifyAuthorInfo(ModifyAuthorRequestDto dto) {
        this.authorName = dto.getAuthorName();
        this.mainBook = dto.getMainBook();
    }
}
