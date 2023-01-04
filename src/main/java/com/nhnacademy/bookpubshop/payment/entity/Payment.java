package com.nhnacademy.bookpubshop.payment.entity;

import com.nhnacademy.bookpubshop.order.entity.Order;
import com.nhnacademy.bookpubshop.paymentstatecode.entity.PaymentStateCode;
import com.nhnacademy.bookpubshop.paymenttypestatecode.entity.PaymentTypeStateCode;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 결제(payment) 테이블.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_number", nullable = false)
    private Long paymentNo;

    @OneToOne
    @JoinColumn(name = "order_number", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "payment_state_code_number", nullable = false)
    private PaymentStateCode paymentStateCode;

    @ManyToOne
    @JoinColumn(name = "payment_type_state_code_number", nullable = false)
    private PaymentTypeStateCode paymentTypeStateCode;

    @Column(name = "payment_created_at", nullable = false)
    private LocalDateTime createdAt;

}
