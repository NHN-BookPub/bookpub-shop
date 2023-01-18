package com.nhnacademy.bookpubshop.address.relationship.entity;

import com.nhnacademy.bookpubshop.address.entity.Address;
import com.nhnacademy.bookpubshop.base.BaseCreateTimeEntity;
import com.nhnacademy.bookpubshop.member.entity.Member;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
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
public class AddressMember extends BaseCreateTimeEntity {
    @EmbeddedId
    private Pk id;

    @MapsId("memberNo")
    @ManyToOne
    @NotNull
    @JoinColumn(name = "member_number", unique = true)
    private Member member;

    @MapsId("addressNo")
    @ManyToOne
    @NotNull
    @JoinColumn(name = "address_number", unique = true)
    private Address address;

    @NotNull
    @Column(name = "address_member_based")
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
