package com.nhnacademy.bookpubshop.cardstatecode.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 카드상태코드(card_state_code) 테이블.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "card_state_code")
public class CardStateCode {

    @Id
    @Column(name = "card_state_code_number", nullable = false)
    private Integer codeNo;

    @Column(name = "card_state_code_name", nullable = false)
    private String codeName;

    @Column(name = "card_state_code_used", nullable = false)
    private boolean codeUsed;

    @Column(name = "card_state_code_info")
    private String codeInfo;
}
