package com.nhnacademy.bookpubshop.subscribe.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
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
    @Column(name = "subscribe_number")
    private Long subscribeNo;

    @Column(name = "subscribe_name")
    private String subscribeName;

    @Column(name = "subscribe_sales_price")
    private Long salesPrice;

    @Column(name = "subscribe_price")
    private Long subscribePrice;

    @Column(name = "subscribe_sales_rate")
    private Integer salesRate;

    @Column(name = "subscribe_view_count")
    private Long viewCount;

    @Column(name = "subscribe_deleted")
    private boolean subscribeDeleted;

    @Column(name = "subscribe_created_at")
    private LocalDateTime createdAt;

    @Column(name = "subscribe_renewed")
    private boolean subscribeRenewed;
}
