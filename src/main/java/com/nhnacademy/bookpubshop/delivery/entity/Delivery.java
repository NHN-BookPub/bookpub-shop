package com.nhnacademy.bookpubshop.delivery.entity;

import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
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
 * 배송(delivery) 테이블.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "delivery")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_number", nullable = false)
    private Long deliveryNo;

    @ManyToOne
    @JoinColumn(name = "order_number", nullable = false)
    private BookpubOrder order;

    @Column(name = "delivery_invoice_number", nullable = false)
    private String invoiceNo;

    @Column(name = "delivery_company", nullable = false)
    private String deliveryCompany;

    @Column(name = "delivery_state", nullable = false)
    private String deliveryState;

    @Column(name = "delivery_request")
    private String deliveryRequest;

    @Column(name = "delivery_recipient", nullable = false)
    private String deliveryRecipient;

    @Column(name = "delivery_recipient_phone", nullable = false)
    private String recipientPhone;

    @Column(name = "delivery_finished_at")
    private LocalDateTime finishedAt;

}
