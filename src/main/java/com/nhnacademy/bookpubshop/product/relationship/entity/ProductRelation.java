package com.nhnacademy.bookpubshop.product.relationship.entity;

import com.nhnacademy.bookpubshop.product.entity.Product;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import lombok.ToString;


/**
 * 연관상품(product_relation) 테이블.
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_relation_number", nullable = false, unique = true)
    private Long productRelationNo;

    @ManyToOne
    @JoinColumn(name = "product_number", nullable = false)
    private Product product;

    @Column(name = "product_relation_deleted", nullable = false)
    private boolean relationDeleted;

}
