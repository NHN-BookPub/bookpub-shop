package com.nhnacademy.bookpubshop.tag.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Some description here.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "tag")
public class Tag {
    @Id
    @Column(name = "tag_name")
    private String tagName;

    @Column(name = "tag_color_code")
    private String colorCode;
}
