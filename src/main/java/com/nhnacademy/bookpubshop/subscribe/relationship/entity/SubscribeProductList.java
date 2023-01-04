package com.nhnacademy.bookpubshop.subscribe.relationship.entity;

import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.subscribe.entity.Subscribe;
import java.time.LocalDateTime;
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
 * 구독상품리스트(subscribe_product_list) 테이블.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Entity
@Table(name = "subscribe_product_list")
public class SubscribeProductList {
    @Id
    @Column(name = "subscribe_product_list_number")
    private Long listNumber;

    @Column(name = "subscribe_product_list_publish")
    private LocalDateTime listPublish;

    @ManyToOne
    @JoinColumn(name = "subscribe_number")
    private Subscribe subscribe;

    @ManyToOne
    @JoinColumn(name = "product_number")
    private Product product;
}