package com.nhnacademy.bookpubshop.payment.entity;

import com.nhnacademy.bookpubshop.base.BaseCreateTimeEntity;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.paymentstatecode.entity.PaymentStateCode;
import com.nhnacademy.bookpubshop.paymenttypestatecode.entity.PaymentTypeStateCode;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 결제(payment) 테이블.
 *
 * @author : 김서현, 임태원
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "payment")
public class Payment extends BaseCreateTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_number")
    private Long paymentNo;

    @NotNull
    @OneToOne
    @JoinColumn(name = "order_number")
    private BookpubOrder order;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_state_code_number")
    private PaymentStateCode paymentStateCode;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_type_state_code_number")
    private PaymentTypeStateCode paymentTypeStateCode;

    @JoinColumn(name = "approved_at")
    private LocalDateTime approvedAt;

    @JoinColumn(name = "payment_failure")
    private String paymentFailure;
}
