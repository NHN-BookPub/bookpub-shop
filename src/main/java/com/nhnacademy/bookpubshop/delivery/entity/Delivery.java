package com.nhnacademy.bookpubshop.delivery.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Some description here.
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
    @Column(name = "delivery_number")
    private Long deliveryNo;

    //TODO : order_number join

    @Column(name = "delivery_invoice_number")
    private String invoiceNo;

    @Column(name = "delivery_company")
    private String deliveryCompany;

    @Column(name = "delivery_state")
    private String deliveryState;

    @Column(name = "delivery_request")
    private String deliveryRequest;

    @Column(name = "delivery_recipient")
    private String deliveryRecipient;

    @Column(name = "delivery_recipient_phone")
    private String recipientPhone;

    @Column(name = "delivery_finished_at")
    private LocalDateTime finishedAt;

}
