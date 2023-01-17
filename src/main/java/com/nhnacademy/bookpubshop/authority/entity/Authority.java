package com.nhnacademy.bookpubshop.authority.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 권한 개체입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Table(name = "authority")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_number")
    private Integer authorityNo;

    @NotNull
    @Column(name = "authority_name", unique = true)
    private String authorityName;

    /**
     * pk 를 제외한 권한을 처리하기위한 생성자클래스.
     *
     * @param authorityName 권한명이들어간다.
     */
    @Builder
    public Authority(String authorityName) {
        this.authorityName = authorityName;
    }
}
