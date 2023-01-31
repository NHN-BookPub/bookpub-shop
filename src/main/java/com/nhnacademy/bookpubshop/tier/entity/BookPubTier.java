package com.nhnacademy.bookpubshop.tier.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 등급 개체입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Table(name = "tier")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BookPubTier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tier_number")
    private Integer tierNo;

    @NotNull
    @Column(name = "tier_name", unique = true)
    private String tierName;

    @NotNull
    @Column(name = "tier_value")
    private Integer tierValue;

    @NotNull
    @Column(name = "tier_price")
    private Long tierPrice;

    @NotNull
    @Column(name = "tier_point")
    private Long tierPoint;

    /**
     * pk를 제외한 생성자입니다.
     *
     * @param tierName 등급명
     */
    @Builder
    public BookPubTier(String tierName, Integer tierValue, Long tierPrice, Long tierPoint) {
        this.tierName = tierName;
        this.tierValue = tierValue;
        this.tierPrice = tierPrice;
        this.tierPoint = tierPoint;
    }

    /**
     * 등급명이 수정될때 쓰이는 메서드입니다.
     *
     * @param tierName 등급이름
     * @author : 유호철
     */
    public void modifyTierName(String tierName) {
        this.tierName = tierName;
    }
}
