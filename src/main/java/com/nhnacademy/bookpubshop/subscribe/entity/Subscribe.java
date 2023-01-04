package com.nhnacademy.bookpubshop.subscribe.entity;

import java.time.LocalDateTime;
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
import lombok.ToString;

/**
 * 구독(subscribe) 테이블.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Entity
@Table(name = "subscribe")
public class Subscribe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscribe_number", nullable = false, unique = true)
    private Long subscribeNo;

    @Column(name = "subscribe_name", nullable = false)
    private String subscribeName;

    @Column(name = "subscribe_sales_price", nullable = false)
    private Long salesPrice;

    @Column(name = "subscribe_price", nullable = false)
    private Long subscribePrice;

    @Column(name = "subscribe_sales_rate", nullable = false)
    private Integer salesRate;

    @Column(name = "subscribe_view_count", nullable = false)
    private Long viewCount;

    @Column(name = "subscribe_deleted", nullable = false)
    private boolean subscribeDeleted;

    @Column(name = "subscribe_created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "subscribe_renewed", nullable = false)
    private boolean subscribeRenewed;
}
