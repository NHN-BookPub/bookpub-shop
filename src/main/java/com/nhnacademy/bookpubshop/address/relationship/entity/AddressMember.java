package com.nhnacademy.bookpubshop.address.relationship.entity;

import com.nhnacademy.bookpubshop.address.entity.Address;
import com.nhnacademy.bookpubshop.member.entity.Member;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원과 주소의 연관관계 개체입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/

@Table(name = "address_and_member")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AddressMember {
    @EmbeddedId
    private Pk id;

    @MapsId("memberNo")
    @ManyToOne
    @JoinColumn(name = "member_number", nullable = false, unique = true)
    private Member member;

    @MapsId("addressNo")
    @ManyToOne
    @JoinColumn(name = "address_number", nullable = false, unique = true)
    private Address address;

    @Column(name = "address_member_created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "address_member_based", nullable = false)
    private boolean memberBased;

    /**
     * 주소와 회원의 연관관계 키입니다.
     *
     * @author : 임태원
     * @since : 1.0
     **/
    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @EqualsAndHashCode
    @Getter
    public static class Pk implements Serializable {
        private Long memberNo;
        private Integer addressNo;
    }
}
