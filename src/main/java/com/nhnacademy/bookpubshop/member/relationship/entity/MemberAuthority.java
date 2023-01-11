package com.nhnacademy.bookpubshop.member.relationship.entity;

import com.nhnacademy.bookpubshop.authority.entity.Authority;
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
 * 회원과 권한의 연관관계 개체입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Table(name = "member_and_authority")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberAuthority {
    @EmbeddedId
    private Pk id;

    @MapsId("memberNo")
    @ManyToOne
    @JoinColumn(name = "member_number")
    private Member member;

    @MapsId("authorityNo")
    @ManyToOne
    @JoinColumn(name = "authority_number")
    private Authority authority;

    /**
     * 회원과 권한의 연관관계 키입니다.
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
        private Integer authorityNo;
    }
}
