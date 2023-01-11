package com.nhnacademy.bookpubshop.card.entity;

import com.nhnacademy.bookpubshop.cardstatecode.entity.CardStateCode;
import com.nhnacademy.bookpubshop.payment.entity.Payment;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 카드(Card) 테이블.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "card")
public class Card implements Serializable {

    @Id
    @Column(name = "payment_number")
    private Long paymentNo;

    @NotNull
    @MapsId(value = "paymentNo")
    @OneToOne
    @JoinColumn(name = "payment_number")
    private Payment payment;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "card_state_code_number")
    private CardStateCode cardStateCode;

    @NotNull
    @Column(name = "card_company")
    private String cardCompany;

    @NotNull
    @Column(name = "card_number")
    private String cardNo;

    @Column(name = "card_code_succeed")
    private boolean codeSucceed;

    @Column(name = "card_installment_month")
    private Integer installmentMonth;
}
