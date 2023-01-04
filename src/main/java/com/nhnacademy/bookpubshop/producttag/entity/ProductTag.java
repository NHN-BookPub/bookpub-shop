package com.nhnacademy.bookpubshop.producttag.entity;

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
@Table(name = "product_and_tag")
public class ProductTag {
    @EmbeddedId
    private Pk pk;

    @EqualsAndHashCode
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class Pk implements Serializable {
        @Column(name = "tag_name")
        private String tagName;

        @Column(name = "product_number")
        private Long productNo;
    }
}
