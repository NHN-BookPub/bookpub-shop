package com.nhnacademy.bookpubshop.order.relationship.entity;

import com.nhnacademy.bookpubshop.base.BaseCreateTimeEntity;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.subscribe.entity.Subscribe;
import com.nhnacademy.bookpubshop.subscribe.relationship.entity.OrderSubscribeStateCode;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 구독주문상품(order_and_subscribe) 테이블.
 *
 * @author : 박경서
 * @since : 1.0
 **/

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "order_and_subscribe")
public class OrderSubscribe extends BaseCreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_subscribe_number")
    private Long subscribeNo;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "subscribe_number")
    private Subscribe subscribe;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "order_number")
    private BookpubOrder order;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "order_subscribe_state_code_number")
    private OrderSubscribeStateCode orderSubscribeStateCode;

    @Column(name = "order_subscribe_coupon_amount")
    private Long couponAmount;

    @NotNull
    @Column(name = "order_subscribe_product_price")
    private Long productPrice;

    @Column(name = "order_subscribe_finished_at")
    private LocalDateTime finishedAt;

}
