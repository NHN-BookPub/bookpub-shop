package com.nhnacademy.bookpubshop.address.entity;

import com.nhnacademy.bookpubshop.member.entity.Member;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 주소 개체입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/

@Table(name = "address")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_number", nullable = false)
    private Long addressNo;

    @ManyToOne
    @JoinColumn(name = "member_number")
    private Member member;

    @Column(name = "address_member_number")
    private boolean addressMemberNumber;

    @Column(name = "road_address")
    private String roadAddress;

    @Column(name = "address_detail")
    private String addressDetail;


    /**
     * 주소를 등록하기위한 builder 클래스입니다.
     *
     * @param member              회원정보
     * @param addressMemberNumber 주 주소로 사용할 정보인지 조회
     * @param roadAddress         도로명 주소
     * @param addressDetail       상세주소 기입.
     */
    @Builder
    public Address(Member member, boolean addressMemberNumber,
                   String roadAddress, String addressDetail) {
        this.member = member;
        this.addressMemberNumber = addressMemberNumber;
        this.roadAddress = roadAddress;
        this.addressDetail = addressDetail;
    }
}