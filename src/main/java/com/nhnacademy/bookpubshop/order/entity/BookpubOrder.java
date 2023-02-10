package com.nhnacademy.bookpubshop.order.entity;

import com.nhnacademy.bookpubshop.base.BaseCreateTimeEntity;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.orderstatecode.entity.OrderStateCode;
import com.nhnacademy.bookpubshop.pricepolicy.entity.PricePolicy;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 주문(order) 테이블.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "bookpub_order")
public class BookpubOrder extends BaseCreateTimeEntity {
    @Id
    @Column(name = "order_number")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_number")
    private Member member;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "price_policy_delivery_number")
    private PricePolicy deliveryPricePolicy;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "price_policy_packaging_number")
    private PricePolicy packagingPricePolicy;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_state_code_number")
    private OrderStateCode orderStateCode;

    @NotNull
    @Column(name = "order_id")
    private String orderId;

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

    @Column(name = "order_point_save")
    private Long pointSave;

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

    @Column(name = "order_name")
    private String orderName;

    /**
     * Builder 를 적용하기 위한 생성자입니다.
     * 모든 필드를 받습니다.
     */
    @Builder
    public BookpubOrder(Long orderNo,
                        Member member,
                        PricePolicy deliveryPricePolicy,
                        PricePolicy packagingPricePolicy,
                        OrderStateCode orderStateCode,
                        String orderRecipient,
                        String recipientPhone,
                        String orderBuyer,
                        String buyerPhone,
                        LocalDateTime receivedAt,
                        String invoiceNumber,
                        Long orderPrice,
                        Long pointAmount,
                        Long pointSave,
                        boolean orderPackaged,
                        String orderRequest,
                        Long couponDiscount,
                        String addressDetail,
                        String roadAddress,
                        String orderId,
                        String orderName) {
        this.orderNo = orderNo;
        this.member = member;
        this.deliveryPricePolicy = deliveryPricePolicy;
        this.packagingPricePolicy = packagingPricePolicy;
        this.orderStateCode = orderStateCode;
        this.orderRecipient = orderRecipient;
        this.recipientPhone = recipientPhone;
        this.orderBuyer = orderBuyer;
        this.buyerPhone = buyerPhone;
        this.receivedAt = receivedAt;
        this.invoiceNumber = invoiceNumber;
        this.orderPrice = orderPrice;
        this.pointAmount = pointAmount;
        this.pointSave = pointSave;
        this.orderPackaged = orderPackaged;
        this.orderRequest = orderRequest;
        this.couponDiscount = couponDiscount;
        this.addressDetail = addressDetail;
        this.roadAddress = roadAddress;
        this.orderId = orderId;
        this.orderName = orderName;
    }

    public void modifyInvoiceNo(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public void modifyState(OrderStateCode orderStateCode) {
        this.orderStateCode = orderStateCode;
    }
}
