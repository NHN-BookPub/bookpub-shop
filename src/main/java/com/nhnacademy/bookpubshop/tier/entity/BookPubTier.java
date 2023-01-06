package com.nhnacademy.bookpubshop.tier.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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
    @Column(name = "tier_number", nullable = false, unique = true)
    private Integer tierNo;

    @Column(name = "tier_name", nullable = false, unique = true)
    private String tierName;
}
