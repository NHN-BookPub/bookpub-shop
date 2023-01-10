package com.nhnacademy.bookpubshop.tag.entity;

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
 * 태그(tag) 테이블.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_number")
    private Integer tagNo;

    @Column(name = "tag_name", unique = true)
    @NotNull
    private String tagName;

    @Column(name = "tag_color_code")
    @NotNull
    private String colorCode;

    public void modifyTagInfo(String tagName, String colorCode) {
        this.tagName = tagName;
        this.colorCode = colorCode;
    }

}
