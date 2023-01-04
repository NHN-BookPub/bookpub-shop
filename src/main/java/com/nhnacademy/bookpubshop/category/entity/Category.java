package com.nhnacademy.bookpubshop.category.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Entity
@Table(name = "category")
public class Category {
    @Id
    @Column(name = "category_number")
    private Integer categoryNo;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "category_priority")
    private Integer categoryPriority;

    @Column(name = "category_displayed")
    private boolean categoryDisplayed;

    @ManyToOne
    @JoinColumn(name = "category_number")
    private Category parentNo;
}
