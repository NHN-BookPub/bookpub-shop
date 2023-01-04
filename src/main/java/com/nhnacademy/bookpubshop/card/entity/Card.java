package com.nhnacademy.bookpubshop.card.entity;

import com.nhnacademy.bookpubshop.cardstatecode.entity.CardStateCode;
import com.nhnacademy.bookpubshop.payment.entity.Payment;
import javax.persistence.Column;
import javax.persistence.Entity;
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
public class Card {

    @Id
    @OneToOne
    @JoinColumn(name = "payment_number", nullable = false)
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "card_state_code_number", nullable = false)
    private CardStateCode cardStateCode;

    @Column(name = "card_company", nullable = false)
    private String cardCompany;

    @Column(name = "card_number", nullable = false)
    private String cardNo;

    @Column(name = "card_code_succeed", nullable = false)
    private boolean codeSucceed;

    @Column(name = "card_installment_month")
    private Integer installmentMonth;
}
