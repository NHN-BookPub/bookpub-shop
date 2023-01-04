package com.nhnacademy.bookpubshop.product.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
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
@Table
public class ProductCategory {
    @EmbeddedId
    private Pk pk;

    @EqualsAndHashCode
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class Pk implements Serializable {
        @Column(name = "category_number")
        private Integer categoryNo;

        @Column(name = "product_number")
        private Long productNo;
    }
}
