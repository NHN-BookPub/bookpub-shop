package com.nhnacademy.bookpubshop.category.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 카테고리(category) 테이블.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_number")
    private Integer categoryNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_parent_number")
    private Category parentCategory;

    @Column(name = "category_name", unique = true)
    @Length(min = 1, max = 10)
    private String categoryName;

    @Column(name = "category_priority")
    private Integer categoryPriority;

    @Column(name = "category_displayed")
    private boolean categoryDisplayed;


    /**
     * 카테고리명 수정 시 사용되는 메소드입니다.
     *
     * @param categoryName      카테고리 이름
     * @param parentCategory    부모 카테고리
     * @param categoryPriority  카테고리 우선순위
     * @param categoryDisplayed 카테고리 노출여부
     * @author : 김서현
     */
    public void modifyCategory(String categoryName, Category parentCategory,
                               Integer categoryPriority, boolean categoryDisplayed) {
        this.categoryName = categoryName;
        this.parentCategory = parentCategory;
        this.categoryPriority = categoryPriority;
        this.categoryDisplayed = categoryDisplayed;
    }

}
