package com.nhnacademy.bookpubshop.card.entity;

import com.nhnacademy.bookpubshop.cardstatecode.entity.CardStateCode;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
@NoArgsConstructor
@Entity(name = "card")
@Table
public class Card {

    @Id
    @Column(name = "payment_number")
    private Long paymentNumber;

    @ManyToOne
    @JoinColumn(name = "card_state_code_number")
    private CardStateCode codeNumber;

    @Column(name = "card_company")
    private String cardCompany;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "card_code_succeed")
    private boolean codeSucceed;

    @Column(name = "card_installment_month")
    private Integer installmentMonth;
}
