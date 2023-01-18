package com.nhnacademy.bookpubshop.order.entity;

import com.nhnacademy.bookpubshop.base.BaseCreateTimeEntity;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.orderstatecode.entity.OrderStateCode;
import com.nhnacademy.bookpubshop.pricepolicy.entity.PricePolicy;
import java.time.LocalDateTime;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.*;

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
@Builder
public class BookpubOrder extends BaseCreateTimeEntity {

    @Id
    @Column(name = "order_number")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderNo;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "price_policy_delivery_number")
    private PricePolicy deliveryPricePolicy;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "price_policy_packaging_number")
    private PricePolicy packagingPricePolicy;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "order_state_code_number")
    private OrderStateCode orderStateCode;

    @NotNull
    @Column(name = "order_recipient")
    private String orderRecipient;

    @NotNull
    @Column(name = "order_recipient_phone")
    private String recipientPhone;

    @NotNull
    @Column(name = "order_buyer")
    private String orderBuyer;

    @NotNull
    @Column(name = "order_buyer_phone")
    private String buyerPhone;

    @NotNull
    @Column(name = "order_received_at")
    private LocalDateTime receivedAt;

    @Column(name = "order_invoice_number")
    private String invoiceNumber;

    @Column(name = "order_price")
    private Long orderPrice;

    @Column(name = "order_point_amount")
    private Long pointAmount;

    @Column(name = "order_packaged")
    private boolean orderPackaged;

    @Column(name = "order_request")
    private String orderRequest;

    @Column(name = "order_coupon_discount")
    private Long couponDiscount;

    @Column(name = "order_address_detail")
    private String addressDetail;

    @Column(name = "order_road_address")
    private String roadAddress;

    public void modifyInvoiceNo(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public void modifyState(OrderStateCode orderStateCode) {
        this.orderStateCode = orderStateCode;
    }
}
