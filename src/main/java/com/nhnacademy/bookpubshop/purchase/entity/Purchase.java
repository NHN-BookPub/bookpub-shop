package com.nhnacademy.bookpubshop.purchase.entity;

import com.nhnacademy.bookpubshop.product.entity.Product;
import java.time.LocalDateTime;
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
 * 매입이력(purchase) 테이블.
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_number", nullable = false, unique = true)
    private Long purchaseNo;

    @ManyToOne
    @JoinColumn(name = "product_number", nullable = false)
    private Product product;

    @Column(name = "purchase_price", nullable = false)
    private Long purchasePrice;

    @Column(name = "purchase_created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "purchase_amount", nullable = false)
    private Long purchaseAmount;

}
