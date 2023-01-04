package com.nhnacademy.bookpubshop.member.relationship;

import com.nhnacademy.bookpubshop.address.entity.Address;
import com.nhnacademy.bookpubshop.member.entity.Member;
import java.io.Serializable;
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

@Table(name = "member_and_address")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberAddress {
    @EmbeddedId
    private Pk id;

    @MapsId("memberNo")
    @ManyToOne
    @JoinColumn(name = "member_number")
    private Member member;

    @MapsId("addressNo")
    @ManyToOne
    @JoinColumn(name = "address_number")
    private Address address;

    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @EqualsAndHashCode
    @Getter
    private static class Pk implements Serializable {
        private Long memberNo;
        private Integer addressNo;
    }
}
