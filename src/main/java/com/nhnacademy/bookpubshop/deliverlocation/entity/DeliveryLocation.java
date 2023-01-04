package com.nhnacademy.bookpubshop.deliverlocation.entity;

import com.nhnacademy.bookpubshop.delivery.entity.Delivery;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 배송위치(delivery_location) 테이블.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "delivery_location")
public class DeliveryLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_location_number", nullable = false)
    private Long locationNo;

    @ManyToOne
    @JoinColumn(name = "delivery_number", nullable = false, unique = true)
    private Delivery delivery;

    @Column(name = "delivery_location_name", nullable = false)
    private String locationName;

    @Column(name = "delivery_location_created_at", nullable = false)
    private LocalDateTime createdAt;

}
