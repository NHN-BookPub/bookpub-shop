package com.nhnacademy.bookpubshop.cardstatecode.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Entity
@Table(name = "card_state_code")
public class CardStateCode {

    @Id
    @Column(name = "card_state_code_number")
    private Integer codeNumber;

    @Column(name = "card_state_code_name")
    private String codeName;

    @Column(name = "card_state_code_used")
    private boolean codeUsed;

    @Column(name = "card_state_code_info")
    private String codeInfo;
}
