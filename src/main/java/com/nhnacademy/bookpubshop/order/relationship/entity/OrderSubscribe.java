package com.nhnacademy.bookpubshop.order.relationship.entity;

import com.nhnacademy.bookpubshop.subscribe.entity.Subscribe;
import com.nhnacademy.bookpubshop.subscribe.relationship.entity.OrderSubscribeStateCode;
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
 * 구독주문상품(order_and_subscribe) 테이블.
 *
 * @author : 박경서
 * @since : 1.0
 **/

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Entity
@Table(name = "order_and_subscribe")
public class OrderSubscribe {
    @Id
    @Column(name = "order_subscribe_number")
    private Long subscribeNo;

    @ManyToOne
    @JoinColumn(name = "subscribe_number")
    private Subscribe subscribe;

    @ManyToOne
    @JoinColumn(name = "order_subscribe_state_code_number")
    private OrderSubscribeStateCode orderSubscribeStateCode;

    @Column(name = "order_subscribe_coupon_amount")
    private Long couponAmount;

    @Column(name = "order_subscribe_product_price")
    private Long productPrice;

    @Column(name = "order_subscribe_finished_at")
    private LocalDateTime finishedAt;

    @Column(name = "order_subscribe_created_at")
    private LocalDateTime createdAt;
}
