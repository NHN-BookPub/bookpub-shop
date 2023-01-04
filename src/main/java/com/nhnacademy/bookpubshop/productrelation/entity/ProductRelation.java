package com.nhnacademy.bookpubshop.productrelation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.nhnacademy.bookpubshop.product.entity.Product;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.nhnacademy.bookpubshop.product.entity.Product;

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
@Table(name = "product_relation")
public class ProductRelation {
    @Id
    @Column(name = "product_relation_number")
    private Long productRelationNo;

    @Column(name = "product_relation_deleted")
    private boolean relationDeleted;

    @ManyToOne
    @JoinColumn(name = "product_number")
    private Product productNo;
}
