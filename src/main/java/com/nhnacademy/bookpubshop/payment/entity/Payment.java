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
import lombok.Builder;
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

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "payment_cancel_reason")
    private String paymentCancelReason;

    @Column(name = "payment_key")
    private String paymentKey;

    @Column(name = "receipt")
    private String receipt;


    /**
     * 페이먼트 엔티티 생성자.
     *
     * @param order                주문.
     * @param paymentStateCode     결제상태.
     * @param paymentTypeStateCode 결제유형.
     * @param approvedAt           승인일시.
     * @param paymentKey           결제 키.
     * @param receipt              영수증.
     */
    @Builder
    public Payment(BookpubOrder order,
                   PaymentStateCode paymentStateCode,
                   PaymentTypeStateCode paymentTypeStateCode,
                   LocalDateTime approvedAt,
                   String paymentKey,
                   String receipt) {
        this.order = order;
        this.paymentStateCode = paymentStateCode;
        this.paymentTypeStateCode = paymentTypeStateCode;
        this.approvedAt = approvedAt;
        this.paymentKey = paymentKey;
        this.receipt = receipt;
    }

    /**
     * 결제 취소 시 결제상태와 취소이유를 업데이트 해주는 메소드 입니다.
     *
     * @param reason 환불 사유.
     */
    public void refund(PaymentStateCode paymentStateCode, String reason) {
        this.paymentStateCode = paymentStateCode;
        this.paymentCancelReason = reason;
    }
}
