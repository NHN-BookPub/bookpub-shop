package com.nhnacademy.bookpubshop.purchase.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.nhnacademy.bookpubshop.product.entity.Product;
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
@Table(name = "purchase")
public class Purchase {
    @Id
    @Column(name = "purchase_number")
    private Long purchaseNo;

    @Column(name = "purchase_price")
    private Long purchasePrice;

    @Column(name = "purchase_created_at")
    private LocalDateTime createdAt;

    @Column(name = "purchase_amount")
    private Integer purchaseAmount;

    @ManyToOne
    @JoinColumn(name = "product_number")
    private Product productNo;
}
