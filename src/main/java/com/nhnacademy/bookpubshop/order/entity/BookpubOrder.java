package com.nhnacademy.bookpubshop.order.entity;

import com.nhnacademy.bookpubshop.address.entity.Address;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.orderstatecode.entity.OrderStateCode;
import com.nhnacademy.bookpubshop.pricepolicy.entity.PricePolicy;
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

/**
 * 주문(order) 테이블.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "bookpub_order")
public class BookpubOrder {

    @Id
    @Column(name = "order_number", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderNo;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "price_policy_delivery_number", nullable = false)
    private PricePolicy deliveryPricePolicy;

    @ManyToOne
    @JoinColumn(name = "price_policy_packaging_number", nullable = false)
    private PricePolicy packagingPricePolicy;

    @ManyToOne
    @JoinColumn(name = "address_number", nullable = false)
    private Address address;

    @ManyToOne
    @JoinColumn(name = "order_state_code_number", nullable = false)
    private OrderStateCode orderStateCode;

    @Column(name = "order_ordered_at", nullable = false)
    private LocalDateTime orderedAt;

    @Column(name = "order_recipient", nullable = false)
    private String orderRecipient;

    @Column(name = "order_recipient_phone", nullable = false)
    private String recipientPhone;

    @Column(name = "order_buyer", nullable = false)
    private String orderBuyer;

    @Column(name = "order_buyer_phone", nullable = false)
    private String buyerPhone;

    @Column(name = "order_received_at", nullable = false)
    private LocalDateTime receivedAt;

    @Column(name = "order_invoice_number")
    private String invoiceNumber;

    @Column(name = "order_price", nullable = false)
    private Long orderPrice;

    @Column(name = "order_point_amount", nullable = false)
    private Long pointAmount;

    @Column(name = "order_packaged", nullable = false)
    private boolean orderPackaged;

    @Column(name = "order_request")
    private String orderRequest;

    @Column(name = "order_coupon_discount", nullable = false)
    private Long couponDiscount;

}
