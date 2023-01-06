package com.nhnacademy.bookpubshop.address.entity;

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
 * 주소 개체입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/

@Table(name = "address")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_number", nullable = false, unique = true)
    private Integer addressNo;

    @Column(name = "address_zipcode", nullable = false)
    private String addressZipcode;

    @Column(name = "address_base", nullable = false)
    private String addressBase;

    @Column(name = "address_detail", nullable = false)
    private String addressMemberDetail;
}
