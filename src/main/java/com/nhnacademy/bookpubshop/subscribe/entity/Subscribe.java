package com.nhnacademy.bookpubshop.subscribe.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 구독(subscribe) 테이블.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "subscribe")
public class Subscribe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscribe_number")
    private Long subscribeNo;

    @NotNull
    @Column(name = "subscribe_name")
    private String subscribeName;

    @NotNull
    @Column(name = "subscribe_sales_price")
    private Long salesPrice;

    @NotNull
    @Column(name = "subscribe_price")
    private Long subscribePrice;

    @Column(name = "subscribe_sales_rate")
    private Integer salesRate;

    @Column(name = "subscribe_view_count")
    private Long viewCount;

    @Column(name = "subscribe_deleted")
    private boolean subscribeDeleted;

    @Column(name = "subscribe_renewed")
    private boolean subscribeRenewed;
}
