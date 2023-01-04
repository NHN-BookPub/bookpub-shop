package com.nhnacademy.bookpubshop.deliverlocation.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "delivery_location")
public class DeliveryLocation {

    @Id
    @Column(name = "delivery_location_number")
    private Long locationNum;

    //TODO : delivery_number join

    @Column(name = "delivery_location_name")
    private String locationName;

    @Column(name = "delivery_location_created_at")
    private LocalDateTime createdAt;

}
